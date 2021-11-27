package com.flinkpages.pages;


import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.flinkpages.utilities.ElementUtil;

public class Productpage{

	private WebDriver driver;
	private ElementUtil elementUtil;


	// By Locators
	private By allProductsXpathName = By.xpath("//div[contains(@class,'text-center col-4')]//p[1]");
	private By allProductsXpathValues = By.xpath("//div[contains(@class,'text-center col-4')]//p[2]");
	private String productBtn = "//p[contains(text(),'%s')]//following-sibling::button";
	private By cartBtn = By.id("cart"); 


	//page constructor
	public Productpage(WebDriver driver) {
		this.driver = driver;
		elementUtil = new ElementUtil(this.driver);
	}

	//
	public String getProductPageTitle() {
		return driver.getTitle();
	}


	private String getProductNameWithLeastPrice(String proudctType) {

		Map.Entry<String,Integer> leastPrizedItem = elementUtil.leastPrizedProductName(proudctType,allProductsXpathName,allProductsXpathValues);
		return String.format(productBtn, leastPrizedItem.getKey());
	}

	/*
	 * Select least Values products based on string passed in test
	 */



	public checkoutpage selectProductsAndClickCart(String product1,String product2){

		elementUtil.doClick(By.xpath(getProductNameWithLeastPrice(product1)));

		elementUtil.doClick(By.xpath(getProductNameWithLeastPrice(product2)));

		elementUtil.doClick(cartBtn);

		return new checkoutpage(driver);
	}

}
