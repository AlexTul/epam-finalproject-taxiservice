package com.epam.alextuleninov.taxiservice.service.crud.route;

import com.epam.alextuleninov.taxiservice.dao.route.JDBCRouteDAO;
import com.epam.alextuleninov.taxiservice.dao.route.RouteDAO;
import com.epam.alextuleninov.taxiservice.data.route.RouteResponse;
import com.epam.alextuleninov.taxiservice.exceptions.route.RouteExceptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.epam.alextuleninov.taxiservice.TestUtils.getTestOrderRequest;
import static com.epam.alextuleninov.taxiservice.TestUtils.getTestRoute;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RouteServiceTest {

    private RouteDAO routeDAO;
    private RouteService routeService;

    @BeforeEach
    void setUp() {
        routeDAO = mock(JDBCRouteDAO.class);
        routeService = new RouteService(routeDAO);
    }

    @Test
    void testFindByStartEnd() {
        when(routeDAO.findByStartEnd(getTestOrderRequest())).thenReturn(Optional.of(getTestRoute()));

        var expectedRouteResponse = RouteResponse.fromRoute(getTestRoute());

        var actualRouteResponse = routeService.findByStartEnd(getTestOrderRequest())
                .orElseThrow(() -> RouteExceptions.routeNotFound(getTestOrderRequest()));

        assertEquals(expectedRouteResponse, actualRouteResponse);
    }
}
