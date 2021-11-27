package com.flinkpages.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.flinkpages.utilities.ElementUtil;

import io.qameta.allure.Step;

public class homepage {

	private WebDriver driver;
	private ElementUtil elementUtil;


	// By Locators of Home Page

	private By currentTemperature = By.id("temperature");
	private By moisturizerBtn = By.xpath("//button[contains(text(),'moisturizers')]");
	private By sunscreenBtn = By.xpath("//button[contains(text(),'sunscreens')]");


	//page constructor
	public homepage(WebDriver driver) {
		this.driver = driver;
		elementUtil = new ElementUtil(this.driver);

	}


	//page actions/methods
	@Step("Get the Current temperature")
	public int getCurrentTemperature() {

		return Integer.parseInt(elementUtil.getElement(currentTemperature).getText().split(" ")[0]);

	}

	@Step("Select the Buy Moisturizer button")
	public Productpage selectMositurizerType() {

		elementUtil.doClick(moisturizerBtn);
		return new Productpage(driver);
	}

	@Step("Select the Buy Suncreen button button")
	public Productpage selectSunscreenType() {

		elementUtil.doClick(sunscreenBtn);
		return new Productpage(driver);
	}

}
