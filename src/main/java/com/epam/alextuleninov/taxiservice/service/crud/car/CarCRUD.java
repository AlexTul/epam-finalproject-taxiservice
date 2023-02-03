package com.epam.alextuleninov.taxiservice.service.crud.car;

import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.model.car.Car;

import java.util.List;

/**
 * Interface CRUD for Car.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public interface CarCRUD {

    /**
     * Find all cars by category status from the database.
     *
     * @param request       request with order`s parameters
     * @return              all cars from the database
     */
    List<Car> findAllByCategoryStatus(OrderRequest request);

    /**
     * Change car status int the database.
     *
     * @param request       request with order`s parameters
     */
    void changeCarStatus(OrderRequest request);
}
