package com.epam.alextuleninov.taxiservice.exceptions.car;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CarExceptions {

    private static final Logger log = LoggerFactory.getLogger(CarExceptions.class);

    public static NullPointerException carNotFound(int id) {
        log.info("Car with id: '" + id + "' was not found");
        return new NullPointerException("Car with id: '" + id + "' was not found");
    }
}
