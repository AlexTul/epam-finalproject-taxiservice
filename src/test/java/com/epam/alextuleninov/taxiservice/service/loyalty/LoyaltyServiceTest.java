package com.epam.alextuleninov.taxiservice.service.loyalty;

import com.epam.alextuleninov.taxiservice.service.crud.order.OrderCRUD;
import com.epam.alextuleninov.taxiservice.service.routecharacteristics.RouteCharacteristics;
import org.junit.jupiter.api.Test;

import static com.epam.alextuleninov.taxiservice.Constants.*;
import static com.epam.alextuleninov.taxiservice.TestUtils.getRouteCharacteristicsResp;
import static com.epam.alextuleninov.taxiservice.TestUtils.getTestOrderRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoyaltyServiceTest {

    public static final double ROUTE_PRICE = 40.0;
    public static final double ROUTE_TOTAL_COST_FIRST = 40.0;
    public static final double ROUTE_TOTAL_COST_SECOND = 240.0;
    public static final double ROUTE_TOTAL_COST_THIRD = 310.8;

    @Test
    void testGetLoyaltyPriceFirst() {
        var orderCRUD = mock(OrderCRUD.class);
        var routeCharacteristics = mock(RouteCharacteristics.class);

        var request = getTestOrderRequest();
        var routeCharacteristicsResp = getRouteCharacteristicsResp();

        when(orderCRUD.sumCostByCustomer(request)).thenReturn(ROUTE_TOTAL_COST_FIRST);
        when(routeCharacteristics.getRouteCharacteristics(request)).thenReturn(routeCharacteristicsResp);

        var loyaltyRatio = determineLoyaltyRatio(ROUTE_TOTAL_COST_FIRST);

        double expectedFirst = loyaltyRatio * ROUTE_PRICE;
        var actualFirst = new LoyaltyService(orderCRUD, routeCharacteristics).getLoyaltyPrice(request);

        assertEquals(expectedFirst, actualFirst.loyaltyPrice());
    }

    @Test
    void testGetLoyaltyPriceSecond() {
        var orderCRUD = mock(OrderCRUD.class);
        var routeCharacteristics = mock(RouteCharacteristics.class);

        var request = getTestOrderRequest();
        var routeCharacteristicsResp = getRouteCharacteristicsResp();

        when(orderCRUD.sumCostByCustomer(request)).thenReturn(ROUTE_TOTAL_COST_SECOND);
        when(routeCharacteristics.getRouteCharacteristics(request)).thenReturn(routeCharacteristicsResp);

        var loyaltyRatio = determineLoyaltyRatio(ROUTE_TOTAL_COST_SECOND);

        double expectedFirst = loyaltyRatio * ROUTE_PRICE;
        var actualFirst = new LoyaltyService(orderCRUD, routeCharacteristics).getLoyaltyPrice(request);

        assertEquals(expectedFirst, actualFirst.loyaltyPrice());
    }

    @Test
    void testGetLoyaltyPriceThird() {
        var orderCRUD = mock(OrderCRUD.class);
        var routeCharacteristics = mock(RouteCharacteristics.class);

        var request = getTestOrderRequest();
        var routeCharacteristicsResp = getRouteCharacteristicsResp();

        when(orderCRUD.sumCostByCustomer(request)).thenReturn(ROUTE_TOTAL_COST_THIRD);
        when(routeCharacteristics.getRouteCharacteristics(request)).thenReturn(routeCharacteristicsResp);

        var loyaltyRatio = determineLoyaltyRatio(ROUTE_TOTAL_COST_THIRD);

        double expectedFirst = loyaltyRatio * ROUTE_PRICE;
        var actualFirst = new LoyaltyService(orderCRUD, routeCharacteristics).getLoyaltyPrice(request);

        assertEquals(expectedFirst, actualFirst.loyaltyPrice());
    }

    private double determineLoyaltyRatio(double totalCost) {
        double loyaltyRatio;
        if (totalCost <= COUNT_COST_MINIMAL) {
            loyaltyRatio = LOYALTY_RATIO_MINIMAL;
        } else if (totalCost <= COUNT_COST_MEDIUM) {
            loyaltyRatio = LOYALTY_RATIO_MEDIUM;
        } else {
            loyaltyRatio = LOYALTY_RATIO_MAXIMUM;
        }
        return loyaltyRatio;
    }
}
