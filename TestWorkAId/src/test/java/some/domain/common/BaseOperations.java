package some.domain.common;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.Assert;

import com.google.common.base.Function;

import some.domain.utils.TraceOps;
import some.domain.utils.TraceOps.LogLevel;

public class BaseOperations{
	private final static String webdriverFolderPath = "C:\\Tools\\WebDrivers\\";
	private final static String DRV_FileNamechrome = "chromedriver.exe";
	private final static String DRV_FileName_Firefox = "geckodriver.exe";
	private final static String DRV_FileName_IE = "MicrosoftWebDriver.exe";
	public WebDriver driver;

	public BaseOperations () throws IOException {
		
		Init();
		driver = new ChromeDriver();
	}

 	private static void Init() throws IOException {
 
		Path pathChrome = Paths.get(webdriverFolderPath, DRV_FileNamechrome);
		Path pathFirefox = Paths.get(webdriverFolderPath, DRV_FileName_Firefox);
		Path pathIE = Paths.get(webdriverFolderPath,DRV_FileName_IE);
		File chromeDrvFile = new File(pathChrome.toString());
		File firefoxDrvFile = new File(pathFirefox.toString());
		File IEDrvFile = new File(pathIE.toString());	
		 
		System.setProperty("webdriver.chrome.driver",chromeDrvFile.getCanonicalPath());
		System.setProperty("webdriver.gecko.driver", firefoxDrvFile.getCanonicalPath());
		System.setProperty("webdriver.ie.driver", IEDrvFile.getCanonicalPath());
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

	public static void waitForElement(WebDriver driver, final By by, int timeout, int polling, boolean ignoreEx){
		
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
	
	protected void failOperation (String message){
		
		TraceOps.printMessage(LogLevel.ERROR, message);
		
		Assert.assertTrue(false);
		driver.close();
	}

	protected int getWebElementListSize(By by){
		
		List <WebElement> elems = driver.findElements(by);
		int size = elems.size();
		TraceOps.printMessage(LogLevel.INFO, "  List Size is %d",size );
		
		return size;
	}

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

	protected void onFinish(){

		try {
			TraceOps.printMessage(LogLevel.INFO, "%s execution has been completed. Closing browser window.", (this.getClass().getSimpleName()));
			driver.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
