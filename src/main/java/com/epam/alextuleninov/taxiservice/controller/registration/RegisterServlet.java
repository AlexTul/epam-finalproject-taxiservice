package com.epam.alextuleninov.taxiservice.controller.registration;

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

        if (DataValidator.initValidationRegisterCredentials(req)) {
            boolean register = userCRUD.register(UserRequest.getUserRequest(req));

            if (!register) {
                log.info("Email: " + req.getParameter("email") + " already taken");
                req.getSession().setAttribute(SCOPE_MESSAGE, USER_REGISTER_FAIL);
                req.getSession().setAttribute(SCOPE_MESSAGE_UK, USER_REGISTER_FAIL_UK);

                resp.sendRedirect(URL_MESSAGE);
            } else {
                log.info("User successfully registered");
                req.getSession().setAttribute(SCOPE_MESSAGE, USER_REGISTER_SUC);
                req.getSession().setAttribute(SCOPE_MESSAGE_UK, USER_REGISTER_SUC_UK);

                new Thread(() ->
                        emailSender.send(EMAIL_REGISTER_SUBJECT, EMAIL_REGISTER_BODY, req.getParameter(SCOPE_LOGIN))
                ).start();

                resp.sendRedirect(URL_MESSAGE);
            }
        } else {
            resp.sendRedirect(URL_REGISTER);
        }
    }

    @Override
    public void destroy() {
        log.info(getServletName() + " destroyed");
    }
}
