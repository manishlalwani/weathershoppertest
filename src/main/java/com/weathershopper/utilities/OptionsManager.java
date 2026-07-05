package com.weathershopper.utilities;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Browser options manager for Chrome and Firefox configurations
 *
 * @author manishlalwani
 */
public class OptionsManager {

    private static final Logger logger = LoggerFactory.getLogger(OptionsManager.class);
    private final Properties properties;
    private ChromeOptions chromeOptions;
    private FirefoxOptions firefoxOptions;

    public OptionsManager(Properties properties) {
        this.properties = properties;
    }

    /**
     * Get Chrome options based on configuration properties
     *
     * @return ChromeOptions
     */
    public ChromeOptions getChromeOptions() {
        chromeOptions = new ChromeOptions();

        if (isPropertyTrue("headless")) {
            chromeOptions.addArguments("--headless");
            logger.info("Chrome headless mode enabled");
        }

        if (isPropertyTrue("incognito")) {
            chromeOptions.addArguments("--incognito");
            logger.info("Chrome incognito mode enabled");
        }

        if (isPropertyTrue("no-sandbox")) {
            chromeOptions.addArguments("--no-sandbox");
            logger.info("Chrome no-sandbox mode enabled");
        }

        if (isPropertyTrue("disable-dev-shm-usage")) {
            chromeOptions.addArguments("--disable-dev-shm-usage");
            logger.info("Chrome disable-dev-shm-usage enabled");
        }

        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--disable-plugins");
        chromeOptions.addArguments("--start-maximized");

        return chromeOptions;
    }

    /**
     * Get Firefox options based on configuration properties
     *
     * @return FirefoxOptions
     */
    public FirefoxOptions getFirefoxOptions() {
        firefoxOptions = new FirefoxOptions();

        if (isPropertyTrue("headless")) {
            firefoxOptions.addArguments("--headless");
            logger.info("Firefox headless mode enabled");
        }

        if (isPropertyTrue("incognito")) {
            firefoxOptions.addArguments("-private");
            logger.info("Firefox private mode enabled");
        }

        return firefoxOptions;
    }

    /**
     * Check if a property is set to true
     *
     * @param propertyName Property name to check
     * @return true if property value is "true", false otherwise
     */
    private boolean isPropertyTrue(String propertyName) {
        return Boolean.parseBoolean(properties.getProperty(propertyName, "false"));
    }
}
