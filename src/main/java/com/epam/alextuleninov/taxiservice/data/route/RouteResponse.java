package com.epam.alextuleninov.taxiservice.data.route;

import com.epam.alextuleninov.taxiservice.model.route.Route;

/**
 * Record for the RouteResponse.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public record RouteResponse(

        long id,
        long addressesId,
        String startEnd,
        String startEndUk,
        long distance,
        double price,
        int travelTime
) {

    /**
     * Create the new record from Route.
     *
     * @param route         route entity
     * @return              record from route
     */
    public static RouteResponse fromRoute(Route route) {
        return new RouteResponse(
                route.getId(),
                route.getAddress().id(),
                route.getAddress().startEnd(),
                route.getAddress().startEndUk(),
                route.getDistance(),
                route.getPrice(),
                route.getTravelTime()
        );
    }
}
