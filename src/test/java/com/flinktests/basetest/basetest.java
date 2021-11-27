package com.flinktests.basetest;

import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.flinkpages.pages.Productpage;
import com.flinkpages.pages.checkoutpage;
import com.flinkpages.pages.confirmationpage;
import com.flinkpages.pages.homepage;
import com.flinkpages.utilities.Driverfactory;





public class basetest {

	com.flinkpages.utilities.Driverfactory df;

	public WebDriver driver;
	public homepage hp;
	public Productpage pg;
	public checkoutpage cp;
	public Properties prop;
	public confirmationpage cm;






	@Parameters({"browser"})
	@BeforeMethod
	public void setUp(@Optional String browser) {


		df = new Driverfactory();
		prop = df.readPropertiesFile();
		if(browser!=null) {
			prop.setProperty("browser", browser);
			
		}
		;
		driver = df.setUpDriver(prop);
		hp = new homepage(driver);

	}

	@AfterMethod
	public void endTestCase() {
		driver.quit();
	}

}
