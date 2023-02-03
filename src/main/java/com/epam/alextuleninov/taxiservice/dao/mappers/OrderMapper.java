package com.epam.alextuleninov.taxiservice.dao.mappers;

import com.epam.alextuleninov.taxiservice.Constants;
import com.epam.alextuleninov.taxiservice.connectionpool.DataSourceFields;
import com.epam.alextuleninov.taxiservice.model.order.Order;
import com.epam.alextuleninov.taxiservice.model.route.adress.Address;
import com.epam.alextuleninov.taxiservice.model.route.Route;
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
                LocalDateTime.parse(
                        (resultSet.getTimestamp(DataSourceFields.ORDER_DATE).toLocalDateTime()
                                .format(Constants.FORMATTER)),
                        Constants.FORMATTER),
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
                new Route(
                        resultSet.getLong(DataSourceFields.ROUTE_ID),
                        new Address(
                                resultSet.getLong(DataSourceFields.ADDRESS_ID),
                                resultSet.getString(DataSourceFields.ADDRESS_START_END),
                                resultSet.getString(DataSourceFields.ADDRESS_START_END_UK)
                        ),
                        resultSet.getLong(DataSourceFields.ROUTE_DISTANCE),
                        resultSet.getDouble(DataSourceFields.ROUTE_PRICE),
                        resultSet.getInt(DataSourceFields.ROUTE_TRAVEL_TIME)
                ),
                resultSet.getDouble(DataSourceFields.ORDER_COST),
                resultSet.getTimestamp(DataSourceFields.ORDER_STARTED_AT).toLocalDateTime(),
                resultSet.getTimestamp(DataSourceFields.ORDER_FINISHED_AT).toLocalDateTime()
        );
    }
}
