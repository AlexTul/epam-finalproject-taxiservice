package com.epam.alextuleninov.taxiservice.service.message;

import com.epam.alextuleninov.taxiservice.ConstantsTest;
import com.epam.alextuleninov.taxiservice.controller.filter.Request;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PageMessageBuilderTest {

    private HttpServletRequest req;

    @BeforeEach
    void setUp() {
        req = mock(HttpServletRequest.class);
    }

    @Test
    void testBuildMessageUserLocaleEN() {
        var locale = "en";
        when(req.getAttribute("messageUser")).thenReturn(ConstantsTest.USER);

        var request = new Request(this.req);
        PageMessageBuilder.buildMessageUser(request, locale, ConstantsTest.USER_UK, ConstantsTest.USER);

        assertEquals(req.getAttribute("messageUser"), request.getAttribute("messageUser"));
    }

    @Test
    void testBuildMessageUserLocaleUA() {
        var locale = "uk_UA";
        when(req.getAttribute("messageUser")).thenReturn(ConstantsTest.USER_UK);

        var request = new Request(this.req);
        PageMessageBuilder.buildMessageUser(request, locale, ConstantsTest.USER_UK, ConstantsTest.USER);

        assertEquals(req.getAttribute("messageUser"), request.getAttribute("messageUser"));
    }

    @Test
    void testBuildMessageAdminLocaleEN() {
        var locale = "en";
        when(req.getAttribute("whoseOrders")).thenReturn(ConstantsTest.ADMIN_REPORT_ALL);

        var request = new Request(this.req);
        PageMessageBuilder.buildMessageAdmin(request, locale, "whoseOrders", ConstantsTest.ADMIN_REPORT_ALL_UK, ConstantsTest.ADMIN_REPORT_ALL);

        assertEquals(req.getAttribute("whoseOrders"), request.getAttribute("whoseOrders"));
    }

    @Test
    void testBuildMessageAdminLocaleUA() {
        var locale = "uk_UA";
        when(req.getAttribute("whoseOrders")).thenReturn(ConstantsTest.ADMIN_REPORT_ALL_UK);

        var request = new Request(this.req);
        PageMessageBuilder.buildMessageAdmin(request, locale, "whoseOrders", ConstantsTest.ADMIN_REPORT_ALL_UK, ConstantsTest.ADMIN_REPORT_ALL);

        assertEquals(req.getAttribute("whoseOrders"), request.getAttribute("whoseOrders"));
    }
}
