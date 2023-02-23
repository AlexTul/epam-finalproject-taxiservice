package com.epam.alextuleninov.taxiservice.controller.profile;

import com.epam.alextuleninov.taxiservice.config.context.AppContext;
import com.epam.alextuleninov.taxiservice.config.mail.EmailConfig;
import com.epam.alextuleninov.taxiservice.data.user.ChangeUserPasswordRequest;
import com.epam.alextuleninov.taxiservice.data.user.UserRequest;
import com.epam.alextuleninov.taxiservice.service.crud.user.UserCRUD;
import com.epam.alextuleninov.taxiservice.validation.DataValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.epam.alextuleninov.taxiservice.Constants.*;
import static com.epam.alextuleninov.taxiservice.Routes.*;
import static com.epam.alextuleninov.taxiservice.exceptions.user.UserExceptions.userNotFound;

/**
 * Servlet for processing Http request to change profile data.
 */
@WebServlet(name = "ProfileServlet", urlPatterns = URL_PROFILE)
public class ProfileServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ProfileServlet.class);

    private UserCRUD userCRUD;
    private EmailConfig emailSender;

    @Override
    public void init() {
        this.userCRUD = AppContext.getAppContext().getUserCRUD();
        this.emailSender = AppContext.getAppContext().getEmailSender();
        log.info(getServletName() + " initialized");
    }

    /**
     * To process Get requests:
     * - forward on profile page;
     *
     *
     * @param req HttpServletRequest request
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String login = (String) req.getSession().getAttribute(SCOPE_LOGIN);

        String action = req.getParameter(SCOPE_ACTION) == null ?
                (String) req.getSession().getAttribute(SCOPE_ACTION) : req.getParameter(SCOPE_ACTION);
        req.getSession().setAttribute(SCOPE_ACTION, action);

        if (action != null && action.equals("updatePassword")) {

            req.getRequestDispatcher(PAGE_CHANGE_PASSWORD)
                    .forward(req, resp);
        } else {
            var userResponse = userCRUD.findByEmail(login)
                    .orElseThrow(() -> userNotFound(login));

            req.getSession().setAttribute(SCOPE_USER_RESPONSE, userResponse);

            req.getRequestDispatcher(PAGE_PROFILE)
                    .forward(req, resp);
        }
    }

    /**
     * To process Post requests:
     * - update user`s credentials;
     *
     * @param req HttpServletRequest request
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String action = (String) req.getSession().getAttribute(SCOPE_ACTION);

        if (action != null && action.equals("updatePassword")) {
            if(passwordValidation(req, resp)) {
                var login = (String) req.getSession().getAttribute(SCOPE_LOGIN);
                var newPassword = req.getParameter(SCOPE_NEW_PASSWORD);

                var changeUserPasswordRequest = new ChangeUserPasswordRequest(null, newPassword, null);

                userCRUD.changePasswordByEmail(login, changeUserPasswordRequest);
                req.getSession().removeAttribute(SCOPE_ACTION);

                new Thread(() ->
                        emailSender.send(EMAIL_UPDATE_PASSWORD,
                                String.format(EMAIL_UPDATE_PASSWORD_BODY, newPassword),
                                login)
                ).start();

                resp.sendRedirect(URL_PROFILE);
            }
        } else {
            if (credentialValidation(req, resp)) {
                var userRequest = UserRequest.getUserRequest(req);

                String oldLogin = (String) req.getSession().getAttribute(SCOPE_LOGIN);
                userCRUD.changeCredentialsByEmail(oldLogin, userRequest);
                req.getSession().setAttribute(SCOPE_LOGIN, userRequest.email());

                new Thread(() ->
                        emailSender.send(EMAIL_UPDATE_CREDENTIALS,
                                String.format(EMAIL_UPDATE_CREDENTIALS_BODY, userRequest.firstName(), userRequest.lastName(), userRequest.email()),
                                userRequest.email())
                ).start();

                resp.sendRedirect(URL_LOGOUT);
            }
        }
    }

    @Override
    public void destroy() {
        log.info(getServletName() + " destroyed");
    }

    /**
     * To process Post requests from user:
     * validation a user`s credentials.
     *
     * @param req    HttpServletRequest request
     * @param resp   HttpServletResponse response
     * @return true if user credentials is valid
     */
    private boolean credentialValidation(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        if (!(DataValidator.initChangeCredentialValidation(req))) {
            log.info("User credentials not validated");

            resp.sendRedirect(URL_PROFILE);
            return false;
        }
        return true;
    }

    /**
     * To process Post requests from user:
     * validation a user`s credentials.
     *
     * @param req    HttpServletRequest request
     * @param resp   HttpServletResponse response
     * @return true if user credentials is valid
     */
    private boolean passwordValidation(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        var locale = req.getSession().getAttribute(SCOPE_LOCALE);
        var login = (String) req.getSession().getAttribute(SCOPE_LOGIN);
        var oldPassword = req.getParameter(SCOPE_PASSWORD);
        var newPassword = req.getParameter(SCOPE_NEW_PASSWORD);
        var confirmPassword = req.getParameter(SCOPE_CONFIRM_PASSWORD);

        if (!userCRUD.authentication(login, oldPassword)) {
            log.info("User not authenticated");
            if (locale.equals("uk_UA")) {
                req.getSession().setAttribute(SCOPE_AUTHENTICATION, USER_AUTHENTICATED_NOT_UK);
            } else {
                req.getSession().setAttribute(SCOPE_AUTHENTICATION, USER_AUTHENTICATED_NOT);
            }

            resp.sendRedirect(URL_PROFILE);

            req.getSession().removeAttribute(SCOPE_AUTHENTICATION);
            return false;
        }

        if(!newPassword.equals(confirmPassword)) {
            if (locale.equals("uk_UA")) {
                req.getSession().setAttribute(SCOPE_PASSWORD_CONFIRMING, PASSWORD_CONFIRMING_NOT_UK);
            } else {
                req.getSession().setAttribute(SCOPE_PASSWORD_CONFIRMING, PASSWORD_CONFIRMING_NOT);
            }

            resp.sendRedirect(URL_PROFILE);

            req.getSession().removeAttribute(SCOPE_PASSWORD_CONFIRMING);
            return false;
        }

        if (!DataValidator.initChangePasswordValidation(req)) {
            log.info("User credentials not validated");

            resp.sendRedirect(URL_PROFILE);

            req.getSession().removeAttribute(SCOPE_PASSWORD_VALIDATE);
            return false;
        }
        return true;
    }
}
