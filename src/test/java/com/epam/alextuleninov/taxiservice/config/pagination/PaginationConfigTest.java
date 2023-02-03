package com.epam.alextuleninov.taxiservice.config.pagination;

import com.epam.alextuleninov.taxiservice.controller.filter.Request;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PaginationConfigTest {

    private static final int N_PAGES_FIRST = 1;
    private static final int N_PAGES_PREV = 1;
    private static final int N_PAGES_NEXT = 1;
    private static final int N_PAGES_LAST = 1;

    private HttpServletRequest req;

    @BeforeEach
    void setUp() {
        req = mock(HttpServletRequest.class);
    }

    @Test
    void testConfigPage0() {
        String currentPage = "0";
        when(req.getParameter("page")).thenReturn(currentPage);

        var request = new Request(this.req);
        int actual = new PaginationConfig().configPage(request);

        assertEquals(0 , actual);
        assertEquals(0 , request.getAttribute("current_page"));
    }

    @Test
    void testConfigPageNull() {
        when(req.getParameter("page")).thenReturn(null);

        var request = new Request(this.req);
        int actual = new PaginationConfig().configPage(request);

        assertEquals(0 , actual);
        assertEquals(0 , request.getAttribute("current_page"));
    }

    @Test
    void testConfig() {
        Long totalRecords = 10L;
        Integer currentPage = 0;
        int lastPage = 4;
        boolean showAllPrev = true;
        boolean showAllNext = false;

        var request = new Request(this.req);
        request.setAttribute("total_records", totalRecords);
        request.setAttribute("current_page", currentPage);
        new PaginationConfig().config(request);

        assertEquals(lastPage, request.getAttribute("last_page"));
        assertEquals(N_PAGES_FIRST, request.getAttribute("N_PAGES_FIRST"));
        assertEquals(N_PAGES_PREV, request.getAttribute("N_PAGES_PREV"));
        assertEquals(N_PAGES_NEXT, request.getAttribute("N_PAGES_NEXT"));
        assertEquals(N_PAGES_LAST, request.getAttribute("N_PAGES_LAST"));
        assertEquals(showAllPrev, request.getAttribute("showAllPrev"));
        assertEquals(showAllNext, request.getAttribute("showAllNext"));
        assertEquals("report", request.getAttribute("url"));
    }
}
