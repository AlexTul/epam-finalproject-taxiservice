package com.epam.alextuleninov.taxiservice.controller.message;

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

@WebServlet(name = "MessageServlet", urlPatterns = URL_MESSAGE)
public class MessageServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(MessageServlet.class);

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

        if (req.getSession().getAttribute(SCOPE_MESSAGE_ORDER) != null
                || req.getSession().getAttribute(SCOPE_MESSAGE_ORDER_UK) != null
                || req.getSession().getAttribute(SCOPE_DATE_TIME_OF_TRAVEL) != null) {

            req.getRequestDispatcher(PAGE_MESSAGE_ORDER)
                    .forward(req, resp);
        } else {
            req.getRequestDispatcher(PAGE_MESSAGE)
                    .forward(req, resp);
        }
    }

    @Override
    public void destroy() {
        log.info(getServletName() + " destroyed");
    }
}
