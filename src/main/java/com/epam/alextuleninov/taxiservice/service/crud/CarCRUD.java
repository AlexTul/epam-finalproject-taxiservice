package com.epam.alextuleninov.taxiservice.service.crud;

import com.epam.alextuleninov.taxiservice.data.car.CarRequest;
import com.epam.alextuleninov.taxiservice.data.car.CarResponse;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.data.pageable.PageableRequest;
import com.epam.alextuleninov.taxiservice.model.car.Car;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Interface CRUD for Car.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public interface CarCRUD {

    /**
     * Create the car in the database.
     *
     * @param request       request with order parameters
     * @return              the created car from database
     */
    CarResponse create(CarRequest request);

    /**
     * Find all cars from database.
     *
     * @param pageable       request with pagination information
     * @return               list with cars
     */
    List<CarResponse> findAll(PageableRequest pageable);

    /**
     * Find all cars by category status from the database.
     *
     * @param request       request with order`s parameters
     * @return              all cars from the database
     */
    Set<Car> findAllByCategoryStatus(OrderRequest request);

    /**
     * Find car by ID from the database.
     *
     * @param id            id of car
     * @return              car from the database
     */
    Optional<CarResponse> findByID(int id);

    /**
     * Change car status int the database.
     *
     * @param request       request with order`s parameters
     */
    void changeCarStatus(OrderRequest request);

    /**
     * Find number of records from the database.
     *
     * @return              number of record in database
     */
    long findNumberRecords();

    /**
     * Update the car from database.
     *
     * @param id            id of car
     * @param request       request with parameter
     */
    void updateByID(int id, CarRequest request);

    /**
     * Delete the car from database.
     *
     * @param id            id of car
     */
    void deleteByID(int id);
}
