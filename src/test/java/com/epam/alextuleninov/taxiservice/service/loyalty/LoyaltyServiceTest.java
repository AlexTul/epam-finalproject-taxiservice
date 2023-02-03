package com.epam.alextuleninov.taxiservice.service.loyalty;

import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.data.route.RouteResponse;
import com.epam.alextuleninov.taxiservice.service.crud.order.OrderCRUD;
import com.epam.alextuleninov.taxiservice.service.crud.route.RouteCRUD;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoyaltyServiceTest {

    public static final double ROUTE_PRICE = 40.0;
    public static final double ROUTE_TOTAL_SPENT_FIRST = 40;
    public static final double ROUTE_TOTAL_SPENT_SECOND = 240;
    public static final double ROUTE_TOTAL_SPENT_THIRD = 310.8;

    @Test
    void testGetLoyaltyPrice() {
        OrderCRUD orderCRUD = mock(OrderCRUD.class);
        RouteCRUD routeCRUD = mock(RouteCRUD.class);

        var request = new OrderRequest(
                null, null, 0.0, null, 0,
                null, null, null
        );

        var routeResponse = new RouteResponse(
                0, 0, null, null, 0, ROUTE_PRICE, 0
        );

        // ROUTE_TOTAL_SPENT_FIRST
        when(orderCRUD.sumCostByCustomer(request)).thenReturn(ROUTE_TOTAL_SPENT_FIRST);
        when(routeCRUD.findByStartEnd(request)).thenReturn(Optional.of(routeResponse));

        var loyaltyRatioFirst = calculateRatio(ROUTE_TOTAL_SPENT_FIRST);

        double expectedFirst = loyaltyRatioFirst * ROUTE_PRICE;
        var actualFirst = new LoyaltyService(orderCRUD, routeCRUD).getLoyaltyPrice(request);

        assertEquals(expectedFirst, actualFirst.loyaltyPrice());

        // ROUTE_TOTAL_SPENT_SECOND
        when(orderCRUD.sumCostByCustomer(request)).thenReturn(ROUTE_TOTAL_SPENT_SECOND);
        when(routeCRUD.findByStartEnd(request)).thenReturn(Optional.of(routeResponse));

        var loyaltyRatioSecond = calculateRatio(ROUTE_TOTAL_SPENT_SECOND);

        double expectedSecond = loyaltyRatioSecond * ROUTE_PRICE;
        var actualSecond = new LoyaltyService(orderCRUD, routeCRUD).getLoyaltyPrice(request);

        assertEquals(expectedSecond, actualSecond.loyaltyPrice());

        // ROUTE_TOTAL_SPENT_THIRD
        when(orderCRUD.sumCostByCustomer(request)).thenReturn(ROUTE_TOTAL_SPENT_THIRD);
        when(routeCRUD.findByStartEnd(request)).thenReturn(Optional.of(routeResponse));

        var loyaltyRatioThird = calculateRatio(ROUTE_TOTAL_SPENT_THIRD);

        double expectedThird = loyaltyRatioThird * ROUTE_PRICE;
        var actualThird = new LoyaltyService(orderCRUD, routeCRUD).getLoyaltyPrice(request);

        assertEquals(expectedThird, actualThird.loyaltyPrice());
    }

    @Test
    void testGetLoyaltyPriceNullPointerException() {
        OrderCRUD orderCRUD = mock(OrderCRUD.class);
        RouteCRUD routeCRUD = mock(RouteCRUD.class);

        var request = new OrderRequest(
                null, null, 0.0, null, 0,
                null, null, null
        );

        // ROUTE_TOTAL_SPENT_FIRST
        when(orderCRUD.sumCostByCustomer(request)).thenReturn(ROUTE_TOTAL_SPENT_FIRST);
        when(routeCRUD.findByStartEnd(request)).thenThrow(new NullPointerException());

        assertThrows(NullPointerException.class, () -> routeCRUD.findByStartEnd(request));
    }


    private static double calculateRatio(double countCost) {
        double loyaltyRatio;
        if (countCost <= 200) {
            loyaltyRatio = 1;
        } else if (countCost <= 300) {
            loyaltyRatio = 0.97;
        } else {
            loyaltyRatio = 0.95;
        }
        return loyaltyRatio;
    }
}
