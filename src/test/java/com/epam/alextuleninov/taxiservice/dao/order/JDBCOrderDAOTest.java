package com.epam.alextuleninov.taxiservice.dao.order;

import com.epam.alextuleninov.taxiservice.ConstantsTest;
import com.epam.alextuleninov.taxiservice.connectionpool.DataSourceFields;
import com.epam.alextuleninov.taxiservice.dao.mappers.*;
import com.epam.alextuleninov.taxiservice.exceptions.datasource.UnexpectedDataAccessException;
import com.epam.alextuleninov.taxiservice.model.car.Car;
import com.epam.alextuleninov.taxiservice.model.order.Order;
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
import java.util.Set;

import static com.epam.alextuleninov.taxiservice.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
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
        ResultSetMapper<User> userMapper = new UserMapper();
        ResultSetMapper<Order> orderMapper = new OrderMapper();
        orderDAO = new JDBCOrderDAO(dataSource, carMapper, userMapper, orderMapper);
    }

    @Test
    void testCreate() throws SQLException {

    }

    @Test
    void testFindAll() throws SQLException {
//        try (var preparedStatement = prepareMocks(dataSource)) {
//            when(preparedStatement.executeQuery()).thenReturn(resultSet);
//
//            // if value is present
//            prepareResultSetPresent(resultSet);
//            List<Order> resultPresent = orderDAO.findAll(getTestPageableRequest());
//
//            assertEquals(1, resultPresent.size());
//            assertEquals(getTestOrder(), resultPresent.get(0));
//
//            // if value is absent
//            prepareResultSetAbsent(resultSet);
//            List<Order> resultAbsent = orderDAO.findAll(getTestPageableRequest());
//
//            assertEquals(0, resultAbsent.size());
//        }
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
    void testSqlExceptionFindAllByRange() throws SQLException {
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

    @Test
    void testSqlExceptionFindAllByCustomer() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        try {
            assertThrows(UnexpectedDataAccessException.class, (Executable) orderDAO.findAllByCustomer(
                    getTestOrderRequest().customer(), getTestPageableRequest()));
        } catch (UnexpectedDataAccessException ignored) {
        }
    }

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

    @Test
    void testSqlExceptionFindAllByDate() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        try {
            assertThrows(UnexpectedDataAccessException.class, (Executable) orderDAO.findAllByDate(
                    getTestOrderRequest().startedAt(), getTestPageableRequest()));
        } catch (UnexpectedDataAccessException ignored) {
        }
    }

    @Test
    void testFindById() throws SQLException {
//        try (var preparedStatement = prepareMocks(dataSource)) {
//            when(preparedStatement.executeQuery()).thenReturn(resultSet);
//
//            long id = 0;
//            // if value is present
//            prepareResultSetPresent(resultSet);
//            Optional<Order> resultPresent = orderDAO.findById(id);
//
//            assertEquals(getTestOrder(), resultPresent.orElseThrow(() -> orderNotFound(id)));
//
//            // if value is absent
//            prepareResultSetAbsent(resultSet);
//            Optional<Order> resultAbsent = orderDAO.findById(id);
//
//            assertEquals(Optional.empty(), resultAbsent);
//        }
    }

    @Test
    void testSqlExceptionFindByID() throws SQLException {
//        when(dataSource.getConnection()).thenThrow(new SQLException());
//        try {
//            assertThrows(UnexpectedDataAccessException.class, carDAO.findByID(0));
//        } catch (UnexpectedDataAccessException ignored) {}
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
    void testSqlExceptionFindNumberRecordsByCustomer() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            prepareResultSetNumberRecordsBy_Absent(resultSet);

            try {
                orderDAO.findNumberRecordsByCustomer(getTestOrderRequest().customer());
            } catch (UnexpectedDataAccessException ignored) {
            }
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
    void testSqlExceptionFindNumberRecordsByDateStartedAt() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            prepareResultSetNumberRecordsBy_Absent(resultSet);

            try {
                orderDAO.findNumberRecordsByDateStartedAt(getTestOrderRequest().startedAt());
            } catch (UnexpectedDataAccessException ignored) {
            }
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

    @Test
    void testUpdateById() throws SQLException {
        try (var ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> orderDAO.updateByID(0, getTestOrderRequest()));
        }
    }

    @Test
    void testSqlExceptionUpdateById() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> orderDAO.updateByID(0, getTestOrderRequest()));
    }

    @Test
    void testDeleteById() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true).thenReturn(false);
            when(resultSet.getLong(1)).thenReturn(1L);

            assertDoesNotThrow(() -> orderDAO.deleteByID(0));
        }
    }

    @Test
    void testSqlExceptionDeleteById() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> orderDAO.deleteByID(0));
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
        when(resultSet.next()).thenReturn(true).thenReturn(false);

        when(resultSet.getLong(DataSourceFields.ORDER_ID)).thenReturn(ConstantsTest.ORDER_ID_VALUE);
        when(resultSet.getTimestamp(DataSourceFields.ORDER_DATE)).thenReturn(ConstantsTest.ORDER_DATE_VALUE);
        when(resultSet.getLong(DataSourceFields.USER_ID)).thenReturn(ConstantsTest.USER_ID_VALUE);
        when(resultSet.getString(DataSourceFields.USER_FIRST_NAME)).thenReturn(ConstantsTest.USER_FIRST_NAME_VALUE);
        when(resultSet.getString(DataSourceFields.USER_LAST_NAME)).thenReturn(ConstantsTest.USER_LAST_NAME_VALUE);
        when(resultSet.getString(DataSourceFields.USER_EMAIL)).thenReturn(ConstantsTest.USER_EMAIL_VALUE);
        when(resultSet.getString(DataSourceFields.USER_ROLE)).thenReturn(ConstantsTest.USER_ROLE_CLIENT_VALUE);
        when(resultSet.getInt(DataSourceFields.ORDER_PASSENGERS)).thenReturn(ConstantsTest.ORDER_PASSENGERS_VALUE);


        when(resultSet.getString(DataSourceFields.ROUTE_START_TRAVEL)).thenReturn(ConstantsTest.ADDRESS_START_END_VALUE);
        when(resultSet.getString(DataSourceFields.ROUTE_END_TRAVEL)).thenReturn(ConstantsTest.ADDRESS_START_END_UK_VALUE);
        when(resultSet.getLong(DataSourceFields.ROUTE_TRAVEL_DISTANCE)).thenReturn(ConstantsTest.ROUTE_DISTANCE_VALUE);
        when(resultSet.getInt(DataSourceFields.ROUTE_TRAVEL_DURATION)).thenReturn(ConstantsTest.ROUTE_TRAVEL_TIME_VALUE);

        when(resultSet.getDouble(DataSourceFields.ORDER_COST)).thenReturn(ConstantsTest.ORDER_COST_VALUE);
        when(resultSet.getTimestamp(DataSourceFields.ORDER_STARTED_AT)).thenReturn(ConstantsTest.ORDER_STARTED_AT_VALUE);
        when(resultSet.getTimestamp(DataSourceFields.ORDER_FINISHED_AT)).thenReturn(ConstantsTest.ORDER_FINISHED_AT_VALUE);
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
