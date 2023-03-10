package com.epam.alextuleninov.taxiservice.dao.user;

import com.epam.alextuleninov.taxiservice.ConstantsTest;
import com.epam.alextuleninov.taxiservice.connectionpool.DataSourceFields;
import com.epam.alextuleninov.taxiservice.dao.mappers.ResultSetMapper;
import com.epam.alextuleninov.taxiservice.dao.mappers.UserMapper;
import com.epam.alextuleninov.taxiservice.data.user.UserRequest;
import com.epam.alextuleninov.taxiservice.exceptions.datasource.UnexpectedDataAccessException;
import com.epam.alextuleninov.taxiservice.model.user.User;
import com.epam.alextuleninov.taxiservice.model.user.role.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;
import java.util.Set;

import static com.epam.alextuleninov.taxiservice.TestUtils.*;
import static com.epam.alextuleninov.taxiservice.exceptions.user.UserExceptions.userNotFound;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

public class JDBCUserDAOTest {

    private DataSource dataSource;
    private UserDAO userDAO;
    private ResultSet resultSet;
    private ResultSetMapper<User> mapper;

    @BeforeEach
    void setUp() {
        this.dataSource = mock(DataSource.class);
        this.mapper = new UserMapper();
        this.userDAO = new JDBCUserDAO(dataSource, mapper);
        this.resultSet = mock(ResultSet.class);
    }

    @Test
    void testCreate() throws SQLException {
        Connection connection = mock(Connection.class);
        DataSource dataSource = mock(DataSource.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet generatedKeys = mock(ResultSet.class);
        var request = new UserRequest(
                getTestUserRequest().firstName(),
                getTestUserRequest().lastName(),
                getTestUserRequest().email(),
                getTestUserRequest().password()
        );

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(generatedKeys);
        when(generatedKeys.next()).thenReturn(true);
        when(generatedKeys.getLong(1)).thenReturn(123L);

        var userDAO = new JDBCUserDAO(dataSource, mapper);

        var createdUser = userDAO.create(request);

        assertNotNull(createdUser);
        assertEquals(123L, createdUser.getId());
        assertEquals(request.firstName(), createdUser.getFirstName());
        assertEquals(request.lastName(), createdUser.getLastName());
        assertEquals(request.email(), createdUser.getEmail());
        assertNull(createdUser.getPassword());
    }

    @Test
    void testSQLExceptionCreate() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> userDAO.create(getTestUserRequest()));
    }

    @Test
    void testFindAllClient() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            // if value is present
            prepareResultSetPresent(resultSet);
            Set<User> resultPresent = userDAO.findAllClient();

            assertEquals(1, resultPresent.size());
            assertEquals(getTestUser(), resultPresent.stream().toList().get(0));

            // if value is absent
            prepareResultSetAbsent(resultSet);
            Set<User> resultAbsent = userDAO.findAllClient();

            assertEquals(0, resultAbsent.size());
        }
    }

    @Test
    void testSQLExceptionFindAllClient() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> userDAO.findAllClient());
    }

    @Test
    void testFindAll() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            // if value is present
            prepareResultSetPresent(resultSet);
            Set<User> resultPresent = userDAO.findAll(getTestPageableRequest());

            assertEquals(1, resultPresent.size());
            assertEquals(getTestUser(), resultPresent.stream().toList().get(0));

            // if value is absent
            prepareResultSetAbsent(resultSet);
            Set<User> resultAbsent = userDAO.findAll(getTestPageableRequest());

            assertEquals(0, resultAbsent.size());
        }
    }

    @Test
    void testSQLExceptionFindAll() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> userDAO.findAll(getTestPageableRequest()));
    }

    @Test
    void testFindByEmail() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            // if value is present
            prepareResultSetPresent(resultSet);
            Optional<User> resultPresent = userDAO.findByEmail(getTestOrderRequest().customer());

            assertEquals(getTestUser(), resultPresent.orElseThrow(() -> userNotFound(getTestOrderRequest().customer())));

            // if value is absent
            prepareResultSetAbsent(resultSet);
            Optional<User> resultAbsent = userDAO.findByEmail(getTestOrderRequest().customer());

            assertEquals(Optional.empty(), resultAbsent);
        }
    }

    @Test
    void testSQLExceptionFindByEmail() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> userDAO.findByEmail(getTestOrderRequest().customer())
                .orElseThrow(() -> userNotFound(getTestOrderRequest().customer())));
    }

    @Test
    void testFindRoleByEmail() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            // if value is present
            prepareResultSetPresent(resultSet);
            String resultPresent = userDAO.findRoleByEmail(getTestOrderRequest().customer());

            assertEquals(Role.CLIENT.toString(), resultPresent);

            // if value is absent
            prepareResultSetAbsent(resultSet);
            String resultAbsent = userDAO.findRoleByEmail(getTestOrderRequest().customer());

            assertEquals("", resultAbsent);
        }
    }

    @Test
    void testSQLExceptionFindRoleByEmail() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> userDAO.findRoleByEmail(getTestOrderRequest().customer()));
    }

    @Test
    void testExistsByEmail() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            // if value is present
            prepareResultSetPresent(resultSet);
            boolean resultPresent = userDAO.existsByEmail(getTestOrderRequest().customer());

            assertTrue(resultPresent);

            // if value is absent
            prepareResultSetAbsent(resultSet);
            boolean resultAbsent = userDAO.existsByEmail(getTestOrderRequest().customer());

            assertFalse(resultAbsent);
        }
    }

    @Test
    void testSQLExceptionExistsByEmail() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> userDAO.existsByEmail(getTestOrderRequest().customer()));
    }

    @Test
    void testExistsByEmailPassword() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            // if value is present
            prepareResultSetEmailPasswordPresent(resultSet);
            boolean resultPresent = userDAO.existsByEmailPassword(getTestOrderRequest().customer(),
                    "QAsQaqGMoG1K5hQCCVFd");

            assertTrue(resultPresent);

            // if value is absent
            prepareResultSetEmailPasswordAbsent(resultSet);
            boolean resultAbsent = userDAO.existsByEmailPassword(getTestOrderRequest().customer(),
                    "QAsQaqGMoG1K5hQCCVFd");

            assertFalse(resultAbsent);
        }
    }

    @Test
    void testSQLExceptionExistsByEmailPassword() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> userDAO.existsByEmail(getTestOrderRequest().customer()));
    }

    @Test
    void testFindNumberRecords() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            // if value is present
            when(resultSet.next()).thenReturn(true).thenReturn(false);
            when(resultSet.getLong(1)).thenReturn(1L);
            long resultPresent = userDAO.findNumberRecords();

            assertEquals(1, resultPresent);

            // if value is absent
            when(resultSet.next()).thenReturn(true).thenReturn(false);
            when(resultSet.getLong(1)).thenReturn(0L);
            long resultAbsent = userDAO.findNumberRecords();

            assertEquals(0, resultAbsent);
        }
    }

    @Test
    void testSQLExceptionFindNumberRecords() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> userDAO.findNumberRecords());
    }

    @Test
    void testChangeCredentialsByEmail() throws SQLException {
        try (var ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> userDAO.changeCredentialsByEmail(
                    getTestUserRequest().email(), getTestUserRequest()));
        }
    }

    @Test
    void testSQLExceptionChangeCredentialsByEmail() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> userDAO.changeCredentialsByEmail(
                getTestUserRequest().email(), getTestUserRequest()));
    }

    @Test
    void testChangePasswordByEmail() throws SQLException {
        try (var ignored = prepareMocks(dataSource)) {
            assertDoesNotThrow(() -> userDAO.changePasswordByEmail(
                    getTestUserRequest().email(), getTestUserRequest().password()));
        }
    }

    @Test
    void testSQLExceptionChangePasswordByEmail() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> userDAO.changePasswordByEmail(
                getTestUserRequest().email(), getTestUserRequest().password()));
    }

    @Test
    void testDeleteById() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true).thenReturn(false);
            when(resultSet.getLong(1)).thenReturn(1L);

            assertDoesNotThrow(() -> userDAO.deleteByEmail(getTestUser().getEmail()));
        }
    }

    @Test
    void testSQLExceptionDeleteById() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        assertThrows(UnexpectedDataAccessException.class, () -> userDAO.deleteByEmail(getTestUser().getEmail()));
    }

    private PreparedStatement prepareMocks(DataSource dataSource) throws SQLException {
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(isA(String.class))).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(isA(int.class), isA(int.class));
        doNothing().when(preparedStatement).setLong(isA(int.class), isA(long.class));
        doNothing().when(preparedStatement).setString(isA(int.class), isA(String.class));
        when(preparedStatement.execute()).thenReturn(true);

        return preparedStatement;
    }

    private static void prepareResultSetPresent(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong(DataSourceFields.USER_ID)).thenReturn(ConstantsTest.USER_ID_VALUE);
        when(resultSet.getString(DataSourceFields.USER_FIRST_NAME)).thenReturn(ConstantsTest.USER_FIRST_NAME_VALUE);
        when(resultSet.getString(DataSourceFields.USER_LAST_NAME)).thenReturn(ConstantsTest.USER_LAST_NAME_VALUE);
        when(resultSet.getString(DataSourceFields.USER_EMAIL)).thenReturn(ConstantsTest.USER_EMAIL_VALUE);
        when(resultSet.getString(DataSourceFields.USER_PASSWORD)).thenReturn(null);
        when(resultSet.getString(DataSourceFields.USER_ROLE)).thenReturn(ConstantsTest.USER_ROLE_CLIENT_VALUE);
    }

    private static void prepareResultSetAbsent(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(false);
    }

    private static void prepareResultSetEmailPasswordPresent(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString(DataSourceFields.USER_EMAIL)).thenReturn(ConstantsTest.USER_EMAIL_VALUE);
        when(resultSet.getString(DataSourceFields.USER_PASSWORD)).thenReturn(ConstantsTest.USER_PASSWORD_VALUE);
    }

    private static void prepareResultSetEmailPasswordAbsent(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(false);
    }
}
