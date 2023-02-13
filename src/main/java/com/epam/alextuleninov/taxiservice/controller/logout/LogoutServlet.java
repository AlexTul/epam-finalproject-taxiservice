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

/**
 * LogoutServlet to handle logout.
 */
@WebServlet(name = "LogoutServlet", urlPatterns = "/logout")
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

        resp.sendRedirect("/");
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

        session.removeAttribute("locale");
        session.removeAttribute("cars");
        session.removeAttribute("loyaltyPrice");
        session.removeAttribute("startEnd");
        session.removeAttribute("numberOfPassengers");
        session.removeAttribute("listOfCars");
        session.removeAttribute("dateOfRide");
        session.removeAttribute("priceOfRide");
        session.removeAttribute("login");
        session.removeAttribute("role");
        session.removeAttribute("orders");
        session.removeAttribute("sort");
        session.removeAttribute("customerOfOrders");
        session.removeAttribute("dateOfOrders");
        session.removeAttribute("orderBy");
        session.removeAttribute("updateOrderID");

        log.info("User: " + session.getAttribute("login") + " logout");
    }
}
