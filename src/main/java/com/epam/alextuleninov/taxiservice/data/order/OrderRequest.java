package com.epam.alextuleninov.taxiservice.data.order;

import com.epam.alextuleninov.taxiservice.model.car.Car;
import com.epam.alextuleninov.taxiservice.model.car.status.CarStatus;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Record for the OrderRequest.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public record OrderRequest(

        String customer,
        List<Car> cars,
        double loyaltyPrice,
        String startTravel,
        String endTravel,
        double travelDistance,
        int travelDuration,
        int numberOfPassengers,
        String carCategory,
        LocalDateTime startedAt,
        String carStatus
) {

    /**
     * Create the new record for OrderRequest.
     *
     * @param req       HttpServletRequest request
     * @param startedAt date and time of travel
     * @return new record
     */
    public static OrderRequest getOrderRequest(HttpServletRequest req, LocalDateTime startedAt) {
        return new OrderRequest(
                (String) req.getSession().getAttribute("login"),
                null,
                0.0,
                req.getParameter("startTravel"),
                req.getParameter("endTravel"),
                0.0,
                0,
                Integer.parseInt(req.getParameter("numberOfPassengers")),
                req.getParameter("carCategory"),
                startedAt,
                CarStatus.AVAILABLE.toString()
        );
    }

    /**
     * Create the new record for OrderRequest.
     *
     * @param req       HttpServletRequest request
     * @param cars      cars for OrderRequest from the database
     * @param startedAt date and time of travel
     * @return new record
     */
    public static OrderRequest getOrderRequest(HttpServletRequest req, List<Car> cars, LocalDateTime startedAt) {
        return new OrderRequest(
                (String) req.getSession().getAttribute("login"),
                cars,
                req.getSession().getAttribute("loyaltyPrice") == null ? 0 : (double) req.getSession().getAttribute("loyaltyPrice"),
                (String) req.getSession().getAttribute("startTravel"),
                (String) req.getSession().getAttribute("endTravel"),
                req.getSession().getAttribute("travelDistance") == null ? 0.0 : (Double) req.getSession().getAttribute("travelDistance"),
                req.getSession().getAttribute("travelDuration") == null ? 0 : (Integer) req.getSession().getAttribute("travelDuration"),
                req.getSession().getAttribute("numberOfPassengers") == null ? 0 : Integer.parseInt((String) req.getSession().getAttribute("numberOfPassengers")),
                (String) req.getSession().getAttribute("carCategory"),
                startedAt,
                CarStatus.AVAILABLE.toString()
        );
    }
}
