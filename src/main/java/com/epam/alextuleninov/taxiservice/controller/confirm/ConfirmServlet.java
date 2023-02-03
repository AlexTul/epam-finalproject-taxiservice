package com.epam.alextuleninov.taxiservice.controller.confirm;

import com.epam.alextuleninov.taxiservice.Constants;
import com.epam.alextuleninov.taxiservice.Routes;
import com.epam.alextuleninov.taxiservice.config.context.AppContext;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.model.car.Car;
import com.epam.alextuleninov.taxiservice.service.crud.car.CarCRUD;
import com.epam.alextuleninov.taxiservice.service.crud.order.OrderCRUD;
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
 * ConfirmServlet for to process a Http request from a user.
 */
@WebServlet(name = "ConfirmServlet", urlPatterns = "/confirm")
public class ConfirmServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ConfirmServlet.class);

    private OrderCRUD orderCRUD;
    private CarCRUD carCRUD;

    @Override
    public void init() {
        this.orderCRUD = AppContext.getAppContext().getOrderCRUD();
        this.carCRUD = AppContext.getAppContext().getCarCRUD();
        log.info(getServletName() + " initialized");
    }

    /**
     * To process Post requests from user.
     *
     * @param req                   HttpServletRequest request
     * @param resp                  HttpServletResponse response
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        processRequest(req);

        req.getRequestDispatcher(Routes.PAGE_ORDER_SUCCESSFUL)
                .forward(req, resp);
    }

    @Override
    public void destroy() {
        log.info(getServletName() + " destroyed");
    }

    /**
     * To process Post requests from user:
     * generating a request for creating order to the database
     * and setting the date and un booking the cars
     * and time of the trip in the request for presentation to the user.
     *
     * @param req                   HttpServletRequest request
     */
    private void processRequest(HttpServletRequest req) {
        @SuppressWarnings("unchecked")
        var cars = (List<Car>) req.getSession().getAttribute("cars");

        var dateTimeOfRide = LocalDateTime.parse(
                (CharSequence) req.getSession().getAttribute("dateOfRide"), Constants.FORMATTER);

        var orderRequest = OrderRequest.getOrderRequest(req, cars, dateTimeOfRide);

        var order = orderCRUD.create(orderRequest);
        log.info("User with login = " + orderRequest.customer() + " was registered.");

        carCRUD.changeCarStatus(orderRequest);

        req.setAttribute("dateTimeTrip", order.getStartedAt().format(Constants.FORMATTER));
    }
}
