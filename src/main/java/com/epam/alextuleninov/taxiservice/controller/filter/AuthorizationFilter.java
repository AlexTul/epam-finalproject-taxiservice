package com.epam.alextuleninov.taxiservice.controller.filter;

import com.epam.alextuleninov.taxiservice.model.user.role.Role;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

import static com.epam.alextuleninov.taxiservice.Constants.SCOPE_ROLE;
import static com.epam.alextuleninov.taxiservice.Routes.*;

/**
 * AuthorizationFilter for intercepts the request.
 * The filter pre-processes the request before it reaches the servlet.
 * AuthorizationFilter checks authorization.
 */
@WebFilter(filterName = "AuthorizationFilter", urlPatterns = {URL_ADMIN, URL_REPORT_})
public class AuthorizationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        Role role = (Role) httpRequest.getSession().getAttribute(SCOPE_ROLE);

        if (role == null || !role.equals(Role.ADMINISTRATOR)) {
            request.getRequestDispatcher(PAGE_LOGIN).forward(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}
