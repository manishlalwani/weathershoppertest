package com.weathershopper.utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

/**
 * DriverFactory class to initialize and manage WebDriver instances
 * Uses ThreadLocal for thread-safe driver management in parallel execution
 *
 * @author manishlalwani
 */
public class DriverFactory {

    private static final Logger logger = LoggerFactory.getLogger(DriverFactory.class);
    private static final String PROPERTIES_FILE = "application.properties";
    private static final String SCREENSHOT_DIR = "screenshots";
    private static final long PAGE_LOAD_TIMEOUT = 20;
    private static final long IMPLICIT_WAIT = 10;

    private WebDriver driver;
    private String browser;
    private OptionsManager optionsManager;
    public static ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();

    /**
     * Setup WebDriver based on browser specified in properties
     *
     * @param properties Configuration properties containing browser name and URL
     * @return WebDriver instance
     */
    public WebDriver setUpDriver(Properties properties) {
        browser = properties.getProperty("browser", "chrome").toLowerCase();
        optionsManager = new OptionsManager(properties);

        logger.info("Setting up WebDriver for browser: {}", browser);

        if ("chrome".equalsIgnoreCase(browser)) {
            WebDriverManager.chromedriver().setup();
            threadLocalDriver.set(new ChromeDriver(optionsManager.getChromeOptions()));
        } else if ("firefox".equalsIgnoreCase(browser) || "ff".equalsIgnoreCase(browser)) {
            WebDriverManager.firefoxdriver().setup();
            threadLocalDriver.set(new FirefoxDriver(optionsManager.getFirefoxOptions()));
        } else {
            logger.warn("Unknown browser: {}. Defaulting to Chrome", browser);
            WebDriverManager.chromedriver().setup();
            threadLocalDriver.set(new ChromeDriver(optionsManager.getChromeOptions()));
        }

        driver = getDriver();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT));

        String applicationUrl = properties.getProperty("application.url","https://weathershopper.pythonanywhere.com/");
        logger.info("Navigating to URL: {}", applicationUrl);
        driver.get(applicationUrl);

        return driver;
    }

    /**
     * Read properties from application.properties file
     *
     * @return Properties object containing configuration
     */
    public Properties readPropertiesFile() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                logger.error("Properties file not found: {}", PROPERTIES_FILE);
                throw new RuntimeException("Config properties file '" + PROPERTIES_FILE + "' not found in classpath");
            }
            properties.load(input);
            logger.info("Properties file loaded successfully");
        } catch (IOException e) {
            logger.error("Error loading properties file", e);
            throw new RuntimeException("Failed to load properties file", e);
        }
        return properties;
    }

    /**
     * Get thread-local WebDriver instance
     *
     * @return WebDriver instance
     */
    public synchronized WebDriver getDriver() {
        return threadLocalDriver.get();
    }

    /**
     * Close and remove WebDriver from ThreadLocal
     */
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            threadLocalDriver.remove();
            logger.info("WebDriver closed and ThreadLocal cleared");
        }
    }

    /**
     * Capture screenshot and save to screenshots directory
     *
     * @return Path to the saved screenshot file
     */
    public String getScreenshot() {
        try {
            File screenshotDir = new File(SCREENSHOT_DIR);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
            String fileName = "screenshot_" + System.currentTimeMillis() + ".png";
            File destinationFile = new File(screenshotDir + File.separator + fileName);

            FileUtils.copyFile(srcFile, destinationFile);
            logger.info("Screenshot saved: {}", destinationFile.getAbsolutePath());

            return destinationFile.getAbsolutePath();
        } catch (IOException e) {
            logger.error("Error capturing screenshot", e);
            return null;
        }
    }
}
