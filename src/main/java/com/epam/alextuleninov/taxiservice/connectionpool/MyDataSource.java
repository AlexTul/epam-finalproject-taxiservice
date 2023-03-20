package com.epam.alextuleninov.taxiservice.connectionpool;

import com.epam.alextuleninov.taxiservice.config.properties.PropertiesConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MyDataSource is the configuration class used to initialize a data source.
 * */
public class MyDataSource {

    private static final Logger log = LoggerFactory.getLogger(MyDataSource.class);

    // Initialize HikariConfig using a properties file located in the resources directory
    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;

    static {
        var properties = new PropertiesConfig().properties();
        config.setDriverClassName(properties.getProperty("driver"));
        config.setJdbcUrl(properties.getProperty("jdbcUrl"));
        config.setUsername(properties.getProperty("username"));
        config.setPassword(properties.getProperty("password"));
        config.setDataSourceProperties(properties);

        ds = new HikariDataSource(config);
    }

    private MyDataSource() {}

    public static HikariDataSource getConnectionsPool() {
        log.info("Get connection pool");
        return ds;
    }
}
