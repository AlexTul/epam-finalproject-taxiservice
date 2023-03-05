package com.epam.alextuleninov.taxiservice.service.verifyorder;

import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.model.car.Car;
import com.epam.alextuleninov.taxiservice.model.car.category.CarCategory;
import com.epam.alextuleninov.taxiservice.model.car.status.CarStatus;
import com.epam.alextuleninov.taxiservice.service.crud.car.CarCRUD;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.epam.alextuleninov.taxiservice.Constants.NUMBER_OF_CARGO_CAR;
import static com.epam.alextuleninov.taxiservice.Constants.NUMBER_OF_PASSENGER_CAR;
import static java.lang.Math.ceil;

/**
 * Class for verify order.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public class VerifyOrderService implements VerifyOrder {

    private final CarCRUD carCRUD;

    public VerifyOrderService(CarCRUD carCRUD) {
        this.carCRUD = carCRUD;
    }

    /**
     * Get total cars for new order:
     * - if number of passengers by request < sum of passengers by all cars by current category;
     * - if number of passengers by request > sum of passengers by all cars by current category - alternative offer;
     * - if number of passengers by request > sum of passengers by all cars by all categories - cancellations order.
     *
     * @param request request with order`s parameters
     * @return list with available cars for order
     */
    @Override
    public List<Car> checkOrder(OrderRequest request) {
        var sumPassengersByAllCarsByCategoryAnother = 0;
        List<Car> allCarsByCategoryAnother = new ArrayList<>();
        List<Car> totalCarsForOrder = new ArrayList<>();

        // count sum passengers by all available cars by car`s category
        var carsInTimeRange = new CarsInNewOrdersTimeRange();
        var allAvailableCarsByCategory = carsInTimeRange.getAvailableCars(request);
        var sumPassengersByAllCarsByCategory = getSumPassengersByAllCarsByCategory(allAvailableCarsByCategory);

        // count sum passengers by all available cars by car`s category another
        if (request.numberOfPassengers() > sumPassengersByAllCarsByCategory) {
            String carCategoryAnother =
                    request.carCategory().equals(CarCategory.PASSENGER.toString())
                            ? CarCategory.CARGO.toString()
                            : CarCategory.PASSENGER.toString();
            allCarsByCategoryAnother = carsInTimeRange.getAvailableCars(
                    new OrderRequest(
                            request.customer(),
                            null,
                            0.0,
                            request.startTravel(),
                            request.endTravel(),
                            request.travelDistance(),
                            request.travelDuration(),
                            request.numberOfPassengers(),
                            carCategoryAnother,
                            request.startedAt(),
                            CarStatus.AVAILABLE.toString())
            );
            sumPassengersByAllCarsByCategoryAnother = getSumPassengersByAllCarsByCategory(allCarsByCategoryAnother);
        }

        // define number of car passengers by current car`s category
        var numberOfCarPassengers =
                request.carCategory().equals(CarCategory.PASSENGER.toString()) ? NUMBER_OF_PASSENGER_CAR : NUMBER_OF_CARGO_CAR;

        // get total available cars by new order
        if (request.numberOfPassengers() <= sumPassengersByAllCarsByCategory) {
            var countCarForOrderByCategory = (int) ceil((double) (request.numberOfPassengers()) / numberOfCarPassengers);
            return getCars(request, totalCarsForOrder, allAvailableCarsByCategory, countCarForOrderByCategory);
        } else if (request.numberOfPassengers() <= sumPassengersByAllCarsByCategory + sumPassengersByAllCarsByCategoryAnother) {
            var deltaPassengers = request.numberOfPassengers() - sumPassengersByAllCarsByCategory;
            // list of cars for order for category
            totalCarsForOrder.addAll(allAvailableCarsByCategory);
            // count numberOfCarPassengers by another car`s category
            numberOfCarPassengers = numberOfCarPassengers == NUMBER_OF_PASSENGER_CAR ? NUMBER_OF_CARGO_CAR : NUMBER_OF_PASSENGER_CAR;
            var countCarForOrderByCategoryAnother = (int) ceil((double) deltaPassengers / numberOfCarPassengers);
            // list of cars for order for category + another category
            return getCars(request, totalCarsForOrder, allCarsByCategoryAnother, countCarForOrderByCategoryAnother);
        } else {
            return totalCarsForOrder;
        }
    }

    /**
     * Get sum passengers by all cars by category from database.
     *
     * @param allCarsByCategory all cars by category
     * @return sum passengers by all cars by category from database
     */
    private int getSumPassengersByAllCarsByCategory(List<Car> allCarsByCategory) {
        return allCarsByCategory.stream()
                .map(Car::getNumberOfPassengers)
                .mapToInt(Integer::intValue)
                .sum();
    }

    /**
     * Get cars from category:
     * - if number of cars by category > count cars for order - random selection of cars from the database;
     * - another - selection all cars from the database.
     *
     * @param request request with order`s parameters
     * @param totalCarsForOrder total cars for order
     * @param allCarsByCategory all cars by category
     * @param countCarForOrderByCategory number of cars for order by category
     * @return list with available cars for order
     */
    private List<Car> getCars(OrderRequest request, List<Car> totalCarsForOrder,
                              List<Car> allCarsByCategory, int countCarForOrderByCategory) {
        for (int i = 0; i < countCarForOrderByCategory; i++) {
            if (allCarsByCategory.size() > countCarForOrderByCategory) {
                totalCarsForOrder.add(allCarsByCategory.get(new Random().nextInt(allCarsByCategory.size())));
            } else {
                totalCarsForOrder.add(allCarsByCategory.get(i));
            }
        }

        bookingCars(new OrderRequest(
                        request.customer(),
                        totalCarsForOrder,
                        0.0,
                        request.startTravel(),
                        request.endTravel(),
                        request.travelDistance(),
                        request.travelDuration(),
                        request.numberOfPassengers(),
                        request.carCategory(),
                        request.startedAt(),
                        CarStatus.BOOKED.toString()
                )
        );

        return totalCarsForOrder;
    }

    /**
     * Booking cars in the database.
     *
     * @param orderRequest orderRequest with order`s parameters
     */
    private void bookingCars(OrderRequest orderRequest) {
        carCRUD.changeCarStatus(orderRequest);
    }
}
