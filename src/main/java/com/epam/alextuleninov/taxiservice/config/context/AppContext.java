package com.epam.alextuleninov.taxiservice.config.context;

import com.epam.alextuleninov.taxiservice.config.mail.EmailConfig;
import com.epam.alextuleninov.taxiservice.config.properties.PropertiesConfig;
import com.epam.alextuleninov.taxiservice.connectionpool.MyDataSource;
import com.epam.alextuleninov.taxiservice.dao.car.CarDAO;
import com.epam.alextuleninov.taxiservice.dao.car.JDBCCarDAO;
import com.epam.alextuleninov.taxiservice.dao.mappers.CarMapper;
import com.epam.alextuleninov.taxiservice.dao.mappers.OrderMapper;
import com.epam.alextuleninov.taxiservice.dao.mappers.ResultSetMapper;
import com.epam.alextuleninov.taxiservice.dao.mappers.UserMapper;
import com.epam.alextuleninov.taxiservice.dao.order.JDBCOrderDAO;
import com.epam.alextuleninov.taxiservice.dao.order.OrderDAO;
import com.epam.alextuleninov.taxiservice.dao.user.JDBCUserDAO;
import com.epam.alextuleninov.taxiservice.dao.user.UserDAO;
import com.epam.alextuleninov.taxiservice.model.car.Car;
import com.epam.alextuleninov.taxiservice.model.order.Order;
import com.epam.alextuleninov.taxiservice.model.user.User;
import com.epam.alextuleninov.taxiservice.service.crud.car.CarCRUD;
import com.epam.alextuleninov.taxiservice.service.crud.car.CarService;
import com.epam.alextuleninov.taxiservice.service.crud.order.OrderCRUD;
import com.epam.alextuleninov.taxiservice.service.crud.order.OrderService;
import com.epam.alextuleninov.taxiservice.service.crud.user.UserCRUD;
import com.epam.alextuleninov.taxiservice.service.crud.user.UserService;
import com.epam.alextuleninov.taxiservice.service.dateride.DateTimeRide;
import com.epam.alextuleninov.taxiservice.service.dateride.DateTimeRideRideService;
import com.epam.alextuleninov.taxiservice.service.loyalty.Loyalty;
import com.epam.alextuleninov.taxiservice.service.loyalty.LoyaltyService;
import com.epam.alextuleninov.taxiservice.service.routecharacteristics.RouteCharacteristics;
import com.epam.alextuleninov.taxiservice.service.routecharacteristics.RouteCharacteristicsService;
import com.epam.alextuleninov.taxiservice.service.verifyorder.VerifyOrder;
import com.epam.alextuleninov.taxiservice.service.verifyorder.VerifyOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * AppContext class for initialization of resources of application.
 */
public class AppContext {

    private static final Logger log = LoggerFactory.getLogger(AppContext.class);

    private static AppContext appContext;
    private final RouteCharacteristics routeCharacteristics;
    private final UserCRUD userCRUD;
    private final OrderCRUD orderCRUD;
    private final CarCRUD carCRUD;
    private final Loyalty loyaltyService;
    private final VerifyOrder verifyOrderService;
    private final DateTimeRide dateTimeRide;
    private final EmailConfig emailSender;

    private AppContext() {
        DataSource dataSource = MyDataSource.getConnectionsPool();
        ResultSetMapper<Car> carMapper = new CarMapper();
        CarDAO carDAO = new JDBCCarDAO(dataSource, carMapper);
        ResultSetMapper<User> userMapper = new UserMapper();
        UserDAO userDAO = new JDBCUserDAO(dataSource, userMapper);
        ResultSetMapper<Order> orderMapper = new OrderMapper();
        OrderDAO orderDAO = new JDBCOrderDAO(dataSource, carMapper, userMapper, orderMapper);
        Properties properties = new PropertiesConfig().jdbcProperties();
        this.routeCharacteristics = new RouteCharacteristicsService();
        this.carCRUD = new CarService(carDAO);
        this.orderCRUD = new OrderService(orderDAO);
        this.userCRUD = new UserService(userDAO);
        this.loyaltyService = new LoyaltyService(orderCRUD);
        this.verifyOrderService = new VerifyOrderService(carCRUD);
        this.dateTimeRide = new DateTimeRideRideService();
        this.emailSender = new EmailConfig(properties);
        log.info("AppContext.class is initialized");
    }

    public static void createAppContext() {
        appContext = new AppContext();
    }

    public static AppContext getAppContext() {
        return appContext;
    }

    public RouteCharacteristics getRouteCharacteristics() {
        return routeCharacteristics;
    }

    public UserCRUD getUserCRUD() {
        return userCRUD;
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

    public EmailConfig getEmailSender() {
        return emailSender;
    }
}
