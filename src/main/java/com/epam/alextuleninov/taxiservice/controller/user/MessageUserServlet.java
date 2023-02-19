package com.epam.alextuleninov.taxiservice.controller.user;

import com.epam.alextuleninov.taxiservice.service.message.PageMessageBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.epam.alextuleninov.taxiservice.Constants.*;
import static com.epam.alextuleninov.taxiservice.Routes.PAGE_MESSAGE_USER;
import static com.epam.alextuleninov.taxiservice.Routes.URL_MESSAGE_USER;

@WebServlet(name = "MessageUserServlet", urlPatterns = URL_MESSAGE_USER)
public class MessageUserServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(MessageUserServlet.class);

    @Override
    public void init() {
        log.info(getServletName() + " initialized");
    }

    /**
     * To process Get requests from admin:
     * - forward on user`s page message depending on locale;
     * - forward on user`s page message.
     *
     * @param req HttpServletRequest request
     * @param resp HttpServletResponse response
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // change locale
        String locale = (String) req.getSession().getAttribute(SCOPE_LOCALE);
        Object attributeRegister = req.getSession().getAttribute(SCOPE_REGISTER_TRUE_FALSE);
        boolean register = false;
        if (attributeRegister != null) {
            register = (boolean) attributeRegister;
        }
        if (register) {
            PageMessageBuilder.buildMessageUser(req, locale,
                    USER_REGISTER_SUC_UK, USER_REGISTER_SUC);
        } else {
            PageMessageBuilder.buildMessageUser(req, locale,
                    USER_REGISTER_FAIL_UK, USER_REGISTER_FAIL);
        }

        req.getRequestDispatcher(PAGE_MESSAGE_USER)
                .forward(req, resp);
    }

    @Override
    public void destroy() {
        log.info(getServletName() + " destroyed");
    }
}
