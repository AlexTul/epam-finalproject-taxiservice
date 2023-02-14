package com.epam.alextuleninov.taxiservice.controller.confirm;

import com.epam.alextuleninov.taxiservice.Constants;
import com.epam.alextuleninov.taxiservice.Routes;
import com.epam.alextuleninov.taxiservice.config.context.AppContext;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.exceptions.route.RouteExceptions;
import com.epam.alextuleninov.taxiservice.model.car.Car;
import com.epam.alextuleninov.taxiservice.service.crud.car.CarCRUD;
import com.epam.alextuleninov.taxiservice.service.crud.order.OrderCRUD;
import com.epam.alextuleninov.taxiservice.service.crud.route.RouteCRUD;
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
    private RouteCRUD routeCRUD;

    @Override
    public void init() {
        this.orderCRUD = AppContext.getAppContext().getOrderCRUD();
        this.carCRUD = AppContext.getAppContext().getCarCRUD();
        this.routeCRUD = AppContext.getAppContext().getRouteCRUD();
        log.info(getServletName() + " initialized");
    }

    /**
     * To process Get requests from user:
     * change locale for presentation to the user.
     *
     * @param req HttpServletRequest request
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        processRequestGet(req);

        req.getRequestDispatcher(Routes.PAGE_CONFIRM)
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

        processRequestPost(req);

        if (req.getSession().getAttribute("updateOrderID") != null) {
            req.getSession().removeAttribute("updateOrderID");
            resp.sendRedirect("/auth");
        } else {
            resp.sendRedirect("/successful");
        }
    }

    @Override
    public void destroy() {
        log.info(getServletName() + " destroyed");
    }

    /**
     * To process Get requests from user:
     * change locale for presentation to the user.
     *
     * @param req HttpServletRequest request
     */
    private void processRequestGet(HttpServletRequest req) {
        String locale = req.getParameter("locale");

        String startEnd;
        var routeResponse = routeCRUD.findByStartEnd(OrderRequest.getOrderRequest(req, null, null))
                .orElseThrow(() -> RouteExceptions.routeNotFound(OrderRequest.getOrderRequest(req, null, null)));
        if (locale.equals("en")) {
            startEnd = routeResponse.startEnd();
        } else {
            startEnd = routeResponse.startEndUk();
        }
        req.getSession().setAttribute("startEnd", startEnd);
    }

    /**
     * To process Post requests:
     * - if it is creat a new order:
     * generating a request for creating order to the database
     * and setting the date and un booking the cars
     * and time of the trip in the request for presentation to the user.
     * - if it is change of order - update order by ID in the database and on the order page.
     *
     * @param req HttpServletRequest request
     */
    private void processRequestPost(HttpServletRequest req) {
        String locale = (String) req.getSession().getAttribute("locale");
        String orderID = (String) req.getSession().getAttribute("updateOrderID");

        @SuppressWarnings("unchecked")
        var cars = (List<Car>) req.getSession().getAttribute("cars");

        var dateTimeOfRide = LocalDateTime.parse(
                (CharSequence) req.getSession().getAttribute("dateOfRide"), Constants.FORMATTER);

        var orderRequest = OrderRequest.getOrderRequest(req, cars, dateTimeOfRide);

        if (orderID == null) {
            orderCRUD.create(orderRequest, locale);
        } else {
            orderCRUD.updateByID(Long.parseLong(orderID), orderRequest);
        }

        carCRUD.changeCarStatus(orderRequest);

        req.setAttribute("dateTimeTrip", orderRequest.startedAt().format(Constants.FORMATTER));
    }
}
