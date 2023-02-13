package com.epam.alextuleninov.taxiservice.exceptions.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderExceptions {

    private static final Logger log = LoggerFactory.getLogger(OrderExceptions.class);

    public static NullPointerException orderNotFound(long id) {
        log.info("Order with id: '" + id + "' was not found");
        return new NullPointerException("Order with id: '" + id + "' was not found");
    }
}
