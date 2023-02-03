package com.epam.alextuleninov.taxiservice.config.context;

import com.epam.alextuleninov.taxiservice.connectionpool.MyDataSource;
import com.epam.alextuleninov.taxiservice.dao.car.CarDAO;
import com.epam.alextuleninov.taxiservice.dao.car.JDBCCarDAO;
import com.epam.alextuleninov.taxiservice.dao.mappers.*;
import com.epam.alextuleninov.taxiservice.dao.order.JDBCOrderDAO;
import com.epam.alextuleninov.taxiservice.dao.order.OrderDAO;
import com.epam.alextuleninov.taxiservice.dao.route.JDBCRouteDAO;
import com.epam.alextuleninov.taxiservice.dao.route.RouteDAO;
import com.epam.alextuleninov.taxiservice.dao.user.JDBCUserDAO;
import com.epam.alextuleninov.taxiservice.dao.user.UserDAO;
import com.epam.alextuleninov.taxiservice.model.car.Car;
import com.epam.alextuleninov.taxiservice.model.order.Order;
import com.epam.alextuleninov.taxiservice.model.route.Route;
import com.epam.alextuleninov.taxiservice.model.user.User;
import com.epam.alextuleninov.taxiservice.service.crud.car.CarCRUD;
import com.epam.alextuleninov.taxiservice.service.crud.car.CarService;
import com.epam.alextuleninov.taxiservice.service.crud.order.OrderCRUD;
import com.epam.alextuleninov.taxiservice.service.crud.order.OrderService;
import com.epam.alextuleninov.taxiservice.service.crud.route.RouteCRUD;
import com.epam.alextuleninov.taxiservice.service.crud.route.RouteService;
import com.epam.alextuleninov.taxiservice.service.crud.user.UserCRUD;
import com.epam.alextuleninov.taxiservice.service.crud.user.UserService;
import com.epam.alextuleninov.taxiservice.service.dateride.DateTimeRide;
import com.epam.alextuleninov.taxiservice.service.dateride.DateTimeRideRideService;
import com.epam.alextuleninov.taxiservice.service.loyalty.Loyalty;
import com.epam.alextuleninov.taxiservice.service.loyalty.LoyaltyService;
import com.epam.alextuleninov.taxiservice.service.verifyorder.VerifyOrder;
import com.epam.alextuleninov.taxiservice.service.verifyorder.VerifyOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

/**
 * AppContext class for initialization of resources of application.
 */
public class AppContext {

    private static final Logger log = LoggerFactory.getLogger(AppContext.class);

    private static AppContext appContext;

    private final UserCRUD userCRUD;
    private final RouteCRUD routeCRUD;
    private final OrderCRUD orderCRUD;
    private final CarCRUD carCRUD;
    private final Loyalty loyaltyService;
    private final VerifyOrder verifyOrderService;
    private final DateTimeRide dateTimeRide;

    private AppContext() {
        DataSource dataSource = MyDataSource.getConnectionsPool();
        ResultSetMapper<Car> carMapper = new CarMapper();
        CarDAO carDAO = new JDBCCarDAO(dataSource, carMapper);
        ResultSetMapper<Route> routeMapper = new RouteMapper();
        RouteDAO routeDAO = new JDBCRouteDAO(dataSource, routeMapper);
        ResultSetMapper<User> userMapper = new UserMapper();
        UserDAO userDAO = new JDBCUserDAO(dataSource, userMapper);
        ResultSetMapper<Order> orderMapper = new OrderMapper();
        OrderDAO orderDAO = new JDBCOrderDAO(dataSource, carMapper, routeMapper, userMapper, orderMapper);
        this.carCRUD = new CarService(carDAO);
        this.orderCRUD = new OrderService(orderDAO);
        this.routeCRUD = new RouteService(routeDAO);
        this.userCRUD = new UserService(userDAO);
        this.loyaltyService = new LoyaltyService(orderCRUD, routeCRUD);
        this.verifyOrderService = new VerifyOrderService(carCRUD);
        this.dateTimeRide = new DateTimeRideRideService();
        log.info("AppContext.class is initialized");
    }

    public static void createAppContext() {
        appContext = new AppContext();
    }

    public static AppContext getAppContext() {
        return appContext;
    }

    public UserCRUD getUserCRUD() {
        return userCRUD;
    }

    public RouteCRUD getRouteCRUD() {
        return routeCRUD;
    }

    public OrderCRUD getOrderCRUD() {
        return orderCRUD;
    }

    public CarCRUD getCarCRUD() {
        return carCRUD;
    }

    public Loyalty getLoyaltyService() {
        return loyaltyService;
    }

    public VerifyOrder getVerifyService() {
        return verifyOrderService;
    }

    public DateTimeRide getDateTimeRide() {
        return dateTimeRide;
    }
}
