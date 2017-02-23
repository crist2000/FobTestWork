package some.domain.scenarios;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import some.domain.common.BaseOperations;
import some.domain.utils.Helper;
import some.domain.utils.TraceOps;
import some.domain.utils.TraceOps.LogLevel;

public class Scenario1 extends BaseOperations{

	
	public Scenario1() throws IOException {
		super();
	}
	
	@Test(priority =0) 
	@Parameters({"wego_home"})
	public void openHomePage(String url) throws InterruptedException{
		
		TraceOps.printMessage(LogLevel.TRACE, "Openening home page %s...", url);
		driver.get(url);
		TraceOps.printMessage(LogLevel.TRACE, "Home page has been opened.");
	}

	@Test(priority =3)
	public void navigateToDirections(){
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		TraceOps.printMessage(LogLevel.TRACE, "Getting to directions...");
		String xpathDirectionBtn = "//*[@id='searchbar']/div/div/a";
		
		try {
			driver.findElement(By.xpath(xpathDirectionBtn)).click();
		} catch (Exception e) {
			TraceOps.printMessage(LogLevel.ERROR, "Directions element %s not found", xpathDirectionBtn);
		}
		
	}
	
	@Test (dataProvider="Directions",priority=5)
	public void setFrom(String from, String to, final String xpathFrom, 
						String xpathTo){

		WebElement element;
		String idFrom = "itinerary_item_input_0";
		
		TraceOps.printMessage(LogLevel.TRACE, "Entering %s as starting location...", from);
		element= driver.findElement(By.id(idFrom));
		element.clear();
		element.sendKeys(from);
		waitForElement(By.xpath(xpathFrom), 10, 1, false);
		driver.findElement(By.xpath(xpathFrom)).click();
		TraceOps.printMessage(LogLevel.TRACE, "From location is set.");
	}
	
	@Test (dataProvider="Directions",priority=6)
	public void setTo (String from, String to, final String xpathFrom, 
						String xpathTo){
		
		WebElement element;
		String idTo = "itinerary_item_input_1";
		
		TraceOps.printMessage(LogLevel.TRACE, "Entering %s as destination...", to);
		element = driver.findElement(By.id(idTo));
		element.clear();
		element.sendKeys(to);
		waitForElement(By.xpath(xpathTo), 10, 1, false);
		driver.findElement(By.xpath(xpathTo)).click();
		TraceOps.printMessage(LogLevel.TRACE, "Destination is set.");
		
	}
	
	@Test(dataProvider ="TravelTimeOptions", priority=10)
	public void verifyTravelTimeVisible(String from, String to, String byCar, String byBus,String byBike){
		
		int routeOptions = Helper.calculateNonEmptyArgs(byCar,byBus,byBike);
	
		String xpathTravelOption = "//*[@id='routes_list']/ul/li";
		String xpathMaxTravelOption = xpathTravelOption+"["+routeOptions+"]";
		
		TraceOps.printMessage(LogLevel.TRACE, "Verifying travel time options for the route %s - %s.", from,to);
		waitForElement(By.xpath(xpathMaxTravelOption),10,1,true);
		List <WebElement> elems = driver.findElements(By.xpath(xpathTravelOption));
		
		Assert.assertEquals(routeOptions, elems.size());
	
	}

	@Test(dataProvider ="TravelTimeByCar", priority=11)
	public void verifyTravelTimebyCar_calculated(String from, String to, String travelTime){
		
		String xpathBtn = "/html/body/div[1]/div[6]/div/div/div[2]/div/button[1]";
		String[] travelTimeArray = travelTime.split(",");
		boolean isMatch = false;
		
		TraceOps.printMessage(LogLevel.TRACE, "Verifying car travel time for the route %s - %s.", from,to);
		driver.findElement(By.xpath(xpathBtn)).click();
		String xpathTravelTime = "//*[@id='routes_list']/ul/li[1]/div/div[2]/div[1]/span";
		List<WebElement> elems = driver.findElements(By.xpath(xpathTravelTime));

		String result ="";
		for(WebElement e:elems){
			
			String str = e.getText();
			result = result+str;
		}
		
		for (int i =0; i<travelTimeArray.length;i++){
			if (result.contains(travelTimeArray[i])){
				isMatch=true;
				break;
			}
		}
		
		Assert.assertTrue(isMatch,"Calculated travel time does not match expected values.");
	}
	
	@Test(dataProvider ="TravelTimeByBus", priority=12)
	public void verifyTravelTimebyBus_calculated(String from, String to, String travelTime){

		String[] travelTimeArray = travelTime.split(",");
		String xpathBtn = "/html/body/div[1]/div[6]/div/div/div[2]/div/button[1]";
		boolean isMatch = false;
		
		TraceOps.printMessage(LogLevel.TRACE, "Verifying bus travel time for the route %s - %s.", from,to);
		driver.findElement(By.xpath(xpathBtn)).click();
		String xpathTravelTime = "//*[@id='routes_list']/ul/li[2]/div/div[2]/div[1]/span";
		List<WebElement> elems = driver.findElements(By.xpath(xpathTravelTime));
		
		String result ="";
		for(WebElement e:elems){
			
			String str = e.getText();
			result = result+str;
		}
		
		for (int i =0; i<travelTimeArray.length;i++){
			if (result.contains(travelTimeArray[i])){
				isMatch=true;
				break;
			}
		}
		
		Assert.assertTrue(isMatch,"Calculated travel time does not match expected values.");

	}
	
	@Test(dataProvider ="TravelTimeByBike", priority=13)
	public void verifyTravelTimebyBike_calculated(String from, String to, String travelTime ){

		String[] travelTimeArray = travelTime.split(",");
		String xpathBtn = "/html/body/div[1]/div[6]/div/div/div[2]/div/button[1]";
		boolean isMatch = false;
		
		TraceOps.printMessage(LogLevel.TRACE, "Verifying bike travel time for the route %s - %s.", from,to);
		driver.findElement(By.xpath(xpathBtn)).click();
		String xpathTravelTime = "//*[@id='routes_list']/ul/li[3]/div/div[2]/div[1]/span";
		List<WebElement> elems = driver.findElements(By.xpath(xpathTravelTime));
		
		String result ="";
		for(WebElement e:elems){
			
			String str = e.getText();
			result = result+str;
		}
		
		for (int i =0; i<travelTimeArray.length;i++){
			if (result.contains(travelTimeArray[i])){
				isMatch=true;
				break;
			}
		}
		
		Assert.assertTrue(isMatch,"Calculated travel time does not match expected values.");
	}
	
	@Test(dataProvider ="TravelTimeOnFoot", priority=13)
	public void verifyTravelTimeonFoot_calculated(String from, String to, String travelTime ){
		
		String xpathBtn = "/html/body/div[1]/div[6]/div/div/div[2]/div/button[5]";
		String[] travelTimeArray = travelTime.split(",");
		boolean isMatch = false;
		
		TraceOps.printMessage(LogLevel.TRACE, "Verifying on foot travel time for the route %s - %s.", from,to);
		
		driver.findElement(By.xpath(xpathBtn)).click();
		String xpathTravelTime = "//*[@id='routes_list']/ul/li[1]/div/div[2]/div[1]";
		
		waitForElement(By.xpath(xpathTravelTime), 10, 1, false);
		List<WebElement> elems = driver.findElements(By.xpath(xpathTravelTime));
		
		String result ="";
		for(WebElement e:elems){
			
			String str = e.getText();
			result = result+str;
		}
		
		for (int i =0; i<travelTimeArray.length;i++){
			if (result.contains(travelTimeArray[i])){
				isMatch=true;
				break;
			}
		}
		
		Assert.assertTrue(isMatch,"Calculated travel time does not match expected values.");
	}
	
	
	
	@DataProvider(name="Directions")
    public Object[][] getDirectionData(){

        return new Object[][] {
    		//From, 		To, 	xpathFrom,		xpathTo 
            { "Tallinn",	"Narva",
            						"//*[@id='itinerary_item_0']/div[2]/div[1]/span[1]/div",
            										"//*[@id='itinerary_item_1']/div[2]/div[2]/span[1]/span/span"}
        };
	}
  
	@DataProvider(name="TravelTimeOptions")
    public Object[][] getTravelTimeOptions(){

        return new Object[][] {
    		//From, 		To,		byCar,	byBus, 	byBike
            { "Tallinn",	"Narva","+",	"+",	"+"	}
        };
	}
	
	@DataProvider(name="TravelTimeByCar")
    public Object[][] getTravelTime_ByCar(){

        return new Object[][] {
    		//From, 		To,		Travel time in hours
            {"Tallinn",		"Narva","1 h,2 h,3 h"}
        };
	}

	@DataProvider(name="TravelTimeByBus")
    public Object[][] getTravelTime_ByBus(){

        return new Object[][] {
    		//From, 		To,		Travel time in hours
            {"Tallinn",		"Narva","2 h,3 h,4 h"}
        };
	}
	
	@DataProvider(name="TravelTimeByBike")
    public Object[][] getTravelTime_ByBike(){

        return new Object[][] {
    		//From, 		To,		Travel time in hours
            {"Tallinn",		"Narva","15 h,16 h,17 h"}
        };
	}
	
	@DataProvider(name="TravelTimeOnFoot")
    public Object[][] getTravelTime_onFoot(){

        return new Object[][] {
    		//From, 		To,		Travel time in hours
            {"Tallinn",		"Narva","1 d,2 d"}
        };
	}
	
	
}
