package com.epam.alextuleninov.taxiservice.config.listener;

import com.epam.alextuleninov.taxiservice.config.context.AppContext;
import com.epam.alextuleninov.taxiservice.config.properties.PropertiesConfig;
import com.epam.alextuleninov.taxiservice.connectionpool.MyDataSource;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ContextListener class for initialization of resources at the time of system startup.
 */
@WebListener
public class ContextListener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(ContextListener.class);

    /**
     * Initialization of AppContext at the time of system startup.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        AppContext.createAppContext();
        log.info("'Taxi Service' was started");
    }

    /**
     * Close connection from database and deregister driver at the time of system shutdown.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        MyDataSource.getConnectionsPool().close();
        java.sql.Driver mySqlDriver;
        try {
            mySqlDriver = DriverManager.getDriver(new PropertiesConfig().jdbcProperties().getProperty("jdbcUrl"));
            DriverManager.deregisterDriver(mySqlDriver);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        log.info("DB was closed. 'Taxi Service' was closed");
    }
}
