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
import java.util.List;

import static com.epam.alextuleninov.taxiservice.TestUtils.getTestOrderRequest;
import static com.epam.alextuleninov.taxiservice.TestUtils.getTestCar;
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
    void testFindAllByCategoryStatus() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            // if value is present
            prepareResultSetPresent(resultSet);
            List<Car> resultPresent = carDAO.findAllByCategoryStatus(getTestOrderRequest());

            assertEquals(1, resultPresent.size());
            assertEquals(getTestCar(), resultPresent.get(0));

            // if value is absent
            prepareResultSetAbsent(resultSet);
            List<Car> resultAbsent = carDAO.findAllByCategoryStatus(getTestOrderRequest());

            assertEquals(0, resultAbsent.size());
        }
    }

    @Test
    void testSqlExceptionFindAllByCategoryStatus() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
      try {
          assertThrows(UnexpectedDataAccessException.class, (Executable) carDAO.findAllByCategoryStatus(getTestOrderRequest()));
      } catch (UnexpectedDataAccessException ignored) {}
    }

    @Test
    void testChangeCarStatus() throws SQLException {
        try (PreparedStatement ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> carDAO.changeCarStatus(getTestOrderRequest()));
        }
    }

    @Test
    void testSqlExceptionChangeCarStatus() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> carDAO.changeCarStatus(getTestOrderRequest()));
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
