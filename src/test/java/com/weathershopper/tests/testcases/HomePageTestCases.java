package com.weathershopper.tests.testcases;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weathershopper.tests.base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

/**
 * Test cases for Home Page functionality
 *
 * @author manishlalwani
 */
@Feature("Weather Shopper Application")
public class HomePageTestCases extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(HomePageTestCases.class);

    @Test
    @Description("End to End Product Selection based on Current Temperature")
    @Severity(SeverityLevel.CRITICAL)
    public void endToEndProductSelectionTest() {
        logger.info("Starting end-to-end product selection test");

        int currentTemperature = homePage.getCurrentTemperature();
        logger.info("Current temperature: {} degrees", currentTemperature);

        if (currentTemperature < 18) {
            logger.info("Temperature is below 18 - selecting moisturizers");
            productPage = homePage.selectMoisturizerType();
            checkoutPage = productPage.selectProductsAndClickCart("aloe", "almond");
        } else if (currentTemperature > 34) {
            logger.info("Temperature is above 34 - selecting sunscreens");
            productPage = homePage.selectSunscreenType();
            checkoutPage = productPage.selectProductsAndClickCart("spf-50", "spf-30");
        } else {
            logger.warn("Temperature is in comfortable range (18-34 degrees)");
            Assert.fail("Temperature not in limits for product selection");
        }

        // Verify product total
        Assert.assertTrue(checkoutPage.verifyProductTotal(), "Product total verification failed");
        logger.info("Product total verification passed");

        // Make payment
        try {
            confirmationPage = checkoutPage.makePayment();
            logger.info("Payment process completed");
        } catch (InterruptedException e) {
            logger.error("Payment was interrupted", e);
            Thread.currentThread().interrupt();
            Assert.fail("Payment interrupted: " + e.getMessage());
        }

        // Verify payment success
        String transactionMessage = confirmationPage.getTransactionMessage();
        Assert.assertEquals(transactionMessage, "PAYMENT SUCCESS", "Payment was not successful");
        logger.info("Test passed - Transaction successful");
    }
}
