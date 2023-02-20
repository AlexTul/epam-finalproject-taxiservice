package com.epam.alextuleninov.taxiservice;

import java.time.format.DateTimeFormatter;

/**
 * The Constants class contains the constants for application.
 */
public final class Constants {

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
    public static final String SCOPE_NEW_PASSWORD = "newPassword";
    public static final String SCOPE_ROLE = "role";
    public static final String SCOPE_ID = "id";
    public static final String SCOPE_PAGE = "page";
    public static final String SCOPE_CURRENT_PAGE = "currentPage";
    public static final String SCOPE_TOTAL_RECORDS = "totalRecords";
    public static final String SCOPE_LAST_PAGE = "lastPage";
    public static final String SCOPE_USER_RESPONSE = "userResponse";
    public static final String SCOPE_USER_RESPONSES = "userResponses";
    public static final String SCOPE_UPDATE_USER_LOGIN = "updateUserLogin";
    public static final String SCOPE_MESSAGE_USER = "messageUser";
    public static final String SCOPE_REGISTER_TRUE_FALSE = "registerSuccess";
    public static final String SCOPE_CAR = "car";
    public static final String SCOPE_CARS = "cars";
    public static final String SCOPE_CAR_NAME = "carName";
    public static final String SCOPE_UPDATE_CAR_ID = "updateCarID";
    public static final String SCOPE_LIST_OF_CARS = "listOfCars";
    public static final String SCOPE_CAR_RESPONSES = "carResponses";
    public static final String SCOPE_ORDERS = "orders";
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
    public static final String SCOPE_TRAVEL_DURATION = "travelDuration";
    public static final String SCOPE_NUMBER_OF_PASSENGERS = "numberOfPassengers";
    public static final String SCOPE_LOYALTY_PRICE = "loyaltyPrice";
    public static final String SCOPE_PRICE_OF_TRAVEL = "priceOfTravel";
    public static final String SCOPE_SORT_BY_DATE = "sortByDate";
    public static final String SCOPE_SORT_BY_COST = "sortByCost";
    public static final String SCOPE_FILTER_BY_CUSTOMER = "filterByCustomer";
    public static final String SCOPE_FILTER_BY_DATE = "filterByDate";
    // endregion scope

    // region of the travel
    public static final long CAR_DELIVERY_TIME_MILLI = 15 * 60 * 1000;
    public static final long CAR_DELIVERY_TIME_SECOND = 15 * 60;
    public static final double TRAVEL_PRICE_KILOMETER = 10.00;
    public static final double TRAVEL_PRICE_MINIMAL = 40.00;
    // endregion of the travel

    // region to format date and time
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    // endregion to format date and time

    // region pagination settings
    public static final int PAGE_SIZE = 5;
    public static final String SORTING_ASC = "asc";
    public static final String SORTING_DESC = "desc";
    // endregion pagination settings

    // region message
    public static final String FIRST_NAME_NOT_VALID_UK = "Ім'я не валідне";
    public static final String FIRST_NAME_NOT_VALID = "First name is not valid";
    public static final String LAST_NAME_NOT_VALID_UK = "Прізвище не валідне";
    public static final String LAST_NAME_NOT_VALID = "Last name is not valid";
    public static final String LOGIN_NOT_VALID_UK = "Логін не валідний";
    public static final String LOGIN_NOT_VALID = "Login is not valid";
    public static final String PASSWORD_NOT_VALID_UK = "Пароль не валідний";
    public static final String PASSWORD_NOT_VALID = "Password is not valid";
    public static final String ADMIN_REPORT_ALL_UK = " усі замовлення";
    public static final String ADMIN_REPORT_ALL = " all orders";
    public static final String ADMIN_REPORT_CUSTOM_UK = " за замовником ";
    public static final String ADMIN_REPORT_CUSTOM = " by customer of ";
    public static final String ADMIN_REPORT_DATE_UK = " за датою початку ";
    public static final String ADMIN_REPORT_DATE = " by started at date ";
    public static final String USER_UK = "Вибачте, ваші дані не валідні.";
    public static final String USER = "Sorry, your data not validated.";
    public static final String USER_REGISTER_SUC_UK = "Успішна реєстрація. Пройдіть на сторінку логіну.";
    public static final String USER_REGISTER_SUC = "Successful registration. Go to login page.";
    public static final String USER_REGISTER_FAIL_UK = "Вибачте, цей email існує.";
    public static final String USER_REGISTER_FAIL = "Sorry, this email exists.";
    public static final String USER_ORDER_CANCEL_UK = "Немає вільних машин, змініть або скасуйте замовлення.";
    public static final String USER_ORDER_CANCEL = "No available cars, change or cancel order.";
    // endregion message

    // region email
    public static final String EMAIL_REGISTER_SUBJECT = "Registration in a Taxi Service";
    public static final String EMAIL_REGISTER_BODY = "Dear user,<br>welcome to Taxi Service.<br>" +
            "Thank you for your choosing!<br><br>Beast regards!<br>Taxi Service team";
    public static final String EMAIL_DELETE_USER_SUBJECT = "Deleting profile in a Taxi Service";
    public static final String EMAIL_DELETE_USER_BODY = "Dear user,<br>your profile was delete in Taxi Service.<br><br>" +
            "Best Regards,<br>Market Place";
    public static final String EMAIL_UPDATE_USER_PASSWORD = "Updating password in a Taxi Service";
    public static final String EMAIL_UPDATE_USER_BODY = "Dear user,<br>your password was change in Market Place.<br>" +
            "Your password is: %s.<br>Best Regards,<br>Taxi Service team";

    // endregion email
}
