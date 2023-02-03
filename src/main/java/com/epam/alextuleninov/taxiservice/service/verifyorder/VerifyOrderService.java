package com.epam.alextuleninov.taxiservice.service.verifyorder;

import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.model.car.Car;
import com.epam.alextuleninov.taxiservice.model.car.category.CarCategory;
import com.epam.alextuleninov.taxiservice.model.car.status.CarStatus;
import com.epam.alextuleninov.taxiservice.service.crud.car.CarCRUD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger log = LoggerFactory.getLogger(VerifyOrderService.class);

    private final CarCRUD carCRUD;

    public VerifyOrderService(CarCRUD carCRUD) {
        this.carCRUD = carCRUD;
    }

    /**
     * Verify order.
     *
     * @param orderRequest  orderRequest with order`s parameters
     * @return              list with available cars for order
     */
    @Override
    public List<Car> checkOrder(OrderRequest orderRequest) {
        List<Car> totalCarsForOrder = new ArrayList<>();

        var verifyCarsStatus = new CarsInNewOrdersTimeRange();
        var allCarsByCategory = verifyCarsStatus.getCarsInNewOrdersTimeRange(orderRequest);
        int sumPassengersByAllCarsByCategory = allCarsByCategory.stream()
                .map(Car::getNumberOfPassengers)
                .mapToInt(Integer::intValue)
                .sum();

        int numberOfCarPassengers =
                orderRequest.carCategory().equals(CarCategory.PASSENGER.toString()) ? 4 : 2;

        int countCarForOrderByCategory;
        if (orderRequest.numberOfPassengers() <= sumPassengersByAllCarsByCategory) {
            countCarForOrderByCategory = (int) ceil((double) (orderRequest.numberOfPassengers()) / numberOfCarPassengers);
            return getCars(orderRequest, totalCarsForOrder, allCarsByCategory, countCarForOrderByCategory);
        }

        List<Car> allCarsByCategoryAnother = new ArrayList<>();
        int sumPassengersByAllCarsByCategoryAnother = 0;
        if (orderRequest.numberOfPassengers() > sumPassengersByAllCarsByCategory) {
            String carCategory =
                    orderRequest.carCategory().equals(CarCategory.PASSENGER.toString())
                            ? CarCategory.CARGO.toString()
                            : CarCategory.PASSENGER.toString();
            allCarsByCategoryAnother = verifyCarsStatus.getCarsInNewOrdersTimeRange(
                    new OrderRequest(
                            orderRequest.customer(),
                            null,
                            0,
                            orderRequest.startEnd(),
                            orderRequest.numberOfPassengers(),
                            carCategory,
                            orderRequest.startedAt(),
                            CarStatus.AVAILABLE.toString())
            );
            sumPassengersByAllCarsByCategoryAnother = allCarsByCategoryAnother.stream()
                    .map(Car::getNumberOfPassengers)
                    .mapToInt(Integer::intValue)
                    .sum();
        }

        int deltaPassengers;
        int countCarForOrderByCategoryAnother;
        if (orderRequest.numberOfPassengers() > sumPassengersByAllCarsByCategory
                && orderRequest.numberOfPassengers() <= sumPassengersByAllCarsByCategory + sumPassengersByAllCarsByCategoryAnother) {
            deltaPassengers = orderRequest.numberOfPassengers() - sumPassengersByAllCarsByCategory;
            countCarForOrderByCategory = allCarsByCategory.size();
            // list of cars for order for category
            for (int i = 0; i < countCarForOrderByCategory; i++) {
                totalCarsForOrder.add(allCarsByCategory.get(i));
            }

            countCarForOrderByCategoryAnother = (int) ceil((double) deltaPassengers / numberOfCarPassengers);
            // list of cars for order for category + another category
            return getCars(orderRequest, totalCarsForOrder, allCarsByCategoryAnother, countCarForOrderByCategoryAnother);
        }
        return totalCarsForOrder;
    }

    private List<Car> getCars(OrderRequest request, List<Car> totalCarsForOrder,
                              List<Car> allCarsByCategory, int countCarForOrderByCategory) {
        for (int i = 0; i < countCarForOrderByCategory; i++) {
            int i1 = new Random().nextInt(allCarsByCategory.size());
            totalCarsForOrder.add(allCarsByCategory.get(i1));
        }

        bookingCars(new OrderRequest(
                        request.customer(),
                        totalCarsForOrder,
                        0,
                        request.startEnd(),
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
