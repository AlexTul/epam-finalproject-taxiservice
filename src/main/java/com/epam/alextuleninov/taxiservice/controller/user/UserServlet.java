package com.epam.alextuleninov.taxiservice.controller.user;

import com.epam.alextuleninov.taxiservice.config.context.AppContext;
import com.epam.alextuleninov.taxiservice.config.mail.EmailConfig;
import com.epam.alextuleninov.taxiservice.config.pagination.PaginationConfig;
import com.epam.alextuleninov.taxiservice.data.car.CarRequest;
import com.epam.alextuleninov.taxiservice.data.pageable.PageableRequest;
import com.epam.alextuleninov.taxiservice.data.user.ChangeUserPasswordRequest;
import com.epam.alextuleninov.taxiservice.data.user.UserResponse;
import com.epam.alextuleninov.taxiservice.service.crud.user.UserCRUD;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

import static com.epam.alextuleninov.taxiservice.Constants.*;
import static com.epam.alextuleninov.taxiservice.Routes.*;
import static com.epam.alextuleninov.taxiservice.exceptions.car.CarExceptions.carNotFound;
import static com.epam.alextuleninov.taxiservice.exceptions.user.UserExceptions.userNotFound;

@WebServlet(name = "UserServlet", urlPatterns = URL_USER_)
public class UserServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UserServlet.class);

    private UserCRUD userCRUD;
    private EmailConfig emailSender;

    @Override
    public void init() {
        this.userCRUD = AppContext.getAppContext().getUserCRUD();
        this.emailSender = AppContext.getAppContext().getEmailSender();
        log.info(getServletName() + " initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String actionAdd = req.getRequestURL().toString();
        if (actionAdd.endsWith("update")) {
            String updateUserLogin = req.getParameter(SCOPE_LOGIN);

            String login;
            if (updateUserLogin != null) {
                login = updateUserLogin;
                req.getSession().setAttribute(SCOPE_UPDATE_USER_LOGIN, login);
            } else {
                login = (String) req.getSession().getAttribute(SCOPE_UPDATE_USER_LOGIN);
            }

            var userResponse = userCRUD.findClientByEmail(login)
                    .orElseThrow(() -> userNotFound(login));

            req.setAttribute(SCOPE_USER_RESPONSE, userResponse);

            req.getRequestDispatcher(PAGE_USER_ACTION)
                    .forward(req, resp);
        } else {
            // show all users in the database
            processRequestGet(req);

            req.getRequestDispatcher(PAGE_USER)
                    .forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        // update car in the database
        String updateUserLogin = (String) req.getSession().getAttribute(SCOPE_UPDATE_USER_LOGIN);
        // delete car from the database
        String userLogin = req.getParameter(SCOPE_LOGIN);

        if (updateUserLogin != null) {
            String newPassword = req.getParameter(SCOPE_NEW_PASSWORD);
            var changeUserPasswordRequest = new ChangeUserPasswordRequest(null, newPassword);
            userCRUD.changePasswordByEmail(updateUserLogin, changeUserPasswordRequest);
            req.getSession().removeAttribute(SCOPE_UPDATE_CAR_ID);

            emailSender.send(EMAIL_UPDATE_USER_PASSWORD,
                    String.format(EMAIL_UPDATE_USER_BODY, changeUserPasswordRequest.newPassword()), updateUserLogin);
        } else if (userLogin != null) {
            userCRUD.deleteByEmail(userLogin);

            emailSender.send(EMAIL_DELETE_USER_SUBJECT, EMAIL_DELETE_USER_BODY, userLogin);
        }

        resp.sendRedirect(URL_USER);
    }

    @Override
    public void destroy() {
        log.info(getServletName() + " destroyed");
    }

    private void processRequestGet(HttpServletRequest req) {
        long numberRecordsUsersInDatabase = userCRUD.findNumberRecords();

        // set attribute for pagination, total_records = all records from database - 1 (admin records)
        req.setAttribute(SCOPE_TOTAL_RECORDS, numberRecordsUsersInDatabase - 1);
        // set current page for pagination
        int page = new PaginationConfig().configPage(req);
        // find all orders with pagination for report`s page
        new PaginationConfig().config(req);

        var allClients = userCRUD.findAllClients(PageableRequest.getPageableRequest(page));
        req.getSession().setAttribute(SCOPE_USER_RESPONSES, allClients);
    }
}
