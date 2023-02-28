package com.epam.alextuleninov.taxiservice.service.crud.car;

import com.epam.alextuleninov.taxiservice.dao.car.CarDAO;
import com.epam.alextuleninov.taxiservice.data.car.CarRequest;
import com.epam.alextuleninov.taxiservice.data.car.CarResponse;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.data.pageable.PageableRequest;
import com.epam.alextuleninov.taxiservice.model.car.Car;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Class CRUD for Car.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public class CarService implements CarCRUD {

    private final CarDAO carDAO;

    public CarService(CarDAO carDAO) {
        this.carDAO = carDAO;
    }

    @Override
    public CarResponse create(CarRequest request) {
        return CarResponse.fromCar(carDAO.create(request));
    }

    /**
     * Find all cars from database.
     *
     * @param pageable       request with pagination information
     * @return               list with cars
     */
    @Override
    public List<CarResponse> findAll(PageableRequest pageable) {
        return carDAO.findAll(pageable).stream()
                .map(CarResponse::fromCar).toList();
    }

    /**
     * Find all cars by category status from the database.
     *
     * @param request       request with order`s parameters
     * @return              all cars from the database
     */
    @Override
    public Set<Car> findAllByCategoryStatus(OrderRequest request) {
        return carDAO.findAllByCategoryStatus(request);
    }

    /**
     * Find car by ID from the database.
     *
     * @param id            id of car
     * @return              car from the database
     */
    @Override
    public Optional<CarResponse> findByID(int id) {
        return carDAO.findByID(id).map(CarResponse::fromCar);
    }

    /**
     * Change car status int the database.
     *
     * @param request       request with order`s parameters
     */
    @Override
    public void changeCarStatus(OrderRequest request) {
        carDAO.changeCarStatus(request);
    }

    /**
     * Find number of records from the database.
     *
     * @return              number of record in database
     */
    @Override
    public long findNumberRecords() {
        return carDAO.findNumberRecords();
    }

    @Override
    public void updateByID(int id, CarRequest request) {
        carDAO.updateByID(id, request);
    }

    /**
     * Delete the car from database.
     *
     * @param id            id of car
     */
    @Override
    public void deleteByID(int id) {
        carDAO.deleteByID(id);
    }
}
