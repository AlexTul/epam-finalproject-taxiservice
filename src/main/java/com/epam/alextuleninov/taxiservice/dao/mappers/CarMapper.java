package com.epam.alextuleninov.taxiservice.dao.mappers;

import com.epam.alextuleninov.taxiservice.connectionpool.DataSourceFields;
import com.epam.alextuleninov.taxiservice.model.car.Car;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class for entity`s mapping.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public class CarMapper implements ResultSetMapper<Car> {

    /**
     * The method for entity`s mapping.
     *
     * @param resultSet         result set from database
     * @return                  mapping entity
     */
    @Override
    public Car map(ResultSet resultSet) throws SQLException {
        return new Car(
                resultSet.getInt(DataSourceFields.CAR_ID),
                resultSet.getString(DataSourceFields.CAR_NAME),
                resultSet.getInt(DataSourceFields.CAR_PASSENGERS),
                resultSet.getString(DataSourceFields.CAR_CAR_CATEGORY),
                resultSet.getString(DataSourceFields.CAR_STATUS)
        );
    }
}
