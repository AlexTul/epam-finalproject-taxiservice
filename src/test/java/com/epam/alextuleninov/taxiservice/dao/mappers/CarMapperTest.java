package com.epam.alextuleninov.taxiservice.dao.mappers;

import com.epam.alextuleninov.taxiservice.ConstantsTest;
import com.epam.alextuleninov.taxiservice.connectionpool.DataSourceFields;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.epam.alextuleninov.taxiservice.TestUtils.getTestCar;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CarMapperTest {

    private ResultSet resultSet;

    @BeforeEach
    void setUp() {
        resultSet = mock(ResultSet.class);
    }

    @Test
    void testMapPresent() throws SQLException {
        prepareResultSetPresent(resultSet);
        var resultPresent = new CarMapper().map(resultSet);

        assertEquals(getTestCar(), resultPresent);
    }

    @Test
    void testMapAbsent() throws SQLException {
        prepareResultSetAbsent(resultSet);
        var resultAbsent = new CarMapper().map(resultSet);

        assertNotEquals(getTestCar(), resultAbsent);
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
        when(resultSet.next()).thenReturn(true).thenReturn(false);
    }
}
