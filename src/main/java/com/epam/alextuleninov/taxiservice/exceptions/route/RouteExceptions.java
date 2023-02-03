package com.epam.alextuleninov.taxiservice.exceptions.route;

import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for exceptions process for route.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public final class RouteExceptions {

    private static final Logger log = LoggerFactory.getLogger(RouteExceptions.class);

    public RouteExceptions() {
    }

    public static NullPointerException routeNotFound(OrderRequest request) {
        log.info("Route with start-end point '" + request.startEnd() + "' was not found");
        return new NullPointerException("Route with start-end point '" + request.startEnd() + "' was not found");
    }
}
