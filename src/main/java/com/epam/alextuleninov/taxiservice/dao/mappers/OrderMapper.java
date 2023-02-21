package com.epam.alextuleninov.taxiservice.dao.mappers;

import com.epam.alextuleninov.taxiservice.Constants;
import com.epam.alextuleninov.taxiservice.connectionpool.DataSourceFields;
import com.epam.alextuleninov.taxiservice.model.order.Order;
import com.epam.alextuleninov.taxiservice.model.user.User;
import com.epam.alextuleninov.taxiservice.model.user.role.Role;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Class for entity`s mapping.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public class OrderMapper implements ResultSetMapper<Order> {

    /**
     * The method for entity`s mapping.
     *
     * @param resultSet         result set from database
     * @return                  mapping entity
     */
    @Override
    public Order map(ResultSet resultSet) throws SQLException {
        return new Order(
                resultSet.getLong(DataSourceFields.ORDER_ID),
                resultSet.getTimestamp(DataSourceFields.ORDER_DATE).toLocalDateTime(),
                new User(
                        resultSet.getLong(DataSourceFields.USER_ID),
                        resultSet.getString(DataSourceFields.USER_FIRST_NAME),
                        resultSet.getString(DataSourceFields.USER_LAST_NAME),
                        resultSet.getString(DataSourceFields.USER_EMAIL),
                        null,
                        Role.valueOf(resultSet.getString(DataSourceFields.USER_ROLE))
                ),
                resultSet.getInt(DataSourceFields.ORDER_PASSENGERS),
                null,
                resultSet.getString(DataSourceFields.ROUTE_START_TRAVEL),
                resultSet.getString(DataSourceFields.ROUTE_END_TRAVEL),
                resultSet.getDouble(DataSourceFields.ROUTE_TRAVEL_DISTANCE),
                resultSet.getInt(DataSourceFields.ROUTE_TRAVEL_DURATION),
                resultSet.getDouble(DataSourceFields.ORDER_COST),
                resultSet.getTimestamp(DataSourceFields.ORDER_STARTED_AT).toLocalDateTime(),
                resultSet.getTimestamp(DataSourceFields.ORDER_FINISHED_AT).toLocalDateTime()
        );
    }
}
