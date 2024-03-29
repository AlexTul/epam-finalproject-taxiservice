package com.epam.alextuleninov.taxiservice.dao;

import com.epam.alextuleninov.taxiservice.data.pageable.PageableRequest;
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
     * @param request request with user`s parameters
     * @return created user from database
     */
    User create(UserRequest request);

    /**
     * Find all users with role like 'client' from the database.
     *
     * @return all users from the database
     */
    Set<User> findAllClient();

    /**
     * Find all users from the database with pagination.
     *
     * @return all users from database
     */
    Set<User> findAll(PageableRequest pageable);

    /**
     * Find user by email from the database.
     *
     * @param email user`s email
     * @return user from the database
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user`s role  by email from the database.
     *
     * @param email email by user
     * @return user from database
     */
    String findRoleByEmail(String email);

    /**
     * Check if user exists by email in the database.
     *
     * @param email email by user
     * @return true if user exists in database
     */
    boolean existsByEmail(String email);

    /**
     * Check if user exists by email and password in the database.
     *
     * @param email    email by user
     * @param password password by user
     * @return true if user exists in database
     */
    boolean existsByEmailPassword(String email, String password);

    /**
     * Find number of records from the database.
     *
     * @return number of record in database
     */
    long findNumberRecords();

    /**
     * Change user`s credentials by email int the database.
     *
     * @param request request form user
     */
    void changeCredentialsByEmail(String email, UserRequest request);

    /**
     * Change user`s password by email int the database.
     *
     * @param email    user`s login
     * @param password new user`s password
     */
    void changePasswordByEmail(String email, String password);

    /**
     * Delete the user from database.
     *
     * @param email email of user
     */
    void deleteByEmail(String email);
}
