package com.epam.alextuleninov.taxiservice.controller.report;

import com.epam.alextuleninov.taxiservice.config.context.AppContext;
import com.epam.alextuleninov.taxiservice.config.pagination.PaginationConfig;
import com.epam.alextuleninov.taxiservice.data.order.OrderResponse;
import com.epam.alextuleninov.taxiservice.data.pageable.PageableRequest;
import com.epam.alextuleninov.taxiservice.service.crud.order.OrderCRUD;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.epam.alextuleninov.taxiservice.Constants.*;
import static com.epam.alextuleninov.taxiservice.Routes.PAGE_REPORT_CUSTOMER;
import static com.epam.alextuleninov.taxiservice.Routes.URL_REPORT_CUSTOMER;

/**
 * Servlet for to process a Http request from a user.
 */
@WebServlet(name = "ReportCustomerServlet", urlPatterns = URL_REPORT_CUSTOMER)
public class ReportCustomerServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ReportCustomerServlet.class);

    private OrderCRUD orderCRUD;

    @Override
    public void init() {
        this.orderCRUD = AppContext.getAppContext().getOrderCRUD();
        log.info(getServletName() + " initialized");
    }

    /**
     * To process Get requests from user:
     * - forward to report client page.
     *
     * @param req HttpServletRequest request
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        processRequest(req);

        req.getRequestDispatcher(PAGE_REPORT_CUSTOMER)
                .forward(req, resp);
    }

    @Override
    public void destroy() {
        log.info(getServletName() + " destroyed");
    }

    /**
     * To process Get requests from user:
     * configuration sorting by date, configuration sorting by cost,
     * configuration for pagination for customer, configuration for pagination for date,
     * set current page for pagination, sorting by date, sorting by cost.
     *
     * @param req HttpServletRequest request
     */
    private void processRequest(HttpServletRequest req) {
        // section for configuration report page
        String sortTypeByDateFromRequest = req.getParameter(SCOPE_SORT_BY_DATE);
        Object sortTypeByDateFromSession = req.getSession().getAttribute(SCOPE_SORT_BY_DATE);
        String sortTypeByCostFromRequest = req.getParameter(SCOPE_SORT_BY_COST);
        Object sortTypeByCostFromSession = req.getSession().getAttribute(SCOPE_SORT_BY_COST);
        String customer = (String) req.getSession().getAttribute(SCOPE_LOGIN);

        // sort configuration by date
        if (sortTypeByDateFromRequest != null) {
            req.getSession().removeAttribute(SCOPE_SORT_BY_COST);
            sortTypeByCostFromSession = null;
            sortTypeByDateFromSession = configureSort(sortTypeByDateFromRequest, req, SCOPE_SORT_BY_DATE);
        }
        // sort configuration by cost
        if (sortTypeByCostFromRequest != null) {
            req.getSession().removeAttribute(SCOPE_SORT_BY_DATE);
            sortTypeByDateFromSession = null;
            sortTypeByCostFromSession = configureSort(sortTypeByCostFromRequest, req, SCOPE_SORT_BY_COST);
        }

        long numberRecordsInDatabaseByClient = orderCRUD.findNumberRecordsByCustomer(customer);
        // set attribute for pagination, total_records = all records from database
        req.setAttribute(SCOPE_TOTAL_RECORDS, numberRecordsInDatabaseByClient);

        // config pagination
        var paginationConfig = new PaginationConfig();
        int page = paginationConfig.configPage(req);
        paginationConfig.config(req);

        // find all orders with pagination for report`s page
        var allOrders = orderCRUD.findAllByCustomer(customer, PageableRequest.getPageableRequest(page));
        // sorting by date or sorting by cost
        allOrders = getOrdersResponsesSortBy(sortTypeByDateFromSession, sortTypeByCostFromSession, allOrders);
        req.setAttribute(SCOPE_ORDERS, allOrders);
    }

    /**
     * To process Get requests from user:
     * configuration sorting by date, configuration sorting by cost,
     * configuration for pagination for customer, configuration for pagination for date,
     * set current page for pagination, sorting by date, sorting by cost.
     *
     * @param sortTypeFromRequest sort type from request (attribute) (date or cost)
     * @param req                 HttpServletRequest request
     * @param sortBy              attribute form the scope with customer`s request parameter
     */
    private Object configureSort(String sortTypeFromRequest, HttpServletRequest req, String sortBy) {
        req.getSession().setAttribute(sortBy, sortTypeFromRequest);
        req.getSession().setAttribute(SCOPE_ORDER_BY, sortTypeFromRequest);
        return req.getSession().getAttribute(sortBy);
    }

    /**
     * To process Get requests from user:
     * sorting by date.
     *
     * @param sortTypeByDateFromSession sort type by date from session (attribute) for sorting orders by date
     * @param sortTypeByCostFromSession sort type by cost from session (attribute) for sorting orders by cost
     * @param allOrders                 all orders (from customer or date)
     */
    private List<OrderResponse> getOrdersResponsesSortBy(Object sortTypeByDateFromSession, Object sortTypeByCostFromSession,
                                                         List<OrderResponse> allOrders) {
        // sorting by date
        if (sortTypeByDateFromSession != null) {
            if (sortTypeByDateFromSession.equals(SORTING_ASC)) {
                allOrders = allOrders.stream()
                        .sorted(Comparator.comparing(OrderResponse::getCreatedAt))
                        .collect(Collectors.toCollection(ArrayList::new));
            } else {
                allOrders = allOrders.stream()
                        .sorted(Comparator.comparing(OrderResponse::getCreatedAt).reversed())
                        .collect(Collectors.toCollection(ArrayList::new));
            }
        }

        // sorting by cost
        if (sortTypeByCostFromSession != null) {
            if (sortTypeByCostFromSession.equals(SORTING_ASC)) {
                allOrders = allOrders.stream()
                        .sorted(Comparator.comparingDouble(OrderResponse::getCost))
                        .collect(Collectors.toCollection(ArrayList::new));
            } else {
                allOrders = allOrders.stream()
                        .sorted(Comparator.comparingDouble(OrderResponse::getCost).reversed())
                        .collect(Collectors.toCollection(ArrayList::new));
            }
        }
        return allOrders;
    }
}