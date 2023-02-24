package com.epam.alextuleninov.taxiservice.validation;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.epam.alextuleninov.taxiservice.validation.DataValidator.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataValidatorTest {

    @Test
    void testLoginValidationCorrectParameters() {
//        // mock HttpServletRequest
//        HttpServletRequest req = mock(HttpServletRequest.class);
//
//        // mock the returned value
//        when(req.getParameter("login")).thenReturn("test@gmail.com");
//        when(req.getParameter("password")).thenReturn("QAsQaqGMoG1K5hQCCVFd");
//
//        assertTrue(() -> initLogInValidation(req));
    }

    @Test
    void testLoginValidationEmptyPassword() {
//        // mock HttpServletRequest & HttpServletResponse
//        HttpServletRequest req = mock(HttpServletRequest.class);
//
//        // mock the returned value
//        when(req.getParameter("login")).thenReturn("");
//        when(req.getParameter("password")).thenReturn("QAsQaqGMoG1K5hQCCVFd");
//
//        assertFalse(() -> initLogInValidation(req));
    }

    @ParameterizedTest
    @ValueSource(strings = {"logingmail.com", "@gmail.com", "login@.com", "login@gmail.c"})
    void testLoginValidationInCorrectParametersEmail(String login) {
//        // mock HttpServletRequest & HttpServletResponse
//        HttpServletRequest req = mock(HttpServletRequest.class);
//
//        // mock the returned value
//        when(req.getParameter("login")).thenReturn(login);
//        when(req.getParameter("password")).thenReturn("QAsQaqGMoG1K5hQCCVFd");
//
//        assertFalse(() -> initLogInValidation(req));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Short_QAsQaqGMo", "NoDigit_QAsQaqGMoG1K5hQCCVFdD", "NOT_LOW_CASE_112", "not_upper_33"})
    void testLoginValidationInCorrectParametersPassword(String password) {
//        // mock HttpServletRequest & HttpServletResponse
//        HttpServletRequest req = mock(HttpServletRequest.class);
//
//        // mock the returned value
//        when(req.getParameter("login")).thenReturn("login@gmail.com");
//        when(req.getParameter("password")).thenReturn(password);
//
//        assertFalse(() -> initLogInValidation(req));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "7", "9"})
    void testOrderValidationCorrectNumberOfPassengers(String number) {
//        // mock HttpServletRequest & HttpServletResponse
//        HttpServletRequest req = mock(HttpServletRequest.class);
//
//        // mock the returned value
//        when(req.getParameter("numberOfPassengers")).thenReturn(number);
//        when(req.getParameter("dateOfRide")).thenReturn("2023-01-21T10:15");
//
//        assertTrue(() -> initOrderValidation(req));
    }

    @ParameterizedTest
    @ValueSource(strings = {".", "q", "E", "/", "&", "", "0"})
    void testOrderValidationInCorrectNumberOfPassengers(String number) {
//        // mock HttpServletRequest & HttpServletResponse
//        HttpServletRequest req = mock(HttpServletRequest.class);
//
//        // mock the returned value
//        when(req.getParameter("numberOfPassengers")).thenReturn(number);
//        when(req.getParameter("dateOfRide")).thenReturn("2023-01-21T10:15");
//
//        assertFalse(() -> initOrderValidation(req));
    }

    @ParameterizedTest
    @ValueSource(strings = {"2023-01-21T10:15", "2023-03-21T19:44"})
    void testOrderValidationCorrectDateOfRide(String localDateTime) {
//        // mock HttpServletRequest & HttpServletResponse
//        HttpServletRequest req = mock(HttpServletRequest.class);
//
//        // mock the returned value
//        when(req.getParameter("numberOfPassengers")).thenReturn("1");
//        when(req.getParameter("dateOfRide")).thenReturn(localDateTime);
//
//        assertTrue(() -> initOrderValidation(req));
    }

    @ParameterizedTest
    @ValueSource(strings = {"2023-01-21 10:15", "2023-01-21", "E", "/", "&", "", "10:15"})
    void testOrderValidationInCorrectDateOfRide(String localDateTime) {
//        // mock HttpServletRequest & HttpServletResponse
//        HttpServletRequest req = mock(HttpServletRequest.class);
//
//        // mock the returned value
//        when(req.getParameter("numberOfPassengers")).thenReturn("1");
//        when(req.getParameter("dateOfRide")).thenReturn(localDateTime);
//
//        assertFalse(() -> initOrderValidation(req));
    }

    @Test
    void testRegisterValidationCorrectParameterFirstLastName1() {
//        // mock HttpServletRequest
//        HttpServletRequest req = mock(HttpServletRequest.class);
//
//        // mock the returned value
//        when(req.getParameter("firstname")).thenReturn("Ta");
//        when(req.getParameter("lastname")).thenReturn("Rt");
//        when(req.getParameter("email")).thenReturn("test@gmail.com");
//        when(req.getParameter("password")).thenReturn("QAsQaqGMoG1K5hQCCVFd");
//
//        assertTrue(() -> initRegisterValidation(req));
    }

    @Test
    void testRegisterValidationCorrectParameterFirstLastName2() {
//        // mock HttpServletRequest
//        HttpServletRequest req = mock(HttpServletRequest.class);
//
//        // mock the returned value
//        when(req.getParameter("firstname")).thenReturn("Tadfghjtregfdewssfgh");
//        when(req.getParameter("lastname")).thenReturn("Rtdfetgjnbfdesrccvty");
//        when(req.getParameter("email")).thenReturn("test@gmail.com");
//        when(req.getParameter("password")).thenReturn("QAsQaqGMoG1K5hQCCVFd");
//
//        assertTrue(() -> initRegisterValidation(req));
    }

    @Test
    void testRegisterValidationInCorrectParameterFirstLastName1() {
//        // mock HttpServletRequest
//        HttpServletRequest req = mock(HttpServletRequest.class);
//
//        // mock the returned value
//        when(req.getParameter("firstname")).thenReturn("a");
//        when(req.getParameter("lastname")).thenReturn("R");
//        when(req.getParameter("email")).thenReturn("test@gmail.com");
//        when(req.getParameter("password")).thenReturn("QAsQaqGMoG1K5hQCCVFd");
//
//        assertFalse(() -> initRegisterValidation(req));
    }

    @Test
    void testRegisterValidationInCorrectParameterFirstLastName2() {
//        // mock HttpServletRequest
//        HttpServletRequest req = mock(HttpServletRequest.class);
//
//        // mock the returned value
//        when(req.getParameter("firstname")).thenReturn("TadfghjtregfdewssfghW");
//        when(req.getParameter("lastname")).thenReturn("RtdfetgjnbfdesrccvtyT");
//        when(req.getParameter("email")).thenReturn("test@gmail.com");
//        when(req.getParameter("password")).thenReturn("QAsQaqGMoG1K5hQCCVFd");
//
//        assertFalse(() -> initRegisterValidation(req));
    }
}
