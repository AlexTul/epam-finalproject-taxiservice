package com.epam.alextuleninov.taxiservice.controller.customer;

import com.epam.alextuleninov.taxiservice.config.context.AppContext;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.model.car.Car;
import com.epam.alextuleninov.taxiservice.service.crud.CarCRUD;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static com.epam.alextuleninov.taxiservice.Constants.SCOPE_CARS;
import static com.epam.alextuleninov.taxiservice.Routes.PAGE_CUSTOMER_MENU;
import static com.epam.alextuleninov.taxiservice.Routes.URL_CUSTOMER;

/**
 * Servlet for to process a Http request from a customer.
 */
@WebServlet(name = "CustomerServlet", urlPatterns = URL_CUSTOMER)
public class CustomerServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(CustomerServlet.class);

    private CarCRUD carCRUD;

    @Override
    public void init() {
        this.carCRUD = AppContext.getAppContext().getCarCRUD();
        log.info(getServletName() + " initialized");
    }

    /**
     * To process Get requests from user:
     * -
     *
     * @param req HttpServletRequest request
     * @param resp HttpServletResponse response
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        processRequestGet(req);

        req.getRequestDispatcher(PAGE_CUSTOMER_MENU)
                .forward(req, resp);
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
}
