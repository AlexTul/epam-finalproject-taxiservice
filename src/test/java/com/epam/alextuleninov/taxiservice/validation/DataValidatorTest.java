package com.epam.alextuleninov.taxiservice.validation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.epam.alextuleninov.taxiservice.Constants.*;
import static com.epam.alextuleninov.taxiservice.validation.DataValidator.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataValidatorTest {

    private HttpServletRequest req;
    private HttpSession session;

    @BeforeEach
    void setUp() {
        req = mock(HttpServletRequest.class);
        session = mock(HttpSession.class);
    }

    @Test
    void testInitValidationRegisterCredentialsCorrectParameterFirstLastName1() {
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute(SCOPE_LOCALE)).thenReturn("en");
        when(req.getParameter(SCOPE_FIRST_NAME)).thenReturn("Ta");
        when(req.getParameter(SCOPE_LAST_NAME)).thenReturn("Rt");
        when(req.getParameter(SCOPE_LOGIN)).thenReturn("test@gmail.com");
        when(req.getParameter(SCOPE_PASSWORD)).thenReturn("QAsQaqGMoG1K5hQCCVFd");

        assertTrue(() -> initValidationRegisterCredentials(req));
    }

    @Test
    void testRegisterValidationCorrectParameterFirstLastName2() {
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute(SCOPE_LOCALE)).thenReturn("en");
        when(req.getParameter(SCOPE_FIRST_NAME)).thenReturn("Tadfghjtregfdewssfgh");
        when(req.getParameter(SCOPE_LAST_NAME)).thenReturn("Rtdfetgjnbfdesrccvty");
        when(req.getParameter(SCOPE_LOGIN)).thenReturn("test@gmail.com");
        when(req.getParameter(SCOPE_PASSWORD)).thenReturn("QAsQaqGMoG1K5hQCCVFd");

        assertTrue(() -> initValidationRegisterCredentials(req));
    }

    @Test
    void testRegisterValidationIncorrectParameterFirstLastName1() {
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute(SCOPE_LOCALE)).thenReturn("en");
        when(req.getParameter(SCOPE_FIRST_NAME)).thenReturn("a");
        when(req.getParameter(SCOPE_LAST_NAME)).thenReturn("R");
        when(req.getParameter(SCOPE_LOGIN)).thenReturn("test@gmail.com");
        when(req.getParameter(SCOPE_PASSWORD)).thenReturn("QAsQaqGMoG1K5hQCCVFd");

        assertFalse(() -> initValidationRegisterCredentials(req));
    }

    @Test
    void testRegisterValidationInCorrectParameterFirstLastName2() {
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute(SCOPE_LOCALE)).thenReturn("en");
        when(req.getParameter(SCOPE_FIRST_NAME)).thenReturn("TadfghjtregfdewssfghW");
        when(req.getParameter(SCOPE_LAST_NAME)).thenReturn("RtdfetgjnbfdesrccvtyT");
        when(req.getParameter(SCOPE_LOGIN)).thenReturn("test@gmail.com");
        when(req.getParameter(SCOPE_PASSWORD)).thenReturn("QAsQaqGMoG1K5hQCCVFd");

        assertFalse(() -> initValidationRegisterCredentials(req));
    }

    @Test
    void testInitValidationChangeCredentialsCorrectParameterFirstLastName1() {
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute(SCOPE_LOCALE)).thenReturn("en");
        when(req.getParameter(SCOPE_FIRST_NAME)).thenReturn("Ta");
        when(req.getParameter(SCOPE_LAST_NAME)).thenReturn("Rt");
        when(req.getParameter(SCOPE_LOGIN)).thenReturn("test@gmail.com");

        assertTrue(() -> initValidationChangeCredentials(req));
    }

    @Test
    void testInitValidationChangeCredentialsCorrectParameterFirstLastName2() {
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute(SCOPE_LOCALE)).thenReturn("en");
        when(req.getParameter(SCOPE_FIRST_NAME)).thenReturn("Tadfghjtregfdewssfgh");
        when(req.getParameter(SCOPE_LAST_NAME)).thenReturn("Rtdfetgjnbfdesrccvty");
        when(req.getParameter(SCOPE_LOGIN)).thenReturn("test@gmail.com");

        assertTrue(() -> initValidationChangeCredentials(req));
    }

    @Test
    void testInitValidationChangeCredentialsIncorrectParameterFirstLastName1() {
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute(SCOPE_LOCALE)).thenReturn("en");
        when(req.getParameter(SCOPE_FIRST_NAME)).thenReturn("a");
        when(req.getParameter(SCOPE_LAST_NAME)).thenReturn("R");
        when(req.getParameter(SCOPE_LOGIN)).thenReturn("test@gmail.com");

        assertFalse(() -> initValidationChangeCredentials(req));
    }

    @Test
    void testInitValidationChangeCredentialsIncorrectParameterFirstLastName2() {
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute(SCOPE_LOCALE)).thenReturn("en");
        when(req.getParameter(SCOPE_FIRST_NAME)).thenReturn("TadfghjtregfdewssfghW");
        when(req.getParameter(SCOPE_LAST_NAME)).thenReturn("RtdfetgjnbfdesrccvtyT");
        when(req.getParameter(SCOPE_LOGIN)).thenReturn("test@gmail.com");

        assertFalse(() -> initValidationChangeCredentials(req));
    }

    @Test
    void testInitValidationLogInCredentials() {
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute(SCOPE_LOCALE)).thenReturn("en");
        when(req.getParameter(SCOPE_LOGIN)).thenReturn("test@gmail.com");
        when(req.getParameter(SCOPE_PASSWORD)).thenReturn("QAsQaqGMoG1K5hQCCVFd");

        assertTrue(() -> initValidationLogInCredentials(req));

    }

    @Test
    void testInitValidationLogInCredentialsEmptyLogin() {
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute(SCOPE_LOCALE)).thenReturn("en");
        when(req.getParameter(SCOPE_LOGIN)).thenReturn("");
        when(req.getParameter(SCOPE_PASSWORD)).thenReturn("QAsQaqGMoG1K5hQCCVFd");

        assertFalse(() -> initValidationLogInCredentials(req));
    }

    @Test
    void testInitValidationLogInCredentialsEmptyPassword() {
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute(SCOPE_LOCALE)).thenReturn("en");
        when(req.getParameter(SCOPE_LOGIN)).thenReturn("test@gmail.com");
        when(req.getParameter(SCOPE_PASSWORD)).thenReturn("");

        assertFalse(() -> initValidationLogInCredentials(req));
    }

    @ParameterizedTest
    @ValueSource(strings = {"logingmail.com", "@gmail.com", "login@.com", "login@gmail.c"})
    void testInitValidationLogInCredentialsIncorrectParametersEmail(String login) {
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute(SCOPE_LOCALE)).thenReturn("en");
        when(req.getParameter(SCOPE_LOGIN)).thenReturn(login);
        when(req.getParameter(SCOPE_PASSWORD)).thenReturn("QAsQaqGMoG1K5hQCCVFd");

        assertFalse(() -> initValidationLogInCredentials(req));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Short_QAsQaqGMo", "NoDigit_QAsQaqGMoG1K5hQCCVFdD", "NOT_LOW_CASE_112", "not_upper_33"})
    void testInitValidationLogInCredentialsIncorrectParametersPassword(String password) {
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute(SCOPE_LOCALE)).thenReturn("en");
        when(req.getParameter(SCOPE_LOGIN)).thenReturn("login@gmail.com");
        when(req.getParameter(SCOPE_PASSWORD)).thenReturn(password);

        assertFalse(() -> initValidationLogInCredentials(req));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "7", "9"})
    void testInitValidationOrderDataCorrectNumberOfPassengers(String number) {
        var now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute(SCOPE_LOCALE)).thenReturn("en");
        when(req.getParameter(SCOPE_NUMBER_OF_PASSENGERS)).thenReturn(number);
        when(req.getParameter(SCOPE_DATE_OF_TRAVEL)).thenReturn(now);

        assertTrue(() -> initValidationOrderData(req));
    }

    @ParameterizedTest
    @ValueSource(strings = {".", "q", "E", "/", "&", "", "0"})
    void testInitValidationOrderDataIncorrectNumberOfPassengers(String number) {
        var now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute(SCOPE_LOCALE)).thenReturn("en");
        when(req.getParameter(SCOPE_NUMBER_OF_PASSENGERS)).thenReturn(number);
        when(req.getParameter(SCOPE_DATE_OF_TRAVEL)).thenReturn(now);

        assertFalse(() -> initValidationOrderData(req));
    }

    @Test
    void testInitValidationOrderDataCorrectDateOfTravel() {
        var now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute(SCOPE_LOCALE)).thenReturn("en");
        when(req.getParameter(SCOPE_NUMBER_OF_PASSENGERS)).thenReturn("1");
        when(req.getParameter(SCOPE_DATE_OF_TRAVEL)).thenReturn(now);

        assertTrue(() -> initValidationOrderData(req));
    }

    @Test
    void testInitValidationOrderIncorrectDateOfTravel() {
        var nowIncorrect = LocalDateTime.now().minusMinutes(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute(SCOPE_LOCALE)).thenReturn("en");
        when(req.getParameter(SCOPE_NUMBER_OF_PASSENGERS)).thenReturn("1");
        when(req.getParameter(SCOPE_DATE_OF_TRAVEL)).thenReturn(nowIncorrect);

        assertFalse(() -> initValidationOrderData(req));
    }

    @Test
    void testInitPasswordValidationEmptyPassword() {
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute(SCOPE_LOCALE)).thenReturn("en");
        when(req.getParameter(SCOPE_NEW_PASSWORD)).thenReturn("");

        assertFalse(() -> initPasswordValidation(req, SCOPE_NEW_PASSWORD));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Short_QAsQaqGMo", "NoDigit_QAsQaqGMoG1K5hQCCVFdD", "NOT_LOW_CASE_112", "not_upper_33"})
    void testInitPasswordValidationIncorrectParametersPassword(String password) {
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute(SCOPE_LOCALE)).thenReturn("en");
        when(req.getParameter(SCOPE_NEW_PASSWORD)).thenReturn(password);

        assertFalse(() -> initPasswordValidation(req, SCOPE_NEW_PASSWORD));
    }

    @Test
    void testInitValidationChangePasswordEmptyPassword() {
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute(SCOPE_LOCALE)).thenReturn("en");
        when(req.getParameter(SCOPE_NEW_PASSWORD)).thenReturn("");
        when(req.getParameter(SCOPE_CONFIRM_PASSWORD)).thenReturn("");

        assertFalse(() -> initValidationChangePassword(req));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Short_QAsQaqGMo", "NoDigit_QAsQaqGMoG1K5hQCCVFdD", "NOT_LOW_CASE_112", "not_upper_33"})
    void testInitValidationChangePasswordIncorrectParametersNewPassword(String password) {
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute(SCOPE_LOCALE)).thenReturn("en");
        when(req.getParameter(SCOPE_NEW_PASSWORD)).thenReturn(password);
        when(req.getParameter(SCOPE_CONFIRM_PASSWORD)).thenReturn("QAsQaqGMoG1K5hQCCVFd");

        assertFalse(() -> initValidationChangePassword(req));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Short_QAsQaqGMo", "NoDigit_QAsQaqGMoG1K5hQCCVFdD", "NOT_LOW_CASE_112", "not_upper_33"})
    void testInitValidationChangePasswordIncorrectParametersConfirmPassword(String password) {
        when(req.getSession()).thenReturn(session);
        when(req.getSession().getAttribute(SCOPE_LOCALE)).thenReturn("en");
        when(req.getParameter(SCOPE_NEW_PASSWORD)).thenReturn("QAsQaqGMoG1K5hQCCVFd");
        when(req.getParameter(SCOPE_CONFIRM_PASSWORD)).thenReturn(password);

        assertFalse(() -> initValidationChangePassword(req));
    }
}
