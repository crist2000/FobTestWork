package some.domain.common;

import java.io.File;
import java.io.IOException;
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
			TraceOps.printMessage(LogLevel.TRACE, "Verfying selected element...", "");
			List<WebElement> elems = driver.findElements(by);
			if (elems.size()!=0){
				TraceOps.printMessage(LogLevel.PASSED, "Verfying %s locator is present: PASS", by);
				return true;
			}
			else{
				TraceOps.printMessage(LogLevel.ERROR, "Verfying %s locator is present: FAIL", by);
				return false;
			}
		} 
		catch (Exception e) {
			TraceOps.printMessage(LogLevel.ERROR, "Unexpected access locator %s was provided.", by);
			return false;
		}
	}

	protected void waitForElement(final By by, int timeout, int polling, boolean ignoreEx){
		
		WebElement element;
		Wait<WebDriver> wait;
		
		TraceOps.printMessage(LogLevel.TRACE, "Waiting for element %s", by);
		if (!ignoreEx){
				wait = new FluentWait<WebDriver>(driver)
				.withTimeout(timeout, TimeUnit.SECONDS)
				.pollingEvery(polling, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);}
		else {
				wait = new FluentWait<WebDriver>(driver)
				.withTimeout(timeout, TimeUnit.SECONDS)
				.pollingEvery(polling, TimeUnit.SECONDS);}
	
		  try {
			  element = wait.until(new Function<WebDriver, WebElement>() {

		      @Override
		      public WebElement apply(WebDriver driver)
		      {
		    	  return driver.findElement(by);
		      }
		    });
			  TraceOps.printMessage(LogLevel.TRACE, "Element %s was found.", by);
		  }catch (Exception e){
			  TraceOps.printMessage(LogLevel.WARN, "Element %s was not found.", by);;
		  }
		
	}

	protected void tryToClick(By by, int tryCount, long polling){

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
			
			try {
			Thread.sleep(polling);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
			count++;	
		}
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
