package com.epam.alextuleninov.taxiservice.controller.confirm;

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

import static com.epam.alextuleninov.taxiservice.Constants.*;
import static com.epam.alextuleninov.taxiservice.Routes.*;

/**
 * ConfirmServlet for to process a Http request from a user.
 */
@WebServlet(name = "ConfirmServlet", urlPatterns = URL_CONFIRM)
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
     * To process Get requests from user:
     * change locale for presentation to the user.
     *
     * @param req HttpServletRequest request
     * @param resp HttpServletResponse response
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher(PAGE_CONFIRM)
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
            throws IOException {

        processRequestPost(req);

        if (req.getSession().getAttribute(SCOPE_UPDATE_ORDER_ID) != null) {
            req.getSession().removeAttribute(SCOPE_UPDATE_ORDER_ID);
            if (req.getSession().getAttribute(SCOPE_ROLE).equals("ADMINISTRATOR")) {
                resp.sendRedirect(URL_REPORT_ADMIN);
            } else {
                resp.sendRedirect(URL_REPORT_CLIENT);
            }
        } else {
            resp.sendRedirect(URL_SUC);
        }
    }

    @Override
    public void destroy() {
        log.info(getServletName() + " destroyed");
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
        String updateOrderID = (String) req.getSession().getAttribute("updateOrderID");

        @SuppressWarnings("unchecked")
        var cars = (List<Car>) req.getSession().getAttribute(SCOPE_CARS);

        var dateTimeOfTravel = LocalDateTime.parse(
                (CharSequence) req.getSession().getAttribute(SCOPE_DATE_OF_TRAVEL), FORMATTER);

        var request = OrderRequest.getOrderRequest(req, cars, dateTimeOfTravel);

        if (updateOrderID == null) {
            orderCRUD.create(request);
        } else {
            orderCRUD.updateById(Long.parseLong(updateOrderID), request);
        }

        carCRUD.changeCarStatus(request);

        req.getSession().setAttribute(SCOPE_DATE_TIME_OF_TRAVEL, request.startedAt().format(FORMATTER));
    }
}
