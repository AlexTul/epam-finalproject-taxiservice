package com.epam.alextuleninov.taxiservice.dao.order;

import com.epam.alextuleninov.taxiservice.ConstantsTest;
import com.epam.alextuleninov.taxiservice.connectionpool.DataSourceFields;
import com.epam.alextuleninov.taxiservice.dao.mappers.CarMapper;
import com.epam.alextuleninov.taxiservice.dao.mappers.ResultSetMapper;
import com.epam.alextuleninov.taxiservice.dao.mappers.UserMapper;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.exceptions.datasource.UnexpectedDataAccessException;
import com.epam.alextuleninov.taxiservice.model.car.Car;
import com.epam.alextuleninov.taxiservice.model.order.Order;
import com.epam.alextuleninov.taxiservice.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.epam.alextuleninov.taxiservice.TestUtils.*;
import static com.epam.alextuleninov.taxiservice.TestUtils.getTestOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

public class JDBCOrderDAOTest {

    private DataSource dataSource;
    private ResultSet resultSet;
    private OrderDAO orderDAO;
    private ResultSetMapper<Car> carMapper;
    private ResultSetMapper<User> userMapper;

    @BeforeEach
    void setUp() {
        this.dataSource = mock(DataSource.class);
        this.resultSet = mock(ResultSet.class);
        this.carMapper = new CarMapper();
        this.userMapper = new UserMapper();
        this.orderDAO = new JDBCOrderDAO(dataSource, carMapper, userMapper);
    }

    @Test
    void testCreate() throws SQLException {
        // Create a new OrderRequest with test data
        var orderRequest = new OrderRequest(
                getTestOrderRequest().customer(),
                getTestOrderRequest().cars(),
                getTestOrderRequest().loyaltyPrice(),
                getTestOrderRequest().startTravel(),
                getTestOrderRequest().endTravel(),
                getTestOrderRequest().travelDistance(),
                getTestOrderRequest().travelDuration(),
                getTestOrderRequest().numberOfPassengers(),
                getTestOrderRequest().carCategory(),
                LocalDateTime.of(2023, 3, 9, 12, 0, 0),
                getTestOrderRequest().carStatus()
        );

        // Mock the Connection and PreparedStatements needed for the test
        DataSource dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(mock(PreparedStatement.class));

        // mock the ResultSet returned by the getUserByEmail PreparedStatement
        ResultSet resultSetUser = mock(ResultSet.class);
        when(resultSetUser.next()).thenReturn(true);
        when(resultSetUser.getLong(DataSourceFields.USER_ID)).thenReturn(ConstantsTest.USER_ID_VALUE);
        when(resultSetUser.getString(DataSourceFields.USER_FIRST_NAME)).thenReturn(ConstantsTest.USER_FIRST_NAME_VALUE);
        when(resultSetUser.getString(DataSourceFields.USER_LAST_NAME)).thenReturn(ConstantsTest.USER_LAST_NAME_VALUE);
        when(resultSetUser.getString(DataSourceFields.USER_EMAIL)).thenReturn(ConstantsTest.USER_EMAIL_VALUE);
//        when(resultSetUser.getString(DataSourceFields.USER_PASSWORD)).thenReturn(ConstantsTest.USER_ENCRYPT_PASSWORD_VALUE);
        when(resultSetUser.getString(DataSourceFields.USER_ROLE)).thenReturn(ConstantsTest.USER_ROLE_CLIENT_VALUE);

        PreparedStatement getUserByEmail = mock(PreparedStatement.class);
        when(connection.prepareStatement(anyString())).thenReturn(getUserByEmail);
        when(getUserByEmail.executeQuery()).thenReturn(resultSetUser);
//        doReturn(resultSetUser).when(getUserByEmail).executeQuery();

        // mock the ResultSet returned by the createOrder PreparedStatement
        ResultSet generatedKeys = mock(ResultSet.class);
        when(generatedKeys.next()).thenReturn(true);
        when(generatedKeys.getLong(1)).thenReturn(1L);

        PreparedStatement createOrder = mock(PreparedStatement.class);
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(createOrder);
        when(createOrder.getGeneratedKeys()).thenReturn(generatedKeys);

        // mock the ResultSet returned by the createOrderCar PreparedStatement
        PreparedStatement createOrderCar = mock(PreparedStatement.class);
        when(connection.prepareStatement(anyString())).thenReturn(createOrderCar);

        // Create a new OrderDAO using the mock DataSource
        var orderDAO = new JDBCOrderDAO(dataSource, carMapper, userMapper);

        // Call the create() method and verify that it returns a new Order with the correct data
        var order = orderDAO.create(orderRequest);

        // verify that the correct SQL statements were executed
//        verify(getUserByEmail).setString(1, "customer@example.com");
//        verify(getUserByEmail).executeQuery();
//
//        verify(createOrder).setTimestamp(1, Timestamp.valueOf(order.getCreatedOn()));
//        verify(createOrder).setLong(2, 1L);
//        verify(createOrder).setInt(3, 2);
//        verify(createOrder).setString(4, "Start");
//        verify(createOrder).setString(5, "End");
//        verify(createOrder).setDouble(6, 10.0);
//        verify(createOrder).setInt(7, 60);
//        verify(createOrder).setDouble(8, 100.0);
//        verify(createOrder).setTimestamp(9, Timestamp.valueOf(request.startedAt()));
//        verify(createOrder).setTimestamp(10, Timestamp.valueOf(request.startedAt().plusSeconds(request.travelDuration())));
//        verify(createOrder).executeUpdate();
//
//        verify(createOrderCar).setLong(1, 1L);
//        verify(createOrderCar).setInt(2, 1);
//        verify(createOrderCar).executeUpdate();
//
//        verify(createOrderCar).setLong(1, 1L);
//        verify(createOrderCar).setInt(2, 2);
//        verify(createOrderCar).executeUpdate();

        assertNotNull(order);
        assertEquals(1L, order.getId());
        assertEquals(orderRequest.customer(), order.getCustomer().getEmail());
        assertEquals(orderRequest.numberOfPassengers(), order.getNumberOfPassengers());
        assertEquals(orderRequest.cars(), order.getCars());
        assertEquals(orderRequest.startTravel(), order.getStartTravel());
        assertEquals(orderRequest.endTravel(), order.getEndTravel());
        assertEquals(orderRequest.travelDistance(), order.getTravelDistance(), 0.0);
        assertEquals(orderRequest.travelDuration(), order.getTravelDuration());
        assertEquals(orderRequest.loyaltyPrice(), order.getCost(), 0.0);
        assertEquals(orderRequest.startedAt(), order.getStartedAt());
        assertEquals(orderRequest.startedAt().plusSeconds(orderRequest.travelDuration()), order.getFinishedAt());
    }

    @Test
    void testSQLExceptionCreate() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> orderDAO.create(getTestOrderRequest()));
    }

    @Test
    void testFindAll() throws SQLException {
        // create mock-objects
        Connection connection = mock(Connection.class);
        PreparedStatement getAllOrdersID = mock(PreparedStatement.class);
        PreparedStatement getAllCarsByOrderID = mock(PreparedStatement.class);
        PreparedStatement getAllOrders = mock(PreparedStatement.class);
        ResultSet rsAllOrdersID = mock(ResultSet.class);
        ResultSet rsAllCarsByOrderID = mock(ResultSet.class);
        ResultSet rsAllOrders = mock(ResultSet.class);

        // configure mock objects
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(getAllOrdersID, getAllCarsByOrderID, getAllOrders);
        when(getAllOrdersID.executeQuery()).thenReturn(rsAllOrdersID);
        when(getAllCarsByOrderID.executeQuery()).thenReturn(rsAllCarsByOrderID);
        when(getAllOrders.executeQuery()).thenReturn(rsAllOrders);
        when(rsAllOrdersID.next()).thenReturn(true, false);

        when(rsAllOrdersID.getLong(DataSourceFields.ORDER_ID)).thenReturn(ConstantsTest.ORDER_ID_VALUE);

        // prepare ResultSet for Cars and Orders
        prepareResultSetCarsForFind(rsAllCarsByOrderID);
        prepareResultSetOrderForFind(rsAllOrders);

        // execute test method
        List<Order> orders = orderDAO.findAll(getTestPageableRequest());

        // verify results
        assertEquals(1, orders.size());
        var order = orders.get(0);

        prepareAssertEqualsForFind(order);
    }

    @Test
    void testSQLExceptionFindAll() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> orderDAO.findAll(getTestPageableRequest()));
    }

    @Test
    void testFindAllByRange() throws SQLException {
        // create mock-objects
        Connection connection = mock(Connection.class);
        PreparedStatement getAllOrdersID = mock(PreparedStatement.class);
        PreparedStatement getAllCarsByOrderID = mock(PreparedStatement.class);
        PreparedStatement getAllOrders = mock(PreparedStatement.class);
        ResultSet rsAllOrdersID = mock(ResultSet.class);
        ResultSet rsAllCarsByOrderID = mock(ResultSet.class);
        ResultSet rsAllOrders = mock(ResultSet.class);

        // configure mock objects
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(getAllOrdersID, getAllCarsByOrderID, getAllOrders);
        when(getAllOrdersID.executeQuery()).thenReturn(rsAllOrdersID);
        when(getAllCarsByOrderID.executeQuery()).thenReturn(rsAllCarsByOrderID);
        when(getAllOrders.executeQuery()).thenReturn(rsAllOrders);
        when(rsAllOrdersID.next()).thenReturn(true, false);

        when(rsAllOrdersID.getLong(DataSourceFields.ORDER_ID)).thenReturn(ConstantsTest.ORDER_ID_VALUE);

        // prepare ResultSet for Cars and Orders
        prepareResultSetCarsForFind(rsAllCarsByOrderID);
        prepareResultSetOrderForFind(rsAllOrders);

        // execute test method
        List<Order> orders = orderDAO.findAllByRange(getTestOrderRequest());

        // verify results
        assertEquals(1, orders.size());
        var order = orders.get(0);

        prepareAssertEqualsForFind(order);
    }

    @Test
    void testSQlExceptionFindAllByRange() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> orderDAO.findAllByRange(getTestOrderRequest()));
    }

    @Test
    void testFindAllByCustomer() throws SQLException {
        // create mock-objects
        Connection connection = mock(Connection.class);
        PreparedStatement getAllOrdersID = mock(PreparedStatement.class);
        PreparedStatement getAllCarsByOrderID = mock(PreparedStatement.class);
        PreparedStatement getAllOrders = mock(PreparedStatement.class);
        ResultSet rsAllOrdersID = mock(ResultSet.class);
        ResultSet rsAllCarsByOrderID = mock(ResultSet.class);
        ResultSet rsAllOrders = mock(ResultSet.class);

        // configure mock objects
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(getAllOrdersID, getAllCarsByOrderID, getAllOrders);
        when(getAllOrdersID.executeQuery()).thenReturn(rsAllOrdersID);
        when(getAllCarsByOrderID.executeQuery()).thenReturn(rsAllCarsByOrderID);
        when(getAllOrders.executeQuery()).thenReturn(rsAllOrders);
        when(rsAllOrdersID.next()).thenReturn(true, false);

        when(rsAllOrdersID.getLong(DataSourceFields.ORDER_ID)).thenReturn(ConstantsTest.ORDER_ID_VALUE);

        // prepare ResultSet for Cars and Orders
        prepareResultSetCarsForFind(rsAllCarsByOrderID);
        prepareResultSetOrderForFind(rsAllOrders);

        // execute test method
        List<Order> orders = orderDAO.findAllByCustomer(getTestUserRequest().email(), getTestPageableRequest());

        // verify results
        assertEquals(1, orders.size());
        var order = orders.get(0);

        prepareAssertEqualsForFind(order);
    }

    @Test
    void testSQLExceptionFindAllByCustomer() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> orderDAO.findAllByCustomer(
                getTestOrderRequest().customer(), getTestPageableRequest()));
    }

    @Test
    void testFindAllByDate() throws SQLException {
        // create mock-objects
        Connection connection = mock(Connection.class);
        PreparedStatement getAllOrdersID = mock(PreparedStatement.class);
        PreparedStatement getAllCarsByOrderID = mock(PreparedStatement.class);
        PreparedStatement getAllOrders = mock(PreparedStatement.class);
        ResultSet rsAllOrdersID = mock(ResultSet.class);
        ResultSet rsAllCarsByOrderID = mock(ResultSet.class);
        ResultSet rsAllOrders = mock(ResultSet.class);

        // configure mock objects
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(getAllOrdersID, getAllCarsByOrderID, getAllOrders);
        when(getAllOrdersID.executeQuery()).thenReturn(rsAllOrdersID);
        when(getAllCarsByOrderID.executeQuery()).thenReturn(rsAllCarsByOrderID);
        when(getAllOrders.executeQuery()).thenReturn(rsAllOrders);
        when(rsAllOrdersID.next()).thenReturn(true, false);

        when(rsAllOrdersID.getLong(DataSourceFields.ORDER_ID)).thenReturn(ConstantsTest.ORDER_ID_VALUE);

        // prepare ResultSet for Cars and Orders
        prepareResultSetCarsForFind(rsAllCarsByOrderID);
        prepareResultSetOrderForFind(rsAllOrders);

        // execute test method
        List<Order> orders = orderDAO.findAllByDate(ConstantsTest.ORDER_STARTED_AT_VALUE.toLocalDateTime(), getTestPageableRequest());

        // verify results
        assertEquals(1, orders.size());
        var order = orders.get(0);

        prepareAssertEqualsForFind(order);
    }

    @Test
    void testSQLExceptionFindAllByDate() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> orderDAO.findAllByDate(
                getTestOrderRequest().startedAt(), getTestPageableRequest()));
    }

    @Test
    void testFindById() throws SQLException {
        // create mock-objects
        Connection connection = mock(Connection.class);
        PreparedStatement getAllCarsByOrderID = mock(PreparedStatement.class);
        PreparedStatement psGetOrderByID = mock(PreparedStatement.class);
        ResultSet rsAllCarsByOrderID = mock(ResultSet.class);
        ResultSet resultSet = mock(ResultSet.class);

        // create mock-data
        long orderId = 0L;

        // configure mock objects
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(getAllCarsByOrderID, psGetOrderByID);
        when(getAllCarsByOrderID.executeQuery()).thenReturn(rsAllCarsByOrderID);
        when(psGetOrderByID.executeQuery()).thenReturn(resultSet);

        // prepare ResultSet for Cars and Orders
        prepareResultSetCarsForFind(rsAllCarsByOrderID);
        prepareResultSetOrderForFind(resultSet);

        // execute test method
        Optional<Order> result = orderDAO.findByID(orderId);

        // verify results
        assertTrue(result.isPresent());
        var order = result.get();

        prepareAssertEqualsForFind(order);
    }

    @Test
    void testSQLExceptionFindByID() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> orderDAO.findByID(0));
    }

    @Test
    void testFindAllStartedAtDatesFromOrder() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            // if value is present
            prepareResultSetStartedAtDatesFromOrderPresent(resultSet);
            Set<LocalDateTime> resultPresent = orderDAO.findAllStartedAtDatesFromOrder();

            assertEquals(1, resultPresent.size());
            assertEquals(ConstantsTest.ORDER_STARTED_AT_VALUE.toLocalDateTime(), resultPresent.stream().toList().get(0));

            // if value is absent
            prepareResultSetStartedAtDatesFromOrderAbsent(resultSet);
            Set<LocalDateTime> resultAbsent = orderDAO.findAllStartedAtDatesFromOrder();

            assertEquals(0, resultAbsent.size());
        }
    }

    @Test
    void testSqlExceptionFindAllStartedAtDatesFromOrder() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> orderDAO.findAllStartedAtDatesFromOrder());
    }

    @Test
    void testFindNumberRecords() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            prepareResultSetNumberRecordsPresent(resultSet);
            long resultPresent = orderDAO.findNumberRecords();

            assertEquals(1L, resultPresent);
        }
    }

    @Test
    void testSQLExceptionFindNumberRecords() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            prepareResultSetNumberRecordsAbsent(resultSet);

            assertThrows(UnexpectedDataAccessException.class,
                    () -> orderDAO.findNumberRecords());
        }
    }

    @Test
    void testFindNumberRecordsByCustomer() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            prepareResultSetNumberRecordsBy_Present(resultSet);
            long resultPresent = orderDAO.findNumberRecordsByCustomer(getTestOrderRequest().customer());

            assertEquals(1L, resultPresent);
        }
    }

    @Test
    void testSQLExceptionFindNumberRecordsByCustomer() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            prepareResultSetNumberRecordsBy_Absent(resultSet);

            assertThrows(UnexpectedDataAccessException.class,
                    () -> orderDAO.findNumberRecordsByCustomer(getTestOrderRequest().customer()));
        }
    }

    @Test
    void testFindNumberRecordsByDateStartedAt() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            prepareResultSetNumberRecordsBy_Present(resultSet);
            long resultPresent = orderDAO.findNumberRecordsByDateStartedAt(getTestOrderRequest().startedAt());

            assertEquals(1L, resultPresent);
        }
    }

    @Test
    void testSQlExceptionFindNumberRecordsByDateStartedAt() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            prepareResultSetNumberRecordsBy_Absent(resultSet);

            assertThrows(UnexpectedDataAccessException.class,
                    () -> orderDAO.findNumberRecordsByDateStartedAt(getTestOrderRequest().startedAt()));
        }
    }

    @Test
    void testSumCostByCustomer() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            prepareResultSetSumCostByCustomerPresent(resultSet);
            double resultPresent = orderDAO.sumCostByCustomer(getTestOrderRequest());

            assertEquals(ConstantsTest.ORDER_COST_VALUE, resultPresent);
        }
    }

    @Test
    void testSQLExceptionSumCostByCustomer() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            prepareResultSetSumCostByCustomerAbsent(resultSet);

            assertThrows(UnexpectedDataAccessException.class, () -> orderDAO.sumCostByCustomer(getTestOrderRequest()));
        }
    }

    @Test
    void testUpdateById() throws SQLException {
        try (var ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> orderDAO.updateByID(0, getTestOrderRequest()));
        }
    }

    @Test
    void testSQLExceptionUpdateById() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> orderDAO.updateByID(0, getTestOrderRequest()));
    }

    @Test
    void testDeleteById() throws SQLException {
        try (var ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> orderDAO.deleteByID(0));
        }
    }

    @Test
    void testSQLExceptionDeleteById() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> orderDAO.deleteByID(0));
    }

    private void prepareResultSetCarsForFind(ResultSet rsAllCarsByOrderID) throws SQLException {
        when(rsAllCarsByOrderID.next()).thenReturn(true, true, false);
        when(rsAllCarsByOrderID.getInt(DataSourceFields.CAR_ID)).thenReturn(ConstantsTest.CAR_ID_VALUE, 1);
        when(rsAllCarsByOrderID.getString(DataSourceFields.CAR_NAME)).thenReturn(ConstantsTest.CAR_NAME_VALUE, ConstantsTest.CAR_NAME_VALUE);
        when(rsAllCarsByOrderID.getInt(DataSourceFields.CAR_PASSENGERS)).thenReturn(ConstantsTest.CAR_PASSENGERS_VALUE, ConstantsTest.CAR_PASSENGERS_VALUE);
        when(rsAllCarsByOrderID.getString(DataSourceFields.CAR_CAR_CATEGORY)).thenReturn(ConstantsTest.CAR_CAR_CATEGORY_VALUE, ConstantsTest.CAR_CAR_CATEGORY_VALUE);
        when(rsAllCarsByOrderID.getString(DataSourceFields.CAR_STATUS)).thenReturn(ConstantsTest.CAR_STATUS_VALUE, ConstantsTest.CAR_STATUS_VALUE);
    }

    private void prepareResultSetOrderForFind(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getTimestamp(DataSourceFields.ORDER_DATE)).thenReturn(ConstantsTest.ORDER_DATE_VALUE);
        when(resultSet.getLong(DataSourceFields.USER_ID)).thenReturn(ConstantsTest.USER_ID_VALUE);
        when(resultSet.getString(DataSourceFields.USER_FIRST_NAME)).thenReturn(ConstantsTest.USER_FIRST_NAME_VALUE);
        when(resultSet.getString(DataSourceFields.USER_LAST_NAME)).thenReturn(ConstantsTest.USER_LAST_NAME_VALUE);
        when(resultSet.getString(DataSourceFields.USER_EMAIL)).thenReturn(ConstantsTest.USER_EMAIL_VALUE);
        when(resultSet.getString(DataSourceFields.USER_ROLE)).thenReturn(ConstantsTest.USER_ROLE_CLIENT_VALUE);
        when(resultSet.getInt(DataSourceFields.ORDER_PASSENGERS)).thenReturn(ConstantsTest.ORDER_PASSENGERS_VALUE);
        when(resultSet.getString(DataSourceFields.ORDER_START_TRAVEL)).thenReturn(ConstantsTest.ORDER_START_TRAVEL_VALUE);
        when(resultSet.getString(DataSourceFields.ORDER_END_TRAVEL)).thenReturn(ConstantsTest.ORDER_END_TRAVEL_VALUE);
        when(resultSet.getDouble(DataSourceFields.ORDER_TRAVEL_DISTANCE)).thenReturn(ConstantsTest.ORDER_DISTANCE_TRAVEL_VALUE);
        when(resultSet.getInt(DataSourceFields.ORDER_TRAVEL_DURATION)).thenReturn(ConstantsTest.ORDER_DURATION_TRAVEL_VALUE);
        when(resultSet.getDouble(DataSourceFields.ORDER_COST)).thenReturn(ConstantsTest.ORDER_COST_VALUE);
        when(resultSet.getTimestamp(DataSourceFields.ORDER_STARTED_AT)).thenReturn(ConstantsTest.ORDER_STARTED_AT_VALUE);
        when(resultSet.getTimestamp(DataSourceFields.ORDER_FINISHED_AT)).thenReturn(ConstantsTest.ORDER_FINISHED_AT_VALUE);
    }

    private void prepareAssertEqualsForFind(Order order) {
        assertEquals(getTestOrder().getId(), order.getId());
        assertEquals(getTestOrder().getCreatedAt(), order.getCreatedAt());
        assertEquals(getTestOrder().getCustomer().getId(), order.getCustomer().getId());
        assertEquals(getTestOrder().getCustomer().getFirstName(), order.getCustomer().getFirstName());
        assertEquals(getTestOrder().getCustomer().getLastName(), order.getCustomer().getLastName());
        assertEquals(getTestOrder().getCustomer().getEmail(), order.getCustomer().getEmail());
        assertEquals(getTestOrder().getCustomer().getRole(), order.getCustomer().getRole());
        assertEquals(getTestOrder().getNumberOfPassengers(), order.getNumberOfPassengers());

        assertEquals(2, order.getCars().size());

        var carFirst = order.getCars().get(0);
        assertEquals(getTestCar().getId(), carFirst.getId());
        assertEquals(getTestCar().getCarName(), carFirst.getCarName());
        assertEquals(getTestCar().getNumberOfPassengers(), carFirst.getNumberOfPassengers());
        assertEquals(getTestCar().getCarCategory(), carFirst.getCarCategory());
        assertEquals(getTestCar().getCarStatus(), carFirst.getCarStatus());

        var carSecond = order.getCars().get(1);
        assertEquals(1, carSecond.getId());
        assertEquals(getTestCar().getCarName(), carSecond.getCarName());
        assertEquals(getTestCar().getNumberOfPassengers(), carSecond.getNumberOfPassengers());
        assertEquals(getTestCar().getCarCategory(), carSecond.getCarCategory());
        assertEquals(getTestCar().getCarStatus(), carSecond.getCarStatus());

        assertEquals(getTestOrder().getStartTravel(), order.getStartTravel());
        assertEquals(getTestOrder().getEndTravel(), order.getEndTravel());
        assertEquals(getTestOrder().getTravelDistance(), order.getTravelDistance());
        assertEquals(getTestOrder().getTravelDuration(), order.getTravelDuration());
        assertEquals(getTestOrder().getCost(), order.getCost());
        assertEquals(getTestOrder().getStartedAt(), order.getStartedAt());
        assertEquals(getTestOrder().getFinishedAt(), order.getFinishedAt());
    }

    private PreparedStatement prepareMocks(DataSource dataSource) throws SQLException {
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(isA(String.class))).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(isA(int.class), isA(int.class));
        doNothing().when(preparedStatement).setLong(isA(int.class), isA(long.class));
        doNothing().when(preparedStatement).setString(isA(int.class), isA(String.class));
        when(preparedStatement.execute()).thenReturn(true);

        return preparedStatement;
    }

    private static void prepareResultSetStartedAtDatesFromOrderPresent(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getTimestamp(1)).thenReturn(ConstantsTest.ORDER_STARTED_AT_VALUE);
    }

    private static void prepareResultSetStartedAtDatesFromOrderAbsent(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(false);
    }

    private static void prepareResultSetNumberRecordsPresent(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong(1)).thenReturn(1L);
    }

    private static void prepareResultSetNumberRecordsAbsent(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(false);
    }

    private static void prepareResultSetNumberRecordsBy_Present(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong(1)).thenReturn(1L);
    }

    private static void prepareResultSetNumberRecordsBy_Absent(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(false);
    }

    private static void prepareResultSetSumCostByCustomerPresent(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getDouble(1)).thenReturn(ConstantsTest.ORDER_COST_VALUE);
    }

    private static void prepareResultSetSumCostByCustomerAbsent(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(false);
    }
}
