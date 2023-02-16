package com.epam.alextuleninov.taxiservice.service.verifyorder;

import com.epam.alextuleninov.taxiservice.config.context.AppContext;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.model.car.Car;
import com.epam.alextuleninov.taxiservice.model.order.Order;
import com.epam.alextuleninov.taxiservice.service.crud.car.CarCRUD;
import com.epam.alextuleninov.taxiservice.service.crud.order.OrderCRUD;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class for getting cars from orders that are in the new order's time range.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public class CarsInNewOrdersTimeRange {

    private final CarCRUD carCRUD;
    private final OrderCRUD orderCRUD;

    public CarsInNewOrdersTimeRange() {
        this.carCRUD = AppContext.getAppContext().getCarCRUD();
        this.orderCRUD = AppContext.getAppContext().getOrderCRUD();
    }

    /**
     * Get cars from orders that are in the new order's time range.
     *
     * @param request  request with order`s parameters
     * @return              list with cars from orders
     */
    public List<Car> getCarsInNewOrdersTimeRange(OrderRequest request) {

        var allOrdersByRange = orderCRUD.findAllByRange(request);

        // add to the car`s collection the cars that have orders that overlap with the new order
        Set<Car> bookingCars = new HashSet<>();
        for (Order variable : allOrdersByRange) {
            bookingCars.addAll(variable.getCars());
        }

        //add to the car`s collection the cars without orders, but available
        var allAvailableCars = carCRUD.findAllByCategoryStatus(request);
        // delete booking cars
        allAvailableCars.removeAll(bookingCars);

        return allAvailableCars.stream().toList();
    }
}
