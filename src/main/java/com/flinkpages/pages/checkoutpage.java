package com.flinkpages.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.flinkpages.utilities.ElementUtil;
import com.flinkpages.utilities.JavaScriptUtil;

/*
 * 
 * author manishlalwani
 * 
 */

public class checkoutpage {

	private WebDriver driver;
	private ElementUtil elementUtil;
	private JavaScriptUtil jsUtil;

	private By payForBtn = By.xpath("//button/span[contains(text(),'Pay')]");
	private By emailTextBox = By.xpath("//input[@id='email']");
	private By cvvNumber = By.id("cc-csc");
	private By zipNumberTxtBx = By.id("billing-zip");
	private By submitCCBtn = By.xpath("//span[@class='iconTick']");
	private By frameForPaymentPopup = By.xpath("//iframe[contains(@src,'stripe')]");

	// page constructor
	public checkoutpage(WebDriver driver) {
		this.driver = driver;
		elementUtil = new ElementUtil(this.driver);
		jsUtil = new JavaScriptUtil(this.driver);
	}

	// get page title
	public String getCheckoutPageTitle() {
		return driver.getTitle();
	}

	// get product values which were selected

	private ArrayList<Integer> getProductValues() {

		String productNamexpath = "//tbody//td";
		List<WebElement> elements = elementUtil.getElements(By.xpath(productNamexpath));

		ArrayList<Integer> productValues = new ArrayList<Integer>();

		for (int i = 0; i < elements.size(); i++) {
			if (i % 2 != 0) {
				productValues.add(Integer.parseInt(elements.get(i).getText()));
			}
		}
		return productValues;

	}

	// get total values calculated by app

	private int getTotalValues() {
		String finalValueText = elementUtil.getElement(By.id("total")).getText();
		String[] values = finalValueText.split(" ");
		int totalValue = Integer.parseInt(values[values.length - 1]);
		return totalValue;

	}

	// verify total values with summation of products displayed
	public boolean verifyProductTotal() {

		int sum = getProductValues().stream().mapToInt(x -> x).sum();
		if (sum == getTotalValues()) {
			return true;
		}
		return false;

	}

	public confirmationpage makePayment() throws InterruptedException {

		elementUtil.waitForElementVisible(payForBtn, 30).click();
		elementUtil.moveToFrame(frameForPaymentPopup);
		elementUtil.waitForElementVisible(By.xpath("//h1"), 30);

		elementUtil.waitForElementVisible(emailTextBox, 30)
				.sendKeys(RandomStringUtils.randomAlphabetic(10) + "@hotmail.com");
		jsUtil.sendKeysUsingWithId("card_number", elementUtil.getRandomCreditCardNumbers());
		jsUtil.sendKeysUsingWithId("cc-exp", "03/22");
		elementUtil.doEnterText(cvvNumber, RandomStringUtils.randomNumeric(3));
		elementUtil.waitForElementVisible(zipNumberTxtBx, 30).sendKeys(RandomStringUtils.randomNumeric(6));
		elementUtil.waitForElementVisible(submitCCBtn, 30).click();
		elementUtil.moveToDefaultFromFrame();

		return new confirmationpage(driver);

	}

}
