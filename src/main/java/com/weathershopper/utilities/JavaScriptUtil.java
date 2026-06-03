package com.weathershopper.utilities;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JavaScript utility for DOM manipulation and interaction
 *
 * @author manishlalwani
 */
public class JavaScriptUtil {

    private static final Logger logger = LoggerFactory.getLogger(JavaScriptUtil.class);
    private final WebDriver driver;
    private final JavascriptExecutor jsExecutor;

    public JavaScriptUtil(WebDriver driver) {
        this.driver = driver;
        this.jsExecutor = (JavascriptExecutor) driver;
    }

    /**
     * Execute JavaScript script
     *
     * @param script JavaScript code to execute
     * @param args Arguments for the script
     * @return Result of script execution
     */
    public Object executeScript(String script, Object... args) {
        logger.debug("Executing JavaScript: {}", script);
        return jsExecutor.executeScript(script, args);
    }

    /**
     * Send keys to element by ID using JavaScript
     *
     * @param elementId Element ID
     * @param keys Keys to send
     */
    public void sendKeysById(String elementId, String keys) {
        String script = "document.getElementById('" + elementId + "').value = '" + keys + "';";
        jsExecutor.executeScript(script);
        logger.debug("Sent keys to element with ID: {}", elementId);
    }

    /**
     * Click element using JavaScript (when regular click fails)
     *
     * @param script JavaScript containing element selector
     */
    public void clickElement(String script) {
        jsExecutor.executeScript(script);
        logger.debug("Clicked element using JavaScript");
    }

    /**
     * Get page title using JavaScript
     *
     * @return Page title
     */
    public String getPageTitle() {
        return (String) jsExecutor.executeScript("return document.title;");
    }

    /**
     * Get page URL using JavaScript
     *
     * @return Page URL
     */
    public String getPageURL() {
        return (String) jsExecutor.executeScript("return window.location.href;");
    }

    /**
     * Scroll to element
     *
     * @param script JavaScript to scroll to element
     */
    public void scrollToElement(String script) {
        jsExecutor.executeScript(script);
        logger.debug("Scrolled to element using JavaScript");
    }

    /**
     * Refresh page using JavaScript
     */
    public void refreshPage() {
        jsExecutor.executeScript("location.reload();");
        logger.info("Page refreshed using JavaScript");
    }

    /**
     * Wait for element to be ready
     *
     * @param script JavaScript wait condition
     */
    public void waitForElement(String script) {
        jsExecutor.executeScript(script);
        logger.debug("Waited for element using JavaScript");
    }
}
