package com.epam.alextuleninov.taxiservice.service.loyalty;

import com.epam.alextuleninov.taxiservice.data.loyalty.LoyaltyRatio;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.exceptions.route.RouteExceptions;
import com.epam.alextuleninov.taxiservice.service.crud.order.OrderCRUD;
import com.epam.alextuleninov.taxiservice.service.crud.route.RouteCRUD;

/**
 * Interface for count loyalty price.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public class LoyaltyService implements Loyalty {

    private final OrderCRUD orderCRUD;
    private final RouteCRUD routeCRUD;

    public LoyaltyService(OrderCRUD orderCRUD, RouteCRUD routeCRUD) {
        this.orderCRUD = orderCRUD;
        this.routeCRUD = routeCRUD;
    }

    /**
     * Count loyalty price.
     *
     * @param orderRequest  request with order`s parameters
     * @return              loyalty price
     */
    @Override
    public LoyaltyRatio getLoyaltyPrice(OrderRequest orderRequest) {
        var countCost = orderCRUD.sumCostByCustomer(orderRequest);
        var loyaltyRatio = calculateRatio(countCost);

        var routeResponse = routeCRUD.findByStartEnd(orderRequest)
                .orElseThrow(() -> RouteExceptions.routeNotFound(orderRequest));

        return new LoyaltyRatio(loyaltyRatio * routeResponse.price());
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
