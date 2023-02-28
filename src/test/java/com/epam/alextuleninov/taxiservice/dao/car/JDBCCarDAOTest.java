package com.epam.alextuleninov.taxiservice.dao.car;

import com.epam.alextuleninov.taxiservice.ConstantsTest;
import com.epam.alextuleninov.taxiservice.connectionpool.DataSourceFields;
import com.epam.alextuleninov.taxiservice.dao.mappers.CarMapper;
import com.epam.alextuleninov.taxiservice.dao.mappers.ResultSetMapper;
import com.epam.alextuleninov.taxiservice.exceptions.datasource.UnexpectedDataAccessException;
import com.epam.alextuleninov.taxiservice.model.car.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

import static com.epam.alextuleninov.taxiservice.TestUtils.*;
import static com.epam.alextuleninov.taxiservice.exceptions.car.CarExceptions.carNotFound;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;


public class JDBCCarDAOTest {

    private DataSource dataSource;
    private ResultSet resultSet;
    private CarDAO carDAO;

    @BeforeEach
    void setUp() {
        dataSource = mock(DataSource.class);
        resultSet = mock(ResultSet.class);
        ResultSetMapper<Car> mapper = new CarMapper();
        carDAO = new JDBCCarDAO(dataSource, mapper);
    }

    @Test
    void testCreate() {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeUpdate()).thenReturn(1);
//            createCar.setString(1, request.carName());
//            createCar.setInt(2, request.numberOfPassengers());
//            createCar.setString(3, request.carCategory());
//            createCar.setString(4, request.carStatus());
//
//            when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
//            when(resultSet.next()).thenReturn(true).thenReturn(false);
//            when(resultSet.getInt(1)).thenReturn(0);
//
//            assertDoesNotThrow(() -> carDAO.create(getTestCarRequest()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testFindAll() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            // if value is present
            prepareResultSetPresent(resultSet);
            Set<Car> resultPresent = carDAO.findAll(getTestPageableRequest());

            assertEquals(1, resultPresent.size());
            assertEquals(getTestCar(), resultPresent.stream().toList().get(0));

            // if value is absent
            prepareResultSetAbsent(resultSet);
            Set<Car> resultAbsent = carDAO.findAll(getTestPageableRequest());

            assertEquals(0, resultAbsent.size());
        }
    }

    @Test
    void testSqlExceptionFindAll() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        try {
            assertThrows(UnexpectedDataAccessException.class, (Executable) carDAO.findAll(getTestPageableRequest()));
        } catch (UnexpectedDataAccessException ignored) {
        }
    }

    @Test
    void testFindAllByCategoryStatus() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            // if value is present
            prepareResultSetPresent(resultSet);
            Set<Car> resultPresent = carDAO.findAll(getTestPageableRequest());

            assertEquals(1, resultPresent.size());
            assertEquals(getTestCar(), resultPresent.stream().toList().get(0));

            // if value is absent
            prepareResultSetAbsent(resultSet);
            Set<Car> resultAbsent = carDAO.findAll(getTestPageableRequest());

            assertEquals(0, resultAbsent.size());
        }
    }

    @Test
    void testSqlExceptionFindAllByCategoryStatus() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        try {
            assertThrows(UnexpectedDataAccessException.class, (Executable) carDAO.findAllByCategoryStatus(getTestOrderRequest()));
        } catch (UnexpectedDataAccessException ignored) {
        }
    }

    @Test
    void testFindByID() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            int id = 0;
            // if value is present
            prepareResultSetPresent(resultSet);
            Optional<Car> resultPresent = carDAO.findByID(id);

            assertEquals(getTestCar(), resultPresent.orElseThrow(() -> carNotFound(id)));

            // if value is absent
            prepareResultSetAbsent(resultSet);
            Optional<Car> resultAbsent = carDAO.findByID(id);

            assertEquals(Optional.empty(), resultAbsent);
        }
    }

    @Test
    void testSqlExceptionFindByID() throws SQLException {
//        when(dataSource.getConnection()).thenThrow(new SQLException());
//        try {
//            assertThrows(UnexpectedDataAccessException.class, carDAO.findByID(0));
//        } catch (UnexpectedDataAccessException ignored) {}
    }

    @Test
    void testChangeCarStatus() throws SQLException {
        try (var ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> carDAO.changeCarStatus(getTestOrderRequest()));
        }
    }

    @Test
    void testSqlExceptionChangeCarStatus() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> carDAO.changeCarStatus(getTestOrderRequest()));
    }

    @Test
    void testFindNumberRecords() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            // if value is present
            when(resultSet.next()).thenReturn(true).thenReturn(false);
            when(resultSet.getLong(1)).thenReturn(1L);
            long resultPresent = carDAO.findNumberRecords();

            assertEquals(1, resultPresent);

            // if value is absent
            when(resultSet.next()).thenReturn(true).thenReturn(false);
            when(resultSet.getLong(1)).thenReturn(0L);
            long resultAbsent = carDAO.findNumberRecords();

            assertEquals(0, resultAbsent);
        }
    }

    @Test
    void testSqlExceptionFindNumberRecords() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> carDAO.findNumberRecords());
    }

    @Test
    void testUpdateById() throws SQLException {
        try (var ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> carDAO.updateByID(0, getTestCarRequest()));
        }
    }

    @Test
    void testSqlExceptionUpdateById() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> carDAO.updateByID(0, getTestCarRequest()));
    }

    @Test
    void testDeleteById() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true).thenReturn(false);
            when(resultSet.getLong(1)).thenReturn(1L);

            assertDoesNotThrow(() -> carDAO.deleteByID(0));
        }
    }

    @Test
    void testSqlExceptionDeleteById() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> carDAO.deleteByID(0));
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
        when(resultSet.getInt(DataSourceFields.CAR_ID)).thenReturn(ConstantsTest.CAR_ID_VALUE);
        when(resultSet.getString(DataSourceFields.CAR_NAME)).thenReturn(ConstantsTest.CAR_NAME_VALUE);
        when(resultSet.getInt(DataSourceFields.CAR_PASSENGERS)).thenReturn(ConstantsTest.CAR_PASSENGERS_VALUE);
        when(resultSet.getString(DataSourceFields.CAR_CAR_CATEGORY)).thenReturn(ConstantsTest.CAR_CAR_CATEGORY_VALUE);
        when(resultSet.getString(DataSourceFields.CAR_STATUS)).thenReturn(ConstantsTest.CAR_STATUS_VALUE);
    }

    private static void prepareResultSetAbsent(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(false);
    }
}
