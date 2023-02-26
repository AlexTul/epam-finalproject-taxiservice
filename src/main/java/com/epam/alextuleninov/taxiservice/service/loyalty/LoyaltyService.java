package com.epam.alextuleninov.taxiservice.service.loyalty;

import com.epam.alextuleninov.taxiservice.Constants;
import com.epam.alextuleninov.taxiservice.data.loyalty.LoyaltyRatio;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.data.route.RouteCharacteristicsResponse;
import com.epam.alextuleninov.taxiservice.service.crud.order.OrderCRUD;
import com.epam.alextuleninov.taxiservice.service.routecharacteristics.RouteCharacteristics;
import com.epam.alextuleninov.taxiservice.service.routecharacteristics.RouteCharacteristicsService;

import static com.epam.alextuleninov.taxiservice.Constants.*;

/**
 * Interface for count loyalty price.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public class LoyaltyService implements Loyalty {

    private final OrderCRUD orderCRUD;

    public LoyaltyService(OrderCRUD orderCRUD) {
        this.orderCRUD = orderCRUD;
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
        if (countCost <= COUNT_COST_MINIMAL) {
            loyaltyRatio = LOYALTY_RATIO_MINIMAL;
        } else if (countCost <= COUNT_COST_MEDIUM) {
            loyaltyRatio = LOYALTY_RATIO_MEDIUM;
        } else {
            loyaltyRatio = LOYALTY_RATIO_MAXIMUM;
        }
        return loyaltyRatio;
    }
}
