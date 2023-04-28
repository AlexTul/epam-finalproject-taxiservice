package com.epam.alextuleninov.taxiservice.controller.register;

import com.epam.alextuleninov.taxiservice.config.context.AppContext;
import com.epam.alextuleninov.taxiservice.config.mail.EmailByLocaleConfig;
import com.epam.alextuleninov.taxiservice.config.mail.EmailConfig;
import com.epam.alextuleninov.taxiservice.data.user.UserRequest;
import com.epam.alextuleninov.taxiservice.service.crud.UserCRUD;
import com.epam.alextuleninov.taxiservice.validation.DataValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

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
    private Properties properties;
    private Properties propertiesUk;
    private EmailByLocaleConfig emailByLocaleConfig;

    @Override
    public void init() {
        this.userCRUD = AppContext.getAppContext().getUserCRUD();
        this.emailSender = AppContext.getAppContext().getEmailSender();
        this.properties = AppContext.getAppContext().getProperties();
        this.propertiesUk = AppContext.getAppContext().getPropertiesUk();
        this.emailByLocaleConfig = AppContext.getAppContext().getEmailByLocaleConfig();
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
            processRegisterUser(req);

            resp.sendRedirect(URL_MESSAGE);
        } else {
            resp.sendRedirect(URL_REGISTER);
        }
    }

    @Override
    public void destroy() {
        log.info(getServletName() + " destroyed");
    }

    /**
     * To process register user in the database.
     *
     * @param req HttpServletRequest request
     */
    private void processRegisterUser(HttpServletRequest req) {
        var locale = (String) req.getSession().getAttribute(SCOPE_LOCALE);
        boolean register = userCRUD.register(UserRequest.getUserRequest(req));

        if (!register) {
            log.info("Email: " + req.getParameter("email") + " already taken");
            req.getSession().setAttribute(SCOPE_MESSAGE, properties.getProperty("user.register.fail"));
            req.getSession().setAttribute(SCOPE_MESSAGE_UK, propertiesUk.getProperty("user.register.fail.uk"));
        } else {
            log.info("User successfully registered");
            req.getSession().setAttribute(SCOPE_MESSAGE, properties.getProperty("user.register.suc"));
            req.getSession().setAttribute(SCOPE_MESSAGE_UK, propertiesUk.getProperty("user.register.suc.uk"));

            new Thread(() ->
                    emailSender.send(
                            emailByLocaleConfig.getTextByLocale(locale,
                                    propertiesUk.getProperty("email.register.subject.uk"),
                                    properties.getProperty("email.register.subject")),
                            emailByLocaleConfig.getTextByLocale(locale,
                                    propertiesUk.getProperty("email.register.body.uk"),
                                    properties.getProperty("email.register.body")),
                            req.getParameter(SCOPE_LOGIN)
                    )).start();
        }
    }
}
