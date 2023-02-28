package com.epam.alextuleninov.taxiservice;

import com.epam.alextuleninov.taxiservice.connectionpool.DataSourceFields;
import com.epam.alextuleninov.taxiservice.data.car.CarRequest;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.data.pageable.PageableRequest;
import com.epam.alextuleninov.taxiservice.data.route.RouteCharacteristicsResponse;
import com.epam.alextuleninov.taxiservice.data.user.UserRequest;
import com.epam.alextuleninov.taxiservice.model.car.Car;
import com.epam.alextuleninov.taxiservice.model.car.category.CarCategory;
import com.epam.alextuleninov.taxiservice.model.car.status.CarStatus;
import com.epam.alextuleninov.taxiservice.model.order.Order;
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
                "Pivnichnyy lane, 1-10 - Depovska street, 1 - 30",
                4,
                4,
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
                Role.valueOf(ConstantsTest.USER_ROLE_CLIENT_VALUE)
        );
    }

    public static User getTestAdmin() {
        return new User(
                ConstantsTest.USER_ID_ADMIN_VALUE,
                ConstantsTest.USER_FIRST_NAME_ADMIN_VALUE,
                ConstantsTest.USER_LAST_NAME_ADMIN_VALUE,
                ConstantsTest.USER_EMAIL_ADMIN_VALUE,
                ConstantsTest.USER_ENCRYPT_PASSWORD_ADMIN_VALUE,
                Role.valueOf(ConstantsTest.USER_ROLE_ADMIN_VALUE)
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

    public static CarRequest getTestCarRequest() {
        return new CarRequest(
                "Audi A6 AX 0000 KX",
                4,
                CarCategory.PASSENGER.toString(),
                CarStatus.AVAILABLE.toString()
        );
    }

    public static Order getTestOrder() {
        List<Car> cars = new ArrayList<>();
        cars.add(getTestCar());

        return new Order(
                ConstantsTest.ORDER_ID_VALUE,
                ConstantsTest.ORDER_DATE_VALUE.toLocalDateTime(),
                getTestUser(),
                ConstantsTest.ORDER_PASSENGERS_VALUE,
                cars,
                "Pivnichnyy lane, 1-10 - Depovska street, 1 - 30",
                "Pivnichnyy lane, 1-10 - Depovska street, 1 - 30",
                4,
                300,
                ConstantsTest.ORDER_COST_VALUE,
                ConstantsTest.ORDER_STARTED_AT_VALUE.toLocalDateTime(),
                ConstantsTest.ORDER_FINISHED_AT_VALUE.toLocalDateTime()
        );
    }

    public static RouteCharacteristicsResponse getRouteCharacteristicsResp() {
        return new RouteCharacteristicsResponse(
                4.0, 300
        );
    }
}