package com.epam.alextuleninov.taxiservice.service;

import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.model.car.Car;

import java.util.List;

/**
 * Interface for verify order.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public interface VerifyOrder {

    /**
     * Verify order.
     *
     * @param orderRequest  orderRequest with order`s parameters
     * @return              list with available cars for order
     */
    List<Car> checkOrder(OrderRequest orderRequest);
}
