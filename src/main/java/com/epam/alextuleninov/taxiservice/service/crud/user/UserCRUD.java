package com.epam.alextuleninov.taxiservice.service.crud.user;

import com.epam.alextuleninov.taxiservice.data.pageable.PageableRequest;
import com.epam.alextuleninov.taxiservice.data.user.UserRequest;
import com.epam.alextuleninov.taxiservice.data.user.UserResponse;

import java.util.List;
import java.util.Optional;

/**
 * Interface CRUD for User.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public interface UserCRUD {

    /**
     * Create the user in the database.
     *
     * @param request request with user`s parameters
     * @return created user from database
     */
    boolean register(UserRequest request);

    /**
     * Find all clients from database.
     *
     * @param pageable request with pagination information
     * @return list with clients
     */
    List<UserResponse> findAllClients(PageableRequest pageable);

    /**
     * Find all logins of clients.
     *
     * @return list with logins
     */
    List<String> findAllLoginsClient();

    /**
     * Find client by email from database.
     *
     * @param email user`s email
     * @return list with clients
     */
    Optional<UserResponse> findByEmail(String email);

    /**
     * Check if user exists by email in the database.
     *
     * @param email email by user
     * @return true if user exists in database
     */
    boolean existsByEmail(String email);


    boolean authentication(String email, String password);

    /**
     * Find user`s role  by email from the database.
     *
     * @param email email by user
     * @return user from database
     */
    String findRoleByEmail(String email);

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
     * @param email user`s login
     * @param newPassword new password
     */
    void changePasswordByEmail(String email, String newPassword);

    /**
     * Delete the user from database.
     *
     * @param id id of user
     */
    void deleteByID(long id);

    /**
     * Delete the user from database.
     *
     * @param email email of user
     */
    void deleteByEmail(String email);
}
