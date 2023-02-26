package com.epam.alextuleninov.taxiservice.controller.order;

import com.epam.alextuleninov.taxiservice.config.context.AppContext;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.model.car.Car;
import com.epam.alextuleninov.taxiservice.model.user.role.Role;
import com.epam.alextuleninov.taxiservice.service.crud.car.CarCRUD;
import com.epam.alextuleninov.taxiservice.service.crud.order.OrderCRUD;
import com.epam.alextuleninov.taxiservice.service.dateride.DateTimeRide;
import com.epam.alextuleninov.taxiservice.service.loyalty.Loyalty;
import com.epam.alextuleninov.taxiservice.service.routecharacteristics.RouteCharacteristics;
import com.epam.alextuleninov.taxiservice.service.verifyorder.VerifyOrder;
import com.epam.alextuleninov.taxiservice.validation.DataValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.epam.alextuleninov.taxiservice.Constants.*;
import static com.epam.alextuleninov.taxiservice.Routes.*;
import static com.epam.alextuleninov.taxiservice.exceptions.order.OrderExceptions.orderNotFound;

/**
 * OrderServlet for to process a Http request from a user.
 */
@WebServlet(name = "OrderServlet", urlPatterns = URL_ORDER)
public class OrderServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(OrderServlet.class);

    private CarCRUD carCRUD;
    private Loyalty loyaltyService;
    private VerifyOrder verifyOrderOrderService;
    private DateTimeRide dateTimeRideService;
    private RouteCharacteristics routeCharacteristics;
    private OrderCRUD orderCRUD;

    @Override
    public void init() {
        this.carCRUD = AppContext.getAppContext().getCarCRUD();
        this.loyaltyService = AppContext.getAppContext().getLoyaltyService();
        this.verifyOrderOrderService = AppContext.getAppContext().getVerifyService();
        this.dateTimeRideService = AppContext.getAppContext().getDateTimeRide();
        this.routeCharacteristics = AppContext.getAppContext().getRouteCharacteristics();
        this.orderCRUD = AppContext.getAppContext().getOrderCRUD();
        log.info(getServletName() + " initialized");
    }

    /**
     * To process Get requests from user:
     * - if new order - forward on order page;
     * - if update order - forward on order update page
     *
     * @param req  HttpServletRequest request
     * @param resp HttpServletResponse response
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter(SCOPE_ACTION) == null ?
                (String) req.getSession().getAttribute(SCOPE_ACTION) : req.getParameter(SCOPE_ACTION);
        req.getSession().setAttribute(SCOPE_ACTION, action);

        if (action != null && action.equals("new")) {
            action = null;
        }

        if (action != null) {
            if (action.equals("confirm")) {
                if (processRequestGetConfirm(req, resp)) {
                    req.getRequestDispatcher(PAGE_CONFIRM)
                            .forward(req, resp);
                }
            } else if (action.equals("update")) {
                String updateOrderByID = req.getParameter(SCOPE_ID);

                long id;
                if (updateOrderByID != null) {
                    id = Long.parseLong(updateOrderByID);
                    req.getSession().setAttribute(SCOPE_UPDATE_ORDER_ID, updateOrderByID);
                } else {
                    id = Integer.parseInt((String) req.getSession().getAttribute(SCOPE_UPDATE_CAR_ID));
                }

                var orderResponse = orderCRUD.findById(id).orElseThrow(() -> orderNotFound(id));

                req.setAttribute(SCOPE_ORDER_RESPONSE, orderResponse);
                req.getSession().removeAttribute(SCOPE_ACTION);

                req.getRequestDispatcher(PAGE_ORDER_UPDATE)
                        .forward(req, resp);
            }
        } else {
            // show all orders in the database
            processRequestGet(req);

            req.getRequestDispatcher(PAGE_ORDER)
                    .forward(req, resp);
        }
    }

    /**
     * To process Post requests:
     * - update order in the database;
     * - delete order in the database;
     * - create order in the database.
     *
     * @param req  HttpServletRequest request
     * @param resp HttpServletResponse response
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        // update order in the database
        String updateOrderID = (String) req.getSession().getAttribute(SCOPE_UPDATE_ORDER_ID);
        // delete order
        String deleteOrderID = req.getParameter(SCOPE_ID);

        OrderRequest request = null;
        if (deleteOrderID == null) {
            @SuppressWarnings("unchecked")
            var cars = (List<Car>) req.getSession().getAttribute(SCOPE_CARS);

            var dateTimeOfTravel = LocalDateTime.parse(
                    (CharSequence) req.getSession().getAttribute(SCOPE_DATE_OF_TRAVEL), FORMATTER);

            request = OrderRequest.getOrderRequest(req, cars, dateTimeOfTravel);
        }

        if (updateOrderID != null) {
            orderCRUD.updateById(Long.parseLong(updateOrderID), request);
            req.getSession().removeAttribute(SCOPE_UPDATE_ORDER_ID);
            req.getSession().removeAttribute(SCOPE_CARS);
            req.getSession().removeAttribute(SCOPE_DATE_OF_TRAVEL);
            if (req.getSession().getAttribute(SCOPE_ROLE).equals(Role.ADMINISTRATOR)) {
                resp.sendRedirect(URL_REPORT_ADMIN);
            } else {
                resp.sendRedirect(URL_REPORT_CUSTOMER);
            }
        } else if (deleteOrderID != null) {
            orderCRUD.deleteById(Long.parseLong(deleteOrderID));
            if (req.getSession().getAttribute(SCOPE_ROLE).equals(Role.ADMINISTRATOR)) {
                resp.sendRedirect(URL_REPORT_ADMIN);
            } else {
                resp.sendRedirect(URL_REPORT_CUSTOMER);
            }
        } else {
            orderCRUD.create(request);
            carCRUD.changeCarStatus(request);

            req.getSession().removeAttribute(SCOPE_ACTION);
            req.getSession().removeAttribute(SCOPE_CARS);
            req.getSession().removeAttribute(SCOPE_CARS);
            req.getSession().removeAttribute(SCOPE_DATE_OF_TRAVEL);

            req.getSession().setAttribute(SCOPE_DATE_TIME_OF_TRAVEL, request.startedAt().format(FORMATTER));

            resp.sendRedirect(URL_MESSAGE);
        }
    }

    @Override
    public void destroy() {
        log.info(getServletName() + " destroyed");
    }

    /**
     * To process Get requests:
     * - if it`s cancellation of car reservation - generating a request and un booking the cars;
     * - if it is a change of order - request routes from the database and from the order page
     *
     * @param req HttpServletRequest request
     */
    private void processRequestGet(HttpServletRequest req) {
        @SuppressWarnings("unchecked")
        var cars = (List<Car>) req.getSession().getAttribute(SCOPE_CARS);

        if (cars != null) {
            var orderRequest = OrderRequest.getOrderRequest(req, cars, null);
            carCRUD.changeCarStatus(orderRequest);
        }
    }

    /**
     * To process Post requests from user:
     * validation an order`s data, calculate the data and time of trip,
     * generating a request for confirming order, calculate loyalty price,
     * setting the attribute in the user`s session.
     *
     * @param req  HttpServletRequest request
     * @param resp HttpServletResponse response
     */
    private boolean processRequestGetConfirm(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        @SuppressWarnings("unchecked")
        var carsFromSession = (List<Car>) req.getSession().getAttribute(SCOPE_CARS);

        if (carsFromSession == null && DataValidator.initValidationOrderData(req)) {
            // calculate the date and time of the trip, taking into account the time the car was delivered
            var dateTimeOfTravel = dateTimeRideService
                    .count(LocalDateTime.parse(req.getParameter(SCOPE_DATE_OF_TRAVEL)));

            var request = OrderRequest.getOrderRequest(req, dateTimeOfTravel);

            var cars = verifyOrderOrderService.checkOrder(request);

            if (cars.size() == 0) {
                log.info("No available cars, order cancellation");
                req.getSession().setAttribute(SCOPE_MESSAGE_ORDER, USER_ORDER_CANCEL);
                req.getSession().setAttribute(SCOPE_MESSAGE_ORDER_UK, USER_ORDER_CANCEL_UK);

                req.getSession().removeAttribute(SCOPE_ACTION);

                resp.sendRedirect(URL_MESSAGE);
                return false;
            } else {
                var stringOfCars = getStringOfCars(cars);

                var routeChar = routeCharacteristics.getRouteCharacteristics(request);

                var loyaltyPrice = loyaltyService.getLoyaltyPrice(request);

                req.getSession().setAttribute(SCOPE_CARS, cars);
                req.getSession().setAttribute(SCOPE_LOYALTY_PRICE, loyaltyPrice.loyaltyPrice());
                req.getSession().setAttribute(SCOPE_START_TRAVEL, req.getParameter(SCOPE_START_TRAVEL));
                req.getSession().setAttribute(SCOPE_END_TRAVEL, req.getParameter(SCOPE_END_TRAVEL));
                req.getSession().setAttribute(SCOPE_TRAVEL_DISTANCE, routeChar.travelDistance());
                req.getSession().setAttribute(SCOPE_TRAVEL_DURATION, routeChar.travelDuration());
                req.getSession().setAttribute(SCOPE_NUMBER_OF_PASSENGERS, req.getParameter(SCOPE_NUMBER_OF_PASSENGERS));
                req.getSession().setAttribute(SCOPE_LIST_OF_CARS, stringOfCars);
                req.getSession().setAttribute(SCOPE_DATE_OF_TRAVEL, dateTimeOfTravel.format(FORMATTER));
                req.getSession().setAttribute(SCOPE_PRICE_OF_TRAVEL, loyaltyPrice.loyaltyPrice());
            }
        }
        return true;
    }

    /**
     * Get string of cars from order.
     *
     * @param cars list with cars
     * @return string with car`s name
     */
    private String getStringOfCars(List<Car> cars) {
        var stringListOfCars = new StringBuilder();
        for (Car variable : cars) {
            stringListOfCars.append(variable.getCarName()).append(", ").append(variable.getCarCategory()).append("; ");
        }
        return stringListOfCars.toString();
    }
}
