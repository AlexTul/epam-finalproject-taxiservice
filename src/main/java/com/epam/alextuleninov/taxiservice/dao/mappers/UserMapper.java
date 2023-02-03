package com.epam.alextuleninov.taxiservice.dao.mappers;

import com.epam.alextuleninov.taxiservice.connectionpool.DataSourceFields;
import com.epam.alextuleninov.taxiservice.model.user.User;
import com.epam.alextuleninov.taxiservice.model.user.role.Role;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class for entity`s mapping.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public class UserMapper implements ResultSetMapper<User> {

    /**
     * The method for entity`s mapping.
     *
     * @param resultSet         result set from database
     * @return                  mapping entity
     */
    @Override
    public User map(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getLong(DataSourceFields.USER_ID),
                resultSet.getString(DataSourceFields.USER_FIRST_NAME),
                resultSet.getString(DataSourceFields.USER_LAST_NAME),
                resultSet.getString(DataSourceFields.USER_EMAIL),
                null,
                Role.valueOf(resultSet.getString(DataSourceFields.USER_ROLE))
        );
    }
}
