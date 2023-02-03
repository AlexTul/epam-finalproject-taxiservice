package com.epam.alextuleninov.taxiservice.dao.mappers;

import com.epam.alextuleninov.taxiservice.ConstantsTest;
import com.epam.alextuleninov.taxiservice.connectionpool.DataSourceFields;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.epam.alextuleninov.taxiservice.TestUtils.getTestOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderMapperTest {

    private ResultSet resultSet;

    @BeforeEach
    void setUp() {
        resultSet = mock(ResultSet.class);
    }

    @Test
    void testMapPresent() throws SQLException {
        prepareResultSetPresent(resultSet);
        var resultPresent = new OrderMapper().map(resultSet);

        assertEquals(getTestOrder(), resultPresent);
    }

    @Test
    void testMapAbsent() throws SQLException {
        prepareResultSetAbsent(resultSet);
        var resultAbsent = new OrderMapper().map(resultSet);

        assertNotEquals(getTestOrder(), resultAbsent);
    }

    private static void prepareResultSetPresent(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);

        when(resultSet.getLong(DataSourceFields.ORDER_ID)).thenReturn(ConstantsTest.ORDER_ID_VALUE);
        when(resultSet.getTimestamp(DataSourceFields.ORDER_DATE)).thenReturn(ConstantsTest.ORDER_DATE_VALUE);
        when(resultSet.getLong(DataSourceFields.USER_ID)).thenReturn(ConstantsTest.USER_ID_VALUE);
        when(resultSet.getString(DataSourceFields.USER_FIRST_NAME)).thenReturn(ConstantsTest.USER_FIRST_NAME_VALUE);
        when(resultSet.getString(DataSourceFields.USER_LAST_NAME)).thenReturn(ConstantsTest.USER_LAST_NAME_VALUE);
        when(resultSet.getString(DataSourceFields.USER_EMAIL)).thenReturn(ConstantsTest.USER_EMAIL_VALUE);
        when(resultSet.getString(DataSourceFields.USER_ROLE)).thenReturn(ConstantsTest.USER_ROLE_VALUE);
        when(resultSet.getInt(DataSourceFields.ORDER_PASSENGERS)).thenReturn(ConstantsTest.ORDER_PASSENGERS_VALUE);
        when(resultSet.getLong(DataSourceFields.ROUTE_ID)).thenReturn(ConstantsTest.ROUTE_ID_VALUE);
        when(resultSet.getLong(DataSourceFields.ADDRESS_ID)).thenReturn(ConstantsTest.ADDRESS_ID_VALUE);
        when(resultSet.getString(DataSourceFields.ADDRESS_START_END)).thenReturn(ConstantsTest.ADDRESS_START_END_VALUE);
        when(resultSet.getString(DataSourceFields.ADDRESS_START_END_UK)).thenReturn(ConstantsTest.ADDRESS_START_END_UK_VALUE);
        when(resultSet.getLong(DataSourceFields.ROUTE_DISTANCE)).thenReturn(ConstantsTest.ROUTE_DISTANCE_VALUE);
        when(resultSet.getDouble(DataSourceFields.ROUTE_PRICE)).thenReturn(ConstantsTest.ROUTE_PRICE_VALUE);
        when(resultSet.getInt(DataSourceFields.ROUTE_TRAVEL_TIME)).thenReturn(ConstantsTest.ROUTE_TRAVEL_TIME_VALUE);
        when(resultSet.getDouble(DataSourceFields.ORDER_COST)).thenReturn(ConstantsTest.ORDER_COST_VALUE);
        when(resultSet.getTimestamp(DataSourceFields.ORDER_STARTED_AT)).thenReturn(ConstantsTest.ORDER_STARTED_AT_VALUE);
        when(resultSet.getTimestamp(DataSourceFields.ORDER_FINISHED_AT)).thenReturn(ConstantsTest.ORDER_FINISHED_AT_VALUE);
    }

    private static void prepareResultSetAbsent(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);

        when(resultSet.getTimestamp(DataSourceFields.ORDER_DATE)).thenReturn(ConstantsTest.ORDER_DATE_VALUE);
        when(resultSet.getString(DataSourceFields.USER_ROLE)).thenReturn(ConstantsTest.USER_ROLE_VALUE);
        when(resultSet.getTimestamp(DataSourceFields.ORDER_STARTED_AT)).thenReturn(ConstantsTest.ORDER_STARTED_AT_VALUE);
        when(resultSet.getTimestamp(DataSourceFields.ORDER_FINISHED_AT)).thenReturn(ConstantsTest.ORDER_FINISHED_AT_VALUE);
    }
}
