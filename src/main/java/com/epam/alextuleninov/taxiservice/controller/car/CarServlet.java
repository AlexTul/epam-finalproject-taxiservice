package com.epam.alextuleninov.taxiservice.controller.car;

import com.epam.alextuleninov.taxiservice.Routes;
import com.epam.alextuleninov.taxiservice.config.context.AppContext;
import com.epam.alextuleninov.taxiservice.config.pagination.PaginationConfig;
import com.epam.alextuleninov.taxiservice.data.car.CarRequest;
import com.epam.alextuleninov.taxiservice.data.pageable.PageableRequest;
import com.epam.alextuleninov.taxiservice.service.crud.car.CarCRUD;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.epam.alextuleninov.taxiservice.exceptions.car.CarExceptions.carNotFound;

/**
 * CarServlet for to process a Http request from admin.
 */
@WebServlet(name = "CarServlet", urlPatterns = "/car/*")
public class CarServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(CarServlet.class);

    private CarCRUD carCRUD;

    @Override
    public void init() {
        this.carCRUD = AppContext.getAppContext().getCarCRUD();
        log.info(getServletName() + " initialized");
    }

    /**
     * To process Get requests from admin:
     * - forward on action car`s page for add car in the database;
     * - forward on action car`s page for update car in the database;
     * - forward on car`s page for show all cars in the database
     *
     * @param req HttpServletRequest request
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // redirect on action car page for add or update car in the database or show all cars in the database
        String actionAdd = req.getRequestURL().toString();
        if (actionAdd.endsWith("add")) {
            req.getRequestDispatcher(Routes.PAGE_CAR_ACTION)
                    .forward(req, resp);
        } else if (actionAdd.endsWith("update")) {
            String updateCarID = req.getParameter("id");
            int id = Integer.parseInt(updateCarID);
            req.getSession().setAttribute("updateCarID", updateCarID);
            var car = carCRUD.findByID(id).orElseThrow(() -> carNotFound(id));
            req.setAttribute("car", car);
            req.getRequestDispatcher(Routes.PAGE_CAR_ACTION)
                    .forward(req, resp);
        } else {
            // show all cars in the database
            processRequestGet(req);

            req.getRequestDispatcher(Routes.PAGE_CAR)
                    .forward(req, resp);
        }
    }

    /**
     * To process Post requests from admin:
     * - create car in the database;
     * - update car in the database;
     * - delete car from the database
     *
     * @param req  HttpServletRequest request
     * @param resp HttpServletResponse response
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        // create car in the database
        String carName = req.getParameter("carName");
        // update car in the database
        String updateCarID = (String) req.getSession().getAttribute("updateCarID");
        // delete car from the database
        String carID = req.getParameter("id");

        if (carName != null && updateCarID == null) {
            carCRUD.create(CarRequest.getCarRequest(req));
        } else if (updateCarID != null) {
            carCRUD.updateByID(Integer.parseInt(updateCarID), CarRequest.getCarRequest(req));
            req.getSession().removeAttribute("updateCarID");
        } else if (carID != null) {
            carCRUD.deleteById(Integer.parseInt(carID));
        }

        resp.sendRedirect("/car");
    }

    @Override
    public void destroy() {
        log.info(getServletName() + " destroyed");
    }

    /**
     * To process Get requests from admin:
     * - forward on car`s page for show all cars in the database
     *
     * @param req HttpServletRequest request
     */
    private void processRequestGet(HttpServletRequest req) {
        long numberRecordsCarsInDatabase = carCRUD.findNumberRecords();
        // set attribute for pagination, total_records = all records from database
        req.setAttribute("total_records", numberRecordsCarsInDatabase);
        // set current page for pagination
        int page = new PaginationConfig().configPage(req);
        // find all orders with pagination for report`s page
        new PaginationConfig().config(req);

        var allCars = carCRUD.findAll(PageableRequest.getCarPageableRequest(page));
        req.getSession().setAttribute("carResponses", allCars);
    }
}
