package some.domain.repository;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import some.domain.common.BaseOperations;
import some.domain.scenarios.Scenario2;
import some.domain.utils.TraceOps;
import some.domain.utils.TraceOps.LogLevel;

public class WegoOps {
	
	public static final String MENU_SIGNIN = "Sign in";
	public static final String MENU_COLLECTIONS = "Collections";
	private static final By cssMenuItems = By.cssSelector("div[class='menu_item']"); 
	
	public static void selectMenuItem(String menuItem, BaseOperations bo){
		
		TraceOps.printMessage(LogLevel.TRACE, "Selecting 'Collections' from menu...");
		
		bo.waitForElement(cssMenuItems, 10, 1, false);
		
		List <WebElement> elems = bo.driver.findElements(cssMenuItems);
		for(WebElement e: elems){
			if(e.getText().contains(menuItem))
				e.click();
		}
	}

	public static void openMenu(WebDriverWait wait, BaseOperations bo){
		
		By xpathMenuBtn = By.cssSelector("button[class='menu_access_btn icon_btn']");
		
		TraceOps.printMessage(LogLevel.TRACE, "Opening menu...");
		
		//Added to avoid unhandled inspector error: 'code':-32000, 
		//caused by element which was in the default content while focus in some iframe
		bo.driver.switchTo().defaultContent();
		
		bo.waitForElement(xpathMenuBtn, 5, 1, true);
		wait.until(ExpectedConditions.elementToBeClickable(xpathMenuBtn));
		
		bo.driver.findElement(xpathMenuBtn).click();
		//TraceOps.printMessage(LogLevel.TRACE, "Finished opening menu...");
	}
}
