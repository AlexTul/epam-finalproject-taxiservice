package com.epam.alextuleninov.taxiservice.validation;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import static com.epam.alextuleninov.taxiservice.Constants.*;

/**
 * The DataValidator class contains the methods for
 * validating an input data (data from forms).
 */
public final class DataValidator {

    private static final Logger log = LoggerFactory.getLogger(DataValidator.class);

    private final static String REGEX_CHECK_FOR_NAME = "^[a-zA-Zа-яА-Я\\s]{2,20}$";
    private final static String REGEX_EMAIL = "^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
    private final static String REGEX_PASSWORD = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,20}$";
    private final static String REGEX_NUMBER = "[0-9]+";
    private final static String REGEX_LOCAL_DATE_TIME = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}";

     /**
     * Validation a user`s register credentials.
     *
     * @param req    HttpServletRequest request
     * @return true if user credentials is valid
     */
    public static boolean initValidationRegisterCredentials(HttpServletRequest req) {
        String locale = (String) req.getSession().getAttribute(SCOPE_LOCALE);

        if (!validateName(req.getParameter(SCOPE_FIRST_NAME))) {
            log.info("User first name not validated");
            changeLocaleSession(locale, req, SCOPE_FIRST_NAME, FIRST_NAME_NOT_VALID_UK, FIRST_NAME_NOT_VALID);

            req.getSession().removeAttribute(SCOPE_LAST_NAME);
            req.getSession().removeAttribute(SCOPE_LOGIN_VALIDATE);
            req.getSession().removeAttribute(SCOPE_PASSWORD_VALIDATE);
            return false;
        }
        if (!validateName(req.getParameter(SCOPE_LAST_NAME))) {
            log.info("User last name not validated");
            changeLocaleSession(locale, req, SCOPE_LAST_NAME, LAST_NAME_NOT_VALID_UK, LAST_NAME_NOT_VALID);

            req.getSession().removeAttribute(SCOPE_FIRST_NAME);
            req.getSession().removeAttribute(SCOPE_LOGIN_VALIDATE);
            req.getSession().removeAttribute(SCOPE_PASSWORD_VALIDATE);
            return false;
        }
        if (!validateLogin(req.getParameter(SCOPE_LOGIN))) {
            log.info("User login not validated");
            changeLocaleSession(locale, req, SCOPE_LOGIN_VALIDATE, LOGIN_NOT_VALID_UK, LOGIN_NOT_VALID);

            req.getSession().removeAttribute(SCOPE_FIRST_NAME);
            req.getSession().removeAttribute(SCOPE_LAST_NAME);
            req.getSession().removeAttribute(SCOPE_PASSWORD_VALIDATE);
            return false;
        }
        if (!validatePassword(req.getParameter(SCOPE_PASSWORD))) {
            log.info("User password not validated");
            changeLocaleSession(locale, req, SCOPE_PASSWORD_VALIDATE, PASSWORD_NOT_VALID_UK, PASSWORD_NOT_VALID);

            req.getSession().removeAttribute(SCOPE_FIRST_NAME);
            req.getSession().removeAttribute(SCOPE_LAST_NAME);
            req.getSession().removeAttribute(SCOPE_LOGIN_VALIDATE);
            return false;
        }
        return true;
    }

    /**
     * Check registration data.
     *
     * @param req request from HttpServletRequest
     * @return true if validation is success
     */
    public static boolean initValidationChangeCredentials(HttpServletRequest req) {
        String locale = (String) req.getSession().getAttribute(SCOPE_LOCALE);

        if (!validateName(req.getParameter(SCOPE_FIRST_NAME))) {
            log.info("User first name not validated");
            changeLocaleSession(locale, req, SCOPE_FIRST_NAME, FIRST_NAME_NOT_VALID_UK, FIRST_NAME_NOT_VALID);

            req.getSession().removeAttribute(SCOPE_LAST_NAME);
            req.getSession().removeAttribute(SCOPE_LOGIN_VALIDATE);
            return false;
        }
        if (!validateName(req.getParameter(SCOPE_LAST_NAME))) {
            log.info("User last name not validated");
            changeLocaleSession(locale, req, SCOPE_LAST_NAME, LAST_NAME_NOT_VALID_UK, LAST_NAME_NOT_VALID);

            req.getSession().removeAttribute(SCOPE_FIRST_NAME);
            req.getSession().removeAttribute(SCOPE_LOGIN_VALIDATE);
            return false;
        }
        if (!validateLogin(req.getParameter(SCOPE_LOGIN))) {
            log.info("User login not validated");
            changeLocaleSession(locale, req, SCOPE_LOGIN_VALIDATE, LOGIN_NOT_VALID_UK, LOGIN_NOT_VALID);

            req.getSession().removeAttribute(SCOPE_FIRST_NAME);
            req.getSession().removeAttribute(SCOPE_LAST_NAME);
            return false;
        }
        return true;
    }

    /**
     * Validation a user`s login credentials.
     *
     * @param req  the HttpServletRequest request
     * @return true if user credentials is valid
     */
    public static boolean initValidationLogInCredentials(HttpServletRequest req) {
        String locale = (String) req.getSession().getAttribute(SCOPE_LOCALE);

        if (!validateLogin(req.getParameter(SCOPE_LOGIN))) {
            log.info("User login not validated");
            changeLocale(locale, req, SCOPE_LOGIN_VALIDATE, LOGIN_NOT_VALID_UK, LOGIN_NOT_VALID);
            return false;
        }
        if (!validatePassword(req.getParameter(SCOPE_PASSWORD))) {
            log.info("User password not validated");
            changeLocale(locale, req, SCOPE_PASSWORD_VALIDATE, PASSWORD_NOT_VALID_UK, PASSWORD_NOT_VALID);
            return false;
        }
        return true;
    }

    /**
     * Validation order`s data.
     *
     * @param req request from HttpServletRequest
     * @return true if validation is success
     */
    public static boolean initValidationOrderData(HttpServletRequest req) {
        String locale = (String) req.getSession().getAttribute(SCOPE_LOCALE);

        if (!validateNumber(req.getParameter(SCOPE_NUMBER_OF_PASSENGERS))) {
            log.info("Number of passengers not validated");
            changeLocale(locale, req, SCOPE_NUMBER_PASSENGERS_VALIDATE, NUMBER_PASSENGERS_NOT_VALID_UK, NUMBER_PASSENGERS_NOT_VALID);
            return false;
        }
        if (!validateLocalDateTime(req.getParameter(SCOPE_DATE_OF_TRAVEL))) {
            log.info("Date of travel not validated");
            changeLocale(locale, req, SCOPE_DATE_TIME_VALIDATE, DATE_TIME_NOT_VALID_UK, DATE_TIME_NOT_VALID);
            return false;
        }
        return true;
    }

    /**
     * Check password data.
     *
     * @param req request from HttpServletRequest
     * @return true if validation is success
     */
    public static boolean initPasswordValidation(HttpServletRequest req, String scope) {
        String locale = (String) req.getSession().getAttribute(SCOPE_LOCALE);
        if (!validatePassword(req.getParameter(scope))) {
            changeLocaleSession(locale, req, SCOPE_PASSWORD_VALIDATE, PASSWORD_NOT_VALID_UK, PASSWORD_NOT_VALID);
            return false;
        }
        return true;
    }

    /**
     * Check password data.
     *
     * @param req request from HttpServletRequest
     * @return true if validation is success
     */
    public static boolean initValidationChangePassword(HttpServletRequest req) {
        String locale = (String) req.getSession().getAttribute(SCOPE_LOCALE);

        if (!validatePassword(req.getParameter(SCOPE_NEW_PASSWORD))) {
            log.info("User new password not validated");
            changeLocaleSession(locale, req, SCOPE_PASSWORD_VALIDATE, PASSWORD_NOT_VALID_UK, PASSWORD_NOT_VALID);
            req.getSession().removeAttribute(SCOPE_CONFIRM_PASSWORD_VALIDATE);
            return false;
        }
        if (!validatePassword(req.getParameter(SCOPE_CONFIRM_PASSWORD))) {
            log.info("User confirm password not validated");
            changeLocaleSession(locale, req, SCOPE_CONFIRM_PASSWORD_VALIDATE, PASSWORD_NOT_VALID_UK, PASSWORD_NOT_VALID);
            req.getSession().removeAttribute(SCOPE_PASSWORD_VALIDATE);
            return false;
        }
        return true;
    }

    /**
     * Validate name.
     *
     * @param name name of user
     * @return true if validation is success
     */
    private static boolean validateName(String name) {
        return Pattern.matches(REGEX_CHECK_FOR_NAME, name);
    }

    /**
     * Validate login.
     *
     * @param login login from request
     * @return true if validation is success
     */
    private static boolean validateLogin(String login) {
        return Pattern.matches(REGEX_EMAIL, login);
    }

    /**
     * Validate password.
     *
     * @param password password from request
     * @return true if validation is success
     */
    private static boolean validatePassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    /**
     * Validate number of passengers in order.
     *
     * @param number number passengers in order
     * @return true if validation is success
     */
    private static boolean validateNumber(String number) {
        return !number.equals("0")
                && Pattern.matches(REGEX_NUMBER, number);
    }

    /**
     * Validate date and time in order.
     *
     * @param localDateTime number passengers in order
     * @return true if validation is success
     */
    private static boolean validateLocalDateTime(String localDateTime) {
        var now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        return Pattern.matches(REGEX_LOCAL_DATE_TIME, localDateTime)
                && localDateTime.compareTo(now) >= 0;
    }

    /**
     * Change locale from user request.
     *
     * @param locale     current locale
     * @param req        request from user
     * @param scope      scope for jsp page
     * @param notValidUk message on Ukrainian language
     * @param notValid   message on English language
     */
    private static void changeLocale(String locale, HttpServletRequest req, String scope,
                                     String notValidUk, String notValid) {
        if (locale.equals("uk_UA")) {
            req.setAttribute(scope, notValidUk);
        } else {
            req.setAttribute(scope, notValid);
        }
    }

    /**
     * Change locale from user request.
     *
     * @param locale     current locale
     * @param req        request from user
     * @param scope      scope for jsp page
     * @param notValidUk message on Ukrainian language
     * @param notValid   message on English language
     */
    private static void changeLocaleSession(String locale, HttpServletRequest req, String scope,
                                            String notValidUk, String notValid) {
        if (locale.equals("uk_UA")) {
            req.getSession().setAttribute(scope, notValidUk);
        } else {
            req.getSession().setAttribute(scope, notValid);
        }
    }
}
