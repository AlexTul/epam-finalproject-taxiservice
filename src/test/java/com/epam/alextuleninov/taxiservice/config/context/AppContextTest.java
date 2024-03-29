package com.epam.alextuleninov.taxiservice.config.context;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AppContextTest {

    @Test
    void testNoSmoke() {
        assertDoesNotThrow(AppContext::createAppContext);
        AppContext appContext = AppContext.getAppContext();
        assertNotNull(appContext.getProperties());
        assertNotNull(appContext.getPropertiesUk());
        assertNotNull(appContext.getRouteCharacteristics());
        assertNotNull(appContext.getUserCRUD());
        assertNotNull(appContext.getOrderCRUD());
        assertNotNull(appContext.getCarCRUD());
        assertNotNull(appContext.getLoyaltyService());
        assertNotNull(appContext.getVerifyService());
        assertNotNull(appContext.getDateTimeRide());
        assertNotNull(appContext.getEmailSender());
        assertNotNull(appContext.getEmailByLocaleConfig());
        assertNotNull(appContext.getSorter());
    }
}
