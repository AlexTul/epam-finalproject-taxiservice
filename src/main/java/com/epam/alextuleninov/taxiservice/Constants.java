package com.epam.alextuleninov.taxiservice;

import java.time.format.DateTimeFormatter;

/**
 * The Constants class contains the constants for application.
 */
public final class Constants {

    public Constants() {
        throw new AssertionError("non-instantiable class");
    }

    // region connection pool
    public static final String SETTINGS_FILE = "app.properties";
    // endregion connection pool

    // region scope
    public static final String SCOPE_FIRST_NAME = "firstname";
    public static final String SCOPE_LAST_NAME = "lastname";
    public static final String SCOPE_LOCALE = "locale";
    public static final String SCOPE_LOGIN = "login";
    public static final String SCOPE_LOGIN_VALIDATE = "loginValidate";
    public static final String SCOPE_PASSWORD = "password";
    public static final String SCOPE_PASSWORD_VALIDATE = "passwordValidate";
    public static final String SCOPE_CONFIRM_PASSWORD_VALIDATE = "confirmPasswordValidate";
    public static final String SCOPE_NEW_PASSWORD = "newPassword";
    public static final String SCOPE_CONFIRM_PASSWORD = "confirmPassword";
    public static final String SCOPE_ROLE = "role";
    public static final String SCOPE_ID = "id";
    public static final String SCOPE_PAGE = "page";
    public static final String SCOPE_CURRENT_PAGE = "currentPage";
    public static final String SCOPE_TOTAL_RECORDS = "totalRecords";
    public static final String SCOPE_LAST_PAGE = "lastPage";
    public static final String SCOPE_USER_RESPONSE = "userResponse";
    public static final String SCOPE_USER_RESPONSES = "userResponses";
    public static final String SCOPE_UPDATE_USER_LOGIN = "updateUserLogin";
    public static final String SCOPE_MESSAGE = "message";
    public static final String SCOPE_MESSAGE_UK = "messageUK";
    public static final String SCOPE_MESSAGE_ORDER = "messageOrder";
    public static final String SCOPE_MESSAGE_ORDER_UK = "messageOrderUK";
    public static final String SCOPE_CARS = "cars";
    public static final String SCOPE_CAR_NAME = "carName";
    public static final String SCOPE_UPDATE_CAR_ID = "updateCarID";
    public static final String SCOPE_LIST_OF_CARS = "listOfCars";
    public static final String SCOPE_CAR_RESPONSES = "carResponses";
    public static final String SCOPE_CAR_RESPONSE = "carResponse";
    public static final String SCOPE_ORDERS = "orders";
    public static final String SCOPE_ORDER_RESPONSE = "orderResponse";
    public static final String SCOPE_ORDER_BY = "orderBy";
    public static final String SCOPE_WHOSE_ORDERS = "whoseOrders";
    public static final String SCOPE_UPDATE_ORDER_ID = "updateOrderID";
    public static final String SCOPE_CUSTOMER_OF_ORDERS = "customerOfOrders";
    public static final String SCOPE_CUSTOMERS_OF_ORDERS = "customersOfOrders";
    public static final String SCOPE_DATE_OF_ORDERS = "dateOfOrders";
    public static final String SCOPE_DATES_OF_ORDERS = "datesOfOrders";
    public static final String SCOPE_START_TRAVEL = "startTravel";
    public static final String SCOPE_END_TRAVEL = "endTravel";
    public static final String SCOPE_TRAVEL_DISTANCE = "travelDistance";
    public static final String SCOPE_DATE_OF_TRAVEL = "dateOfTravel";
    public static final String SCOPE_DATE_TIME_OF_TRAVEL = "dateTimeTravel";
    public static final String SCOPE_DATE_TIME_VALIDATE = "dateTimeValidate";
    public static final String SCOPE_TRAVEL_DURATION = "travelDuration";
    public static final String SCOPE_NUMBER_OF_PASSENGERS = "numberOfPassengers";
    public static final String SCOPE_NUMBER_PASSENGERS_VALIDATE = "numberOfPassengersValidate";
    public static final String SCOPE_LOYALTY_PRICE = "loyaltyPrice";
    public static final String SCOPE_PRICE_OF_TRAVEL = "priceOfTravel";
    public static final String SCOPE_SORT_NOTHING = "--------------------------";
    public static final String SCOPE_SORT_ALL = "all";
    public static final String SCOPE_SORT_BY_DATE = "sortByDate";
    public static final String SCOPE_SORT_BY_COST = "sortByCost";
    public static final String SCOPE_FILTER_BY_CUSTOMER = "filterByCustomer";
    public static final String SCOPE_FILTER_BY_DATE = "filterByDate";
    public static final String SCOPE_ACTION = "action";
    public static final String SCOPE_AUTHENTICATION = "authentication";
    public static final String SCOPE_PASSWORD_CONFIRMING = "passwordConfirming";
    public static final String SCOPE_MESSAGE_ADDRESS_INVALID = "addressInvalid";
    public static final String SCOPE_MESSAGE_ADDRESS_INVALID_UK = "addressInvalidUK";
    // endregion scope

    // region of the travel
    public static final long CAR_DELIVERY_TIME_MILLI = 15 * 60 * 1000;
    public static final long CAR_DELIVERY_TIME_SECOND = 15 * 60;
    public static final double TRAVEL_PRICE_KILOMETER = 10.00;
    public static final double TRAVEL_PRICE_MINIMAL = 40.00;
    // endregion of the travel

    // region of the loyalty
    public static final double COUNT_COST_MINIMAL = 200.00;
    public static final double COUNT_COST_MEDIUM = 300.00;
    public static final double LOYALTY_RATIO_MINIMAL = 1.00;
    public static final double LOYALTY_RATIO_MEDIUM = 0.97;
    public static final double LOYALTY_RATIO_MAXIMUM = 0.95;
    // endregion of the loyalty

    // region of the cars characteristics
    public static final int NUMBER_OF_PASSENGER_CAR = 4;
    public static final int NUMBER_OF_CARGO_CAR = 2;
    // endregion of the cars characteristics

    // region to format date and time
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    // endregion to format date and time

    // region pagination settings
    public static final int PAGE_SIZE = 5;
    public static final String SORTING_ASC = "asc";
    // endregion pagination settings
}
