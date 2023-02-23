package com.epam.alextuleninov.taxiservice.controller.client;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.epam.alextuleninov.taxiservice.Routes.PAGE_CLIENT_MENU;

/**
 * Servlet for to process a Http request from a client.
 */
@WebServlet(name = "ClientServlet", urlPatterns = "/client")
public class ClientServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ClientServlet.class);

    @Override
    public void init() {
        log.info(getServletName() + " initialized");
    }

    /**
     * To process Get requests from user:
     * -
     *
     * @param req HttpServletRequest request
     * @param resp HttpServletResponse response
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher(PAGE_CLIENT_MENU)
                .forward(req, resp);
    }

    @Override
    public void destroy() {
        log.info(getServletName() + " destroyed");
    }
}
