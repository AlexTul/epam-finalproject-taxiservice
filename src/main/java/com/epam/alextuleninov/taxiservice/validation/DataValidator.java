package com.epam.alextuleninov.taxiservice.validation;

import jakarta.servlet.http.HttpServletRequest;

import java.util.regex.Pattern;

/**
 * The DataValidator class contains the methods for
 * validating an input data (data from forms).
 */
public final class DataValidator {

    private final static String REGEX_CHECK_FOR_NAME = "^[a-zA-Zа-яА-Я\\s]{2,20}$";
    private final static String REGEX_EMAIL = "^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
    private final static String REGEX_PASSWORD = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,20}$";
    private final static String REGEX_NUMBER = "\\d";
    private final static String REGEX_LOCAL_DATE_TIME = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}";

    /**
     * Check registration data.
     *
     * @param req               request from HttpServletRequest
     * @return                  true if validation is success
     */
    public static boolean initRegisterValidation(HttpServletRequest req) {
        return validateName(req.getParameter("firstname"))
                && validateName(req.getParameter("lastname"))
                && validateLogin(req.getParameter("email"))
                && validatePassword(req.getParameter("password"));
    }

    /**
     * Check login data.
     *
     * @param req               request from HttpServletRequest
     * @return                  true if validation is success
     */
    public static boolean initLoginValidation(HttpServletRequest req) {
        return validateLogin(req.getParameter("login"))
                && validatePassword(req.getParameter("password"));
    }

    /**
     * Check order data.
     *
     * @param req               request from HttpServletRequest
     * @return                  true if validation is success
     */
    public static boolean initOrderValidation(HttpServletRequest req) {
        return validateNumber(req.getParameter("numberOfPassengers"))
                && validateLocalDateTime(req.getParameter("dateOfRide"));
    }

    /**
     * Validate name.
     *
     * @param name              name of user
     * @return                  true if validation is success
     */
    private static boolean validateName(String name) {
        return Pattern.matches(REGEX_CHECK_FOR_NAME, name);
    }

    /**
     * Validate login.
     *
     * @param login             login of user
     * @return                  true if validation is success
     */
    private static boolean validateLogin(String login) {
        return Pattern.matches(REGEX_EMAIL, login);
    }

    /**
     * Validate password.
     *
     * @param password          password of user
     * @return                  true if validation is success
     */
    private static boolean validatePassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    /**
     * Validate number of passengers in order.
     *
     * @param number            number passengers in order
     * @return                  true if validation is success
     */
    private static boolean validateNumber(String number) {
        return !number.equals("0")
                && Pattern.matches(REGEX_NUMBER, number);
    }

    /**
     * Validate date and time in order.
     *
     * @param localDateTime     number passengers in order
     * @return                  true if validation is success
     */
    private static boolean validateLocalDateTime(String localDateTime) {
        return Pattern.matches(REGEX_LOCAL_DATE_TIME, localDateTime);
    }
}
