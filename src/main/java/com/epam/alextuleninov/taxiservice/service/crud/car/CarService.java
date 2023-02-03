package com.epam.alextuleninov.taxiservice.service.crud.car;

import com.epam.alextuleninov.taxiservice.dao.car.CarDAO;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.model.car.Car;

import java.util.List;

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

    /**
     * Find all cars by category status from the database.
     *
     * @param request       request with order`s parameters
     * @return              all cars from the database
     */
    @Override
    public List<Car> findAllByCategoryStatus(OrderRequest request) {
        return carDAO.findAllByCategoryStatus(request);
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
}
