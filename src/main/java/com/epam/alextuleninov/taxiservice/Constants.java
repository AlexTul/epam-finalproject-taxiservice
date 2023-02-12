package com.epam.alextuleninov.taxiservice;

import java.time.format.DateTimeFormatter;

/**
 * The Constants class contains the constants for application.
 */
public final class Constants {

    // region connection pool
    public static final String SETTINGS_FILE = "app.properties";
    // endregion connection pool

    // region to calculate the date and time of the trip
    public static final long CAR_DELIVERY_TIME_MILLI = 15 * 60 * 1000;
    public static final long CAR_DELIVERY_TIME_SECOND = 15 * 60;
    // endregion to calculate the date and time of the trip

    // region to format date and time
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    // endregion to format date and time

    // region pagination settings
    public static final int PAGE_SIZE = 5;
    public static final String SORTING_ASC = "asc";
    public static final String SORTING_DESC = "desc";
    // endregion pagination settings

    // region message
    public static final String ADMIN_REPORT_ALL_UK = " усі замовлення";
    public static final String ADMIN_REPORT_ALL = " all orders";
    public static final String ADMIN_REPORT_CUSTOM_UK = " за замовником ";
    public static final String ADMIN_REPORT_CUSTOM = " by customer of ";
    public static final String ADMIN_REPORT_DATE_UK = " за датою початку ";
    public static final String ADMIN_REPORT_DATE = " by started at date ";
    public static final String USER_UK = "Вибачте, ваші дані не валідні.";
    public static final String USER = "Sorry, your data not validated.";
    public static final String USER_SUCC_REGISTER_UK = "Успішна реєстрація. Пройдіть на сторінку логіну.";
    public static final String USER_SUCC_REGISTER = "Successful registration. Go to login page.";
    public static final String USER_FAIL_REGISTER_UK = "Вибачте, цей email існує.";
    public static final String USER_FAIL_REGISTER = "Sorry, this email exists.";
    public static final String USER_CANCEL_ORDER_UK = "Немає вільних машин, скасуйте замовлення.";
    public static final String USER_CANCEL_ORDER = "No available cars, cancel order.";
    // endregion message
}
