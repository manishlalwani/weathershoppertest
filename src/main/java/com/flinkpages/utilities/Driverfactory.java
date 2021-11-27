package com.flinkpages.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

/*
 * 
 * author manishlalwani
 * 
 */
public class Driverfactory {

	private WebDriver driver;
	private Properties prop;
	private String browser;
	private OptionsManager op;
	public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<WebDriver>();
	public static final Logger LOG = Logger.getLogger(Driverfactory.class);

	
	/*
	 * This method is used to set up the webdriver on the basis of given brownser name..
	 * 
	 * @param browser
	 * @return will return the driver
	 * 
	 */
	
	public WebDriver setUpDriver(Properties prop) {

		browser = prop.getProperty("browser");
		op = new OptionsManager(prop);

	
		if (browser.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
		    tlDriver.set(new ChromeDriver(op.getChromeOptions()));
		}
		else if (browser.equalsIgnoreCase("ff") || browser.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			tlDriver.set(new FirefoxDriver(op.getFirefoxOptions()));
		}

		
		getDriver().manage().window().maximize();
		getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
		// Launch URL
		getDriver().get(prop.getProperty("applicationUrl"));
		return getDriver();

	}

	/**
	 * this method is used to initliaze the properties on the basis of given
	 * environment
	 * 
	 * @return this method returns prop
	 */
	public Properties readPropertiesFile() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(
					new FileReader("./src/test/java/com/flinkTestcases/runner/application.properties"));
			prop = new Properties();
			try {
				prop.load(reader);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("Config properties file is not found");
		}

		return prop;
	}

	public void tearDown() {

		driver.quit();
	}
	
	public synchronized WebDriver getDriver() {
		return tlDriver.get();
		
	}
	
	
	/*
	 * takes screenshot
	 */
	public String getScreenShot() {
		File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
		String path = System.getProperty("user.dir")+"/screenshot/"+System.currentTimeMillis()+".png";
		File destination = new File(path);
		try {
			FileUtils.copyFile(srcFile, destination);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return path;
	}

}
