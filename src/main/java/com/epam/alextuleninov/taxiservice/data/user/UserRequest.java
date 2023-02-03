package com.epam.alextuleninov.taxiservice.data.user;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Record for the UserRequest.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public record UserRequest(

        String firstName,
        String lastName,
        String email,
        String password
) {

    /**
     * Create the new record from HttpServletRequest.
     *
     * @param req           request from HttpServletRequest
     * @return              new record from User
     */
    public static UserRequest getUserRequest(HttpServletRequest req) {
        return new UserRequest(
                req.getParameter("firstname"),
                req.getParameter("lastname"),
                req.getParameter("email"),
                req.getParameter("password")
        );
    }
}
