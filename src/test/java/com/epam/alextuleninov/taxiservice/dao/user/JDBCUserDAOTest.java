package com.epam.alextuleninov.taxiservice.dao.user;

import com.epam.alextuleninov.taxiservice.ConstantsTest;
import com.epam.alextuleninov.taxiservice.connectionpool.DataSourceFields;
import com.epam.alextuleninov.taxiservice.dao.mappers.ResultSetMapper;
import com.epam.alextuleninov.taxiservice.dao.mappers.UserMapper;
import com.epam.alextuleninov.taxiservice.exceptions.datasource.UnexpectedDataAccessException;
import com.epam.alextuleninov.taxiservice.model.user.User;
import com.epam.alextuleninov.taxiservice.model.user.role.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

import static com.epam.alextuleninov.taxiservice.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

public class JDBCUserDAOTest {

    private DataSource dataSource;
    private UserDAO userDAO;
    private ResultSet resultSet;

    @BeforeEach
    void setUp() {
        dataSource = mock(DataSource.class);
        ResultSetMapper<User> userMapper = new UserMapper();
        userDAO = new JDBCUserDAO(dataSource, userMapper);
        resultSet = mock(ResultSet.class);
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
    void testSqlExceptionFindAllClient() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        try {
            assertThrows(UnexpectedDataAccessException.class, (Executable) userDAO.findAllClient());
        } catch (UnexpectedDataAccessException ignored) {
        }
    }

    @Test
    void testFindByEmail() throws SQLException {
        try (var preparedStatement = prepareMocks(dataSource)) {
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            // if value is present
            prepareResultSetPresent(resultSet);
            User resultPresent = userDAO.findByEmail(getTestOrderRequest()).orElse(null);

            assertEquals(getTestUser(), resultPresent);

            // if value is absent
            prepareResultSetAbsent(resultSet);
            Optional<User> resultAbsent = userDAO.findByEmail(getTestOrderRequest());

            assertEquals(Optional.empty(), resultAbsent);
        }
    }

    @Test
    void testSqlExceptionFindByEmail() throws SQLException {
        when(dataSource.getConnection()).thenThrow(new SQLException());
        try {
            assertThrows(UnexpectedDataAccessException.class, (Executable) userDAO.findByEmail(getTestOrderRequest())
                    .orElse(null));
        } catch (UnexpectedDataAccessException ignored) {}
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
        when(resultSet.getString(DataSourceFields.USER_ROLE)).thenReturn(ConstantsTest.USER_ROLE_VALUE);
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
