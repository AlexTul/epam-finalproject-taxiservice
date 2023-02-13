package com.epam.alextuleninov.taxiservice.dao.order;

import com.epam.alextuleninov.taxiservice.Constants;
import com.epam.alextuleninov.taxiservice.connectionpool.DataSourceFields;
import com.epam.alextuleninov.taxiservice.dao.mappers.ResultSetMapper;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.data.pageable.PageableRequest;
import com.epam.alextuleninov.taxiservice.exceptions.datasource.UnexpectedDataAccessException;
import com.epam.alextuleninov.taxiservice.model.car.Car;
import com.epam.alextuleninov.taxiservice.model.order.Order;
import com.epam.alextuleninov.taxiservice.model.route.Route;
import com.epam.alextuleninov.taxiservice.model.user.User;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Class DAO for Order.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public class JDBCOrderDAO implements OrderDAO {

    private final DataSource dataSource;
    private final ResultSetMapper<Car> carMapper;
    private final ResultSetMapper<Route> routeMapper;
    private final ResultSetMapper<User> userMapper;
    private final ResultSetMapper<Order> orderMapper;

    public JDBCOrderDAO(DataSource dataSource,
                        ResultSetMapper<Car> carMapper,
                        ResultSetMapper<Route> routeMapper,
                        ResultSetMapper<User> userMapper,
                        ResultSetMapper<Order> orderMapper) {
        this.dataSource = dataSource;
        this.carMapper = carMapper;
        this.routeMapper = routeMapper;
        this.userMapper = userMapper;
        this.orderMapper = orderMapper;

    }

    /**
     * Create the order in the database.
     *
     * @param request request with order parameters
     * @return created order from database
     */
    @Override
    public Order create(OrderRequest request) {
        try (Connection connection = dataSource.getConnection()) {
            boolean autoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            try (var getRoute = connection.prepareStatement(
                    """
                            select * from routes r
                            join addresses a on a.id = r.address_id
                            where a.start_end like ? or a.start_end_uk like ?
                            """
            );

                 var getUserByEmail = connection.prepareStatement(
                         """
                                 select * from users as u where u.email like ?
                                 """
                 );

                 var createOrder = connection.prepareStatement(
                         """
                                 insert into orders (
                                 date, customer_id, order_passengers, route_id, cost, started_at, finished_at)
                                 values (?, ?, ?, ?, ?, ?, ?)
                                 """,
                         Statement.RETURN_GENERATED_KEYS);

                 var createOrderCar = connection.prepareStatement(
                         """
                                 insert into order_car (o_id, c_id) values (?, ?)
                                 """
                 )) {

                getRoute.setString(1, request.startEnd());
                getRoute.setString(2, request.startEnd());
                ResultSet resultSetRoute = getRoute.executeQuery();
                Route route = null;
                if (resultSetRoute.next()) {
                    route = routeMapper.map(resultSetRoute);
                }

                getUserByEmail.setString(1, request.customer());
                ResultSet resultSetUser = getUserByEmail.executeQuery();
                User user = null;
                if (resultSetUser.next()) {
                    user = userMapper.map(resultSetUser);
                }

                LocalDateTime createdOn = LocalDateTime.now();

                // init, check and count start route`s time
                LocalDateTime startRoute = request.startedAt();

                // count finish route`s time
                assert route != null;
                LocalDateTime finishRoute = request.startedAt().plusSeconds(route.getTravelTime());

                // insert data to create order table
                assert user != null;
                createOrder.setTimestamp(1, Timestamp.valueOf(createdOn));
                createOrder.setLong(2, user.getId());
                createOrder.setInt(3, request.numberOfPassengers());
                createOrder.setLong(4, route.getId());
                createOrder.setDouble(5, request.loyaltyPrice());
                createOrder.setTimestamp(6, Timestamp.valueOf(startRoute));
                createOrder.setTimestamp(7, Timestamp.valueOf(finishRoute));

                createOrder.executeUpdate();

                ResultSet generatedKeys = createOrder.getGeneratedKeys();
                generatedKeys.next();
                long id = generatedKeys.getLong(1);

                // insert data to create order_car table
                createOrderCar.setLong(1, id);
                createOrderCar.setInt(2, request.cars().get(0).getId());

                createOrderCar.executeUpdate();

                // insert data to create order_car table another cars
                if (request.cars().size() > 1) {
                    for (int i = 1; i < request.cars().size(); i++) {
                        try (var createOrderCarAnother = connection.prepareStatement(
                                """
                                        insert into order_car (o_id, c_id) values (?, ?)
                                        """
                        )) {
                            createOrderCarAnother.setLong(1, id);
                            createOrderCarAnother.setInt(2, request.cars().get(i).getId());

                            createOrderCarAnother.executeUpdate();
                        }
                    }
                }

                connection.commit();

                return new Order(
                        id,
                        createdOn,
                        user,
                        request.numberOfPassengers(),
                        null,
                        route,
                        request.loyaltyPrice(),
                        startRoute,
                        finishRoute
                );
            } catch (Exception e) {
                connection.rollback();
                throw new UnexpectedDataAccessException(e);
            } finally {
                connection.setAutoCommit(autoCommit);
            }
        } catch (SQLException e) {
            throw new UnexpectedDataAccessException(e);
        }
    }

    /**
     * Find all orders from the database with pagination information.
     *
     * @param pageable pageable with pagination information
     * @return all orders from database with pagination information
     */
    @Override
    public List<Order> findAll(PageableRequest pageable) {
        List<Order> result = new ArrayList<>();

        String sql = "select * from orders o " +
                "join users u on u.id = o.customer_id " +
                "join routes r on r.id = o.route_id join addresses a on a.id = r.address_id " +
                " order by o." + pageable.sortField() + " " + pageable.orderBy() +
                " limit " + pageable.limit() + " offset " + pageable.offset();

        try (Connection connection = dataSource.getConnection()) {
            try (var getAllOrder = connection.prepareStatement(
                    sql
            )) {

                ResultSet resultSet = getAllOrder.executeQuery();

                while (resultSet.next()) {
                    result.add(orderMapper.map(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new UnexpectedDataAccessException(e);
        }
        return result;
    }

    /**
     * Find all orders by range from the database.
     *
     * @param request request with order`s parameters
     * @return all users by range from database
     */
    @Override
    public List<Order> findAllByRange(OrderRequest request) {
        List<Order> orders = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            try (var getRoute = connection.prepareStatement(
                    """
                            select * from routes r
                            join addresses a on a.id = r.address_id
                            where a.start_end like ? or a.start_end_uk like ?
                            """
            );

                 var getAllOrdersIDByNow = connection.prepareStatement(
                         """
                                 select o.id from orders o
                                 where o.started_at < ? and finished_at > ?
                                 """
                 );

                 var getAllCarsByOrderID = connection.prepareStatement(
                         """
                                 select * from order_car oc
                                 join cars c on c.car_id = oc.c_id
                                 where oc.o_id = ? and c.car_category like ?
                                 """
                 );

                 var getAllOrdersByNow = connection.prepareStatement(
                         """
                                 select * from orders o
                                 join users u on u.id = o.customer_id
                                 join routes r on r.id = o.route_id
                                 join addresses a on a.id = r.address_id
                                 where o.started_at < ? and finished_at > ?
                                 """
                 )) {

                getRoute.setString(1, request.startEnd());
                getRoute.setString(2, request.startEnd());

                ResultSet resultSet = getRoute.executeQuery();
                Route route = new Route();
                if (resultSet.next()) {
                    route = routeMapper.map(resultSet);
                }

                // calculate startAt and finishAt by request with a range that takes into account the delivery time of the car
                LocalDateTime startedAt = request.startedAt().minusSeconds(Constants.CAR_DELIVERY_TIME_SECOND)
                        .plusSeconds(route.getTravelTime() + 2 * Constants.CAR_DELIVERY_TIME_SECOND);
                LocalDateTime finishedAt = request.startedAt().minusSeconds(Constants.CAR_DELIVERY_TIME_SECOND);

                getAllOrdersIDByNow.setTimestamp(1, Timestamp.valueOf(startedAt));
                getAllOrdersIDByNow.setTimestamp(2, Timestamp.valueOf(finishedAt));

                // find all order id for car`s select
                Map<Long, List<Car>> mapOrderCars = new HashMap<>();
                ResultSet rsAllOrdersIDByNow = getAllOrdersIDByNow.executeQuery();
                while (rsAllOrdersIDByNow.next()) {
                    List<Car> carsByOrder = new ArrayList<>();

                    // find all cars by order`s id
                    getAllCarsByOrderID.setLong(1, rsAllOrdersIDByNow.getLong(DataSourceFields.ORDER_ID));
                    getAllCarsByOrderID.setString(2, request.carCategory());
                    ResultSet rsAllCarsByOrderID = getAllCarsByOrderID.executeQuery();
                    while (rsAllCarsByOrderID.next()) {
                        carsByOrder.add(carMapper.map(rsAllCarsByOrderID));
                    }
                    mapOrderCars.put(rsAllOrdersIDByNow.getLong(DataSourceFields.ORDER_ID), carsByOrder);
                }

                // find all orders by now
                getAllOrdersByNow.setTimestamp(1, Timestamp.valueOf(startedAt));
                getAllOrdersByNow.setTimestamp(2, Timestamp.valueOf(finishedAt));
                ResultSet rsAllOrdersByNow = getAllOrdersByNow.executeQuery();
                while (rsAllOrdersByNow.next()) {
                    orders.add(
                            new Order(
                                    rsAllOrdersByNow.getLong(DataSourceFields.ORDER_ID),
                                    rsAllOrdersByNow.getTimestamp(DataSourceFields.ORDER_DATE).toLocalDateTime(),
                                    userMapper.map(rsAllOrdersByNow),
                                    rsAllOrdersByNow.getInt(DataSourceFields.ORDER_PASSENGERS),
                                    mapOrderCars.get(rsAllOrdersByNow.getLong(DataSourceFields.ORDER_ID)),
                                    routeMapper.map(rsAllOrdersByNow),
                                    rsAllOrdersByNow.getDouble(DataSourceFields.ORDER_COST),
                                    rsAllOrdersByNow.getTimestamp(DataSourceFields.ORDER_STARTED_AT).toLocalDateTime(),
                                    rsAllOrdersByNow.getTimestamp(DataSourceFields.ORDER_FINISHED_AT).toLocalDateTime()
                            ));
                }
            }
        } catch (NullPointerException e) {
            return orders;
        } catch (SQLException e) {
            throw new UnexpectedDataAccessException(e);
        }
        return orders;
    }

    /**
     * Find all orders by customer from the database.
     *
     * @param customer customer from request
     * @param pageable request with pagination information
     * @return all users by range from database
     */
    @Override
    public List<Order> findAllByCustomer(String customer, PageableRequest pageable) {
        List<Order> result = new ArrayList<>();

        String sql = "select * from orders o " +
                "join users u on u.id = o.customer_id " +
                "join routes r on r.id = o.route_id join addresses a on a.id = r.address_id " +
                "where u.email like '" + customer +
                "' order by o." + pageable.sortField() + " " + pageable.orderBy() +
                " limit " + pageable.limit() + " offset " + pageable.offset();

        return findOrders(result, sql);
    }

    /**
     * Find all orders by date start order from the database.
     *
     * @param startedAt trip start date and time
     * @param pageable  request with pagination information
     * @return all users by range from database
     */
    @Override
    public List<Order> findAllByDate(LocalDateTime startedAt, PageableRequest pageable) {
        List<Order> result = new ArrayList<>();

        String sql = "select * from orders o " +
                "join users u on u.id = o.customer_id " +
                "join routes r on r.id = o.route_id join addresses a on a.id = r.address_id " +
                "where o.started_at >= '" + Timestamp.valueOf(startedAt) +
                "' and o.started_at < '" + Timestamp.valueOf(startedAt.plusDays(1)) +
                "' order by o." + pageable.sortField() + " " + pageable.orderBy() +
                " limit " + pageable.limit() + " offset " + pageable.offset();

        return findOrders(result, sql);
    }

    /**
     * Find all dates by start order from the database.
     *
     * @return all dates by start order from database
     */
    @Override
    public Set<LocalDateTime> findAllStartedAtDatesFromOrder() {
        Set<LocalDateTime> result = new TreeSet<>();

        try (Connection connection = dataSource.getConnection()) {
            try (var getDate = connection.prepareStatement(
                    """
                            select started_at result from orders o
                            """
            )) {

                ResultSet resultSet = getDate.executeQuery();
                while (resultSet.next()) {
                    result.add(resultSet.getTimestamp(1).toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            throw new UnexpectedDataAccessException(e);
        }
        return result;
    }

    /**
     * Find number of records from the database.
     *
     * @return number of record in database
     */
    @Override
    public long findNumberRecords() {
        try (Connection connection = dataSource.getConnection()) {
            try (var getNumberRecords = connection.prepareStatement(
                    """
                            select count(o.id) as result from orders o
                            """
            )) {

                ResultSet resultSet = getNumberRecords.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                } else {
                    throw new UnexpectedDataAccessException("Number of orders records not found");
                }
            }
        } catch (SQLException e) {
            throw new UnexpectedDataAccessException(e);
        }
    }

    /**
     * Find number of records from the database by customer.
     *
     * @param customer customer from request
     * @return number of record in database
     */
    @Override
    public long findNumberRecordsByCustomer(String customer) {
        try (Connection connection = dataSource.getConnection()) {
            try (var getNumberRecordsByCustomer = connection.prepareStatement(
                    """
                            select count(o.id) as result from orders o
                            join users u on u.id = o.customer_id
                            where u.email like ?
                            """
            )) {

                getNumberRecordsByCustomer.setString(1, customer);

                ResultSet resultSet = getNumberRecordsByCustomer.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                } else {
                    throw new UnexpectedDataAccessException("Number of orders records not found");
                }
            }
        } catch (SQLException e) {
            throw new UnexpectedDataAccessException(e);
        }
    }

    /**
     * Find number of records from the database by date start order.
     *
     * @param startedAt trip start date and time
     * @return number of record in database
     */
    @Override
    public long findNumberRecordsByDateStartedAt(LocalDateTime startedAt) {
        try (Connection connection = dataSource.getConnection()) {
            try (var getNumberRecordsByCustomer = connection.prepareStatement(
                    """
                            select count(o.id) as result from orders o
                            where o.started_at >= ? and o.started_at < ?
                            """
            )) {

                getNumberRecordsByCustomer.setTimestamp(1, Timestamp.valueOf(startedAt));
                getNumberRecordsByCustomer.setTimestamp(2, Timestamp.valueOf(startedAt.plusDays(1)));

                ResultSet resultSet = getNumberRecordsByCustomer.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                } else {
                    throw new UnexpectedDataAccessException("Number of orders records not found");
                }
            }
        } catch (SQLException e) {
            throw new UnexpectedDataAccessException(e);
        }
    }

    /**
     * Find sum order`s cost by customer from the database.
     *
     * @param request request with order`s parameters
     * @return sum order`s cost by customer
     */
    @Override
    public double sumCostByCustomer(OrderRequest request) {
        try (Connection connection = dataSource.getConnection()) {
            try (var getAllByCustomer = connection.prepareStatement(
                    """
                            select sum(o.cost) result
                            from orders o
                            join users u on u.id = o.customer_id
                            where u.email like ?
                            """
            )) {

                getAllByCustomer.setString(1, request.customer());

                ResultSet resultSet = getAllByCustomer.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getDouble(1);
                } else {
                    throw new UnexpectedDataAccessException(
                            "Cost of orders by customer: " + request.customer() + " not found");
                }
            }
        } catch (SQLException e) {
            throw new UnexpectedDataAccessException(e);
        }
    }

    /**
     * Delete the order from database.
     *
     * @param id id of order
     */
    public void deleteById(long id) {
        try (Connection connection = dataSource.getConnection()) {
            try (var psOrderCar = connection.prepareStatement(
                    """
                            delete from order_car oc where oc.o_id = ?
                            """
            );
                 var psOrder = connection.prepareStatement(
                         """
                                 delete from orders o where o.id = ?
                                 """
                 )) {

                psOrderCar.setLong(1, id);
                psOrderCar.executeUpdate();

                psOrder.setLong(1, id);
                psOrder.executeUpdate();
            }
        } catch (SQLException e) {
            throw new UnexpectedDataAccessException(e);
        }
    }

    /**
     * Find all orders from the database by SQL.
     *
     * @param sql database query
     * @return query result
     */
    private List<Order> findOrders(List<Order> result, String sql) {
        try (Connection connection = dataSource.getConnection()) {
            try (var getAllByCustomer = connection.prepareStatement(
                    sql
            )) {

                ResultSet resultSet = getAllByCustomer.executeQuery();
                while (resultSet.next()) {
                    result.add(orderMapper.map(resultSet));
                }
            }
        } catch (NullPointerException e) {
            return result;
        } catch (SQLException e) {
            throw new UnexpectedDataAccessException(e);
        }
        return result;
    }
}
