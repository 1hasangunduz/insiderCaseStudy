package com.insider.utilities;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.util.Objects;
import java.util.Properties;

public class ConfigReader {
    private static final Logger LOGGER = LogManager.getLogger(ConfigReader.class);
    private ConfigReader() {
        throw new IllegalStateException("Utility class");
    }
    private static Properties properties;

    static {
        String path = "src/main/resources/config.properties";
        try {
            FileInputStream file = new FileInputStream(path);
            properties = new Properties();
            properties.load(file);
        } catch (Exception e) {
            LOGGER.error("Configuration file couldn't found.");
        }
    }

    public static String getProperty(String key) {
        if (Objects.equals(key, "")) return "";
        else return properties.getProperty(key);
    }

}
