package com.epam.alextuleninov.taxiservice.controller.order;

import com.epam.alextuleninov.taxiservice.Constants;
import com.epam.alextuleninov.taxiservice.config.context.AppContext;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.model.car.Car;
import com.epam.alextuleninov.taxiservice.service.crud.car.CarCRUD;
import com.epam.alextuleninov.taxiservice.service.dateride.DateTimeRide;
import com.epam.alextuleninov.taxiservice.service.loyalty.Loyalty;
import com.epam.alextuleninov.taxiservice.service.message.PageMessageBuilder;
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

    @Override
    public void init() {
        this.carCRUD = AppContext.getAppContext().getCarCRUD();
        this.loyaltyService = AppContext.getAppContext().getLoyaltyService();
        this.verifyOrderOrderService = AppContext.getAppContext().getVerifyService();
        this.dateTimeRideService = AppContext.getAppContext().getDateTimeRide();
        this.routeCharacteristics = AppContext.getAppContext().getRouteCharacteristics();
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

        if (req.getSession().getAttribute(SCOPE_UPDATE_ORDER_ID) == null) {
            String updateOrderID = req.getParameter(Constants.SCOPE_ID);
            req.getSession().setAttribute(SCOPE_UPDATE_ORDER_ID, updateOrderID);
        }

        processRequestGet(req);

        req.getRequestDispatcher(PAGE_ORDER)
                .forward(req, resp);
    }

    /**
     * To process Post requests from user.
     *
     * @param req  HttpServletRequest request
     * @param resp HttpServletResponse response
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (req.getParameter(SCOPE_LOCALE) == null) {
            if (processRequestPost(req, resp)) {
                req.getRequestDispatcher(PAGE_CONFIRM)
                        .forward(req, resp);
            }
        } else {
            doGet(req, resp);
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
    private boolean processRequestPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String locale = (String) req.getSession().getAttribute(SCOPE_LOCALE);

        if (!orderValidation(req, resp, locale)) {
            return false;
        }

        // calculate the date and time of the trip, taking into account the time the car was delivered
        var dateTimeOfTravel = dateTimeRideService
                .count(LocalDateTime.parse(req.getParameter(SCOPE_DATE_OF_TRAVEL)));

        var request = OrderRequest.getOrderRequest(req, dateTimeOfTravel);

        var cars = verifyOrderOrderService.checkOrder(request);

        if (cars.size() == 0) {
            log.info("No available cars, order cancellation");

            PageMessageBuilder.buildMessageUser(req, locale, USER_CANCEL_ORDER_UK, USER_CANCEL_ORDER);

            req.getRequestDispatcher(PAGE_MESSAGE_ORDER_USER)
                    .forward(req, resp);
            return false;
        }

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

        return true;
    }

    /**
     * To process Post requests from user:
     * validation an order`s data.
     *
     * @param req    HttpServletRequest request
     * @param resp   HttpServletResponse response
     * @param locale default or current locale of application
     * @return true if user credentials is valid
     */
    private boolean orderValidation(HttpServletRequest req, HttpServletResponse resp, String locale)
            throws ServletException, IOException {
        if (!(DataValidator.initOrderValidation(req))) {
            log.info("User data not validated");

            PageMessageBuilder.buildMessageUser(req, locale, USER_UK, USER);

            req.getRequestDispatcher(PAGE_MESSAGE_USER)
                    .forward(req, resp);
            return false;
        }
        return true;
    }

    /**
     * To process Post requests from user:
     * validation an order`s data.
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
