package com.epam.alextuleninov.taxiservice.controller.user;

import com.epam.alextuleninov.taxiservice.config.context.AppContext;
import com.epam.alextuleninov.taxiservice.config.pagination.PaginationConfig;
import com.epam.alextuleninov.taxiservice.data.pageable.PageableRequest;
import com.epam.alextuleninov.taxiservice.service.crud.user.UserCRUD;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.epam.alextuleninov.taxiservice.Constants.*;
import static com.epam.alextuleninov.taxiservice.Routes.PAGE_USER;
import static com.epam.alextuleninov.taxiservice.Routes.URL_USER;

@WebServlet(name = "UserServlet", urlPatterns = URL_USER)
public class UserServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UserServlet.class);

    private UserCRUD userCRUD;

    @Override
    public void init() {
        this.userCRUD = AppContext.getAppContext().getUserCRUD();
        log.info(getServletName() + " initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        processRequestGet(req);

        req.getRequestDispatcher(PAGE_USER)
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String userID = req.getParameter(SCOPE_ID);
        userCRUD.deleteById(Long.parseLong(userID));

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
