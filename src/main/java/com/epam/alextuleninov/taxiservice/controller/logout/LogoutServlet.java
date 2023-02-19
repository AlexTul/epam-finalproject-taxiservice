package com.epam.alextuleninov.taxiservice.controller.logout;

import com.epam.alextuleninov.taxiservice.config.context.AppContext;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.model.car.Car;
import com.epam.alextuleninov.taxiservice.service.crud.car.CarCRUD;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static com.epam.alextuleninov.taxiservice.Constants.*;
import static com.epam.alextuleninov.taxiservice.Routes.URL_EMPTY_;
import static com.epam.alextuleninov.taxiservice.Routes.URL_LOGOUT;

/**
 * LogoutServlet to handle logout.
 */
@WebServlet(name = "LogoutServlet", urlPatterns = URL_LOGOUT)
public class LogoutServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(LogoutServlet.class);

    private CarCRUD carCRUD;

    @Override
    public void init() {
        this.carCRUD = AppContext.getAppContext().getCarCRUD();
        log.info(getServletName() + " initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        processRequest(req);

        resp.sendRedirect(URL_EMPTY_);
    }

    @Override
    public void destroy() {
        log.info(getServletName() + " destroyed");
    }

    /**
     * To process Get requests from user:
     * generating a request for un booking car if user logout
     * and delete all attribute from user`s session
     * and redirect user on home page.
     *
     * @param req                   HttpServletRequest request
     */
    private void processRequest(HttpServletRequest req) {
        @SuppressWarnings("unchecked")
        var cars = (List<Car>) req.getSession().getAttribute("cars");

        var saveOrderRequest = OrderRequest.getOrderRequest(req, cars, null);

        if (!(cars == null)) {

            carCRUD.changeCarStatus(saveOrderRequest);
        }

        HttpSession session = req.getSession();

        session.removeAttribute(SCOPE_LOGIN);
        session.removeAttribute(SCOPE_ROLE);
        session.removeAttribute(SCOPE_LOCALE);
        session.removeAttribute(SCOPE_CARS);
        session.removeAttribute(SCOPE_LIST_OF_CARS);
        session.removeAttribute(SCOPE_CAR_RESPONSES);
        session.removeAttribute(SCOPE_DATE_OF_TRAVEL);
        session.removeAttribute(SCOPE_PRICE_OF_TRAVEL);
        session.removeAttribute(SCOPE_TRAVEL_DISTANCE);
        session.removeAttribute(SCOPE_TRAVEL_DURATION);
        session.removeAttribute(SCOPE_START_TRAVEL);
        session.removeAttribute(SCOPE_END_TRAVEL);
        session.removeAttribute(SCOPE_LOYALTY_PRICE);
        session.removeAttribute(SCOPE_DATE_TIME_OF_TRAVEL);
        session.removeAttribute(SCOPE_NUMBER_OF_PASSENGERS);
        session.removeAttribute(SCOPE_ORDERS);
        session.removeAttribute(SCOPE_CUSTOMER_OF_ORDERS);
        session.removeAttribute(SCOPE_DATE_OF_ORDERS);
        session.removeAttribute(SCOPE_ORDER_BY);
        session.removeAttribute(SCOPE_UPDATE_ORDER_ID);
        session.removeAttribute(SCOPE_CUSTOMERS_OF_ORDERS);
        session.removeAttribute(SCOPE_DATES_OF_ORDERS);
        session.removeAttribute(SCOPE_USER_RESPONSES);
        session.removeAttribute(SCOPE_MESSAGE_USER);

        log.info("User: " + session.getAttribute("login") + " logout");
    }
}
