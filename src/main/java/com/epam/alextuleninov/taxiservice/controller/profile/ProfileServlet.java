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
        var userResponse = userCRUD.findByEmail(login)
                .orElseThrow(() -> userNotFound(login));

        req.getSession().setAttribute(SCOPE_USER_RESPONSE, userResponse);

        req.getRequestDispatcher(PAGE_PROFILE)
                .forward(req, resp);
    }

    /**
     * To process Post requests:
     * - update user`s credentials;
     *
     * @param req HttpServletRequest request
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        if (credentialValidation(req, resp)) {
            var userRequest = UserRequest.getUserRequest(req);

            String oldLogin = (String) req.getSession().getAttribute(SCOPE_LOGIN);
            userCRUD.changeCredentialsByEmail(oldLogin, userRequest);

            req.getSession().setAttribute(SCOPE_LOGIN, userRequest.email());

            new Thread(() ->
                    emailSender.send(EMAIL_UPDATE_PASSWORD,
                            String.format(EMAIL_UPDATE_CREDENTIALS_BODY, userRequest.firstName(), userRequest.lastName(), userRequest.email()),
                            userRequest.email())
            ).start();

            resp.sendRedirect(URL_LOGOUT);
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
}
