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
        String startEnd,
        int numberOfPassengers,
        String carCategory,
        LocalDateTime startedAt,
        String carStatus
) {

    /**
     * Create the new record for OrderRequest.
     *
     * @param req               HttpServletRequest request
     * @param dateTimeOfRide    date and time of ride
     * @return new record
     */
    public static OrderRequest getOrderRequest(HttpServletRequest req, LocalDateTime dateTimeOfRide) {
        return new OrderRequest(
                (String) req.getSession().getAttribute("login"),
                null,
                0.0,
                req.getParameter("startEnd"),
                Integer.parseInt(req.getParameter("numberOfPassengers")),
                req.getParameter("carCategory"),
                dateTimeOfRide,
                CarStatus.AVAILABLE.toString()
        );
    }

    /**
     * Create the new record for OrderRequest.
     *
     * @param req               HttpServletRequest request
     * @param cars              cars for OrderRequest from the database
     * @param dateTimeOfRide    date and time of ride
     * @return new record
     */
    public static OrderRequest getOrderRequest(HttpServletRequest req, List<Car> cars, LocalDateTime dateTimeOfRide) {
        return new OrderRequest(
                (String) req.getSession().getAttribute("login"),
                cars,
                req.getSession().getAttribute("loyaltyPrice") == null ? 0 : (double) req.getSession().getAttribute("loyaltyPrice"),
                (String) req.getSession().getAttribute("startEnd"),
                req.getSession().getAttribute("numberOfPassengers") == null ? 0 : Integer.parseInt((String) req.getSession().getAttribute("numberOfPassengers")),
                (String) req.getSession().getAttribute("carCategory"),
                dateTimeOfRide,
                CarStatus.AVAILABLE.toString()
        );
    }

    /**
     * Create the new record for OrderRequest.
     *
     * @param customerFromRequest   cars for OrderRequest from the database
     * @param date                  date of ride
     * @return new record
     */
    public static OrderRequest getRequest(String customerFromRequest, LocalDateTime date) {
        return new OrderRequest(
                customerFromRequest, null, 0.0, null, 0, null, date, null
        );
    }
}
