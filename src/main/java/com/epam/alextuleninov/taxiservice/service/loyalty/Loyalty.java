package com.epam.alextuleninov.taxiservice.service.loyalty;

import com.epam.alextuleninov.taxiservice.data.loyalty.LoyaltyRatio;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;

/**
 * Interface for count loyalty price.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public interface Loyalty {

    /**
     * Count loyalty price.
     *
     * @param orderRequest  request with order`s parameters
     * @return              loyalty price
     */
    LoyaltyRatio getLoyaltyPrice(OrderRequest orderRequest);
}
