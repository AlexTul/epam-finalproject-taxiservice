package com.epam.alextuleninov.taxiservice.data.car;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Record for the CarRequest.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public record CarRequest(

        String carName,
        int numberOfPassengers,
        String carCategory,
        String carStatus
) {

    /**
     * Create the new record from HttpServletRequest.
     *
     * @param req request from HttpServletRequest
     * @return new record from Car
     */
    public static CarRequest getCarRequest(HttpServletRequest req) {
        return new CarRequest(
                req.getParameter("carName"),
                Integer.parseInt(req.getParameter("numberOfPassengers")),
                req.getParameter("carCategory"),
                req.getParameter("carStatus")
        );
    }
}
