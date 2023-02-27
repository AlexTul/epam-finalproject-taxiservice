package com.epam.alextuleninov.taxiservice.service.loyalty;

import com.epam.alextuleninov.taxiservice.Constants;
import com.epam.alextuleninov.taxiservice.data.loyalty.LoyaltyRatio;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.service.crud.order.OrderCRUD;
import com.epam.alextuleninov.taxiservice.service.routecharacteristics.RouteCharacteristics;

import static com.epam.alextuleninov.taxiservice.Constants.*;

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
     * @param request request with order`s parameters
     * @return loyalty price
     */
    @Override
    public LoyaltyRatio getLoyaltyPrice(OrderRequest request) {
        var totalCost = orderCRUD.sumCostByCustomer(request);
        var loyaltyRatio = determineLoyaltyRatio(totalCost);

        var routeCharacteristicsResp = routeCharacteristics.getRouteCharacteristics(request);
        double price = routeCharacteristicsResp.travelDistance() * Constants.TRAVEL_PRICE_KILOMETER;
        // if the price is less than the minimum
        price = Math.max(price, Constants.TRAVEL_PRICE_MINIMAL);

        return new LoyaltyRatio(loyaltyRatio * price);
    }

    /**
     * Determine the loyalty ration.
     *
     * @param totalCost total cost all orders by customer
     * @return loyalty price
     */
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
