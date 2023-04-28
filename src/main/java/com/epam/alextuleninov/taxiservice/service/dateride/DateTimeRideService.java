package com.epam.alextuleninov.taxiservice.service.dateride;

import com.epam.alextuleninov.taxiservice.Constants;
import com.epam.alextuleninov.taxiservice.service.DateTimeRide;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Interface for count date and time of ride.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public class DateTimeRideService implements DateTimeRide {

    /**
     * Count date and time of ride.
     *
     * @param startRoute    date and time of start ride
     * @return              date and time of ride
     */
    @Override
    public LocalDateTime count(LocalDateTime startRoute) {
        var createdOn = LocalDateTime.now();

        long differentStartRouteCreateOn =
                startRoute.toInstant(ZoneOffset.UTC).toEpochMilli() - createdOn.toInstant(ZoneOffset.UTC).toEpochMilli();

        if (differentStartRouteCreateOn < Constants.CAR_DELIVERY_TIME_MILLI) {
            long delta = Constants.CAR_DELIVERY_TIME_MILLI - differentStartRouteCreateOn;
            startRoute = startRoute.plusSeconds(delta / 1000);
        }

        return startRoute;
    }
}
