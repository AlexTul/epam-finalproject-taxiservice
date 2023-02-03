package com.epam.alextuleninov.taxiservice.connectionpool;

/**
 * The class with data source fields.
 * */
public final class DataSourceFields {

    // region user
    public static final String USER_ID = "id";
    public static final String USER_FIRST_NAME = "first_name";
    public static final String USER_LAST_NAME = "last_name";
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "password";
    public static final String USER_ROLE = "role";
    // endregion user

    // region car
    public static final String CAR_ID = "car_id";
    public static final String CAR_NAME = "car_name";
    public static final String CAR_PASSENGERS = "car_passengers";
    public static final String CAR_CAR_CATEGORY = "car_category";
    public static final String CAR_STATUS = "car_status";
    // endregion car

    // region route
    public static final String ROUTE_ID = "id";
    public static final String ROUTE_START_END = "start_end";
    public static final String ROUTE_DISTANCE = "distance";
    public static final String ROUTE_PRICE = "route_price";
    public static final String ROUTE_TRAVEL_TIME = "travel_time";
    // endregion route

    // region addresses
    public static final String ADDRESS_ID = "id";
    public static final String ADDRESS_START_END = "start_end";
    public static final String ADDRESS_START_END_UK = "start_end_uk";
    // endregion addresses

    // region order
    public static final String ORDER_ID = "id";
    public static final String ORDER_DATE = "date";
    public static final String ORDER_CUSTOMER_ID = "customer_id";
    public static final String ORDER_PASSENGERS = "order_passengers";
    public static final String ORDER_ROUTE_ID = "route_id";
    public static final String ORDER_COST = "cost";
    public static final String ORDER_STARTED_AT = "started_at";
    public static final String ORDER_FINISHED_AT = "finished_at";
    // endregion order
}
