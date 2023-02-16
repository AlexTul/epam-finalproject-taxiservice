package com.epam.alextuleninov.taxiservice.service.routecharacteristics;

import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.data.route.RouteCharacteristicsResponse;

/**
 * Interface for determining the characteristics of the route depending on the user's request.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public interface RouteCharacteristics {

    /**
     * Determine the characteristics of the route depending on the user's request.
     *
     * @param request               request from user
     */
    RouteCharacteristicsResponse getRouteCharacteristics(OrderRequest request);
}
