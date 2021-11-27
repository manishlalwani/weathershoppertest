package com.flinkpages.utilities;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;


import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


/*
 * 
 * @author manishlalwani
 * 
 */
public class ElementUtil {
	
	private WebDriver driver;
	
	public ElementUtil(WebDriver driver) {
		this.driver = driver;	}
	
	public WebElement getElement(By locator) {
		try {
		return driver.findElement(locator);
		}catch(Exception e) {
			System.out.println("Element not present : "+locator);
			return null;
		}
	}
	
	public List<WebElement> getElements(By locator){
		
		try {
			return driver.findElements(locator);
			}catch(Exception e) {
				System.out.println("Element not present : "+locator);
				return null;
			}	
	}
	
	public void doClick(By locator) {
		getElement(locator).click();
	}
	
	public void doEnterText(By locator,String textToEnter) {
		getElement(locator).sendKeys(textToEnter);
	}
	
	
	public WebElement waitForElementPresent(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}
	
	public WebElement waitForElementVisible(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public void moveToFrame(By frameForPaymentPopup) {
		driver.switchTo().frame(getElement(frameForPaymentPopup));
		// TODO Auto-generated method stub
		
	}

	public void moveToDefaultFromFrame() {
		// TODO Auto-generated method stub
		driver.switchTo().defaultContent();
	}

	public String getRandomCreditCardNumbers() {
		

		String[] cc=  {"4242424242424242","5555555555554444","2223003122003222","5200828282828210","6011111111111117",
				"36227206271667","3566002020360505","6200000000000005","4000056655665556","4242424242424242"};
		
		return cc[Integer.parseInt(org.apache.commons.lang.RandomStringUtils.randomNumeric(1))];
	
	}

	public Map.Entry<String, Integer> leastPrizedProductName(String proudctType, By allProductsXpathName,
			By allProductsXpathValues) {
		Map<String, Integer> map = new HashMap<String,Integer>();
		for(int i = 0; i < getElements(allProductsXpathName).size();i++) {
			if(getElements(allProductsXpathName).get(i).getText().toLowerCase().contains(proudctType))
			map.put(getElements(allProductsXpathName).get(i).getText(), 
					Integer.parseInt(getElements(allProductsXpathValues).get(i).getText().replaceAll("[^0-9]", "")));
			
		}
		return map.entrySet().stream().sorted(Map.Entry.comparingByValue()).findFirst().get();
	
	}

	public Boolean getCurrentURL(String string) {
		
		return driver.getCurrentUrl().contains(string);
		
	}

	public WebElement waitForElementPresenceWithWebDriverWait(By locator, int timeOut, int pollingTime) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.withMessage(Error.TIME_OUT_WEB_ELEMENT_MESG).pollingEvery(Duration.ofMillis(pollingTime))
				.ignoring(StaleElementReferenceException.class, NoSuchElementException.class);
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}

}
