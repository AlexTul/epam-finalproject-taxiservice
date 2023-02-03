package com.epam.alextuleninov.taxiservice.dao.route;

import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.model.route.Route;

import java.util.List;
import java.util.Optional;

/**
 * Interface DAO for Route.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public interface RouteDAO {

    /**
     * Find all routes from the database.
     *
     * @return              all routes from the database
     */
    List<Route> findAll();

    /**
     * Find all routes by customer from the database.
     *
     * @param request       request with order`s parameters
     * @return              all routes by customer from the database
     */
    List<Route> findAllByCustomer(OrderRequest request);

    /**
     * Find route by start and end trip from the database.
     *
     * @param request       request with order`s parameters
     * @return              route by start and end trip from the database
     */
    Optional<Route> findByStartEnd(OrderRequest request);
}
