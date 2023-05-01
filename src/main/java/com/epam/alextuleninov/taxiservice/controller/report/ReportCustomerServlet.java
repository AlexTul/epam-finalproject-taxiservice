package com.epam.alextuleninov.taxiservice.controller.report;

import com.epam.alextuleninov.taxiservice.config.context.AppContext;
import com.epam.alextuleninov.taxiservice.config.pagination.PaginationConfig;
import com.epam.alextuleninov.taxiservice.data.pageable.PageableRequest;
import com.epam.alextuleninov.taxiservice.service.crud.OrderCRUD;
import com.epam.alextuleninov.taxiservice.service.Sortable;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.epam.alextuleninov.taxiservice.Constants.*;
import static com.epam.alextuleninov.taxiservice.Routes.PAGE_REPORT_CUSTOMER;
import static com.epam.alextuleninov.taxiservice.Routes.URL_REPORT_CUSTOMER;

/**
 * Servlet for to process a Http request from a customer.
 */
@WebServlet(name = "ReportCustomerServlet", urlPatterns = URL_REPORT_CUSTOMER)
public class ReportCustomerServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ReportCustomerServlet.class);

    private OrderCRUD orderCRUD;
    private Sortable sorter;

    @Override
    public void init() {
        this.orderCRUD = AppContext.getAppContext().getOrderCRUD();
        this.sorter = AppContext.getAppContext().getSorter();
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

        processRequestGet(req);

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
    private void processRequestGet(HttpServletRequest req) {
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

        // set attribute for pagination, total_records = all records from database
        setNumberRecordsInDatabase(req);

        // config pagination
        int currentPage = new PaginationConfig().config(req);
        // find all orders with pagination for report`s page
        var allOrders = orderCRUD.findAllByCustomer(customer, PageableRequest.getPageableRequest(currentPage));
        // sorting by date or sorting by cost
        allOrders = sorter.sorting(sortTypeByDateFromSession, sortTypeByCostFromSession, allOrders);
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
     * Set attribute for pagination, total_records = all records from database.
     *
     * @param req      HttpServletRequest request
     */
    private void setNumberRecordsInDatabase(HttpServletRequest req) {
        String customer = (String) req.getSession().getAttribute(SCOPE_LOGIN);

        long numberRecordsInDatabaseByClient = orderCRUD.findNumberRecordsByCustomer(customer);
        req.setAttribute(SCOPE_TOTAL_RECORDS, numberRecordsInDatabaseByClient);
    }
}
