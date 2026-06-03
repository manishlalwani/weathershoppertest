package com.weathershopper.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.weathershopper.utilities.ElementUtil;

/**
 * Confirmation Page Object containing locators and methods for confirmation page interactions
 *
 * @author manishlalwani
 */
public class ConfirmationPage {

    private static final Logger logger = LoggerFactory.getLogger(ConfirmationPage.class);
    private final WebDriver driver;
    private final ElementUtil elementUtil;

    // Locators
    private static final By SUCCESS_MESSAGE_DETAILS = By.cssSelector("p.text-justify");
    private static final By SUCCESS_MESSAGE_HEADER = By.xpath("//h2[contains(text(),'SUCCESS')]");

    /**
     * ConfirmationPage constructor
     *
     * @param driver WebDriver instance
     */
    public ConfirmationPage(WebDriver driver) {
        this.driver = driver;
        this.elementUtil = new ElementUtil(driver);
        logger.info("ConfirmationPage initialized");
    }

    /**
     * Get confirmation page title
     *
     * @return Page title
     */
    public String getConfirmationPageTitle() {
        elementUtil.isURLContains("confirmation");
        return driver.getTitle();
    }

    /**
     * Get transaction message from confirmation page
     *
     * @return Transaction message (e.g., "PAYMENT SUCCESS")
     */
    public String getTransactionMessage() {
        try {
            elementUtil.waitForElementPresenceWithWebDriverWait(SUCCESS_MESSAGE_DETAILS, 40, 2);
            String message = elementUtil.getElement(SUCCESS_MESSAGE_HEADER).getText();
            logger.info("Transaction message retrieved: {}", message);
            return message;
        } catch (Exception e) {
            logger.error("Error retrieving transaction message", e);
            throw new RuntimeException("Failed to get transaction message", e);
        }
    }

    /**
     * Get detailed success message
     *
     * @return Detailed success message text
     */
    public String getDetailedSuccessMessage() {
        try {
            String detailedMessage = elementUtil.getElement(SUCCESS_MESSAGE_DETAILS).getText();
            logger.info("Detailed success message: {}", detailedMessage);
            return detailedMessage;
        } catch (Exception e) {
            logger.error("Error retrieving detailed success message", e);
            return null;
        }
    }
}
