package com.epam.alextuleninov.taxiservice.service;

import java.time.LocalDateTime;

/**
 * Interface for count date and time of ride.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public interface DateTimeRide {

    /**
     * Count date and time of ride.
     *
     * @param startRoute    date and time of start ride
     * @return              date and time of ride
     */
    LocalDateTime count(LocalDateTime startRoute);
}
