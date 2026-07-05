package com.weathershopper.utilities;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Custom utility methods to interact with web elements using Selenium
 *
 * @author manishlalwani
 */
public class ElementUtil {

    private static final Logger logger = LoggerFactory.getLogger(ElementUtil.class);
    private final WebDriver driver;

    public ElementUtil(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Find a single element by locator
     *
     * @param locator By locator
     * @return WebElement or null if not found
     */
    public WebElement getElement(By locator) {
        try {
            return driver.findElement(locator);
        } catch (NoSuchElementException e) {
            logger.warn("Element not found: {}", locator);
            return null;
        }
    }

    /**
     * Find multiple elements by locator
     *
     * @param locator By locator
     * @return List of WebElements
     */
    public List<WebElement> getElements(By locator) {
        try {
            return driver.findElements(locator);
        } catch (NoSuchElementException e) {
            logger.warn("Elements not found: {}", locator);
            return List.of();
        }
    }

    /**
     * Click on element
     *
     * @param locator By locator
     */
    public void doClick(By locator) {
        WebElement element = getElement(locator);
        if (element != null) {
            element.click();
            logger.debug("Clicked on element: {}", locator);
        }
    }

    /**
     * Enter text into element
     *
     * @param locator By locator
     * @param text Text to enter
     */
    public void doEnterText(By locator, String text) {
        WebElement element = getElement(locator);
        if (element != null) {
            element.clear();
            element.sendKeys(text);
            logger.debug("Entered text in element: {}", locator);
        }
    }

    /**
     * Wait for element to be present in DOM
     *
     * @param locator By locator
     * @param timeoutSeconds Timeout in seconds
     * @return WebElement
     */
    public WebElement waitForElementPresent(By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Wait for element to be visible on page
     *
     * @param locator By locator
     * @param timeoutSeconds Timeout in seconds
     * @return WebElement
     */
    public WebElement waitForElementVisible(By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait for element with custom polling and error handling
     *
     * @param locator By locator
     * @param timeoutSeconds Timeout in seconds
     * @param pollingMillis Polling interval in milliseconds
     * @return WebElement
     */
    public WebElement waitForElementPresenceWithWebDriverWait(By locator, int timeoutSeconds, int pollingMillis) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.pollingEvery(Duration.ofMillis(pollingMillis))
                .ignoring(StaleElementReferenceException.class, NoSuchElementException.class);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Switch to iframe
     *
     * @param frameLocator By locator for iframe
     */
    public void switchToFrame(By frameLocator) {
        WebElement frameElement = getElement(frameLocator);
        if (frameElement != null) {
            driver.switchTo().frame(frameElement);
            logger.debug("Switched to frame: {}", frameLocator);
        }
    }

    /**
     * Switch back to default content
     */
    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
        logger.debug("Switched back to default content");
    }

    /**
     * Get random credit card number from test data
     *
     * @return Credit card number
     */
    public String getRandomCreditCardNumber() {
        String[] creditCards = {
                "4242424242424242", "5555555555554444", "2223003122003222", "5200828282828210",
                "6011111111111117", "36227206271667", "3566002020360505", "6200000000000005",
                "4000056655665556", "4111111111111111"
        };
        int randomIndex = (int) (Math.random() * creditCards.length);
        return creditCards[randomIndex];
    }

    /**
     * Find the least priced product of a specific type
     *
     * @param productType Product type to search for
     * @param productNamesLocator Locator for product names
     * @param productPricesLocator Locator for product prices
     * @return Map entry with product name and price
     */
    public Map.Entry<String, Integer> getLeastPricedProduct(String productType, By productNamesLocator,
                                                             By productPricesLocator) {
        Map<String, Integer> products = new HashMap<>();
        List<WebElement> productNames = getElements(productNamesLocator);
        List<WebElement> productPrices = getElements(productPricesLocator);

        for (int i = 0; i < productNames.size(); i++) {
            String productName = productNames.get(i).getText();
            if (productName.toLowerCase().contains(productType.toLowerCase())) {
                String priceText = productPrices.get(i).getText().replaceAll("[^0-9]", "");
                int price = Integer.parseInt(priceText);
                products.put(productName, price);
            }
        }

        if (products.isEmpty()) {
            logger.warn("No products found for type: {}", productType);
            return null;
        }

        return products.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .orElse(null);
    }

    /**
     * Check if current URL contains specific text
     *
     * @param urlPart URL part to check
     * @return true if URL contains the text
     */
    public boolean isURLContains(String urlPart) {
        boolean contains = driver.getCurrentUrl().contains(urlPart);
        logger.debug("URL check - '{}' in '{}': {}", urlPart, driver.getCurrentUrl(), contains);
        return contains;
    }
}
