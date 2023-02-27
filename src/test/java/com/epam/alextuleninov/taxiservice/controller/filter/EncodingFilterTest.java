package com.epam.alextuleninov.taxiservice.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EncodingFilterTest {

    private HttpServletRequest req;
    private HttpServletResponse resp;
    private FilterChain chain;
    private FilterConfig config;

    @BeforeEach
    void setUp() {
        req = mock(HttpServletRequest.class);
        resp = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);
        config = mock(FilterConfig.class);
    }

    @Test
    void testDoFilter() throws ServletException, IOException {
        when(config.getInitParameter("encoding")).thenReturn("UTF-8");

        var filter = new EncodingFilter();
        filter.init(config);
        var request = new Request(this.req);
        filter.doFilter(request, resp, chain);

        assertEquals("UTF-8", request.getCharacterEncoding());
    }
}