package some.domain.scenarios;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import some.domain.common.TestClassBaseSettings;
import some.domain.utils.Helper;
import some.domain.utils.TraceOps;
import some.domain.utils.TraceOps.LogLevel;

public class Scenario3 extends TestClassBaseSettings{


	public Scenario3() throws IOException {
		super();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		wait = new WebDriverWait(driver, 10);
	}

	
	@Test(priority =0) 
	@Parameters({"gsmarena_home"})
	public void openHomePage(String url) throws InterruptedException{

		driver.get(url);

	}

	@Test (dependsOnMethods = {"openHomePage"}, enabled = true)
	public void selectRandomManufacturer(){
		
		String xpathManufacturers;
		String xpathManufacturer;
		int elemIndex;
		
		xpathManufacturers = "//*[@id='body']/aside/div[1]/ul/li";
		
		try {
			List<WebElement> manufacturers = driver.findElements(By.xpath(xpathManufacturers));
			elemIndex = Helper.getRandomIndex(manufacturers);
			xpathManufacturer = xpathManufacturers+"["+elemIndex+"]";

			String manufacturer = driver.findElement(By.xpath(xpathManufacturer)).getText();
			TraceOps.printMessage(LogLevel.INFO, "%s manufacturer has been randomly selected", manufacturer);
			
			driver.findElement(By.xpath(xpathManufacturer)).click();
			
		} catch (Exception e) {
			TraceOps.printMessage(LogLevel.ERROR, "Unrecognized manufacturer xpath is provided." );
		}
		
		TraceOps.printMessage(LogLevel.TRACE, "Manufacturer page has been opened.");
	}
	
	@Test (dependsOnMethods = {"selectRandomManufacturer"}, enabled = true)
	public void selectRandomPhone (){
		
		String modelName;
		String xpathPhone;
		int elemIndex;
		
		String xpathPhones = "//*[@id='review-body']/div/ul/li";
		By xpathPhoneStats = By.xpath("//*[@id='body']/div/div[1]/div/div[1]/h1");
		
		List<WebElement> phones = driver.findElements(By.xpath(xpathPhones));
		elemIndex = Helper.getRandomIndex(phones);
		xpathPhone = xpathPhones + "[" + elemIndex + "]"+"/a";
		modelName = driver.findElement(By.xpath(xpathPhone)).getText();
		
		TraceOps.printMessage(LogLevel.INFO, "%s phone model has been randomly selected", modelName);

		//sometimes popup screen overlaps with selected phone and receives the click.
		//the code below just removes this popup.
		try {
			By cssLocker = By.cssSelector("div.cc_banner-wrapper > div > a.cc_btn.cc_btn_accept_all");
			driver.findElement(cssLocker).click();
		} catch (Exception e) {
			TraceOps.printMessage(LogLevel.ERROR, "Failed to remove popup element.");
		}
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathPhone)));
		
		driver.findElement(By.xpath(xpathPhone)).click();
		
		TraceOps.printMessage(LogLevel.TRACE, "Verifying that correct phone model page has been opened...");
		
		waitForElement(xpathPhoneStats, 10, 1, false);
		
		Assert.assertTrue(driver.findElement(xpathPhoneStats).getText().contains(modelName),
											"Incorrect model page has been opened." );

	}

	@Test (dependsOnMethods = {"selectRandomPhone"}, enabled = true)
	public void verifyIsGPS(){
		
		TraceOps.printMessage(LogLevel.TRACE, "Verifying if selected model is designed to use GPS...");
		boolean isMatch = false;
		By xpathSpecTable = By.xpath("//*[@id='specs-list']/table");
		String criteria = "GPS Yes";
		
		waitForElement(xpathSpecTable, 10, 1, false);
		
		List <WebElement> elems = driver.findElements(xpathSpecTable);
		
		isMatch = Helper.isMatchingElement(elems, criteria);
		
		Assert.assertTrue(isMatch, "Selected phone model is not designed to use GPS.");
		
	}
	
//	@AfterClass
//	@Override
//	public void baseAfterClasss() {
//		
//		
//		super.baseAfterClasss();
//	}
	

}
