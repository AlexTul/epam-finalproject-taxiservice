package com.epam.alextuleninov.taxiservice;

import com.epam.alextuleninov.taxiservice.connectionpool.DataSourceFields;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.data.pageable.PageableRequest;
import com.epam.alextuleninov.taxiservice.data.user.UserRequest;
import com.epam.alextuleninov.taxiservice.model.car.Car;
import com.epam.alextuleninov.taxiservice.model.order.Order;
import com.epam.alextuleninov.taxiservice.model.route.adress.Address;
import com.epam.alextuleninov.taxiservice.model.route.Route;
import com.epam.alextuleninov.taxiservice.model.user.User;
import com.epam.alextuleninov.taxiservice.model.user.role.Role;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public final class TestUtils {

    public static OrderRequest getTestOrderRequest() {
        List<Car> cars = new ArrayList<>();
        cars.add(getTestCar());

        return new OrderRequest(
                "test@gmail.com",
                cars,
                38.5,
                "Pivnichnyy lane, 1-10 - Depovska street, 1 - 30",
                4,
                "PASSENGER",
                LocalDateTime.parse("2023-02-01 10:15", Constants.FORMATTER),
                "AVAILABLE"
        );
    }

    public static PageableRequest getTestPageableRequest() {
        return new PageableRequest(
                DataSourceFields.ORDER_ID,
                Constants.SORTING_ASC,
                Constants.PAGE_SIZE,
                0
        );
    }

    public static UserRequest getTestUserRequest() {
        return new UserRequest(
                ConstantsTest.USER_FIRST_NAME_VALUE,
                ConstantsTest.USER_LAST_NAME_VALUE,
                ConstantsTest.USER_EMAIL_VALUE,
                ConstantsTest.USER_PASSWORD_VALUE
        );
    }

    public static User getTestUser() {
        return new User(
                ConstantsTest.USER_ID_VALUE,
                ConstantsTest.USER_FIRST_NAME_VALUE,
                ConstantsTest.USER_LAST_NAME_VALUE,
                ConstantsTest.USER_EMAIL_VALUE,
                ConstantsTest.USER_ENCRYPT_PASSWORD_VALUE,
                Role.valueOf(ConstantsTest.USER_ROLE_VALUE)
        );
    }

    public static Car getTestCar() {
        return new Car(
                ConstantsTest.CAR_ID_VALUE,
                ConstantsTest.CAR_NAME_VALUE,
                ConstantsTest.CAR_PASSENGERS_VALUE,
                ConstantsTest.CAR_CAR_CATEGORY_VALUE,
                ConstantsTest.CAR_STATUS_VALUE
        );
    }

    public static Route getTestRoute() {
        var address = new Address(
                ConstantsTest.ADDRESS_ID_VALUE,
                ConstantsTest.ADDRESS_START_END_VALUE,
                ConstantsTest.ADDRESS_START_END_UK_VALUE
        );

        return new Route(
                ConstantsTest.ROUTE_ID_VALUE,
                address,
                ConstantsTest.ROUTE_DISTANCE_VALUE,
                ConstantsTest.ROUTE_PRICE_VALUE,
                ConstantsTest.ROUTE_TRAVEL_TIME_VALUE
        );
    }

    public static Order getTestOrder() {
        return new Order(
                ConstantsTest.ORDER_ID_VALUE,
                ConstantsTest.ORDER_DATE_VALUE.toLocalDateTime(),
                getTestUser(),
                ConstantsTest.ORDER_PASSENGERS_VALUE,
                null,
                getTestRoute(),
                ConstantsTest.ORDER_COST_VALUE,
                ConstantsTest.ORDER_STARTED_AT_VALUE.toLocalDateTime(),
                ConstantsTest.ORDER_FINISHED_AT_VALUE.toLocalDateTime()
        );
    }
}