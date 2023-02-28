package com.epam.alextuleninov.taxiservice.service.crud.user;

import com.epam.alextuleninov.taxiservice.config.security.PasswordEncoderConfig;
import com.epam.alextuleninov.taxiservice.dao.user.UserDAO;
import com.epam.alextuleninov.taxiservice.data.pageable.PageableRequest;
import com.epam.alextuleninov.taxiservice.data.user.UserRequest;
import com.epam.alextuleninov.taxiservice.data.user.UserResponse;
import com.epam.alextuleninov.taxiservice.model.user.User;
import com.epam.alextuleninov.taxiservice.model.user.role.Role;

import java.util.*;
import java.util.stream.Collectors;

import static com.epam.alextuleninov.taxiservice.Constants.SCOPE_SORT_ALL;
import static com.epam.alextuleninov.taxiservice.Constants.SCOPE_SORT_NOTHING;

/**
 * Class CRUD for User.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public class UserService implements UserCRUD {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Create the user in the database.
     *
     * @param request request with user`s parameters
     * @return created user from database
     */
    @Override
    public boolean register(UserRequest request) {
        if (existsByEmail(request.email())) {
            return false;
        } else {
            var password = request.password();
            var encryptPassword = PasswordEncoderConfig
                    .passwordEncoder()
                    .encrypt(password);

            userDAO.create(new UserRequest(
                    request.firstName(),
                    request.firstName(),
                    request.email(),
                    encryptPassword
            ));
        }
        return true;
    }

    /**
     * Find all clients from database.
     *
     * @param pageable request with pagination information
     * @return list with clients
     */
    @Override
    public List<UserResponse> findAllClients(PageableRequest pageable) {
        return userDAO.findAll(pageable).stream()
                .filter(user -> user.getRole().equals(Role.CLIENT))
                .sorted(Comparator.comparingLong(User::getId))
                .map(UserResponse::fromUser).toList();
    }

    /**
     * Find all logins of clients.
     *
     * @return list with logins
     */
    @Override
    public List<String> findAllLoginsClient() {
        var listLoginsClient = userDAO.findAllClient().stream()
                .map(User::getEmail)
                .collect(Collectors.toCollection(LinkedList::new));
        listLoginsClient.addAll(0, Arrays.asList(SCOPE_SORT_NOTHING, SCOPE_SORT_ALL));

        return listLoginsClient;
    }

    @Override
    public Optional<UserResponse> findByEmail(String email) {
        return userDAO.findByEmail(email).map(UserResponse::fromUser);
    }

    /**
     * Check if user exists by email in the database.
     *
     * @param email email by user
     * @return true if user exists in database
     */
    @Override
    public boolean existsByEmail(String email) {
        return userDAO.existsByEmail(email);
    }

    /**
     * Check if user exists by email and password in the database.
     *
     * @param email    email by user
     * @param password password by user
     * @return user from database
     */
    @Override
    public boolean authentication(String email, String password) {
        var encryptPassword = PasswordEncoderConfig
                .passwordEncoder()
                .encrypt(password);

        return userDAO.existsByEmailPassword(email, encryptPassword);
    }

    /**
     * Find user`s role  by email from the database.
     *
     * @param email email by user
     * @return user from database
     */
    @Override
    public String findRoleByEmail(String email) {
        return userDAO.findRoleByEmail(email);
    }

    /**
     * Find number of records from the database.
     *
     * @return number of record in database
     */
    @Override
    public long findNumberRecords() {
        return userDAO.findNumberRecords();
    }

    /**
     * Change user`s credentials by email int the database.
     *
     * @param request request form user
     */
    @Override
    public void changeCredentialsByEmail(String email, UserRequest request) {
        userDAO.changeCredentialsByEmail(email, request);
    }

    /**
     * Change user`s password by email int the database.
     *
     * @param email   user`s login
     * @param newPassword new password
     */
    @Override
    public void changePasswordByEmail(String email, String newPassword) {
        var encryptPassword = PasswordEncoderConfig
                .passwordEncoder()
                .encrypt(newPassword);

        userDAO.changePasswordByEmail(email, encryptPassword);
    }

    /**
     * Delete the user from database.
     *
     * @param email email of user
     */
    @Override
    public void deleteByEmail(String email) {
        userDAO.deleteByEmail(email);
    }
}
