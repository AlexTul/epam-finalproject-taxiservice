package com.epam.alextuleninov.taxiservice.config.context;

import com.epam.alextuleninov.taxiservice.config.mail.EmailByLocaleConfig;
import com.epam.alextuleninov.taxiservice.config.mail.EmailConfig;
import com.epam.alextuleninov.taxiservice.config.properties.PropertiesConfig;
import com.epam.alextuleninov.taxiservice.connectionpool.MyDataSource;
import com.epam.alextuleninov.taxiservice.dao.CarDAO;
import com.epam.alextuleninov.taxiservice.dao.car.JDBCCarDAO;
import com.epam.alextuleninov.taxiservice.dao.mappers.CarMapper;
import com.epam.alextuleninov.taxiservice.dao.ResultSetMapper;
import com.epam.alextuleninov.taxiservice.dao.mappers.UserMapper;
import com.epam.alextuleninov.taxiservice.dao.order.JDBCOrderDAO;
import com.epam.alextuleninov.taxiservice.dao.OrderDAO;
import com.epam.alextuleninov.taxiservice.dao.user.JDBCUserDAO;
import com.epam.alextuleninov.taxiservice.dao.UserDAO;
import com.epam.alextuleninov.taxiservice.model.car.Car;
import com.epam.alextuleninov.taxiservice.model.user.User;
import com.epam.alextuleninov.taxiservice.service.crud.CarCRUD;
import com.epam.alextuleninov.taxiservice.service.crud.car.CarService;
import com.epam.alextuleninov.taxiservice.service.crud.OrderCRUD;
import com.epam.alextuleninov.taxiservice.service.crud.order.OrderService;
import com.epam.alextuleninov.taxiservice.service.crud.UserCRUD;
import com.epam.alextuleninov.taxiservice.service.crud.user.UserService;
import com.epam.alextuleninov.taxiservice.service.DateTimeRide;
import com.epam.alextuleninov.taxiservice.service.dateride.DateTimeRideService;
import com.epam.alextuleninov.taxiservice.service.Loyalty;
import com.epam.alextuleninov.taxiservice.service.loyalty.LoyaltyService;
import com.epam.alextuleninov.taxiservice.service.RouteCharacteristics;
import com.epam.alextuleninov.taxiservice.service.routecharacteristics.RouteCharacteristicsService;
import com.epam.alextuleninov.taxiservice.service.sort.Sort;
import com.epam.alextuleninov.taxiservice.service.Sortable;
import com.epam.alextuleninov.taxiservice.service.VerifyOrder;
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
    private final EmailByLocaleConfig emailByLocaleConfig;
    private final Sortable sorter;
    private final Properties properties;
    private final Properties propertiesUk;

    private AppContext() {
        DataSource dataSource = MyDataSource.getConnectionsPool();
        ResultSetMapper<Car> carMapper = new CarMapper();
        CarDAO carDAO = new JDBCCarDAO(dataSource, carMapper);
        ResultSetMapper<User> userMapper = new UserMapper();
        UserDAO userDAO = new JDBCUserDAO(dataSource, userMapper);
        OrderDAO orderDAO = new JDBCOrderDAO(dataSource, carMapper, userMapper);
        Properties properties = new PropertiesConfig().properties();
        this.properties = new PropertiesConfig().propertiesBundle();
        this.propertiesUk = new PropertiesConfig().propertiesBundleUk();
        this.routeCharacteristics = new RouteCharacteristicsService();
        this.carCRUD = new CarService(carDAO);
        this.orderCRUD = new OrderService(orderDAO);
        this.userCRUD = new UserService(userDAO);
        this.loyaltyService = new LoyaltyService(orderCRUD, routeCharacteristics);
        this.verifyOrderService = new VerifyOrderService(carCRUD);
        this.dateTimeRide = new DateTimeRideService();
        this.emailSender = new EmailConfig(properties);
        this.emailByLocaleConfig = new EmailByLocaleConfig();
        this.sorter = new Sort();
        log.info("AppContext.class is initialized");
    }

    public static void createAppContext() {
        appContext = new AppContext();
    }

    public static AppContext getAppContext() {
        return appContext;
    }

    public Properties getProperties() {
        return properties;
    }

    public Properties getPropertiesUk() {
        return propertiesUk;
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

    public EmailByLocaleConfig getEmailByLocaleConfig() {
        return emailByLocaleConfig;
    }

    public Sortable getSorter() {
        return sorter;
    }
}
