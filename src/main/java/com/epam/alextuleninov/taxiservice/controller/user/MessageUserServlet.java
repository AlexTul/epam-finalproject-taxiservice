package com.epam.alextuleninov.taxiservice.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.epam.alextuleninov.taxiservice.Routes.PAGE_MESSAGE_USER;
import static com.epam.alextuleninov.taxiservice.Routes.URL_MESSAGE_USER;

@WebServlet(name = "MessageUserServlet", urlPatterns = URL_MESSAGE_USER)
public class MessageUserServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(MessageUserServlet.class);

    @Override
    public void init() {
        log.info(getServletName() + " initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher(PAGE_MESSAGE_USER)
                .forward(req, resp);
    }

    @Override
    public void destroy() {
        log.info(getServletName() + " destroyed");
    }
}
