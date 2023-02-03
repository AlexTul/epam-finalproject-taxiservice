package com.epam.alextuleninov.taxiservice.service.crud.user;

import com.epam.alextuleninov.taxiservice.config.security.PasswordEncoderConfig;
import com.epam.alextuleninov.taxiservice.dao.user.UserDAO;
import com.epam.alextuleninov.taxiservice.data.user.UserRequest;
import com.epam.alextuleninov.taxiservice.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
     * @param request       request with user`s parameters
     * @return              created user from database
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
     * Find all logins of clients.
     *
     * @return              list with logins
     */
    @Override
    public List<String> findAllLoginsClient() {
        var listLoginsClient = userDAO.findAllClient().stream()
                .map(User::getEmail)
                .collect(Collectors.toCollection(LinkedList::new));
        listLoginsClient.addAll(0, Arrays.asList("--------------------------", "all orders"));

        return listLoginsClient;
    }

    /**
     * Check if user exists by email in the database.
     *
     * @param email         email by user
     * @return              true if user exists in database
     */
    @Override
    public boolean existsByEmail(String email) {
        return userDAO.existsByEmail(email);
    }

    /**
     * Check if user exists by email and password in the database.
     *
     * @param email         email by user
     * @param password      password by user
     * @return              user from database
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
     * @param email         email by user
     * @return              user from database
     */
    @Override
    public String findRoleByEmail(String email) {
        return userDAO.findRoleByEmail(email);
    }
}
