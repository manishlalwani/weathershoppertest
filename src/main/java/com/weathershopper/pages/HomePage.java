package com.weathershopper.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.weathershopper.utilities.ElementUtil;
import io.qameta.allure.Step;

/**
 * Home Page Object containing locators and methods for home page interactions
 *
 * @author manishlalwani
 */
public class HomePage {

    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);
    private final WebDriver driver;
    private final ElementUtil elementUtil;

    // Locators
    private static final By CURRENT_TEMPERATURE = By.id("temperature");
    private static final By MOISTURIZERS_BUTTON = By.xpath("//button[contains(text(),'moisturizers')]");
    private static final By SUNSCREENS_BUTTON = By.xpath("//button[contains(text(),'sunscreens')]");

    /**
     * HomePage constructor
     *
     * @param driver WebDriver instance
     */
    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.elementUtil = new ElementUtil(driver);
        logger.info("HomePage initialized");
    }

    /**
     * Get current temperature from the page
     *
     * @return Current temperature as integer
     */
    @Step("Get the current temperature")
    public int getCurrentTemperature() {
        try {
            String temperatureText = elementUtil.getElement(CURRENT_TEMPERATURE).getText();
            String[] parts = temperatureText.split(" ");
            int temperature = Integer.parseInt(parts[0]);
            logger.info("Current temperature retrieved: {} degrees", temperature);
            return temperature;
        } catch (NumberFormatException e) {
            logger.error("Failed to parse temperature", e);
            throw new RuntimeException("Failed to parse temperature value", e);
        }
    }

    /**
     * Click on moisturizers button
     *
     * @return ProductPage instance
     */
    @Step("Click on Buy Moisturizers button")
    public ProductPage selectMoisturizerType() {
        elementUtil.waitForElementVisible(MOISTURIZERS_BUTTON, 10).click();
        logger.info("Moisturizers button clicked");
        return new ProductPage(driver);
    }

    /**
     * Click on sunscreens button
     *
     * @return ProductPage instance
     */
    @Step("Click on Buy Sunscreens button")
    public ProductPage selectSunscreenType() {
        elementUtil.waitForElementVisible(SUNSCREENS_BUTTON, 10).click();
        logger.info("Sunscreens button clicked");
        return new ProductPage(driver);
    }
}
