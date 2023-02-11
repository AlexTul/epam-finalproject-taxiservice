package com.epam.alextuleninov.taxiservice.controller.order;

import com.epam.alextuleninov.taxiservice.Constants;
import com.epam.alextuleninov.taxiservice.Routes;
import com.epam.alextuleninov.taxiservice.config.context.AppContext;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.model.car.Car;
import com.epam.alextuleninov.taxiservice.service.crud.car.CarCRUD;
import com.epam.alextuleninov.taxiservice.service.dateride.DateTimeRide;
import com.epam.alextuleninov.taxiservice.service.loyalty.Loyalty;
import com.epam.alextuleninov.taxiservice.service.message.PageMessageBuilder;
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

/**
 * OrderServlet for to process a Http request from a user.
 */
@WebServlet(name = "OrderServlet", urlPatterns = "/order")
public class OrderServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(OrderServlet.class);

    private CarCRUD carCRUD;
    private Loyalty loyaltyService;
    private VerifyOrder verifyOrderOrderService;
    private DateTimeRide dateTimeRideService;

    @Override
    public void init() {
        this.carCRUD = AppContext.getAppContext().getCarCRUD();
        this.loyaltyService = AppContext.getAppContext().getLoyaltyService();
        this.verifyOrderOrderService = AppContext.getAppContext().getVerifyService();
        this.dateTimeRideService = AppContext.getAppContext().getDateTimeRide();
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

            processRequestGet(req);

            req.getRequestDispatcher(Routes.PAGE_ORDER)
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

        if (req.getParameter("locale") == null) {
            if (processRequestPost(req, resp)) {
                req.getRequestDispatcher(Routes.PAGE_CONFIRM)
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
     * To process Get requests from user:
     * generating a request for un booking the cars
     *
     * @param req HttpServletRequest request
     */
    private void processRequestGet(HttpServletRequest req) {
        @SuppressWarnings("unchecked")
        var cars = (List<Car>) req.getSession().getAttribute("cars");

        var orderRequest = OrderRequest.getOrderRequest(req, cars, null);

        carCRUD.changeCarStatus(orderRequest);
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
        String locale = (String) req.getSession().getAttribute("locale");

        if (!orderValidation(req, resp, locale)) {
            return false;
        }

        // calculate the date and time of the trip, taking into account the time the car was delivered
        var dateTimeOfRide = dateTimeRideService
                .count(LocalDateTime.parse(req.getParameter("dateOfRide")));

        var saveOrderRequest = OrderRequest.getOrderRequest(req, dateTimeOfRide);

        var cars = verifyOrderOrderService.checkOrder(saveOrderRequest);

        if (cars.size() == 0) {
            log.info("No available cars, order cancellation");

            PageMessageBuilder.buildMessageUser(req, locale, Constants.USER_CANCEL_ORDER_UK, Constants.USER_CANCEL_ORDER);

            req.getRequestDispatcher(Routes.PAGE_MESSAGE_USER)
                    .forward(req, resp);
            return false;
        }

        var stringOfCars = getStringOfCars(cars);

        var loyaltyPrice = loyaltyService.getLoyaltyPrice(saveOrderRequest);

        req.getSession().setAttribute("cars", cars);
        req.getSession().setAttribute("loyaltyPrice", loyaltyPrice.loyaltyPrice());

        req.getSession().setAttribute("startEnd", req.getParameter("startEnd"));
        req.getSession().setAttribute("numberOfPassengers", req.getParameter("numberOfPassengers"));
        req.getSession().setAttribute("listOfCars", stringOfCars);
        req.getSession().setAttribute("dateOfRide", dateTimeOfRide.format(Constants.FORMATTER));
        req.getSession().setAttribute("priceOfRide", loyaltyPrice.loyaltyPrice());

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

            PageMessageBuilder.buildMessageUser(req, locale, Constants.USER_UK, Constants.USER);

            req.getRequestDispatcher(Routes.PAGE_MESSAGE_USER)
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
