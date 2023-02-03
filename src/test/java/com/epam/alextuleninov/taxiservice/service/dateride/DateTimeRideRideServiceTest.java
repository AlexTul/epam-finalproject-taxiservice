package com.epam.alextuleninov.taxiservice.service.dateride;

import com.epam.alextuleninov.taxiservice.Constants;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateTimeRideRideServiceTest {

    public static final long TIME_FOR_START_ORDER_SECOND = 5 * 60;

    @Test
    void testCount() {
        var expected = LocalDateTime.now()
                .plusSeconds(Constants.CAR_DELIVERY_TIME_SECOND);

        // imitation of order time outside of machine delivery time
        var startRouteForRequest = LocalDateTime.now()
                .plusSeconds(TIME_FOR_START_ORDER_SECOND);
        // calculation of the time of delivery of the car through the com.epam.alextuleninov.taxiservice.service
        var actual = new DateTimeRideRideService()
                .count(startRouteForRequest);

        assertEquals(expected.format(Constants.FORMATTER), actual.format(Constants.FORMATTER));
    }
}
