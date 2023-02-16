package com.epam.alextuleninov.taxiservice.dao.order;

import com.epam.alextuleninov.taxiservice.ConstantsTest;
import com.epam.alextuleninov.taxiservice.connectionpool.DataSourceFields;
import com.epam.alextuleninov.taxiservice.dao.mappers.*;
import com.epam.alextuleninov.taxiservice.exceptions.datasource.UnexpectedDataAccessException;
import com.epam.alextuleninov.taxiservice.model.car.Car;
import com.epam.alextuleninov.taxiservice.model.order.Order;
import com.epam.alextuleninov.taxiservice.model.route.Route;
import com.epam.alextuleninov.taxiservice.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.epam.alextuleninov.taxiservice.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

public class JDBCOrderDAOTest {

    private DataSource dataSource;
    private ResultSet resultSet;
    private OrderDAO orderDAO;

    @BeforeEach
    void setUp() {
        dataSource = mock(DataSource.class);
        resultSet = mock(ResultSet.class);
        ResultSetMapper<Car> carMapper = new CarMapper();
//        ResultSetMapper<Route> routeMapper = new RouteMapper();
        ResultSetMapper<User> userMapper = new UserMapper();
        ResultSetMapper<Order> orderMapper = new OrderMapper();
        orderDAO = new JDBCOrderDAO(dataSource, carMapper, userMapper, orderMapper);
    }

    @Test
    void testFindAll() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            // if value is present
            prepareResultSetPresent(resultSet);
            List<Order> resultPresent = orderDAO.findAll(getTestPageableRequest());

            assertEquals(1, resultPresent.size());
            assertEquals(getTestOrder(), resultPresent.get(0));

            // if value is absent
            prepareResultSetAbsent(resultSet);
            List<Order> resultAbsent = orderDAO.findAll(getTestPageableRequest());

            assertEquals(0, resultAbsent.size());
        }
    }

    @Test
    void testSqlExceptionFindAll() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        try {
            assertThrows(UnexpectedDataAccessException.class, (Executable) orderDAO.findAll(getTestPageableRequest()));
        } catch (UnexpectedDataAccessException ignored) {
        }
    }

    @Test
    void testSqlExceptionFindAllByCustomerRange() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        try {
            assertThrows(UnexpectedDataAccessException.class, (Executable) orderDAO.findAllByRange(getTestOrderRequest()));
        } catch (UnexpectedDataAccessException ignored) {
        }
    }

//    @Test
//    void testFindAllByCustomer() throws SQLException {
//        try (var preparedStatement = prepareMocks(dataSource)) {
//            when(preparedStatement.executeQuery()).thenReturn(resultSet);
//
//            // if value is present
//            prepareResultSetPresent(resultSet);
//            List<Order> resultPresent = orderDAO.findAllByCustomer(getTestOrderRequest(), getTestPageableRequest());
//
//            assertEquals(1, resultPresent.size());
//            assertEquals(getTestOrder(), resultPresent.get(0));
//
//            // if value is absent
//            prepareResultSetAbsent(resultSet);
//            List<Order> resultAbsent = orderDAO.findAllByCustomer(getTestOrderRequest(), getTestPageableRequest());
//
//            assertEquals(0, resultAbsent.size());
//        }
//    }

//    @Test
//    void testSqlExceptionFindAllByCustomer() throws SQLException {
//        when(dataSource.getConnection()).thenThrow(new SQLException());
//        try {
//            assertThrows(UnexpectedDataAccessException.class, (Executable) orderDAO.findAllByCustomer(
//                    getTestOrderRequest(), getTestPageableRequest()));
//        } catch (UnexpectedDataAccessException ignored) {
//        }
//    }

//    @Test
//    void testFindAllByDate() throws SQLException {
//        try (var preparedStatement = prepareMocks(dataSource)) {
//            when(preparedStatement.executeQuery()).thenReturn(resultSet);
//
//            // if value is present
//            prepareResultSetPresent(resultSet);
//            List<Order> resultPresent = orderDAO.findAllByDate(getTestOrderRequest(), getTestPageableRequest());
//
//            assertEquals(1, resultPresent.size());
//            assertEquals(getTestOrder(), resultPresent.get(0));
//
//            // if value is absent
//            prepareResultSetAbsent(resultSet);
//            List<Order> resultAbsent = orderDAO.findAllByDate(getTestOrderRequest(), getTestPageableRequest());
//
//            assertEquals(0, resultAbsent.size());
//        }
//    }

//    @Test
//    void testSqlExceptionFindAllByDate() throws SQLException {
//        when(dataSource.getConnection()).thenThrow(new SQLException());
//        try {
//            assertThrows(UnexpectedDataAccessException.class, (Executable) orderDAO.findAllByDate(
//                    getTestOrderRequest(), getTestPageableRequest()));
//        } catch (UnexpectedDataAccessException ignored) {
//        }
//    }

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
        try {
            assertThrows(UnexpectedDataAccessException.class, (Executable) orderDAO.findAllStartedAtDatesFromOrder());
        } catch (UnexpectedDataAccessException ignored) {
        }
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
    void testSqlExceptionFindNumberRecords() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            prepareResultSetNumberRecordsAbsent(resultSet);

            try {
                orderDAO.findNumberRecords();
            } catch (UnexpectedDataAccessException ignored) {
            }
        }
    }

//    @Test
//    void testFindNumberRecordsByCustomer() throws SQLException {
//        try (var preparedStatement = prepareMocks(dataSource)) {
//            when(preparedStatement.executeQuery()).thenReturn(resultSet);
//
//            prepareResultSetNumberRecordsByCustomerPresent(resultSet);
//            long resultPresent = orderDAO.findNumberRecordsByCustomer(getTestOrderRequest());
//
//            assertEquals(1L, resultPresent);
//        }
//    }

//    @Test
//    void testSqlExceptionFindNumberRecordsByCustomer() throws SQLException {
//        try (var preparedStatement = prepareMocks(dataSource)) {
//            when(preparedStatement.executeQuery()).thenReturn(resultSet);
//
//            prepareResultSetNumberRecordsByCustomerAbsent(resultSet);
//
//            try {
//                orderDAO.findNumberRecordsByCustomer(getTestOrderRequest());
//            } catch (UnexpectedDataAccessException ignored) {
//            }
//        }
//    }

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
    void testSqlExceptionSumCostByCustomer() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            prepareResultSetSumCostByCustomerAbsent(resultSet);

            try {
                orderDAO.sumCostByCustomer(getTestOrderRequest());
            } catch (UnexpectedDataAccessException ignored) {
            }
        }
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

    private static void prepareResultSetPresent(ResultSet resultSet) throws SQLException {
//        when(resultSet.next()).thenReturn(true).thenReturn(false);
//
//        when(resultSet.getLong(DataSourceFields.ORDER_ID)).thenReturn(ConstantsTest.ORDER_ID_VALUE);
//        when(resultSet.getTimestamp(DataSourceFields.ORDER_DATE)).thenReturn(ConstantsTest.ORDER_DATE_VALUE);
//        when(resultSet.getLong(DataSourceFields.USER_ID)).thenReturn(ConstantsTest.USER_ID_VALUE);
//        when(resultSet.getString(DataSourceFields.USER_FIRST_NAME)).thenReturn(ConstantsTest.USER_FIRST_NAME_VALUE);
//        when(resultSet.getString(DataSourceFields.USER_LAST_NAME)).thenReturn(ConstantsTest.USER_LAST_NAME_VALUE);
//        when(resultSet.getString(DataSourceFields.USER_EMAIL)).thenReturn(ConstantsTest.USER_EMAIL_VALUE);
//        when(resultSet.getString(DataSourceFields.USER_ROLE)).thenReturn(ConstantsTest.USER_ROLE_VALUE);
//        when(resultSet.getInt(DataSourceFields.ORDER_PASSENGERS)).thenReturn(ConstantsTest.ORDER_PASSENGERS_VALUE);
//        when(resultSet.getLong(DataSourceFields.ROUTE_ID)).thenReturn(ConstantsTest.ROUTE_ID_VALUE);
//        when(resultSet.getLong(DataSourceFields.ADDRESS_ID)).thenReturn(ConstantsTest.ADDRESS_ID_VALUE);
//        when(resultSet.getString(DataSourceFields.ADDRESS_START_END)).thenReturn(ConstantsTest.ADDRESS_START_END_VALUE);
//        when(resultSet.getString(DataSourceFields.ADDRESS_START_END_UK)).thenReturn(ConstantsTest.ADDRESS_START_END_UK_VALUE);
//        when(resultSet.getLong(DataSourceFields.ROUTE_DISTANCE)).thenReturn(ConstantsTest.ROUTE_DISTANCE_VALUE);
//        when(resultSet.getDouble(DataSourceFields.ROUTE_PRICE)).thenReturn(ConstantsTest.ROUTE_PRICE_VALUE);
//        when(resultSet.getInt(DataSourceFields.ROUTE_TRAVEL_TIME)).thenReturn(ConstantsTest.ROUTE_TRAVEL_TIME_VALUE);
//        when(resultSet.getDouble(DataSourceFields.ORDER_COST)).thenReturn(ConstantsTest.ORDER_COST_VALUE);
//        when(resultSet.getTimestamp(DataSourceFields.ORDER_STARTED_AT)).thenReturn(ConstantsTest.ORDER_STARTED_AT_VALUE);
//        when(resultSet.getTimestamp(DataSourceFields.ORDER_FINISHED_AT)).thenReturn(ConstantsTest.ORDER_FINISHED_AT_VALUE);
    }

    private static void prepareResultSetAbsent(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(false);
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

    private static void prepareResultSetNumberRecordsByCustomerPresent(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong(1)).thenReturn(1L);
    }

    private static void prepareResultSetNumberRecordsByCustomerAbsent(ResultSet resultSet) throws SQLException {
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
