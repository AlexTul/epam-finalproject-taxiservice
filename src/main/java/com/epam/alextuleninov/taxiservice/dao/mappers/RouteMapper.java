package com.epam.alextuleninov.taxiservice.dao.mappers;

import com.epam.alextuleninov.taxiservice.connectionpool.DataSourceFields;
import com.epam.alextuleninov.taxiservice.model.route.adress.Address;
import com.epam.alextuleninov.taxiservice.model.route.Route;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class for entity`s mapping.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public class RouteMapper implements ResultSetMapper<Route> {

    /**
     * The method for entity`s mapping.
     *
     * @param resultSet         result set from database
     * @return                  mapping entity
     */
    @Override
    public Route map(ResultSet resultSet) throws SQLException {
        return new Route(
                resultSet.getLong(DataSourceFields.ROUTE_ID),
                new Address(
                        resultSet.getLong(DataSourceFields.ADDRESS_ID),
                        resultSet.getString(DataSourceFields.ADDRESS_START_END),
                        resultSet.getString(DataSourceFields.ADDRESS_START_END_UK)
                ),
                resultSet.getLong(DataSourceFields.ROUTE_DISTANCE),
                resultSet.getDouble(DataSourceFields.ROUTE_PRICE),
                resultSet.getInt(DataSourceFields.ROUTE_TRAVEL_TIME)
        );
    }
}
