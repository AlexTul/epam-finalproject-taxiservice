package com.epam.alextuleninov.taxiservice.service.crud.route;

import com.epam.alextuleninov.taxiservice.dao.route.RouteDAO;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.data.route.RouteResponse;

import java.util.List;
import java.util.Optional;

/**
 * Class CRUD for Route.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public class RouteService implements RouteCRUD {

    private final RouteDAO routeDAO;

    public RouteService(RouteDAO routeDAO) {
        this.routeDAO = routeDAO;
    }

    /**
     * Find all routes from the database.
     *
     * @return              all routes from the database
     */
    @Override
    public List<String> findAllByLocale(String locale) {
        var routeResponseStream = routeDAO.findAll().stream()
                .map(RouteResponse::fromRoute);

        if (locale.equals("en")) {
            return routeResponseStream.map(RouteResponse::startEnd).toList();
        } else {
            return routeResponseStream.map(RouteResponse::startEndUk).toList();
        }
    }

    /**
     * Find all routes by customer from the database.
     *
     * @param request       request with order`s parameters
     * @return              all routes by customer from the database in response format
     */
    @Override
    public List<RouteResponse> findAllByCustomer(OrderRequest request) {
        return routeDAO.findAllByCustomer(request).stream()
                .map(RouteResponse::fromRoute).toList();
    }

    /**
     * Find route by start and end trip from the database.
     *
     * @param request       request with order`s parameters
     * @return              route by start and end trip from the database in response format
     */
    @Override
    public Optional<RouteResponse> findByStartEnd(OrderRequest request) {
        return routeDAO.findByStartEnd(request)
                .map(RouteResponse::fromRoute);
    }
}
