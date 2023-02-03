package com.epam.alextuleninov.taxiservice.controller.registration;

import com.epam.alextuleninov.taxiservice.Constants;
import com.epam.alextuleninov.taxiservice.Routes;
import com.epam.alextuleninov.taxiservice.config.context.AppContext;
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

/**
 * RegisterServlet for to process a Http request from a user.
 */
@WebServlet(name = "RegisterServlet", urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(RegisterServlet.class);

    private UserCRUD userCRUD;

    @Override
    public void init() {
        this.userCRUD = AppContext.getAppContext().getUserCRUD();
        log.info(getServletName() + " initialized");
    }

    /**
     * To process Get requests from user:
     * redirect user on register page.
     *
     * @param req                   HttpServletRequest request
     * @param resp                  HttpServletResponse response
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher(Routes.PAGE_REGISTER)
                .forward(req, resp);
    }

    /**
     * To process Post requests from user:
     * validation a user`s credentials and register user to database.
     *
     * @param req                   HttpServletRequest request
     * @param resp                  HttpServletResponse response
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String locale = (String) req.getSession().getAttribute("locale");

        if (registerValidation(req, resp, locale)) {
            boolean register = userCRUD.register(UserRequest.getUserRequest(req));

            if (!register) {
                log.info("Email: " + req.getParameter("email") + " already taken");
                PageMessageBuilder.buildMessageUser(req, locale,
                        Constants.USER_FAIL_REGISTER_UK, Constants.USER_FAIL_REGISTER);

                req.getRequestDispatcher(Routes.PAGE_MESSAGE_USER)
                        .forward(req, resp);
            } else {
                log.info("User successfully registered");
                PageMessageBuilder.buildMessageUser(req, locale,
                        Constants.USER_SUCC_REGISTER_UK, Constants.USER_SUCC_REGISTER);

                req.getRequestDispatcher(Routes.PAGE_MESSAGE_USER)
                        .forward(req, resp);
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
     * @param req                   HttpServletRequest request
     * @param resp                  HttpServletResponse response
     * @param locale                default or current locale of application
     * @return                      true if user credentials is valid
     */
    private boolean registerValidation(HttpServletRequest req, HttpServletResponse resp, String locale)
            throws ServletException, IOException {
        if (!(DataValidator.initRegisterValidation(req))) {
            log.info("User credentials not validated");

            PageMessageBuilder.buildMessageUser(req, locale, Constants.USER_UK, Constants.USER);

            req.getRequestDispatcher(Routes.PAGE_MESSAGE_USER)
                    .forward(req, resp);
            return false;
        }
        return true;
    }
}
