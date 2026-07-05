package com.weathershopper.tests.base;

import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weathershopper.pages.CheckoutPage;
import com.weathershopper.pages.ConfirmationPage;
import com.weathershopper.pages.HomePage;
import com.weathershopper.pages.ProductPage;
import com.weathershopper.utilities.DriverFactory;

/**
 * Base Test class with setup and teardown methods for all tests
 *
 * @author manishlalwani
 */
public class BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    protected DriverFactory driverFactory;
    protected WebDriver driver;
    protected HomePage homePage;
    protected ProductPage productPage;
    protected CheckoutPage checkoutPage;
    protected ConfirmationPage confirmationPage;
    protected Properties properties;

    /**
     * Setup method to initialize driver and page objects before each test
     *
     * @param browser Browser name from TestNG XML
     */
    @Parameters({"browser"})
    @BeforeMethod
    public void setUp(@Optional String browser) {
        logger.info("Setting up test with browser: {}", browser);

        driverFactory = new DriverFactory();
        properties = driverFactory.readPropertiesFile();

        if (browser != null && !browser.isEmpty()) {
            properties.setProperty("browser", browser);
            logger.info("Browser overridden to: {}", browser);
        }

        driver = driverFactory.setUpDriver(properties);
        homePage = new HomePage(driver);
        logger.info("Test setup completed");
    }

    /**
     * Teardown method to close driver after each test
     */
    @AfterMethod
    public void tearDown() {
        logger.info("Tearing down test");
        if (driver != null) {
            driver.quit();
            logger.info("Driver closed");
        }
    }
}
