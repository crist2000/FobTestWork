package some.domain.scenarios;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import some.domain.common.BaseOperations;
import some.domain.utils.Helper;
import some.domain.utils.TraceOps;
import some.domain.utils.TraceOps.LogLevel;

public class Scenario3 extends BaseOperations{

	public Scenario3() throws IOException {
		super();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Test(priority =0) 
	@Parameters({"gsmarena_home"})
	public void openHomePage(String url) throws InterruptedException{
		
		TraceOps.printMessage(LogLevel.TRACE, "Openening home page %s...", url);
		driver.get(url);
		TraceOps.printMessage(LogLevel.TRACE, "Home page has been opened.");
	}

	@Test (priority =5)
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
	
	@Test (priority =7)
	public void selectRandomPhone (){
		
		String modelName;
		String xpathPhones;
		String xpathPhone;
		int elemIndex;
		
		xpathPhones = "//*[@id='review-body']/div/ul/li";
		
		List<WebElement> phones = driver.findElements(By.xpath(xpathPhones));
		elemIndex = Helper.getRandomIndex(phones);
		xpathPhone = xpathPhones + "[" + elemIndex + "]";
		modelName = driver.findElement(By.xpath(xpathPhone)).getText();
		
		TraceOps.printMessage(LogLevel.INFO, "%s phone model has been randomly selected", modelName);

		waitForElement(By.linkText(modelName), 10, 1, false);
		
		driver.findElement(By.xpath(xpathPhone+"/a")).click();
		
		TraceOps.printMessage(LogLevel.TRACE, "Verifying that correct phone model page has been opened...");
		String xpathPhoneStats = "//*[@id='body']/div/div[1]/div/div[1]/h1";
		waitForElement(By.xpath(xpathPhoneStats), 10, 1, false);
		
		Assert.assertTrue(driver.findElement(By.xpath(xpathPhoneStats)).getText().contains(modelName),
											"Incorrect model page has been opened." );

	}

	@Test (priority =10)
	public void verifyIsGPS(){
		
		TraceOps.printMessage(LogLevel.TRACE, "Verifying if selected model is designed to use GPS...");
		
		String xpathSpecTable = "//*[@id='specs-list']/table";
		waitForElement(By.xpath(xpathSpecTable), 10, 1, false);
		
		List <WebElement> elems = driver.findElements(By.xpath(xpathSpecTable));
		
		boolean isMatch = false;

		for(WebElement e:elems){
			if(e.getText().contains("GPS Yes")){
				isMatch =true;
				break;
			}
		}
		
		Assert.assertTrue(isMatch, "Selected phone model is not designed to use GPS.");
		
	}
	
//	@AfterClass
//	@Override
//	public void onFinish() {
//		super.onFinish();
//	}
	

}
