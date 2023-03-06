package com.epam.alextuleninov.taxiservice.controller.profile;

import com.epam.alextuleninov.taxiservice.config.context.AppContext;
import com.epam.alextuleninov.taxiservice.config.mail.EmailConfig;
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

            req.getSession().removeAttribute(SCOPE_PASSWORD_CONFIRMING);
            req.getSession().removeAttribute(SCOPE_AUTHENTICATION);
            req.getSession().removeAttribute(SCOPE_PASSWORD_VALIDATE);
            req.getSession().removeAttribute(SCOPE_CONFIRM_PASSWORD_VALIDATE);
        } else {
            var userResponse = userCRUD.findByEmail(login)
                    .orElseThrow(() -> userNotFound(login));

            req.setAttribute(SCOPE_USER_RESPONSE, userResponse);

            req.getRequestDispatcher(PAGE_PROFILE)
                    .forward(req, resp);
        }
    }

    /**
     * To process Post requests:
     * - update user`s password by administrator;
     * - change user`s credentials.
     *
     * @param req HttpServletRequest request
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String action = (String) req.getSession().getAttribute(SCOPE_ACTION);

        if (action != null && action.equals("updatePassword")) {
            if(validationPasswordData(req, resp)) {
                processUpdatePassword(req);

                resp.sendRedirect(URL_PROFILE);
            }
        } else {
            if (DataValidator.initValidationChangeCredentials(req)) {
                processChangeCredentials(req);

                resp.sendRedirect(URL_LOGOUT);
            } else {
                resp.sendRedirect(URL_PROFILE);
            }
        }
    }

    @Override
    public void destroy() {
        log.info(getServletName() + " destroyed");
    }

    /**
     * To process Post requests from user:
     * validation password data for changing.
     *
     * @param req    HttpServletRequest request
     * @param resp   HttpServletResponse response
     * @return true if user credentials is valid
     */
    private boolean validationPasswordData(HttpServletRequest req, HttpServletResponse resp)
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

            req.getSession().removeAttribute(SCOPE_PASSWORD_CONFIRMING);
            resp.sendRedirect(URL_PROFILE);
            return false;
        }

        if(!newPassword.equals(confirmPassword)) {
            log.info("New password and confirmation password do not match");
            if (locale.equals("uk_UA")) {
                req.getSession().setAttribute(SCOPE_PASSWORD_CONFIRMING, PASSWORD_CONFIRMING_NOT_UK);
            } else {
                req.getSession().setAttribute(SCOPE_PASSWORD_CONFIRMING, PASSWORD_CONFIRMING_NOT);
            }

            req.getSession().removeAttribute(SCOPE_AUTHENTICATION);
            resp.sendRedirect(URL_PROFILE);
            return false;
        }
        req.getSession().removeAttribute(SCOPE_PASSWORD_CONFIRMING);
        req.getSession().removeAttribute(SCOPE_AUTHENTICATION);

        if (!DataValidator.initValidationChangePassword(req)) {
            resp.sendRedirect(URL_PROFILE);
            return false;
        }
        return true;
    }

    /**
     * To process update user`s password by administrator.
     *
     * @param req HttpServletRequest request
     */
    private void processUpdatePassword(HttpServletRequest req) {
        var login = (String) req.getSession().getAttribute(SCOPE_LOGIN);
        var newPassword = req.getParameter(SCOPE_NEW_PASSWORD);

        userCRUD.changePasswordByEmail(login, newPassword);

        new Thread(() ->
                emailSender.send(EMAIL_UPDATE_PASSWORD,
                        String.format(EMAIL_UPDATE_PASSWORD_BODY, newPassword),
                        login)
        ).start();

        req.getSession().removeAttribute(SCOPE_ACTION);
    }

    /**
     * To process change user`s credentials by user.
     *
     * @param req HttpServletRequest request
     */
    private void processChangeCredentials(HttpServletRequest req) {
        var userRequest = UserRequest.getUserRequest(req);

        String oldLogin = (String) req.getSession().getAttribute(SCOPE_LOGIN);
        userCRUD.changeCredentialsByEmail(oldLogin, userRequest);
        req.getSession().setAttribute(SCOPE_LOGIN, userRequest.email());

        new Thread(() ->
                emailSender.send(EMAIL_UPDATE_CREDENTIALS,
                        String.format(EMAIL_UPDATE_CREDENTIALS_BODY, userRequest.firstName(), userRequest.lastName(), userRequest.email()),
                        userRequest.email())
        ).start();
    }
}
