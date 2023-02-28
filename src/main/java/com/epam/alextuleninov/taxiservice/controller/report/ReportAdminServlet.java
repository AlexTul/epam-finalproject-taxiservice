package com.epam.alextuleninov.taxiservice.controller.report;

import com.epam.alextuleninov.taxiservice.config.context.AppContext;
import com.epam.alextuleninov.taxiservice.config.pagination.PaginationConfig;
import com.epam.alextuleninov.taxiservice.data.order.OrderResponse;
import com.epam.alextuleninov.taxiservice.data.pageable.PageableRequest;
import com.epam.alextuleninov.taxiservice.service.crud.order.OrderCRUD;
import com.epam.alextuleninov.taxiservice.service.crud.user.UserCRUD;
import com.epam.alextuleninov.taxiservice.service.message.PageMessageBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.epam.alextuleninov.taxiservice.Constants.*;
import static com.epam.alextuleninov.taxiservice.Routes.PAGE_REPORT;
import static com.epam.alextuleninov.taxiservice.Routes.URL_REPORT_ADMIN;

/**
 * ReportServlet for to process a Http request from admin.
 */
@WebServlet(name = "ReportServlet", urlPatterns = URL_REPORT_ADMIN)
public class ReportAdminServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ReportAdminServlet.class);

    private OrderCRUD orderCRUD;
    private UserCRUD userCRUD;

    @Override
    public void init() {
        this.orderCRUD = AppContext.getAppContext().getOrderCRUD();
        this.userCRUD = AppContext.getAppContext().getUserCRUD();
        log.info(getServletName() + " initialized");
    }

    /**
     * To process Get requests from user.
     *
     * @param req  HttpServletRequest request
     * @param resp HttpServletResponse response
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        processRequest(req);

        req.getRequestDispatcher(PAGE_REPORT)
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
        String clientFromRequest = req.getParameter(SCOPE_CUSTOMER_OF_ORDERS);
        String dateFromRequest = req.getParameter(SCOPE_DATE_OF_ORDERS);
        String sortTypeByDateFromRequest = req.getParameter(SCOPE_SORT_BY_DATE);
        Object sortTypeByDateFromSession = req.getSession().getAttribute(SCOPE_SORT_BY_DATE);
        String sortTypeByCostFromRequest = req.getParameter(SCOPE_SORT_BY_COST);
        Object sortTypeByCostFromSession = req.getSession().getAttribute(SCOPE_SORT_BY_COST);
        String locale = (String) req.getSession().getAttribute(SCOPE_LOCALE);

        // filter configuration
        if (clientFromRequest != null) {
            req.getSession().setAttribute(SCOPE_FILTER_BY_CUSTOMER, clientFromRequest);
            req.getSession().removeAttribute(SCOPE_FILTER_BY_DATE);
        }
        if (dateFromRequest != null) {
            req.getSession().setAttribute(SCOPE_FILTER_BY_DATE, dateFromRequest);
            req.getSession().removeAttribute(SCOPE_FILTER_BY_CUSTOMER);
        }

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

        // find all users for filter for report`s page
        var allUsersForFilter = userCRUD.findAllLoginsClient();

        // find all started at dates for report`s page
        var allStartedAtDates = orderCRUD.findAllStartedAtDatesFromOrder();

        long numberRecordsInDatabase;
        String customer = (String) req.getSession().getAttribute(SCOPE_FILTER_BY_CUSTOMER);
        String localeDate = (String) req.getSession().getAttribute(SCOPE_FILTER_BY_DATE);
        if (customer != null && !customer.equals(SCOPE_SORT_ALL)) {
            numberRecordsInDatabase = orderCRUD.findNumberRecordsByCustomer(customer);
            req.setAttribute(SCOPE_TOTAL_RECORDS, numberRecordsInDatabase);
        } else if (localeDate != null && !localeDate.equals(SCOPE_SORT_ALL)) {
            LocalDateTime localeDateTime = LocalDateTime.parse(localeDate.concat(" 00:00"), FORMATTER);
            numberRecordsInDatabase = orderCRUD.findNumberRecordsByDateStartedAt(localeDateTime);
            req.setAttribute(SCOPE_TOTAL_RECORDS, numberRecordsInDatabase);
        } else {
            numberRecordsInDatabase = orderCRUD.findNumberRecords();
            req.setAttribute(SCOPE_TOTAL_RECORDS, numberRecordsInDatabase);
        }

        // config pagination
        var paginationConfig = new PaginationConfig();
        int page = paginationConfig.configPage(req);
        paginationConfig.config(req);

        // find orders by filter with pagination for report`s page
        // configure the request for pagination
        var pageableRequest = PageableRequest.getPageableRequest(page);
        // put all orders from database or put by dateOfOrders or customerOfOrders
        var allOrders = putOrdersFromDBToPage(req, pageableRequest, locale);
        // sorting by date or sorting by cost
        allOrders = getOrdersResponsesSortBy(sortTypeByDateFromSession, sortTypeByCostFromSession, allOrders);

        req.getSession().setAttribute(SCOPE_DATES_OF_ORDERS, allStartedAtDates);
        req.getSession().setAttribute(SCOPE_CUSTOMERS_OF_ORDERS, allUsersForFilter);
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
     * put all orders from database or put by date of orders or customer of orders.
     *
     * @param req      HttpServletRequest request
     * @param pageable pageable with pagination information
     * @param locale   default or current locale of application
     * @return all orders by request
     */
    private List<OrderResponse> putOrdersFromDBToPage(HttpServletRequest req, PageableRequest pageable, String locale) {
        String customer = (String) req.getSession().getAttribute(SCOPE_FILTER_BY_CUSTOMER);
        String localeDate = (String) req.getSession().getAttribute(SCOPE_FILTER_BY_DATE);

        if (customer != null && !customer.equals(SCOPE_SORT_ALL)) {
            PageMessageBuilder.buildMessageAdmin(req, locale, SCOPE_WHOSE_ORDERS,
                    ADMIN_REPORT_CUSTOM_UK + " " + customer, ADMIN_REPORT_CUSTOM + " " + customer);

            return orderCRUD.findAllByCustomer(customer, pageable);
        } else if (localeDate != null && !localeDate.equals(SCOPE_SORT_ALL)) {
            PageMessageBuilder.buildMessageAdmin(req, locale, SCOPE_WHOSE_ORDERS,
                    ADMIN_REPORT_DATE_UK + " " + localeDate, ADMIN_REPORT_DATE + " " + localeDate);

            var localeDateTime = LocalDateTime.parse(localeDate.concat(" 00:00"), FORMATTER);
            return orderCRUD.findAllByDate(localeDateTime, pageable);
        } else {
            PageMessageBuilder.buildMessageAdmin(req, locale, SCOPE_WHOSE_ORDERS,
                    ADMIN_REPORT_ALL_UK, ADMIN_REPORT_ALL);

            return orderCRUD.findAll(pageable);
        }
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
