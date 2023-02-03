package com.epam.alextuleninov.taxiservice.controller.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LocaleFilterTest {

    private HttpServletRequest req;
    private HttpServletResponse resp;
    private FilterChain chain;
    private FilterConfig config;

    // mock HttpServletRequest, HttpServletResponse, FilterChain, FilterConfig
    @BeforeEach
    void setUp() {
        req = mock(HttpServletRequest.class);
        resp = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);
        config = mock(FilterConfig.class);
    }

    @Test
    void testNoLocale() throws ServletException, IOException {
        var filter = new LocaleFilter();
        filter.init(config);

        var request = new Request(this.req);
        request.getSession().setAttribute("locale", "en");

        filter.doFilter(request, resp, chain);

        assertEquals("en", request.getSession().getAttribute("locale"));
    }

    @Test
    void testUALocale() throws ServletException, IOException {
        // mock the returned value
        when(this.req.getParameter("locale")).thenReturn("uk_UA");
        when(this.req.getServletPath()).thenReturn("login.jsp");

        var filter = new LocaleFilter();
        filter.init(config);

        var request = new Request(this.req);

        filter.doFilter(request, resp, chain);

        assertEquals("uk_UA", request.getSession().getAttribute("locale"));
    }
}
