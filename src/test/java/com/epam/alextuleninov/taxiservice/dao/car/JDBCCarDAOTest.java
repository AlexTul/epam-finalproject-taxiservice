package com.epam.alextuleninov.taxiservice.dao.car;

import com.epam.alextuleninov.taxiservice.ConstantsTest;
import com.epam.alextuleninov.taxiservice.connectionpool.DataSourceFields;
import com.epam.alextuleninov.taxiservice.dao.mappers.CarMapper;
import com.epam.alextuleninov.taxiservice.dao.mappers.ResultSetMapper;
import com.epam.alextuleninov.taxiservice.data.car.CarRequest;
import com.epam.alextuleninov.taxiservice.exceptions.datasource.UnexpectedDataAccessException;
import com.epam.alextuleninov.taxiservice.model.car.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.*;
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
    private ResultSetMapper<Car> mapper;

    @BeforeEach
    void setUp() {
        this.dataSource = mock(DataSource.class);
        this.resultSet = mock(ResultSet.class);
        ResultSetMapper<Car> mapper = new CarMapper();
        this.carDAO = new JDBCCarDAO(dataSource, mapper);
        this.mapper = mapper;
    }

    @Test
    void testCreate() throws SQLException {
        Connection connection = mock(Connection.class);
        DataSource dataSource = mock(DataSource.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet generatedKeys = mock(ResultSet.class);
        var request = new CarRequest(
                getTestCarRequest().carName(),
                getTestCarRequest().numberOfPassengers(),
                getTestCarRequest().carCategory(),
                getTestCarRequest().carStatus()
        );

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(generatedKeys);
        when(generatedKeys.next()).thenReturn(true);
        when(generatedKeys.getInt(1)).thenReturn(123);

        var carDAO = new JDBCCarDAO(dataSource, mapper);

        var createdCar = carDAO.create(request);

        assertNotNull(createdCar);
        assertEquals(123, createdCar.getId());
        assertEquals(request.carName(), createdCar.getCarName());
        assertEquals(request.numberOfPassengers(), createdCar.getNumberOfPassengers());
        assertEquals(request.carCategory(), createdCar.getCarCategory());
        assertEquals(request.carStatus(), createdCar.getCarStatus());
    }

    @Test
    void testSQLExceptionCreate() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> carDAO.create(getTestCarRequest()));
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
    void testSQLExceptionFindAll() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> carDAO.findAll(getTestPageableRequest()));
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
    void testSQLExceptionFindAllByCategoryStatus() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> carDAO.findAllByCategoryStatus(getTestOrderRequest()));
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
    void testSQLExceptionFindByID() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> carDAO.findByID(0));
    }

    @Test
    void testChangeCarStatus() throws SQLException {
        try (var ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> carDAO.changeCarStatus(getTestOrderRequest()));
        }
    }

    @Test
    void testSQLExceptionChangeCarStatus() throws SQLException {
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
    void testSQLExceptionFindNumberRecords() throws SQLException {
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
    void testSQLExceptionUpdateById() throws SQLException {
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
    void testSQLExceptionDeleteById() throws SQLException {
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
