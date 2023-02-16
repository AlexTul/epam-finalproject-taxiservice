package com.epam.alextuleninov.taxiservice.dao.mappers;

import com.epam.alextuleninov.taxiservice.ConstantsTest;
import com.epam.alextuleninov.taxiservice.connectionpool.DataSourceFields;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.epam.alextuleninov.taxiservice.TestUtils.getTestRoute;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RouteMapperTest {

    private ResultSet resultSet;

    @BeforeEach
    void setUp() {
        resultSet = mock(ResultSet.class);
    }

    @Test
    void testMapPresent() throws SQLException {
//        prepareResultSetPresent(resultSet);
////        var resultPresent = new RouteMapper().map(resultSet);
//
//        assertEquals(getTestRoute(), resultPresent);
    }

    @Test
    void testMapAbsent() throws SQLException {
//        prepareResultSetAbsent(resultSet);
//        var resultAbsent = new RouteMapper().map(resultSet);
//
//        assertNotEquals(getTestRoute(), resultAbsent);
    }

    private static void prepareResultSetPresent(ResultSet resultSet) throws SQLException {
//        when(resultSet.next()).thenReturn(true).thenReturn(false);
//
//        when(resultSet.getLong(DataSourceFields.ROUTE_ID)).thenReturn(ConstantsTest.ROUTE_ID_VALUE);
//        when(resultSet.getLong(DataSourceFields.ADDRESS_ID)).thenReturn(ConstantsTest.ADDRESS_ID_VALUE);
//        when(resultSet.getString(DataSourceFields.ADDRESS_START_END)).thenReturn(ConstantsTest.ADDRESS_START_END_VALUE);
//        when(resultSet.getString(DataSourceFields.ADDRESS_START_END_UK)).thenReturn(ConstantsTest.ADDRESS_START_END_UK_VALUE);
//        when(resultSet.getLong(DataSourceFields.ROUTE_DISTANCE)).thenReturn(ConstantsTest.ROUTE_DISTANCE_VALUE);
//        when(resultSet.getDouble(DataSourceFields.ROUTE_PRICE)).thenReturn(ConstantsTest.ROUTE_PRICE_VALUE);
//        when(resultSet.getInt(DataSourceFields.ROUTE_TRAVEL_TIME)).thenReturn(ConstantsTest.ROUTE_TRAVEL_TIME_VALUE);
    }

    private static void prepareResultSetAbsent(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
    }
}
