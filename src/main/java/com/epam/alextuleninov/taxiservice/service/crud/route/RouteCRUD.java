package com.epam.alextuleninov.taxiservice.service.crud.route;

import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.data.route.RouteResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface CRUD for Route.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public interface RouteCRUD {

    /**
     * Find all routes from the database.
     *
     * @return              all routes from the database
     */
    List<String> findAllByLocale(String locale);

    /**
     * Find all routes by customer from the database.
     *
     * @param request       request with order`s parameters
     * @return              all routes by customer from the database in response format
     */
    List<RouteResponse> findAllByCustomer(OrderRequest request);

    /**
     * Find route by start and end trip from the database.
     *
     * @param request       request with order`s parameters
     * @return              route by start and end trip from the database in response format
     */
    Optional<RouteResponse> findByStartEnd(OrderRequest request);
}
