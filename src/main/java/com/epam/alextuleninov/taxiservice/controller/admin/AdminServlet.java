package com.epam.alextuleninov.taxiservice.controller.admin;

import com.epam.alextuleninov.taxiservice.Routes;
import com.epam.alextuleninov.taxiservice.config.context.AppContext;
import com.epam.alextuleninov.taxiservice.controller.confirm.ConfirmServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet(name = "AdminServlet", urlPatterns = "/admin")
public class AdminServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(AdminServlet.class);

    @Override
    public void init() {
        log.info(getServletName() + " initialized");
    }



    @Override
    public void destroy() {
        log.info(getServletName() + " destroyed");
    }
}
