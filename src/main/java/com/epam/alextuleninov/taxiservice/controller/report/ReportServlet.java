package com.epam.alextuleninov.taxiservice.controller.report;

import com.epam.alextuleninov.taxiservice.Constants;
import com.epam.alextuleninov.taxiservice.Routes;
import com.epam.alextuleninov.taxiservice.config.context.AppContext;
import com.epam.alextuleninov.taxiservice.config.pagination.PaginationConfig;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.data.order.OrderResponse;
import com.epam.alextuleninov.taxiservice.data.pageable.PageableRequest;
import com.epam.alextuleninov.taxiservice.service.crud.order.OrderCRUD;
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

import static java.util.Objects.nonNull;

/**
 * ReportServlet for to process a Http request from a user.
 */
@WebServlet(name = "ReportServlet", urlPatterns = {"/report"})
public class ReportServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ReportServlet.class);

    private OrderCRUD orderCRUD;

    @Override
    public void init() {
        this.orderCRUD = AppContext.getAppContext().getOrderCRUD();
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

        long numberRecordsInDatabase = orderCRUD.findNumberRecords();

        if (numberRecordsInDatabase == 0) {
            req.getRequestDispatcher(Routes.PAGE_MESSAGE_ADMIN)
                    .forward(req, resp);
        } else {
            processRequest(req);

            req.getRequestDispatcher(Routes.PAGE_REPORT)
                    .forward(req, resp);
        }
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
        String locale = (String) req.getSession().getAttribute("locale");

        String customerFromRequest = req.getParameter("customerOfOrders");
        Object customerFromSession = req.getSession().getAttribute("customerOfOrders");
        String dateFromRequest = req.getParameter("dateOfOrders");
        Object dateFromSession = req.getSession().getAttribute("dateOfOrders");
        String sortTypeByDateFromRequest = req.getParameter("sortByDate");
        Object sortTypeByDateFromSession = req.getSession().getAttribute("sortByDate");
        String sortTypeByCostFromRequest = req.getParameter("sortByCost");
        Object sortTypeByCostFromSession = req.getSession().getAttribute("sortByCost");

        // configuration for pagination for customer
        customerFromRequest = configurePagination(
                customerFromRequest, customerFromSession, req, "customerOfOrders");

        // configuration for pagination for date
        dateFromRequest = configurePagination(
                dateFromRequest, dateFromSession, req, "dateOfOrders");

        // put all orders from database after filtering
        if (customerFromRequest != null && customerFromRequest.equals("all orders")
                || dateFromRequest != null && dateFromRequest.equals("all orders")) {
            customerFromRequest = null;
            dateFromRequest = null;
            req.getSession().removeAttribute("customerOfOrders");
            req.getSession().removeAttribute("dateOfOrders");
        }

        // set current page for pagination
        int page = new PaginationConfig().configPage(req);
        // configure the request for pagination
        var pageableRequest = PageableRequest.getPageableRequest(page);

        // put all orders from database or put by dateOfOrders or customerOfOrders
        var allOrders = putOrdersFromDBToPage(req, customerFromRequest,
                dateFromRequest, dateFromSession, pageableRequest, locale);

        // configuration sorting by date
        if (sortTypeByDateFromRequest != null) {
            req.getSession().removeAttribute("sortByCost");
            sortTypeByCostFromSession = null;
            sortTypeByDateFromSession = configureSort(sortTypeByDateFromRequest, req, "sortByDate");
        }
        // configuration sorting by cost
        if (sortTypeByCostFromRequest != null) {
            req.getSession().removeAttribute("sortByDate");
            sortTypeByDateFromSession = null;
            sortTypeByCostFromSession = configureSort(sortTypeByCostFromRequest, req, "sortByCost");
        }

        // sorting by date or sorting by cost
        allOrders = getOrdersResponsesSortBy(sortTypeByDateFromSession, sortTypeByCostFromSession, allOrders);

        req.getSession().setAttribute("orders", allOrders);
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
        req.getSession().setAttribute("orderBy", sortTypeFromRequest);
        return req.getSession().getAttribute(sortBy);
    }

    /**
     * To process Get requests from user:
     * configuration sorting by date, configuration sorting by cost,
     * configuration for pagination for customer, configuration for pagination for date,
     * set current page for pagination, sorting by date, sorting by cost.
     *
     * @param fromRequest parameter form the customer`s request (attribute) (customer or date) for search orders in database
     * @param fromSession parameter form the customer`s session (attribute) (customer or date) for search orders in database
     * @param req         HttpServletRequest request
     * @param ofOrders    attribute form the scope with customer`s request parameter
     */
    private String configurePagination(String fromRequest, Object fromSession,
                                       HttpServletRequest req, String ofOrders) {
        if (fromRequest == null && fromSession != null) {
            fromRequest = (String) fromSession;
        }
        // for choose another date or customer
        if (fromRequest != null && fromSession != null) {
            req.getSession().removeAttribute(ofOrders);
        }
        return fromRequest;
    }

    /**
     * To process Get requests from user:
     * put all orders from database or put by date of orders or customer of orders.
     *
     * @param req                 HttpServletRequest request
     * @param customerFromRequest customer from customer`s request (attribute) for search orders in database
     * @param dateFromRequest     date from customer`s request (attribute) for search orders in database
     * @param dateFromSession     date from customer`s session (attribute) for search orders in database
     * @param pageable            pageable with pagination information
     * @param locale              default or current locale of application
     */
    private List<OrderResponse> putOrdersFromDBToPage(HttpServletRequest req, String customerFromRequest,
                                                      String dateFromRequest, Object dateFromSession,
                                                      PageableRequest pageable, String locale) {
        List<OrderResponse> allOrders;
        if (dateFromRequest == null && customerFromRequest == null) {
            allOrders = getOrderResponses(req, pageable, locale);
        } else if (!(customerFromRequest == null) && dateFromSession == null) {
            allOrders = getOrderResponses(req, orderCRUD.findNumberRecordsByCustomer(OrderRequest.getRequest(customerFromRequest, null)),
                    orderCRUD.findAllByCustomer(OrderRequest.getRequest(customerFromRequest, null), pageable, locale),
                    "customerOfOrders", customerFromRequest, locale, Constants.ADMIN_REPORT_CUSTOM_UK, Constants.ADMIN_REPORT_CUSTOM);
        } else {
            assert dateFromRequest != null;
            allOrders = getOrderResponses(req, orderCRUD.findNumberRecordsByDateStartedAt(OrderRequest.getRequest(null, LocalDateTime.parse(dateFromRequest.concat(" 00:00"), Constants.FORMATTER))),
                    orderCRUD.findAllByDate(OrderRequest.getRequest(null, LocalDateTime.parse(dateFromRequest.concat(" 00:00"), Constants.FORMATTER)), pageable, locale),
                    "dateOfOrders", dateFromRequest, locale, Constants.ADMIN_REPORT_DATE_UK, Constants.ADMIN_REPORT_DATE);
        }
        return allOrders;
    }

    /**
     * To process Get requests from user:
     * get all orders from database (if dateFromRequest == null && customerFromRequest == null)
     *
     * @param req      HttpServletRequest request
     * @param pageable pageable with pagination information
     * @param locale   default or current locale of application
     */
    private List<OrderResponse> getOrderResponses(HttpServletRequest req, PageableRequest pageable, String locale) {
        List<OrderResponse> allOrders;
        req.setAttribute("total_records", orderCRUD.findNumberRecords());
        new PaginationConfig().config(req);

        allOrders = orderCRUD.findAll(pageable, locale);

        PageMessageBuilder.buildMessageAdmin(req, locale, "whoseOrders",
                Constants.ADMIN_REPORT_ALL_UK, Constants.ADMIN_REPORT_ALL);
        return allOrders;
    }

    /**
     * To process Get requests from user:
     * get all orders from database (if (!(customerFromRequest == null) && dateFromSession == null) else)
     *
     * @param req                     HttpServletRequest request
     * @param numberRecordsByCustomer number of records in the database
     * @param orderCRUD               all orders (from customer or date)
     * @param customerOfOrders        attribute form the scope with customer`s request parameter
     * @param customerFromRequest     customer from customer`s request (attribute) for search orders in database
     * @param locale                  default or current locale of application
     * @param messageUK               message for admin on UK locale
     * @param message                 message for admin on default locale
     */
    private List<OrderResponse> getOrderResponses(HttpServletRequest req, long numberRecordsByCustomer, List<OrderResponse> orderCRUD,
                                                  String customerOfOrders, String customerFromRequest, String locale,
                                                  String messageUK, String message) {
        List<OrderResponse> allOrders;
        req.setAttribute("total_records", numberRecordsByCustomer);
        new PaginationConfig().config(req);

        allOrders = orderCRUD;

        req.getSession().setAttribute(customerOfOrders, customerFromRequest);
        PageMessageBuilder.buildMessageAdmin(req, locale, "whoseOrders",
                messageUK + customerFromRequest,
                message + customerFromRequest);
        return allOrders;
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
        if (nonNull(sortTypeByDateFromSession)) {
            if (sortTypeByDateFromSession.equals(Constants.SORTING_ASC)) {
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
        if (nonNull(sortTypeByCostFromSession)) {
            if (sortTypeByCostFromSession.equals(Constants.SORTING_ASC)) {
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
