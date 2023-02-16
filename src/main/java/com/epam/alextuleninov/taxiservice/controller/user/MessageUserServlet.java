package com.epam.alextuleninov.taxiservice.controller.user;

import com.epam.alextuleninov.taxiservice.Routes;
import com.epam.alextuleninov.taxiservice.config.context.AppContext;
import com.epam.alextuleninov.taxiservice.controller.registration.RegisterServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet(name = "MessageUserServlet", urlPatterns = "/messageuser")
public class MessageUserServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(MessageUserServlet.class);

    @Override
    public void init() {
        log.info(getServletName() + " initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher(Routes.PAGE_MESSAGE_USER)
                .forward(req, resp);
    }

    @Override
    public void destroy() {
        log.info(getServletName() + " destroyed");
    }
}
