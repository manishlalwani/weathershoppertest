package com.weathershopper.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weathershopper.utilities.ElementUtil;
import com.weathershopper.utilities.JavaScriptUtil;

/**
 * Checkout Page Object containing locators and methods for checkout page interactions
 *
 * @author manishlalwani
 */
public class CheckoutPage {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutPage.class);
    private final WebDriver driver;
    private final ElementUtil elementUtil;
    private final JavaScriptUtil jsUtil;

    // Locators
    private static final By PRODUCT_NAMES_SELECTED = By.xpath("//tbody//td");
    private static final By PAY_BUTTON = By.xpath("//button/span[contains(text(),'Pay')]");
    private static final By EMAIL_TEXTBOX = By.xpath("//input[@id='email']");
    private static final By CVV_NUMBER = By.id("cc-csc");
    private static final By ZIP_CODE_TEXTBOX = By.id("billing-zip");
    private static final By SUBMIT_CARD_BUTTON = By.xpath("//span[@class='iconTick']");
    private static final By STRIPE_IFRAME = By.xpath("//iframe[contains(@src,'stripe')]");
    private static final By TOTAL_AMOUNT = By.id("total");

    // Card details
    private static final String CARD_EXPIRY = "09/26";

    /**
     * CheckoutPage constructor
     *
     * @param driver WebDriver instance
     */
    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.elementUtil = new ElementUtil(driver);
        this.jsUtil = new JavaScriptUtil(driver);
        logger.info("CheckoutPage initialized");
    }

    /**
     * Get checkout page title
     *
     * @return Page title
     */
    public String getCheckoutPageTitle() {
        return driver.getTitle();
    }

    /**
     * Get product values from the table
     *
     * @return List of product prices
     */
    private ArrayList<Integer> getProductValues() {
        List<WebElement> elements = elementUtil.getElements(PRODUCT_NAMES_SELECTED);
        ArrayList<Integer> productValues = new ArrayList<>();

        for (int i = 0; i < elements.size(); i++) {
            // Get every alternate element (prices are at odd indices)
            if (i % 2 != 0) {
                try {
                    int price = Integer.parseInt(elements.get(i).getText());
                    productValues.add(price);
                } catch (NumberFormatException e) {
                    logger.warn("Unable to parse price from element: {}", elements.get(i).getText());
                }
            }
        }

        logger.debug("Product values retrieved: {}", productValues);
        return productValues;
    }

    /**
     * Get total amount displayed on page
     *
     * @return Total amount as integer
     */
    private int getTotalAmount() {
        String totalText = elementUtil.getElement(TOTAL_AMOUNT).getText();
        String[] parts = totalText.split(" ");
        int total = Integer.parseInt(parts[parts.length - 1]);
        logger.debug("Total amount retrieved: ₹{}", total);
        return total;
    }

    /**
     * Verify that the total amount matches sum of products
     *
     * @return true if totals match, false otherwise
     */
    public boolean verifyProductTotal() {
        @SuppressWarnings("null")
        int sum = getProductValues().stream().mapToInt(Integer::intValue).sum();
        int total = getTotalAmount();
        boolean isValid = sum == total;

        if (isValid) {
            logger.info("Product total verification passed: Sum={}, Total={}", sum, total);
        } else {
            logger.warn("Product total verification failed: Sum={}, Total={}", sum, total);
        }

        return isValid;
    }

    /**
     * Make payment with card details
     *
     * @return ConfirmationPage instance
     * @throws InterruptedException if thread is interrupted
     */
    @SuppressWarnings("UseSpecificCatch")
    public ConfirmationPage makePayment() throws InterruptedException {
        try {
            logger.info("Starting payment process");

            // Click Pay button
            elementUtil.waitForElementVisible(PAY_BUTTON, 30).click();
            logger.info("Pay button clicked");

            // Switch to Stripe iframe
            elementUtil.switchToFrame(STRIPE_IFRAME);
            logger.info("Switched to Stripe iframe");

            // Wait for email field to be visible
            elementUtil.waitForElementVisible(EMAIL_TEXTBOX, 30);

            // Enter random email
            String randomEmail = RandomStringUtils.randomAlphabetic(10) + "@hotmail.com";
            elementUtil.doEnterText(EMAIL_TEXTBOX, randomEmail);
            logger.info("Email entered: {}", randomEmail);

            // Enter card number using JavaScript
            String cardNumber = elementUtil.getRandomCreditCardNumber();
            jsUtil.sendKeysById("card_number", cardNumber);
            logger.info("Card number entered");

            // Enter expiry date
            jsUtil.sendKeysById("cc-exp", CARD_EXPIRY);
            logger.info("Card expiry date entered: {}", CARD_EXPIRY);

            // Enter CVV
            String cvv = RandomStringUtils.randomNumeric(3);
            elementUtil.doEnterText(CVV_NUMBER, cvv);
            logger.info("CVV entered");

            // Enter zip code
            String zipCode = RandomStringUtils.randomNumeric(6);
            elementUtil.waitForElementVisible(ZIP_CODE_TEXTBOX, 30).sendKeys(zipCode);
            logger.info("Zip code entered");

            // Click submit button
            elementUtil.waitForElementVisible(SUBMIT_CARD_BUTTON, 30).click();
            logger.info("Submit button clicked");

            // Switch back to main content
            elementUtil.switchToDefaultContent();
            logger.info("Switched back to default content");

            // Wait for confirmation page to load
            Thread.sleep(2000);

            return new ConfirmationPage(driver);
        } catch (Exception e) {
            logger.error("Error during payment process", e);
            throw new RuntimeException("Payment failed", e);
        }
    }
}
