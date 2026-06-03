package com.weathershopper.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.weathershopper.utilities.ElementUtil;

import java.util.Map;

/**
 * Product Page Object containing locators and methods for product page interactions
 *
 * @author manishlalwani
 */
public class ProductPage {

    private static final Logger logger = LoggerFactory.getLogger(ProductPage.class);
    private final WebDriver driver;
    private final ElementUtil elementUtil;

    // Locators
    private static final By ALL_PRODUCTS_NAMES = By.xpath("//div[contains(@class,'text-center col-4')]//p[1]");
    private static final By ALL_PRODUCTS_PRICES = By.xpath("//div[contains(@class,'text-center col-4')]//p[2]");
    private static final By CART_BUTTON = By.id("cart");
    private static final String PRODUCT_BUTTON_XPATH = "//p[contains(text(),'%s')]//following-sibling::button";

    /**
     * ProductPage constructor
     *
     * @param driver WebDriver instance
     */
    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.elementUtil = new ElementUtil(driver);
        logger.info("ProductPage initialized");
    }

    /**
     * Get product page title
     *
     * @return Page title
     */
    public String getProductPageTitle() {
        return driver.getTitle();
    }

    /**
     * Find XPath for the least priced product of a given type
     *
     * @param productType Product type to search
     * @return XPath string for the product button
     */
    private String getProductNameWithLeastPrice(String productType) {
        Map.Entry<String, Integer> leastPricedProduct =
                elementUtil.getLeastPricedProduct(productType, ALL_PRODUCTS_NAMES, ALL_PRODUCTS_PRICES);

        if (leastPricedProduct == null) {
            logger.warn("No product found for type: {}", productType);
            throw new RuntimeException("Product type '" + productType + "' not found");
        }

        String productName = leastPricedProduct.getKey();
        int price = leastPricedProduct.getValue();
        logger.info("Least priced {} product: {} - ₹{}", productType, productName, price);

        return String.format(PRODUCT_BUTTON_XPATH, productName);
    }

    /**
     * Select products and add them to cart
     *
     * @param product1 First product type to select
     * @param product2 Second product type to select
     * @return CheckoutPage instance
     */
    public CheckoutPage selectProductsAndClickCart(String product1, String product2) {
        try {
            String product1XPath = getProductNameWithLeastPrice(product1);
            elementUtil.doClick(By.xpath(product1XPath));
            logger.info("Selected product: {}", product1);

            String product2XPath = getProductNameWithLeastPrice(product2);
            elementUtil.doClick(By.xpath(product2XPath));
            logger.info("Selected product: {}", product2);

            elementUtil.doClick(CART_BUTTON);
            logger.info("Cart button clicked");

            return new CheckoutPage(driver);
        } catch (Exception e) {
            logger.error("Error selecting products and adding to cart", e);
            throw new RuntimeException("Failed to select products", e);
        }
    }
}
