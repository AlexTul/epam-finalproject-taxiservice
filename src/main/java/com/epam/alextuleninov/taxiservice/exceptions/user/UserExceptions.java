package com.epam.alextuleninov.taxiservice.exceptions.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserExceptions {

    private static final Logger log = LoggerFactory.getLogger(UserExceptions.class);

    public static NullPointerException userNotFound(String email) {
        log.info("User with login: '" + email + "' was not found");
        return new NullPointerException("User with login: '" + email + "' was not found");
    }
}
