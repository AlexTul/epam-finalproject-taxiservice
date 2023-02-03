package com.epam.alextuleninov.taxiservice.dao.route;

import com.epam.alextuleninov.taxiservice.dao.mappers.ResultSetMapper;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.exceptions.datasource.UnexpectedDataAccessException;
import com.epam.alextuleninov.taxiservice.model.route.Route;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class DAO for Route.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public class JDBCRouteDAO implements RouteDAO {

    private final DataSource dataSource;
    private final ResultSetMapper<Route> mapper;

    public JDBCRouteDAO(DataSource dataSource, ResultSetMapper<Route> mapper) {
        this.dataSource = dataSource;
        this.mapper = mapper;
    }

    /**
     * Find all routes from the database.
     *
     * @return              all routes from the database
     */
    @Override
    public List<Route> findAll() {
        List<Route> routes = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            try (var getAll = connection.prepareStatement(
                    """
                            select * from routes
                            join addresses a on a.id = routes.address_id
                            order by start_end
                            """
            )) {

                ResultSet resultSet = getAll.executeQuery();
                while (resultSet.next()) {
                    routes.add(mapper.map(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new UnexpectedDataAccessException(e);
        }
        return routes;
    }

    /**
     * Find all routes by customer from the database.
     *
     * @param request       request with order`s parameters
     * @return              all routes by customer from the database
     */
    @Override
    public List<Route> findAllByCustomer(OrderRequest request) {
        List<Route> routes = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            try (var getAllByCustomer = connection.prepareStatement(
                    """
                            select * from routes r
                            join orders o on r.id = o.route_id
                            join addresses a on a.id = r.address_id
                            join users u on u.id = o.customer_id
                            where u.email like ?
                            """
            )) {

                getAllByCustomer.setString(1, request.customer());

                ResultSet resultSet = getAllByCustomer.executeQuery();
                while (resultSet.next()) {
                    routes.add(mapper.map(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new UnexpectedDataAccessException(e);
        }
        return routes;
    }

    /**
     * Find route by start and end trip from the database.
     *
     * @param request       request with order`s parameters
     * @return              route by start and end trip from the database
     */
    @Override
    public Optional<Route> findByStartEnd(OrderRequest request) {
        Route route;

        try (Connection connection = dataSource.getConnection()) {
            try (var getRoute = connection.prepareStatement(
                    """
                            select * from routes r
                            join addresses a on a.id = r.address_id
                            where a.start_end like ? or a.start_end_uk like ?
                            """
            )) {

                getRoute.setString(1, request.startEnd());
                getRoute.setString(2, request.startEnd());

                ResultSet resultSet = getRoute.executeQuery();
                if (resultSet.next()) {
                    route = mapper.map(resultSet);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new UnexpectedDataAccessException(e);
        }
        return Optional.of(route);
    }
}
