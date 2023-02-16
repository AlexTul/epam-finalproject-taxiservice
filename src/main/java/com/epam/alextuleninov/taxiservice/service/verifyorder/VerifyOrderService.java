package com.epam.alextuleninov.taxiservice.service.verifyorder;

import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.model.car.Car;
import com.epam.alextuleninov.taxiservice.model.car.category.CarCategory;
import com.epam.alextuleninov.taxiservice.model.car.status.CarStatus;
import com.epam.alextuleninov.taxiservice.service.crud.car.CarCRUD;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
     * Verify order.
     *
     * @param request       request with order`s parameters
     * @return              list with available cars for order
     */
    @Override
    public List<Car> checkOrder(OrderRequest request) {
        List<Car> totalCarsForOrder = new ArrayList<>();

        var verifyCarsStatus = new CarsInNewOrdersTimeRange();
        var allCarsByCategory = verifyCarsStatus.getCarsInNewOrdersTimeRange(request);
        int sumPassengersByAllCarsByCategory = allCarsByCategory.stream()
                .map(Car::getNumberOfPassengers)
                .mapToInt(Integer::intValue)
                .sum();

        int numberOfCarPassengers =
                request.carCategory().equals(CarCategory.PASSENGER.toString()) ? 4 : 2;

        int countCarForOrderByCategory;
        if (request.numberOfPassengers() <= sumPassengersByAllCarsByCategory) {
            countCarForOrderByCategory = (int) ceil((double) (request.numberOfPassengers()) / numberOfCarPassengers);
            return getCars(request, totalCarsForOrder, allCarsByCategory, countCarForOrderByCategory);
        }

        List<Car> allCarsByCategoryAnother = new ArrayList<>();
        int sumPassengersByAllCarsByCategoryAnother = 0;
        if (request.numberOfPassengers() > sumPassengersByAllCarsByCategory) {
            String carCategory =
                    request.carCategory().equals(CarCategory.PASSENGER.toString())
                            ? CarCategory.CARGO.toString()
                            : CarCategory.PASSENGER.toString();
            allCarsByCategoryAnother = verifyCarsStatus.getCarsInNewOrdersTimeRange(
                    new OrderRequest(
                            request.customer(),
                            null,
                            0.0,
                            request.startTravel(),
                            request.endTravel(),
                            request.travelDistance(),
                            request.travelDuration(),
                            request.numberOfPassengers(),
                            carCategory,
                            request.startedAt(),
                            CarStatus.AVAILABLE.toString())
            );
            sumPassengersByAllCarsByCategoryAnother = allCarsByCategoryAnother.stream()
                    .map(Car::getNumberOfPassengers)
                    .mapToInt(Integer::intValue)
                    .sum();
        }

        int deltaPassengers;
        int countCarForOrderByCategoryAnother;
        if (request.numberOfPassengers() > sumPassengersByAllCarsByCategory
                && request.numberOfPassengers() <= sumPassengersByAllCarsByCategory + sumPassengersByAllCarsByCategoryAnother) {
            deltaPassengers = request.numberOfPassengers() - sumPassengersByAllCarsByCategory;
            countCarForOrderByCategory = allCarsByCategory.size();
            // list of cars for order for category
            for (int i = 0; i < countCarForOrderByCategory; i++) {
                totalCarsForOrder.add(allCarsByCategory.get(i));
            }

            // count numberOfCarPassengers by another car`s category
            numberOfCarPassengers = numberOfCarPassengers == 4 ? 2 : 4;
            countCarForOrderByCategoryAnother = (int) ceil((double) deltaPassengers / numberOfCarPassengers);
            // list of cars for order for category + another category
            return getCars(request, totalCarsForOrder, allCarsByCategoryAnother, countCarForOrderByCategoryAnother);
        }
        return totalCarsForOrder;
    }

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
     * @param orderRequest  orderRequest with order`s parameters
     */
    private void bookingCars(OrderRequest orderRequest) {
        carCRUD.changeCarStatus(orderRequest);
    }
}
