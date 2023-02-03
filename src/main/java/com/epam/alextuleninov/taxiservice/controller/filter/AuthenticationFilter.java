package com.epam.alextuleninov.taxiservice.controller.filter;

import com.epam.alextuleninov.taxiservice.Constants;
import com.epam.alextuleninov.taxiservice.Routes;
import com.epam.alextuleninov.taxiservice.config.context.AppContext;
import com.epam.alextuleninov.taxiservice.config.pagination.PaginationConfig;
import com.epam.alextuleninov.taxiservice.data.pageable.PageableRequest;
import com.epam.alextuleninov.taxiservice.model.user.role.Role;
import com.epam.alextuleninov.taxiservice.service.crud.order.OrderCRUD;
import com.epam.alextuleninov.taxiservice.service.crud.route.RouteCRUD;
import com.epam.alextuleninov.taxiservice.service.crud.user.UserCRUD;
import com.epam.alextuleninov.taxiservice.service.message.PageMessageBuilder;
import com.epam.alextuleninov.taxiservice.validation.DataValidator;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

import static java.util.Objects.nonNull;

/**
 * AuthenticationFilter for intercepts the request.
 * The filter pre-processes the request before it reaches the servlet.
 * AuthenticationFilter authenticates the user and forwards to the appropriate resource.
 */
@WebFilter(filterName = "AuthenticationFilter", urlPatterns = "/auth")
public class AuthenticationFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    private UserCRUD userCRUD;
    private RouteCRUD routeCRUD;
    private OrderCRUD orderCRUD;

    @Override
    public void init(FilterConfig filterConfig) {
        this.userCRUD = AppContext.getAppContext().getUserCRUD();
        this.routeCRUD = AppContext.getAppContext().getRouteCRUD();
        this.orderCRUD = AppContext.getAppContext().getOrderCRUD();
        log.info("Authentication and authorization filter initialized");
    }

    /**
     * Authenticates the user and forwards to the appropriate resource.
     *
     * @param servletRequest  the ServletRequest request
     * @param servletResponse the ServletResponse response
     */
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String locale = (String) req.getSession().getAttribute("locale");

        // validation of entered data
        if (loginValidation(req, resp, locale)) {
            final String login = req.getParameter("login");
            final String password = req.getParameter("password");

            final HttpSession session = req.getSession();

            // Logged user
            if (nonNull(session.getAttribute("login")) &&
                    nonNull(session.getAttribute("password"))) {
                Role role = (Role) session.getAttribute("role");

                moveToMenu(req, resp, role);
            } else if (userCRUD.authentication(login, password)) {
                Role role = Role.valueOf(userCRUD.findRoleByEmail(login));

                req.getSession().setAttribute("login", login);
                req.getSession().setAttribute("role", role.toString());

                moveToMenu(req, resp, role);
            } else {
                moveToMenu(req, resp, Role.UNKNOWN);
            }
            log.info("User: " + login + " passed authentication and authorization filter successfully");
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
    private void moveToMenu(HttpServletRequest req,
                            HttpServletResponse resp,
                            Role role) throws ServletException, IOException {

        String locale = (String) req.getSession().getAttribute("locale");

        if (role.equals(Role.ADMINISTRATOR)) {
            long numberRecordsInDatabase = orderCRUD.findNumberRecords();

            if (numberRecordsInDatabase == 0) {
                req.getRequestDispatcher(Routes.PAGE_MESSAGE_ADMIN)
                        .forward(req, resp);
            } else {
                // set attribute for pagination, total_records = all records from database
                req.setAttribute("total_records", numberRecordsInDatabase);

                // find all users for filter for report`s page
                var allUsersForFilter = userCRUD.findAllLoginsClient();

                // find all started at dates for report`s page
                var allStartedAtDates = orderCRUD.findAllStartedAtDatesFromOrder();

                // set attribute for pagination, current_page = 0
                int page = 0;
                req.setAttribute("current_page", page);
                // find all orders with pagination for report`s page
                new PaginationConfig().config(req);
                var allOrders = orderCRUD.findAll(PageableRequest.getPageableRequest(page), locale);

                req.getSession().setAttribute("datesOfOrders", allStartedAtDates);
                req.getSession().setAttribute("customersOfOrders", allUsersForFilter);

                PageMessageBuilder.buildMessageAdmin(req, locale,
                        "whoseOrders", Constants.ADMIN_REPORT_ALL_UK, Constants.ADMIN_REPORT_ALL);

                req.getSession().setAttribute("listOfSort", Arrays.asList("-----", "without sorting", Constants.SORTING_ASC, Constants.SORTING_DESC));
                req.getSession().setAttribute("orders", allOrders);

                req.getRequestDispatcher(Routes.PAGE_REPORT)
                        .forward(req, resp);
            }
        } else if (role.equals(Role.CLIENT)) {
            var allStartEnd = routeCRUD.findAllByLocale(locale);

            req.getSession().setAttribute("allStartEnd", allStartEnd);

            req.getRequestDispatcher(Routes.PAGE_ORDER)
                    .forward(req, resp);
        } else {
            req.getRequestDispatcher(Routes.PAGE_LOGIN)
                    .forward(req, resp);
        }
    }

    /**
     * To process Post requests from user:
     * validation a user`s login.
     *
     * @param req    the HttpServletRequest request
     * @param resp   the HttpServletResponse response
     * @param locale the default or current locale of application
     * @return true if user credentials is valid
     */
    private boolean loginValidation(HttpServletRequest req, HttpServletResponse resp, String locale)
            throws ServletException, IOException {
        if (!(DataValidator.initLoginValidation(req))) {
            log.info("User credentials not validated");

            PageMessageBuilder.buildMessageUser(req, locale, Constants.USER_UK, Constants.USER);

            req.getRequestDispatcher(Routes.PAGE_MESSAGE_USER)
                    .forward(req, resp);
            return false;
        }
        return true;
    }
}
