package com.epam.alextuleninov.taxiservice.dao.user;

import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.data.user.UserRequest;
import com.epam.alextuleninov.taxiservice.model.user.User;

import java.util.Optional;
import java.util.Set;

/**
 * Interface DAO for User.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public interface UserDAO {

    /**
     * Create the user in the database.
     *
     * @param request       request with user`s parameters
     * @return              created user from database
     */
    User create(UserRequest request);

    /**
     * Find all users by client from the database.
     *
     * @return              all users from database
     */
    Set<User> findAllClient();

    /**
     * Find user by email from the database.
     *
     * @param request       request with order`s parameters
     * @return              user from the database
     */
    Optional<User> findByEmail(OrderRequest request);

    /**
     * Find user`s role  by email from the database.
     *
     * @param email         email by user
     * @return              user from database
     */
    String findRoleByEmail(String email);

    /**
     * Check if user exists by email in the database.
     *
     * @param email         email by user
     * @return              true if user exists in database
     */
    boolean existsByEmail(String email);

    /**
     * Check if user exists by email and password in the database.
     *
     * @param email         email by user
     * @param password      password by user
     * @return              true if user exists in database
     */
    boolean existsByEmailPassword(String email, String password);
}
