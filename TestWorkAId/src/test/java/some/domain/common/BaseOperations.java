package some.domain.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import some.domain.utils.TraceOps;
import some.domain.utils.TraceOps.LogLevel;

public class BaseOperations{
	private final static String webdriverFolderPath = "C:\\Tools\\WebDrivers\\";
	private final static String DRV_FileNamechrome = "chromedriver.exe";
	private final static String DRV_FileName_Firefox = "geckodriver.exe";
	private final static String DRV_FileName_IE = "MicrosoftWebDriver.exe";
	public static WebDriver driver;

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
 	public static void closeAllWindows () throws Exception{
 		
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
	public static boolean AssertElementIsPresent(By by) throws Exception{
		
		TraceOps.printMessage(LogLevel.TRACE, "Verfying selected element...", "");
		try {
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
}
