package com.epam.alextuleninov.taxiservice.controller.user;

import com.epam.alextuleninov.taxiservice.config.context.AppContext;
import com.epam.alextuleninov.taxiservice.config.mail.EmailConfig;
import com.epam.alextuleninov.taxiservice.config.pagination.PaginationConfig;
import com.epam.alextuleninov.taxiservice.data.pageable.PageableRequest;
import com.epam.alextuleninov.taxiservice.service.crud.user.UserCRUD;
import com.epam.alextuleninov.taxiservice.validation.DataValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.epam.alextuleninov.taxiservice.Constants.*;
import static com.epam.alextuleninov.taxiservice.Routes.*;
import static com.epam.alextuleninov.taxiservice.exceptions.user.UserExceptions.userNotFound;

/**
 * Servlet for to process a Http request from admin.
 */
@WebServlet(name = "UserServlet", urlPatterns = URL_USER)
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

    /**
     * To process Get requests from admin:
     * - forward on action user`s page for update user in the database;
     * - forward on user`s page for show all users in the database
     *
     * @param req HttpServletRequest request
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter(SCOPE_ACTION) == null ?
                (String) req.getSession().getAttribute(SCOPE_ACTION) : req.getParameter(SCOPE_ACTION);
        req.getSession().setAttribute(SCOPE_ACTION, action);

        // update user in the database
        if (action != null && action.equals("update")) {
            processRequestGetUpdate(req);

            req.getRequestDispatcher(PAGE_USER_ACTION)
                    .forward(req, resp);
        } else {
            // show all users in the database
            processRequestGet(req);

            req.getRequestDispatcher(PAGE_ADMIN_USER)
                    .forward(req, resp);
        }
    }

    /**
     * To process Post requests from admin:
     * - update user in the database;
     * - delete user from the database
     *
     * @param req  HttpServletRequest request
     * @param resp HttpServletResponse response
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        // update user in the database
        String updateUserLogin = (String) req.getSession().getAttribute(SCOPE_UPDATE_USER_LOGIN);
        // delete user from the database
        String deleteUserLogin = req.getParameter(SCOPE_LOGIN);

        if (deleteUserLogin != null) {
            updateUserLogin = null;
        }

        if (updateUserLogin != null) {
            if (DataValidator.initPasswordValidation(req, SCOPE_NEW_PASSWORD)) {
                processUpdateUser(req, updateUserLogin);

                resp.sendRedirect(URL_USER);
            } else {
                req.getRequestDispatcher(PAGE_USER_ACTION)
                        .forward(req, resp);
            }
        } else if (deleteUserLogin != null) {
            processDeleteUser(deleteUserLogin);

            resp.sendRedirect(URL_USER);
        }
    }

    @Override
    public void destroy() {
        log.info(getServletName() + " destroyed");
    }

    /**
     * To process Get requests from admin:
     * - add parameters to user`s action page for updating user in the database.
     *
     * @param req HttpServletRequest request
     */
    private void processRequestGetUpdate(HttpServletRequest req) {
        String updateUserByLogin = req.getParameter(SCOPE_LOGIN);

        String login;
        if (updateUserByLogin != null) {
            login = updateUserByLogin;
            req.getSession().setAttribute(SCOPE_UPDATE_USER_LOGIN, login);
        } else {
            login = (String) req.getSession().getAttribute(SCOPE_UPDATE_USER_LOGIN);
        }

        var userResponse = userCRUD.findByEmail(login)
                .orElseThrow(() -> userNotFound(login));

        req.getSession().setAttribute(SCOPE_USER_RESPONSE, userResponse);
        req.getSession().removeAttribute(SCOPE_ACTION);
    }

    /**
     * To process Get requests from admin:
     * - forward on user`s page for show all users in the database
     *
     * @param req HttpServletRequest request
     */
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

    /**
     * To process update user in the database.
     *
     * @param req             HttpServletRequest request
     * @param updateUserLogin identifier to update the user from the request
     */
    private void processUpdateUser(HttpServletRequest req, String updateUserLogin) {
        var newPassword = req.getParameter(SCOPE_NEW_PASSWORD);

        userCRUD.changePasswordByEmail(updateUserLogin, newPassword);

        new Thread(() ->
                emailSender.send(EMAIL_UPDATE_PASSWORD,
                        String.format(EMAIL_UPDATE_PASSWORD_BODY, newPassword), updateUserLogin)
        ).start();

        req.getSession().removeAttribute(SCOPE_UPDATE_USER_LOGIN);
        req.getSession().removeAttribute(SCOPE_ACTION);
        req.getSession().removeAttribute(SCOPE_USER_RESPONSE);
        req.getSession().removeAttribute(SCOPE_PASSWORD_VALIDATE);
    }

    /**
     * To process delete user in the database.
     *
     * @param deleteUserLogin identifier to delete the user from the request
     */
    private void processDeleteUser(String deleteUserLogin) {
        userCRUD.deleteByEmail(deleteUserLogin);

        new Thread(() -> emailSender.send(EMAIL_DELETE_USER_SUBJECT, EMAIL_DELETE_USER_BODY, deleteUserLogin)).start();
    }
}
