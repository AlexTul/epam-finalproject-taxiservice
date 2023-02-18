package com.epam.alextuleninov.taxiservice.service.loyalty;

import com.epam.alextuleninov.taxiservice.Constants;
import com.epam.alextuleninov.taxiservice.data.loyalty.LoyaltyRatio;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.data.route.RouteCharacteristicsResponse;
import com.epam.alextuleninov.taxiservice.service.crud.order.OrderCRUD;
import com.epam.alextuleninov.taxiservice.service.routecharacteristics.RouteCharacteristics;
import com.epam.alextuleninov.taxiservice.service.routecharacteristics.RouteCharacteristicsService;

/**
 * Interface for count loyalty price.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public class LoyaltyService implements Loyalty {

    private final OrderCRUD orderCRUD;
    private final RouteCharacteristics routeCharacteristics;

    public LoyaltyService(OrderCRUD orderCRUD, RouteCharacteristics routeCharacteristics) {
        this.orderCRUD = orderCRUD;
        this.routeCharacteristics = routeCharacteristics;
    }

    /**
     * Count loyalty price.
     *
     * @param request  request with order`s parameters
     * @return              loyalty price
     */
    @Override
    public LoyaltyRatio getLoyaltyPrice(OrderRequest request) {
        var countCost = orderCRUD.sumCostByCustomer(request);
        var loyaltyRatio = calculateRatio(countCost);

        var routeCharacteristics = new RouteCharacteristics() {
            @Override
            public RouteCharacteristicsResponse getRouteCharacteristics(OrderRequest request) {
                return new RouteCharacteristicsService().getRouteCharacteristics(request);
            }
        };
        var routeCharacteristicsResp = routeCharacteristics.getRouteCharacteristics(request);
        double price = routeCharacteristicsResp.travelDistance() * Constants.TRAVEL_PRICE_KILOMETER;
        // if the price is less than the minimum
        price = Math.max(price, Constants.TRAVEL_PRICE_MINIMAL);

        return new LoyaltyRatio(loyaltyRatio * price);
    }

    private double calculateRatio(double countCost) {
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
