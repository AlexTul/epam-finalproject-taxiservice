package com.epam.alextuleninov.taxiservice.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.epam.alextuleninov.taxiservice.Routes.*;

/**
 * AdminServlet for to process a Http request from admin.
 */
@WebServlet(name = "AdminServlet", urlPatterns = URL_ADMIN)
public class AdminServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(AdminServlet.class);

    @Override
    public void init() {
        log.info(getServletName() + " initialized");
    }

    /**
     * To process Get requests from admin:
     * - forward on admin page.
     *
     * @param req HttpServletRequest request
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher(PAGE_ADMIN_MENU)
                .forward(req, resp);
    }

    @Override
    public void destroy() {
        log.info(getServletName() + " destroyed");
    }
}
