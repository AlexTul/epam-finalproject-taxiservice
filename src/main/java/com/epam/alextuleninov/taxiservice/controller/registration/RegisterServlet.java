package com.epam.alextuleninov.taxiservice.controller.registration;

import com.epam.alextuleninov.taxiservice.config.context.AppContext;
import com.epam.alextuleninov.taxiservice.config.mail.EmailConfig;
import com.epam.alextuleninov.taxiservice.data.user.UserRequest;
import com.epam.alextuleninov.taxiservice.service.crud.user.UserCRUD;
import com.epam.alextuleninov.taxiservice.service.message.PageMessageBuilder;
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

/**
 * RegisterServlet for to process a Http request from a user.
 */
@WebServlet(name = "RegisterServlet", urlPatterns = URL_REGISTER)
public class RegisterServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(RegisterServlet.class);

    private UserCRUD userCRUD;
    private EmailConfig emailSender;

    @Override
    public void init() {
        this.userCRUD = AppContext.getAppContext().getUserCRUD();
        this.emailSender = AppContext.getAppContext().getEmailSender();
        log.info(getServletName() + " initialized");
    }

    /**
     * To process Get requests from user:
     * redirect user on register page.
     *
     * @param req  HttpServletRequest request
     * @param resp HttpServletResponse response
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher(PAGE_REGISTER)
                .forward(req, resp);
    }

    /**
     * To process Post requests from user:
     * validation a user`s credentials and register user to database.
     *
     * @param req  HttpServletRequest request
     * @param resp HttpServletResponse response
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String locale = (String) req.getSession().getAttribute(SCOPE_LOCALE);

        if (registerValidation(req, resp)) {
            boolean register = userCRUD.register(UserRequest.getUserRequest(req));

            if (!register) {
                log.info("Email: " + req.getParameter("email") + " already taken");
                req.getSession().setAttribute(SCOPE_REGISTER_TRUE_FALSE, false);
                PageMessageBuilder.buildMessageUser(req, locale,
                        USER_REGISTER_FAIL_UK, USER_REGISTER_FAIL);

                resp.sendRedirect(URL_MESSAGE_USER);
            } else {
                log.info("User successfully registered");
                req.getSession().setAttribute(SCOPE_REGISTER_TRUE_FALSE, true);
                PageMessageBuilder.buildMessageUser(req, locale,
                        USER_REGISTER_SUC_UK, USER_REGISTER_SUC);

                new Thread(() ->
                        emailSender.send(EMAIL_REGISTER_SUBJECT, EMAIL_REGISTER_BODY, req.getParameter(SCOPE_LOGIN))
                ).start();

                resp.sendRedirect(URL_MESSAGE_USER);
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
    private boolean registerValidation(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        if (!(DataValidator.initRegisterValidation(req))) {
            log.info("User credentials not validated");

            resp.sendRedirect(URL_REGISTER);
            return false;
        }
        return true;
    }
}
