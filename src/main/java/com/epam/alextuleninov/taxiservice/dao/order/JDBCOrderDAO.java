package com.epam.alextuleninov.taxiservice.dao.order;

import com.epam.alextuleninov.taxiservice.Constants;
import com.epam.alextuleninov.taxiservice.connectionpool.DataSourceFields;
import com.epam.alextuleninov.taxiservice.dao.mappers.ResultSetMapper;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.data.pageable.PageableRequest;
import com.epam.alextuleninov.taxiservice.exceptions.datasource.UnexpectedDataAccessException;
import com.epam.alextuleninov.taxiservice.model.car.Car;
import com.epam.alextuleninov.taxiservice.model.order.Order;
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
    private final ResultSetMapper<User> userMapper;

    public JDBCOrderDAO(DataSource dataSource,
                        ResultSetMapper<Car> carMapper,
                        ResultSetMapper<User> userMapper) {
        this.dataSource = dataSource;
        this.carMapper = carMapper;
        this.userMapper = userMapper;
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

            try (var getUserByEmail = connection.prepareStatement(
                    """
                            select * from users u
                            join roles r on r.id = u.role_id
                            where u.email like ?
                            """
            );

                 var createOrder = connection.prepareStatement(
                         """
                                 insert into orders (
                                 date, customer_id, order_passengers, start_travel, end_travel,
                                 travel_distance, travel_duration, cost, started_at, finished_at)
                                 values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                                 """,
                         Statement.RETURN_GENERATED_KEYS);

                 var createOrderCar = connection.prepareStatement(
                         """
                                 insert into order_car (o_id, c_id) values (?, ?)
                                 """
                 )) {

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
                LocalDateTime finishRoute = request.startedAt().plusSeconds(request.travelDuration());

                // insert data to create order table
                assert user != null;
                createOrder.setTimestamp(1, Timestamp.valueOf(createdOn));
                createOrder.setLong(2, user.getId());
                createOrder.setInt(3, request.numberOfPassengers());
                createOrder.setString(4, request.startTravel());
                createOrder.setString(5, request.endTravel());
                createOrder.setDouble(6, request.travelDistance());
                createOrder.setInt(7, request.travelDuration());
                createOrder.setDouble(8, request.loyaltyPrice());
                createOrder.setTimestamp(9, Timestamp.valueOf(startRoute));
                createOrder.setTimestamp(10, Timestamp.valueOf(finishRoute));

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
                        request.cars(),
                        request.startTravel(),
                        request.endTravel(),
                        request.travelDistance(),
                        request.travelDuration(),
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
        String sql = "select o.id from orders o" +
                " order by o." + pageable.sortField() + " " + pageable.orderBy() +
                " limit " + pageable.limit() + " offset " + pageable.offset();

        return getOrders(sql);
    }

    /**
     * Find all orders by range from the database.
     *
     * @param request request with order`s parameters
     * @return all users by range from database
     */
    @Override
    public List<Order> findAllByRange(OrderRequest request) {
        List<Order> result = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            try (var getAllOrdersIDByRange = connection.prepareStatement(
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

                 var getAllOrdersByRange = connection.prepareStatement(
                         """
                                 select * from orders o
                                 join users u on u.id = o.customer_id
                                 where o.id = ?
                                 """
                 )) {

                // calculate startAt and finishAt by request with a range that takes into account the delivery time of the car
                LocalDateTime startedAt = request.startedAt().minusSeconds(Constants.CAR_DELIVERY_TIME_SECOND)
                        .plusSeconds(request.travelDuration() + 2 * Constants.CAR_DELIVERY_TIME_SECOND);
                LocalDateTime finishedAt = request.startedAt().minusSeconds(Constants.CAR_DELIVERY_TIME_SECOND);

                getAllOrdersIDByRange.setTimestamp(1, Timestamp.valueOf(startedAt));
                getAllOrdersIDByRange.setTimestamp(2, Timestamp.valueOf(finishedAt));

                // find all order id for car`s select
                List<Long> ordersID = new ArrayList<>();
                long id;
                Map<Long, List<Car>> mapOrderCars = new HashMap<>();
                ResultSet rsAllOrdersIDByNow = getAllOrdersIDByRange.executeQuery();
                while (rsAllOrdersIDByNow.next()) {
                    id = rsAllOrdersIDByNow.getLong(DataSourceFields.ORDER_ID);
                    ordersID.add(id);
                    List<Car> carsByOrder = new ArrayList<>();

                    // find all cars by order`s id
                    getAllCarsByOrderID.setLong(1, id);
                    getAllCarsByOrderID.setString(2, request.carCategory());
                    ResultSet rsAllCarsByOrderID = getAllCarsByOrderID.executeQuery();
                    while (rsAllCarsByOrderID.next()) {
                        carsByOrder.add(carMapper.map(rsAllCarsByOrderID));
                    }
                    mapOrderCars.put(id, carsByOrder);
                }

                // get all orders by now
                for (Long variable : ordersID) {
                    getAllOrdersByRange.setLong(1, variable);
                    ResultSet rsAllOrders = getAllOrdersByRange.executeQuery();
                    ordersMap(result, variable, rsAllOrders, mapOrderCars);
                }
            }
        } catch (NullPointerException e) {
            return result;
        } catch (SQLException e) {
            throw new UnexpectedDataAccessException(e);
        }
        return result;
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
        String sql = "select o.id from orders o" +
                " join users u on u.id = o.customer_id" +
                " where u.email like '" + customer +
                "' order by o." + pageable.sortField() + " " + pageable.orderBy() +
                " limit " + pageable.limit() + " offset " + pageable.offset();

        return getOrders(sql);
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
        String sql = "select o.id from orders o" +
                " join users u on u.id = o.customer_id" +
                " where o.started_at >= '" + Timestamp.valueOf(startedAt) +
                "' and o.started_at < '" + Timestamp.valueOf(startedAt.plusDays(1)) +
                "' order by o." + pageable.sortField() + " " + pageable.orderBy() +
                " limit " + pageable.limit() + " offset " + pageable.offset();

        return getOrders(sql);
    }

    /**
     * Find order by id from the database.
     *
     * @return order by id from database in response format
     */
    @Override
    public Optional<Order> findByID(long id) {
        try (Connection connection = dataSource.getConnection()) {
            try (var getAllCarsByOrderID = connection.prepareStatement(
                    """
                            select * from order_car oc
                            join cars c on c.car_id = oc.c_id
                            where oc.o_id = ?
                            """
            );

                 var psGetOrderByID = connection.prepareStatement(
                         """
                                 select * from orders o
                                 join users u on u.id = o.customer_id
                                 join roles r on r.id = u.role_id
                                 where o.id = ?
                                 """
                 )) {

                List<Car> carsByOrder = new ArrayList<>();
                // find all cars by order`s id
                getAllCarsByOrderID.setLong(1, id);
                ResultSet rsAllCarsByOrderID = getAllCarsByOrderID.executeQuery();
                while (rsAllCarsByOrderID.next()) {
                    carsByOrder.add(carMapper.map(rsAllCarsByOrderID));
                }

                // get order by id
                psGetOrderByID.setLong(1, id);

                ResultSet resultSet = psGetOrderByID.executeQuery();
                if (resultSet.next()) {
                    return Optional.of(orderMap(id, resultSet, carsByOrder));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new UnexpectedDataAccessException(e);
        }
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
     * Update the order from database.
     *
     * @param id      id of order
     * @param request request with parameter
     */
    @Override
    public void updateByID(long id, OrderRequest request) {
        try (Connection connection = dataSource.getConnection()) {
            boolean autoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            try (var updateOrderByID = connection.prepareStatement(
                    """
                            update orders o
                            set order_passengers = ?, start_travel = ?, end_travel = ?, travel_distance = ?,
                            travel_duration = ?, cost = ?, started_at = ?, finished_at = ?
                            where o.id = ?
                            """
            );
                 var updateOrderCarByID = connection.prepareStatement(
                         """
                                 update order_car oc
                                 set c_id = ?
                                 where o_id = ?
                                 """
                 )) {

                // init, check and count start route`s time
                LocalDateTime startRoute = request.startedAt();

                // count finish route`s time
                LocalDateTime finishRoute = request.startedAt().plusSeconds(request.travelDuration());

                // insert data to order table
                updateOrderByID.setInt(1, request.numberOfPassengers());
                updateOrderByID.setString(2, request.startTravel());
                updateOrderByID.setString(3, request.endTravel());
                updateOrderByID.setDouble(4, request.travelDistance());
                updateOrderByID.setInt(5, request.travelDuration());
                updateOrderByID.setDouble(6, request.loyaltyPrice());
                updateOrderByID.setTimestamp(7, Timestamp.valueOf(startRoute));
                updateOrderByID.setTimestamp(8, Timestamp.valueOf(finishRoute));
                updateOrderByID.setLong(9, id);

                updateOrderByID.executeUpdate();

                // insert data to order_car table
                updateOrderCarByID.setInt(1, request.cars().get(0).getId());
                updateOrderCarByID.setLong(2, id);

                updateOrderCarByID.executeUpdate();

                // insert data to create order_car table another cars
                if (request.cars().size() > 1) {
                    for (int i = 1; i < request.cars().size(); i++) {
                        try (var updateOrderCarByIDAnother = connection.prepareStatement(
                                """
                                        update order_car oc
                                        set c_id = ?
                                        where o_id = ?
                                        """
                        )) {
                            updateOrderCarByIDAnother.setInt(1, request.cars().get(i).getId());
                            updateOrderCarByIDAnother.setLong(2, id);

                            updateOrderCarByIDAnother.executeUpdate();
                        }
                    }
                }

                connection.commit();
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
     * Delete the order from database.
     *
     * @param id id of order
     */
    @Override
    public void deleteByID(long id) {
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
    private List<Order> getOrders(String sql) {
        List<Order> result = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            try (var getAllOrdersID = connection.prepareStatement(
                    sql
            );

                 var getAllCarsByOrderID = connection.prepareStatement(
                         """
                                 select * from order_car oc
                                 join cars c on c.car_id = oc.c_id
                                 where oc.o_id = ?
                                 """
                 );

                 var getAllOrders = connection.prepareStatement(
                         """
                                 select * from orders o
                                 join users u on u.id = o.customer_id
                                 join roles r on r.id = u.role_id
                                 where o.id = ?
                                 """
                 )) {

                // find all order id for car`s select
                List<Long> ordersID = new ArrayList<>();
                long id;
                Map<Long, List<Car>> mapOrderCars = new HashMap<>();
                ResultSet rsAllOrdersID = getAllOrdersID.executeQuery();
                while (rsAllOrdersID.next()) {
                    id = rsAllOrdersID.getLong(DataSourceFields.ORDER_ID);
                    ordersID.add(id);
                    List<Car> carsByOrder = new ArrayList<>();

                    // find all cars by order`s id
                    getAllCarsByOrderID.setLong(1, id);
                    ResultSet rsAllCarsByOrderID = getAllCarsByOrderID.executeQuery();
                    while (rsAllCarsByOrderID.next()) {
                        carsByOrder.add(carMapper.map(rsAllCarsByOrderID));
                    }
                    mapOrderCars.put(id, carsByOrder);
                }

                // get all result
                for (Long variable : ordersID) {
                    getAllOrders.setLong(1, variable);
                    ResultSet rsAllOrders = getAllOrders.executeQuery();
                    ordersMap(result, variable, rsAllOrders, mapOrderCars);
                }
            }
        } catch (NullPointerException e) {
            return result;
        } catch (SQLException e) {
            throw new UnexpectedDataAccessException(e);
        }
        return result;
    }

    /**
     * The method for entity`s mapping.
     *
     * @param result       result from database
     * @param rsAllOrders  result set
     * @param mapOrderCars map with cars by order
     */
    private void ordersMap(List<Order> result, long variable, ResultSet rsAllOrders, Map<Long, List<Car>> mapOrderCars)
            throws SQLException {
        while (rsAllOrders.next()) {
            result.add(
                    new Order(
                            variable,
                            rsAllOrders.getTimestamp(DataSourceFields.ORDER_DATE).toLocalDateTime(),
                            userMapper.map(rsAllOrders),
                            rsAllOrders.getInt(DataSourceFields.ORDER_PASSENGERS),
                            mapOrderCars.get(variable),
                            rsAllOrders.getString(DataSourceFields.ROUTE_START_TRAVEL),
                            rsAllOrders.getString(DataSourceFields.ROUTE_END_TRAVEL),
                            rsAllOrders.getDouble(DataSourceFields.ROUTE_TRAVEL_DISTANCE),
                            rsAllOrders.getInt(DataSourceFields.ROUTE_TRAVEL_DURATION),
                            rsAllOrders.getDouble(DataSourceFields.ORDER_COST),
                            rsAllOrders.getTimestamp(DataSourceFields.ORDER_STARTED_AT).toLocalDateTime(),
                            rsAllOrders.getTimestamp(DataSourceFields.ORDER_FINISHED_AT).toLocalDateTime()
                    )
            );
        }
    }

    /**
     * The method for entity mapping.
     *
     * @param resultSet   result set
     * @param carsByOrder list with cars by order
     */
    private Order orderMap(long id, ResultSet resultSet, List<Car> carsByOrder) throws SQLException {
        return new Order(
                id,
                resultSet.getTimestamp(DataSourceFields.ORDER_DATE).toLocalDateTime(),
                userMapper.map(resultSet),
                resultSet.getInt(DataSourceFields.ORDER_PASSENGERS),
                carsByOrder,
                resultSet.getString(DataSourceFields.ROUTE_START_TRAVEL),
                resultSet.getString(DataSourceFields.ROUTE_END_TRAVEL),
                resultSet.getDouble(DataSourceFields.ROUTE_TRAVEL_DISTANCE),
                resultSet.getInt(DataSourceFields.ROUTE_TRAVEL_DURATION),
                resultSet.getDouble(DataSourceFields.ORDER_COST),
                resultSet.getTimestamp(DataSourceFields.ORDER_STARTED_AT).toLocalDateTime(),
                resultSet.getTimestamp(DataSourceFields.ORDER_FINISHED_AT).toLocalDateTime()
        );
    }
}
