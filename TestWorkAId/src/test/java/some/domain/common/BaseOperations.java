package some.domain.common;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

import com.google.common.base.Function;

import some.domain.utils.TraceOps;
import some.domain.utils.TraceOps.LogLevel;

/**
 * Base class for all tests. Initializes webDiver object and contains fields and methods which
 * operate with webDiver object. Make sure that path to chrome browser  driver is added to 
 * system PATH variable.
 */
public class BaseOperations{

	public WebDriver driver;
	protected String testName;
	public WebDriverWait wait;

	
	public BaseOperations () throws IOException {
		
		//Make sure that path to chrome browser is added to system PATH variable.
		driver = new ChromeDriver();
		driver.manage().window().setPosition(new Point(-2000, 0));
	}

 	protected void closeAllWindows () throws Exception{
 		
 		TraceOps.printMessage(TraceOps.LogLevel.TRACE, "Closing all windows...", "");
        Set<String> pops=driver.getWindowHandles();
        Iterator<String> it =pops.iterator();
        
        while (it.hasNext()) {
            String popupHandle=it.next().toString();
            driver.switchTo().window(popupHandle);
            TraceOps.printMessage(TraceOps.LogLevel.TRACE, "Closing window: %s", driver.switchTo().window(popupHandle).getTitle());
            driver.close();
         }
 	}
	
 	/**
 	 * Method to identify presence of specific element.
 	 * @param by - locator.
 	 * @return - TRUE if element was found. 
 	 */
 	protected boolean assertElementIsPresent(By by) {

		try {
			TraceOps.printMessage(LogLevel.TRACE, "  Verfying selected element...", "");
			List<WebElement> elems = driver.findElements(by);
			if (elems.size()!=0){
				TraceOps.printMessage(LogLevel.PASSED, "  Verfying %s locator is present: PASS", by);
				return true;
			}
			else{
				TraceOps.printMessage(LogLevel.ERROR, "  Verfying %s locator is present: FAIL", by);
				return false;
			}
		} 
		catch (Exception e) {
			TraceOps.printMessage(LogLevel.ERROR, "  Unexpected access locator %s was provided.", by);
			return false;
		}
	}

	/**
	 * Method to wait for specific element based on FluentWait class object.  
	 * @param by - locator
	 * @param timeout - time to wait for the element
	 * @param polling - polling interval
	 * @param ignoreEx - FALSE if exception is required after timeout.
	 */
	public void waitForElement(final By by, int timeout, int polling, boolean ignoreEx){
		
		Wait<WebDriver> wait;
		boolean isFound = false;
		
		TraceOps.printMessage(LogLevel.TRACE, "  Waiting for element %s", by);
		if (!ignoreEx){
				wait = new FluentWait<WebDriver>(driver)
				.withTimeout(timeout, TimeUnit.SECONDS)
				.pollingEvery(polling, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);}
		else {
				wait = new FluentWait<WebDriver>(driver)
				.withTimeout(timeout, TimeUnit.SECONDS)
				.pollingEvery(polling, TimeUnit.SECONDS);}
	
		Function<WebDriver, Boolean> function = new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver driver) {

				WebElement element = driver.findElement(by);
				return element.isDisplayed();
			}
		};
		isFound = wait.until(function);
		if (isFound)
			TraceOps.printMessage(LogLevel.TRACE, "  Finished waiting. IsDisplayed: %s. ELEMENT: %s: ", isFound, by);
		else
			TraceOps.printMessage(LogLevel.ERROR, "  Finished waiting. IsDisplayed: %s. ELEMENT: %s: ", isFound, by);
	}
	
	/**
	 * Reserved for future use.
	 * @param message
	 */
	protected void click(By by, int tryCount, long polling){

		int count = 0;
		
		while (count<tryCount) {
			
			TraceOps.printMessage(LogLevel.TRACE, "Attempt %d to click the element %s", count+1, by);
			assertElementIsPresent(by);
			
			try {
				driver.findElement(by).click();
				TraceOps.printMessage(LogLevel.TRACE, "Attempt %d to click the element %s was successful", count+1, by);
				return;
			} catch (Exception e) {
				TraceOps.printMessage(LogLevel.WARN,"Failed to click specified element.");
			}
			if(polling!=0 ){
				try {
				Thread.sleep(polling);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}	
			}
			count++;	
		}
	}
	
	/**
	 * Urgent currently running test fail. Reserved for future use.
	 * @param message
	 */
	protected void failOperation (String message){
		
		TraceOps.printMessage(LogLevel.ERROR, message);
		
		Assert.assertTrue(false);
	}

	/**
	 * Utility method to calculate size of the WebElement list.
	 * @param by - locator
	 * @return - list size is returned. 0 if no element was found.
	 */
	protected int getWebElementListSize(By by){
		
		List <WebElement> elems = driver.findElements(by);
		int size = elems.size();
		TraceOps.printMessage(LogLevel.INFO, "  List Size is %d",size );
		
		return size;
	}

	/**
	 * Method to get element from the list that matches specific criteria.
	 * @param by - locator
	 * @param criteria - pattern to be searched for.
	 * @return - matching web element. NULL if no match was found. 
	 */
	protected WebElement getElementFromListByText(By by, String criteria){
		
		List <WebElement> elems = driver.findElements(by);
		WebElement elem = null;
		
		if (elems.size()==0){
			TraceOps.printMessage(LogLevel.WARN, "  No elements were found for the %s locator.", by);
			return elem;
		}
		
		for(WebElement e:elems){
			if (e.getText().contains(criteria)){
				TraceOps.printMessage(LogLevel.INFO, "  Matching element '%s' was found",criteria);
				return e;
			}
		}
		
		TraceOps.printMessage(LogLevel.WARN, "  Matching element was not found.");
		return elem;
		
	}

	/**
	 * Base method to be used in child class's @AfterClass methods
	 */
	protected void closeCurrentWindow(){

		try {
			TraceOps.printMessage(LogLevel.INFO, "%s execution has been completed. Closing browser window.", (this.getClass().getSimpleName()));
			Thread.sleep(2000);
			driver.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
