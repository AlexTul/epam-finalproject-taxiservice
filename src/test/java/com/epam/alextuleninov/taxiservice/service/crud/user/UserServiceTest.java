package com.epam.alextuleninov.taxiservice.service.crud.user;

import com.epam.alextuleninov.taxiservice.ConstantsTest;
import com.epam.alextuleninov.taxiservice.config.security.PasswordEncoderConfig;
import com.epam.alextuleninov.taxiservice.dao.user.JDBCUserDAO;
import com.epam.alextuleninov.taxiservice.dao.user.UserDAO;
import com.epam.alextuleninov.taxiservice.model.user.User;
import com.epam.alextuleninov.taxiservice.model.user.role.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.epam.alextuleninov.taxiservice.TestUtils.getTestUser;
import static com.epam.alextuleninov.taxiservice.TestUtils.getTestUserRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private UserDAO userDAO;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDAO = mock(JDBCUserDAO.class);
        userService = new UserService(userDAO);
    }

    @Test
    void testRegister() {
        // if user exists
        when(userDAO.existsByEmail(getTestUserRequest().email())).thenReturn(true).thenReturn(false);

        boolean registerAbsent = userService.register(getTestUserRequest());

        assertFalse(registerAbsent);

        // if user not exists
        when(userDAO.create(getTestUserRequest())).thenReturn(
                new User(
                        1,
                        "Example",
                        "Example",
                        "example@gmail.com",
                        null,
                        Role.CLIENT
                ));

        boolean registerPresent = userService.register(getTestUserRequest());

        assertTrue(registerPresent);
    }

    @Test
    void testExistsByEmail() {
        var email = ConstantsTest.USER_EMAIL_VALUE;

        if (email.equals(getTestUser().getEmail())) {
            when(userDAO.existsByEmail(email)).thenReturn(true);
        }

        boolean resultPresent = userService.existsByEmail(email);

        assertTrue(resultPresent);
    }

    @Test
    void testAuthentication() {
        var email = ConstantsTest.USER_EMAIL_VALUE;
        var password = ConstantsTest.USER_PASSWORD_VALUE;

        var encryptPassword = PasswordEncoderConfig
                .passwordEncoder()
                .encrypt(password);

        if (email.equals(getTestUser().getEmail())
                && encryptPassword.equals(getTestUser().getPassword())) {
            when(userDAO.existsByEmailPassword(email, encryptPassword)).thenReturn(true);
        }

        boolean resultPresent = userService.authentication(email, password);

        assertTrue(resultPresent);
    }

    @Test
    void testFindRoleByEmail() {
        var email = ConstantsTest.USER_EMAIL_VALUE;

        when(userDAO.findRoleByEmail(email)).thenReturn(ConstantsTest.USER_ROLE_VALUE);

        var actualRole = userService.findRoleByEmail(email);

        assertEquals(getTestUser().getRole().toString(), actualRole);
    }
}
