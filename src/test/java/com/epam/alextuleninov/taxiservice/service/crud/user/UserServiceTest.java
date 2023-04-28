package com.epam.alextuleninov.taxiservice.service.crud.user;

import com.epam.alextuleninov.taxiservice.ConstantsTest;
import com.epam.alextuleninov.taxiservice.config.security.PasswordEncoderConfig;
import com.epam.alextuleninov.taxiservice.dao.user.JDBCUserDAO;
import com.epam.alextuleninov.taxiservice.dao.UserDAO;
import com.epam.alextuleninov.taxiservice.data.user.UserResponse;
import com.epam.alextuleninov.taxiservice.model.user.User;
import com.epam.alextuleninov.taxiservice.model.user.role.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import static com.epam.alextuleninov.taxiservice.Constants.SCOPE_SORT_ALL;
import static com.epam.alextuleninov.taxiservice.Constants.SCOPE_SORT_NOTHING;
import static com.epam.alextuleninov.taxiservice.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserDAO userDAO;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDAO = mock(JDBCUserDAO.class);
        userService = new UserService(userDAO);
    }

    @Test
    void testRegisterIfUserExists() {
        when(userDAO.existsByEmail(getTestUserRequest().email())).thenReturn(true).thenReturn(false);

        boolean registerAbsent = userService.register(getTestUserRequest());

        assertFalse(registerAbsent);
    }

    @Test
    void testRegisterIfUserNotExists() {
        when(userDAO.create(getTestUserRequest())).thenReturn(
                new User.UserBuilder()
                        .id(1)
                        .firstName("Example")
                        .lastName("Example")
                        .email("example@gmail.com")
                        .password(null)
                        .role(Role.CLIENT)
                        .build()
        );

        boolean registerPresent = userService.register(getTestUserRequest());

        assertTrue(registerPresent);
    }

    @Test
    void testFindAllClients() {
        Set<User> usersFromDB = new TreeSet<>();
        usersFromDB.add(getTestUser());
        usersFromDB.add(getTestAdmin());
        usersFromDB.add(getTestUser());

        when(userDAO.findAll(getTestPageableRequest())).thenReturn(usersFromDB);

        var resultPresent = userService.findAllClients(getTestPageableRequest());

        assertNotNull(resultPresent);
        assertEquals(1, resultPresent.size());
        assertEquals(UserResponse.fromUser(getTestUser()).getRole(), resultPresent.get(0).getRole());

        when(userDAO.findAll(getTestPageableRequest())).thenReturn(new TreeSet<>());

        var resultAbsent = userService.findAllClients(getTestPageableRequest());

        assertEquals(0, resultAbsent.size());
    }

    @Test
    void testFindAllLoginsClient() {
        Set<User> usersFromDB = new TreeSet<>();
        usersFromDB.add(getTestUser());

        when(userDAO.findAllClient()).thenReturn(usersFromDB);

        var allLoginsClient = userService.findAllLoginsClient();

        assertNotNull(allLoginsClient);
        assertEquals(SCOPE_SORT_NOTHING, allLoginsClient.get(0));
        assertEquals(SCOPE_SORT_ALL, allLoginsClient.get(1));
        assertEquals(getTestUser().getEmail(), allLoginsClient.get(2));

        // no users in database
        when(userDAO.findAllClient()).thenReturn(new TreeSet<>());

        assertNotNull(allLoginsClient);
        assertEquals(SCOPE_SORT_NOTHING, allLoginsClient.get(0));
        assertEquals(SCOPE_SORT_ALL, allLoginsClient.get(1));
    }

    @Test
    void testFindByEmail() {
        // if value is present
        when(userDAO.findByEmail(getTestOrderRequest().customer())).thenReturn(Optional.of(getTestUser()));
        Optional<UserResponse> resultPresent = userService.findByEmail(getTestOrderRequest().customer());

        assertNotNull(resultPresent);
        assertEquals(Optional.of(UserResponse.fromUser(getTestUser())), resultPresent);

        // if value is absent
        when(userDAO.findByEmail(getTestOrderRequest().customer())).thenReturn(Optional.empty());
        Optional<UserResponse> resultAbsent = userService.findByEmail(getTestOrderRequest().customer());

        assertEquals(Optional.empty(), resultAbsent);
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

        when(userDAO.findRoleByEmail(email)).thenReturn(ConstantsTest.USER_ROLE_CLIENT_VALUE);

        var actualRole = userService.findRoleByEmail(email);

        assertEquals(getTestUser().getRole().toString(), actualRole);
    }

    @Test
    void testFindNumberRecords() {
        // if value is present
        when(userDAO.findNumberRecords()).thenReturn(1L);
        long resultPresent = userService.findNumberRecords();

        assertEquals(1, resultPresent);

        // if value is absent
        when(userDAO.findNumberRecords()).thenReturn(0L);
        long resultAbsent = userService.findNumberRecords();

        assertEquals(0, resultAbsent);
    }

    @Test
    void testCredentialsByEmail() {
        doNothing().when(userDAO).changeCredentialsByEmail(getTestUser().getEmail(), getTestUserRequest());
        assertDoesNotThrow(() -> userService.changeCredentialsByEmail(getTestUser().getEmail(), getTestUserRequest()));
    }

    @Test
    void testChangePasswordByEmail() {
        String encryptPassword = getTestUser().getPassword();

        doNothing().when(userDAO).changeCredentialsByEmail(getTestUser().getEmail(), getTestUserRequest());
        assertDoesNotThrow(() -> userService.changePasswordByEmail(getTestUser().getEmail(), encryptPassword));
    }

    @Test
    void testDeleteById() {
        doNothing().when(userDAO).deleteByEmail(getTestUser().getEmail());
        assertDoesNotThrow(() -> userService.deleteByEmail(getTestUser().getEmail()));
    }
}
