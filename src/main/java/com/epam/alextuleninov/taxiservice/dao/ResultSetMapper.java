package com.epam.alextuleninov.taxiservice.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interface for entity`s mapping.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public interface ResultSetMapper<T> {

    /**
     * The method for entity`s mapping.
     *
     * @param resultSet         result set from database
     * @return                  mapping entity
     */
    T map(ResultSet resultSet) throws SQLException;
}
