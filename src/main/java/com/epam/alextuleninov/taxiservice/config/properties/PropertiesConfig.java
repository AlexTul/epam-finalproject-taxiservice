package com.epam.alextuleninov.taxiservice.config.properties;

import com.epam.alextuleninov.taxiservice.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * Class for getting properties from properties file.
 */
public class PropertiesConfig {

    private static final Logger log = LoggerFactory.getLogger(PropertiesConfig.class);

    /**
     * Get properties from properties file.
     */
    public Properties properties() {
        try (var in = getClass().getClassLoader().getResourceAsStream(Constants.SETTINGS_FILE)) {
            var properties = new Properties();
            properties.load(in);
            return properties;
        } catch (IOException e) {
            log.error("Error getting properties from resources file in PropertiesConfig.class", e);
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Get properties from bundle properties file.
     */
    public Properties propertiesBundle() {
        try (var in = getClass().getClassLoader().getResourceAsStream("resources.properties")) {
            var properties = new Properties();
            properties.load(in);
            return properties;
        } catch (IOException e) {
            log.error("Error getting properties from resources file in PropertiesConfig.class", e);
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Get properties from bundle properties file.
     */
    public Properties propertiesBundleUk() {
        try (var in = getClass().getClassLoader().getResourceAsStream("resources_uk_UA.properties")) {
            var properties = new Properties();
            properties.load(in);
            return properties;
        } catch (IOException e) {
            log.error("Error getting properties from resources file in PropertiesConfig.class", e);
            throw new UncheckedIOException(e);
        }
    }
}
