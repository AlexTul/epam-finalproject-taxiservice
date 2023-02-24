package com.epam.alextuleninov.taxiservice.controller.filter;

import com.epam.alextuleninov.taxiservice.config.context.AppContext;
import com.epam.alextuleninov.taxiservice.model.user.role.Role;
import com.epam.alextuleninov.taxiservice.service.crud.user.UserCRUD;
import com.epam.alextuleninov.taxiservice.validation.DataValidator;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.epam.alextuleninov.taxiservice.Constants.*;
import static com.epam.alextuleninov.taxiservice.Routes.*;
import static java.util.Objects.nonNull;

/**
 * AuthenticationFilter for intercepts the request.
 * The filter pre-processes the request before it reaches the servlet.
 * AuthenticationFilter authenticates the user and forwards to the appropriate resource.
 */
@WebFilter(filterName = "AuthenticationFilter", urlPatterns = URL_AUTH)
public class AuthenticationFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    private UserCRUD userCRUD;

    @Override
    public void init(FilterConfig filterConfig) {
        this.userCRUD = AppContext.getAppContext().getUserCRUD();
        log.info("Authentication and authorization filter initialized");
    }

    /**
     * Authenticates the user and forwards to the appropriate resource.
     *
     * @param servletRequest  the ServletRequest request
     * @param servletResponse the ServletResponse response
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        HttpSession session = req.getSession();

        String login = req.getParameter(SCOPE_LOGIN);
        String password = req.getParameter(SCOPE_PASSWORD);

        if (login == null) {
            // Logged user
            if (nonNull(session.getAttribute(SCOPE_LOGIN))) {
                Role role = (Role) session.getAttribute(SCOPE_ROLE);

                moveToMenu(req, resp, role);
            } else {
                moveToMenu(req, resp, Role.UNKNOWN);
            }
        } else {
            // validation of entered data
            if (DataValidator.initValidationLogInCredentials(req, resp)) {
                if (userCRUD.authentication(login, password)) {
                    Role role = Role.valueOf(userCRUD.findRoleByEmail(login));

                    req.getSession().setAttribute(SCOPE_LOGIN, login);
                    req.getSession().setAttribute(SCOPE_ROLE, role);
                    log.info("User: " + login + " passed authentication and authorization filter successfully");

                    moveToMenu(req, resp, role);
                } else {
                    moveToMenu(req, resp, Role.UNKNOWN);
                }
            }
        }

        filterChain
                .doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        log.info("Authentication and authorization filter destroyed");
    }

    /**
     * Move user to resource.
     *
     * @param req  the HttpServletRequest request
     * @param resp the HttpServletResponse response
     * @param role the role of user
     */
    private void moveToMenu(HttpServletRequest req, HttpServletResponse resp,
                            Role role) throws ServletException, IOException {

        if (role.equals(Role.ADMINISTRATOR)) {

            req.getRequestDispatcher(PAGE_ADMIN_MENU)
                    .forward(req, resp);
        } else if (role.equals(Role.CLIENT)) {

            req.getRequestDispatcher(PAGE_CLIENT_MENU)
                    .forward(req, resp);
        } else {
            req.getRequestDispatcher(PAGE_LOGIN)
                    .forward(req, resp);
        }
    }
}
