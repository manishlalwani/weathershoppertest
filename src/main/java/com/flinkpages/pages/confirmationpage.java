package com.flinkpages.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.flinkpages.utilities.ElementUtil;

public class confirmationpage {

	private WebDriver driver;

	private ElementUtil elementUtil;

	/*
	 * By locatots for Confirmation page
	 */
	private By successMessagewithDetails = By.cssSelector("p.text-justify");
	private By successMessageHeader = By.xpath("//h2[contains(text(),'SUCCESS')]");


	/*
	 * confirmation page constructor
	 */
	public confirmationpage(WebDriver driver) {
		this.driver = driver;
		elementUtil = new ElementUtil(this.driver);

	}

	/*
	 * get title of confirmation page
	 */
	public String getConfirmationPageTitle() {
		elementUtil.getCurrentURL("confirmation");
		return driver.getTitle();
	}


	/*
	 * get the success/failure message after transaction
	 */
	public String getTransactionMessage() {

		 elementUtil.waitForElementPresenceWithWebDriverWait(successMessagewithDetails,40,2);
		 return elementUtil.getElement(successMessageHeader).getText();
		
	}

}
