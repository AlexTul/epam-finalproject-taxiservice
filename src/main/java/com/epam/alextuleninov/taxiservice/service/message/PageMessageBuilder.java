package com.epam.alextuleninov.taxiservice.service.message;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Class for building message for admin and user for page.
 */
public class PageMessageBuilder {

    /**
     * Build message for user.
     *
     * @param req               HttpServletRequest request
     * @param locale            default or current locale of application
     * @param messageUK         message for admin on UK locale
     * @param message           message for admin on default locale
     */
    public static void buildMessageUser(HttpServletRequest req, String locale,
                                        String messageUK, String message) {
        if (locale.equals("uk_UA")) {
            req.getSession().setAttribute("messageUser", messageUK);
        } else {
            req.getSession().setAttribute("messageUser", message);
        }
    }

    /**
     * Build message for admin.
     *
     * @param req               HttpServletRequest request
     * @param locale            default or current locale of application
     * @param whoseOrders       attribute for scope
     * @param messageUK         message for admin on UK locale
     * @param message           message for admin on default locale
     */
    public static void buildMessageAdmin(HttpServletRequest req, String locale,  String whoseOrders,
                                         String messageUK, String message) {
        if (locale.equals("uk_UA")) {
            req.setAttribute(whoseOrders, messageUK);
        } else {
            req.setAttribute(whoseOrders, message);
        }
    }
}
