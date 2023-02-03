package com.epam.alextuleninov.taxiservice.dao.route;

import com.epam.alextuleninov.taxiservice.ConstantsTest;
import com.epam.alextuleninov.taxiservice.connectionpool.DataSourceFields;
import com.epam.alextuleninov.taxiservice.dao.mappers.ResultSetMapper;
import com.epam.alextuleninov.taxiservice.dao.mappers.RouteMapper;
import com.epam.alextuleninov.taxiservice.exceptions.datasource.UnexpectedDataAccessException;
import com.epam.alextuleninov.taxiservice.model.route.Route;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.epam.alextuleninov.taxiservice.TestUtils.getTestOrderRequest;
import static com.epam.alextuleninov.taxiservice.TestUtils.getTestRoute;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

public class JDBCRouteDAOTest {

    private DataSource dataSource;
    private ResultSet resultSet;
    private RouteDAO routeDAO;

    @BeforeEach
    void setUp() {
        dataSource = mock(DataSource.class);
        resultSet = mock(ResultSet.class);
        ResultSetMapper<Route> mapper = new RouteMapper();
        routeDAO = new JDBCRouteDAO(dataSource, mapper);
    }

    @Test
    void testFindAll() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            // if value is present
            prepareResultSetPresent(resultSet);
            List<Route> resultPresent = routeDAO.findAll();

            assertEquals(1, resultPresent.size());
            assertEquals(getTestRoute(), resultPresent.get(0));

            // if value is absent
            prepareResultSetAbsent(resultSet);
            List<Route> resultAbsent = routeDAO.findAll();

            assertEquals(0, resultAbsent.size());
        }
    }

    @Test
    void testSqlExceptionFindAll() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        try {
            assertThrows(UnexpectedDataAccessException.class, (Executable) routeDAO.findAll());
        } catch (UnexpectedDataAccessException ignored) {}
    }

    @Test
    void testFindAllByCustomer() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            // if value is present
            prepareResultSetPresent(resultSet);
            List<Route> resultPresent = routeDAO.findAllByCustomer(getTestOrderRequest());

            assertEquals(1, resultPresent.size());
            assertEquals(getTestRoute(), resultPresent.get(0));

            // if value is absent
            prepareResultSetAbsent(resultSet);
            List<Route> resultAbsent = routeDAO.findAllByCustomer(getTestOrderRequest());

            assertEquals(0, resultAbsent.size());
        }
    }

    @Test
    void testSqlExceptionFindAllByCustomer() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        try {
            assertThrows(UnexpectedDataAccessException.class, (Executable) routeDAO.findAllByCustomer(getTestOrderRequest()));
        } catch (UnexpectedDataAccessException ignored) {}
    }

    @Test
    void testFindAllByStartEnd() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            // if value is present
            prepareResultSetPresent(resultSet);
            Route resultPresent = routeDAO.findByStartEnd(getTestOrderRequest()).orElse(null);

            assertEquals(getTestRoute(), resultPresent);

            // if value is absent
            prepareResultSetAbsent(resultSet);
            Route resultAbsent = routeDAO.findByStartEnd(getTestOrderRequest()).orElse(null);

            assertNull(resultAbsent);
        }
    }

    @Test
    void testSqlExceptionFindAllByStartEnd() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        try {
            assertThrows(UnexpectedDataAccessException.class, (Executable) routeDAO.findByStartEnd(getTestOrderRequest())
                    .orElse(null));
        } catch (UnexpectedDataAccessException ignored) {}
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

        when(resultSet.getLong(DataSourceFields.ROUTE_ID)).thenReturn(ConstantsTest.ROUTE_ID_VALUE);
        when(resultSet.getLong(DataSourceFields.ADDRESS_ID)).thenReturn(ConstantsTest.ADDRESS_ID_VALUE);
        when(resultSet.getString(DataSourceFields.ADDRESS_START_END)).thenReturn(ConstantsTest.ADDRESS_START_END_VALUE);
        when(resultSet.getString(DataSourceFields.ADDRESS_START_END_UK)).thenReturn(ConstantsTest.ADDRESS_START_END_UK_VALUE);
        when(resultSet.getLong(DataSourceFields.ROUTE_DISTANCE)).thenReturn(ConstantsTest.ROUTE_DISTANCE_VALUE);
        when(resultSet.getDouble(DataSourceFields.ROUTE_PRICE)).thenReturn(ConstantsTest.ROUTE_PRICE_VALUE);
        when(resultSet.getInt(DataSourceFields.ROUTE_TRAVEL_TIME)).thenReturn(ConstantsTest.ROUTE_TRAVEL_TIME_VALUE);
    }

    private static void prepareResultSetAbsent(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(false);
    }
}
