package com.epam.alextuleninov.taxiservice.controller.filter;

import com.epam.alextuleninov.taxiservice.Routes;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * AuthorizationFilter for intercepts the request.
 * The filter pre-processes the request before it reaches the servlet.
 * AuthorizationFilter checks authorization.
 */
@WebFilter(filterName = "AuthorizationFilter", urlPatterns = "/report/*")
public class AuthorizationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String role = (String) httpRequest.getSession().getAttribute("role");

        if (role == null || !role.equals("ADMINISTRATOR")) {
            request.getRequestDispatcher(Routes.PAGE_LOGIN).forward(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}