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
    public static final String PAGE_CONFIRM = "/WEB-INF/jsp/confirm.jsp";
    public static final String PAGE_ORDER_SUCCESSFUL = "/WEB-INF/jsp/order_successful.jsp";
    public static final String PAGE_ADMIN = "/WEB-INF/jsp/admin.jsp";
    public static final String PAGE_REPORT = "/WEB-INF/jsp/report.jsp";
    public static final String PAGE_USER = "/WEB-INF/jsp/admin_user.jsp";
    public static final String PAGE_CAR = "/WEB-INF/jsp/admin_car.jsp";
    public static final String PAGE_CAR_ACTION = "/WEB-INF/jsp/car_action.jsp";
    public static final String PAGE_MESSAGE_ADMIN = "/WEB-INF/jsp/message_admin.jsp";
    public static final String PAGE_MESSAGE_USER = "/WEB-INF/jsp/message_user.jsp";
    public static final String PAGE_MESSAGE_ORDER_USER = "/WEB-INF/jsp/message_order_user.jsp";
    // endregion pages

    // region url
    public static final String URL_EMPTY = "/*";
    public static final String URL_EMPTY_ = "/";
    public static final String URL_ADMIN = "/admin";
    public static final String URL_USER = "/user";
    public static final String URL_CAR = "/car";
    public static final String URL_CAR_ = "/car/*";
    public static final String URL_ORDER = "/order";
    public static final String URL_CONFIRM = "/confirm";
    public static final String URL_REPORT = "/report";
    public static final String URL_REPORT_ = "/report/*";
    public static final String URL_SUC = "/successful";
    public static final String URL_MESSAGE_USER = "/messageuser";
    public static final String URL_AUTH = "/auth";
    public static final String URL_REGISTER = "/register";
    public static final String URL_LOGOUT = "/logout";
    // endregion url
}
