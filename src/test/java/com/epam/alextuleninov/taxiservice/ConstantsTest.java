package com.epam.alextuleninov.taxiservice;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public final class ConstantsTest {

    // region user
    public static final long USER_ID_VALUE = 1;
    public static final String USER_FIRST_NAME_VALUE = "Test";
    public static final String USER_LAST_NAME_VALUE = "Test";
    public static final String USER_EMAIL_VALUE = "test@gmail.com";
    public static final String USER_PASSWORD_VALUE = "QAsQaqGMoG1K5hQCCVFd";
    public static final String USER_ENCRYPT_PASSWORD_VALUE = "9a497b0374e8e798f44291ad4a2fe4aad20f11bb";
    public static final String USER_ROLE_VALUE = "CLIENT";
    // endregion user

    // region car
    public static final int CAR_ID_VALUE = 0;
    public static final String CAR_NAME_VALUE = "Audi AX 0000 KX";
    public static final int CAR_PASSENGERS_VALUE = 4;
    public static final String CAR_CAR_CATEGORY_VALUE = "PASSENGER";
    public static final String CAR_STATUS_VALUE = "AVAILABLE";
    // endregion car

    // region route
    public static final long ROUTE_ID_VALUE = 0;
    public static final long ROUTE_DISTANCE_VALUE = 2;
    public static final double ROUTE_PRICE_VALUE = 40.0;
    public static final int ROUTE_TRAVEL_TIME_VALUE = 600;
    // endregion route

    // region address
    public static final long ADDRESS_ID_VALUE = 0;
    public static final String ADDRESS_START_END_VALUE = "Pivnichnyy lane, 1-10 - Depovska street, 1 - 30";
    public static final String ADDRESS_START_END_UK_VALUE = "Poltavskyy Shlyah, 1-10 - Depovska street, 1 - 30";
    // endregion address

    // region order
    public static final long ORDER_ID_VALUE = 0;
    public static final Timestamp ORDER_DATE_VALUE = Timestamp.valueOf("2023-01-28 11:39:11.129002");
    public static final int ORDER_PASSENGERS_VALUE = 4;
    public static final double ORDER_COST_VALUE = 40.0;
    public static final Timestamp ORDER_STARTED_AT_VALUE = Timestamp.valueOf("2023-02-03 14:39:00.000000");
    public static final Timestamp ORDER_FINISHED_AT_VALUE = Timestamp.valueOf("2023-02-03 14:54:00.000000");
    // endregion order

    // message
    public static final String ADMIN_REPORT_ALL_UK = " усі замовлення";
    public static final String ADMIN_REPORT_ALL = " all orders";
    public static final String USER_UK = "Вибачте, ваші дані не валідні.";
    public static final String USER = "Sorry, your data not validated.";
}
