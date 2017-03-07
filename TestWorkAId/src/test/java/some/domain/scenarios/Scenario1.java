package some.domain.scenarios;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import some.domain.common.TestClassBaseSettings;
import some.domain.utils.Helper;
import some.domain.utils.TraceOps;
import some.domain.utils.TraceOps.LogLevel;

public class Scenario1 extends TestClassBaseSettings{

	
	public Scenario1() throws IOException {
		super();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		wait = new WebDriverWait(driver, 10);
	}
	
	@Test(priority =0) 
	@Parameters({"wego_home"})
	public void openHomePage(String url) throws InterruptedException{

		driver.get(url);

	}

	@Test(dependsOnMethods = {"openHomePage"}, enabled = true)
	public void navigateToDirections(){
		
		By xpathDirectionBtn = By.xpath("//*[@id='searchbar']/div/div/a") ;
		
		driver.findElement(xpathDirectionBtn).click();
	}
	
	@Test (dependsOnMethods = {"navigateToDirections"},dataProvider="Directions", enabled = true)
	public void setFrom(String location, String detailedName){
		
		WebElement element;
		By idFrom = By.id("itinerary_item_input_0");
		By cssSelectSuggestion = By.cssSelector("#itinerary_item_0 > div.dropdown_list.flying_style > div.dropdown_list_item.suggestion.hovered");
		
		TraceOps.printMessage(LogLevel.TRACE, "Entering %s as starting location...", location);
		element= driver.findElement(idFrom);
		element.clear();
		element.sendKeys(location);
		
		wait.until(ExpectedConditions.elementToBeClickable(cssSelectSuggestion));
		
		try {
			WebElement elem = getElementFromListByText(cssSelectSuggestion, detailedName);
			elem.click();
		} catch (Exception e) {
			TraceOps.printMessage(LogLevel.ERROR, "Provided pattern %s was not found in dropdown.", location);
		}
	}
	
	@Test (dependsOnMethods = {"setFrom"},dataProvider = "Directions", enabled = true)
	public void setTo (String location, String detailedName){
		
		WebElement element;
		By idTo = By.id("itinerary_item_input_1");
		By cssSelectSuggestion = By.cssSelector("#itinerary_item_1 > div.dropdown_list.flying_style > div.dropdown_list_item.suggestion.hovered");
		
		TraceOps.printMessage(LogLevel.TRACE, "Entering %s as destination...", location);
		element = driver.findElement(idTo);
		element.clear();
		element.sendKeys(location);

		
		//wait.until(ExpectedConditions.elementToBeClickable(cssSelectSuggestion));
		wait.until(ExpectedConditions.presenceOfElementLocated(cssSelectSuggestion));
		
		try {
			Thread.sleep(1000);
			WebElement elem = getElementFromListByText(cssSelectSuggestion, detailedName);
			elem.click();
		} catch (Exception e) {
			TraceOps.printMessage(LogLevel.ERROR, "Provided pattern %s was not found in dropdown.", detailedName);
		}
	}

	//This test is to verify that correct amount of travel time options is calculated based on route.
	@Test (dependsOnMethods = {"setTo"},dataProvider = "TravelTimeOptions",enabled = true)
	public void verifyTravelTimeVisible(String from, String to, String byCar, String byBus,String byBike){
		
		int routeOptions = Helper.calculateNonEmptyArgs(byCar,byBus,byBike);
	
		String xpathTravelOption = "//*[@id='routes_list']/ul/li";
		String xpathMaxTravelOption = xpathTravelOption+"["+routeOptions+"]";
		
		TraceOps.printMessage(LogLevel.TRACE, "Verifying travel time options for the route %s - %s.", from,to);
		waitForElement(By.xpath(xpathMaxTravelOption),10,1,true);
		List <WebElement> elems = driver.findElements(By.xpath(xpathTravelOption));
		
		Assert.assertEquals(routeOptions, elems.size());
	}

	@Test (dependsOnMethods = {"verifyTravelTimeVisible"},dataProvider = "getTravelTime",enabled = true)
	public void verifyTravelTimebyCar_calculated(String from, String to, String travelTime){
		
		By xpathBtn = By.xpath("/html/body/div[1]/div[6]/div/div/div[2]/div/button[1]");
		By xpathTravelTime = By.xpath("//*[@id='routes_list']/ul/li[1]/div/div[2]/div[1]/span");
		boolean isMatch = false;
		
		TraceOps.printMessage(LogLevel.TRACE, "Verifying car travel time for the route %s - %s.", from,to);
		
		driver.findElement(xpathBtn).click();
		
		List<WebElement> elems = driver.findElements(xpathTravelTime);

		isMatch = Helper.isMatchingElement(elems, travelTime);
		
		Assert.assertTrue(isMatch,"Calculated travel time does not match expected values.");
	}
	
	@Test (dependsOnMethods = {"verifyTravelTimeVisible"},dataProvider = "getTravelTime",enabled = true)
	public void verifyTravelTimebyBus_calculated(String from, String to, String travelTime){

		By xpathBtn =By.xpath("/html/body/div[1]/div[6]/div/div/div[2]/div/button[1]");
		By xpathTravelTime = By.xpath("//*[@id='routes_list']/ul/li[2]/div/div[2]/div[1]/span");
		boolean isMatch = false;
		
		TraceOps.printMessage(LogLevel.TRACE, "Verifying bus travel time for the route %s - %s.", from,to);
		
		driver.findElement(xpathBtn).click();
		
		List<WebElement> elems = driver.findElements(xpathTravelTime);
		
		isMatch = Helper.isMatchingElement(elems, travelTime);
		
		Assert.assertTrue(isMatch,"Calculated travel time does not match expected values.");
	}
	
	@Test (dependsOnMethods = {"verifyTravelTimeVisible"},dataProvider = "getTravelTime",enabled = true)
	public void verifyTravelTimebyBike_calculated(String from, String to, String travelTime ){

		By xpathBtn = By.xpath("/html/body/div[1]/div[6]/div/div/div[2]/div/button[1]");
		By xpathTravelTime = By.xpath("//*[@id='routes_list']/ul/li[3]/div/div[2]/div[1]/span");
		boolean isMatch = false;
		
		TraceOps.printMessage(LogLevel.TRACE, "Verifying bike travel time for the route %s - %s.", from,to);
		
		driver.findElement(xpathBtn).click();
		List<WebElement> elems = driver.findElements(xpathTravelTime);
		
		isMatch = Helper.isMatchingElement(elems, travelTime);
		
		Assert.assertTrue(isMatch,"Calculated travel time does not match expected values.");
	}
	
	@Test (dependsOnMethods = {"verifyTravelTimeVisible"},dataProvider = "getTravelTime",enabled = true)
	public void verifyTravelTimeonFoot_calculated(String from, String to, String travelTime ){
		
		By cssWalkBtn= By.cssSelector("button[title='Walk']");
		By xpathTravelTime = By.xpath("//*[@id='routes_list']/ul/li[1]/div/div[2]/div[1]");
		boolean isMatch = false;
		
		TraceOps.printMessage(LogLevel.TRACE, "Verifying on foot travel time for the route %s - %s.", from,to);
		
		driver.findElement(cssWalkBtn).click();
		
		waitForElement(xpathTravelTime, 10, 1, false);
		List<WebElement> elems = driver.findElements(xpathTravelTime);
		
		isMatch = Helper.isMatchingElement(elems, travelTime);

		Assert.assertTrue(isMatch,"Calculated travel time does not match expected values.");
	}
	
	
	@DataProvider(name="Directions")
    public Object[][] getDirectionData(Method e){
		
		String methodName = e.getName();
		
		switch(methodName){
		case "setFrom":
		        return new Object[][] {
		        	//Location,		DetailedName 
		        	{ "Tallinn",	"Harju Maakond"}
		        };
		case "setTo":
		        return new Object[][] {
		        	{ "Narva",		"Ida-Viru Maakond"}
			        };
		 default: return null;
		}
	}
	
	//Script can calculate different travel times for the same route.
	@DataProvider(name="getTravelTime")
    public Object[][] getTravelTime(Method e){
		
		String methodName = e.getName();
		
		switch(methodName){
		case "verifyTravelTimebyCar_calculated":
	        return new Object[][] {
	        	//From,			To 			Travel time options
	        	{ "Tallinn",	"Narva",	"1 h,2 h,3 h"		}
	        };
		case "verifyTravelTimebyBus_calculated":
		        return new Object[][] {
	        	{"Tallinn",		"Narva",	"2 h,3 h,4 h"		}
	        };
		case "verifyTravelTimebyBike_calculated":
	        return new Object[][] {
	        	{"Tallinn",		"Narva",	"15 h,16 h,17 h"	}
	        };	        
		case "verifyTravelTimeonFoot_calculated":
	        return new Object[][] {
	        	{"Tallinn",		"Narva",	"1 d,2 d"			}
	        };	        
		default: return null;
		}

	}
	
 	@DataProvider(name="TravelTimeOptions")
    public Object[][] getTravelTimeOptions(){

        return new Object[][] {
    		//From, 		To,		byCar,	byBus, 	byBike
            { "Tallinn",	"Narva","+",	"+",	"+"	}
        };
	}

}
