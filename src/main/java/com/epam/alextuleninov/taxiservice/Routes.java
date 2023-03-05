package com.epam.alextuleninov.taxiservice;

/**
 * The class contains the routes for application.
 */
public final class Routes {

    public Routes() {
        throw new AssertionError("non-instantiable class");
    }

    // region pages
    public static final String PAGE_LOGIN = "/login.jsp";
    public static final String PAGE_REGISTER = "/WEB-INF/jsp/register.jsp";
    public static final String PAGE_ORDER = "/WEB-INF/jsp/order.jsp";
    public static final String PAGE_ORDER_UPDATE = "/WEB-INF/jsp/order-update.jsp";
    public static final String PAGE_CONFIRM = "/WEB-INF/jsp/confirm.jsp";
    public static final String PAGE_ADMIN_MENU = "/WEB-INF/jsp/admin-menu.jsp";
    public static final String PAGE_CUSTOMER_MENU = "/WEB-INF/jsp/customer-menu.jsp";
    public static final String PAGE_REPORT = "/WEB-INF/jsp/report-admin.jsp";
    public static final String PAGE_REPORT_CUSTOMER = "/WEB-INF/jsp/report-customer.jsp";
    public static final String PAGE_ADMIN_USER = "/WEB-INF/jsp/admin-user.jsp";
    public static final String PAGE_USER_ACTION = "/WEB-INF/jsp/user-action.jsp";
    public static final String PAGE_ADMIN_CAR = "/WEB-INF/jsp/admin-car.jsp";
    public static final String PAGE_CAR_ACTION = "/WEB-INF/jsp/car-action.jsp";
    public static final String PAGE_MESSAGE = "/WEB-INF/jsp/message.jsp";
    public static final String PAGE_MESSAGE_ORDER = "/WEB-INF/jsp/message-order.jsp";
    public static final String PAGE_PROFILE = "/WEB-INF/jsp/profile.jsp";
    public static final String PAGE_CHANGE_PASSWORD = "/WEB-INF/jsp/change-password.jsp";
    // endregion pages

    // region url
    public static final String URL_EMPTY = "/*";
    public static final String URL_EMPTY_ = "/";
    public static final String URL_ADMIN = "/admin";
    public static final String URL_USER = "/user";
    public static final String URL_USER_ = "/user/*";
    public static final String URL_CAR = "/car";
    public static final String URL_CAR_ = "/car/*";
    public static final String URL_ORDER = "/order";
    public static final String URL_REPORT_ADMIN = "/report-admin";
    public static final String URL_REPORT_CUSTOMER = "/report-customer";
    public static final String URL_REPORT_ADMIN_ = "/report-admin/*";
    public static final String URL_MESSAGE = "/message";
    public static final String URL_AUTH = "/auth";
    public static final String URL_REGISTER = "/register";
    public static final String URL_LOGOUT = "/logout";
    public static final String URL_PROFILE = "/profile";
    public static final String URL_CUSTOMER = "/customer";
    // endregion url
}
