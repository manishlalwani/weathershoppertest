package com.flinkpages.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.flinkpages.utilities.ElementUtil;

public class confirmationpage {

	private WebDriver driver;

	private ElementUtil elementUtil;

	private By successMessage = By.cssSelector("p.text-justify");
	private By successMessage1 = By.xpath("//h2[contains(text(),'SUCCESS')]");


	public confirmationpage(WebDriver driver) {
		this.driver = driver;
		elementUtil = new ElementUtil(this.driver);

	}

	public String getConfirmationPageTitle() {
		elementUtil.getCurrentURL("confirmation");
		return driver.getTitle();
	}


	public String getSuccessMessage() {

		

		 elementUtil.waitForElementPresenceWithWebDriverWait(successMessage,40,2);
		 return elementUtil.getElement(successMessage1).getText();
		
	}

}
