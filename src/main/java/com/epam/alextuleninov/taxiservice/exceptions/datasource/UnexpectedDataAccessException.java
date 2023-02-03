package com.epam.alextuleninov.taxiservice.exceptions.datasource;

/**
 * Class for the custom exception.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public class UnexpectedDataAccessException extends RuntimeException {

    public UnexpectedDataAccessException(String message) {
        super(message);
    }

    public UnexpectedDataAccessException(Throwable cause) {
        super(cause);
    }
}
