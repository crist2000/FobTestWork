package some.domain.scenarios;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.beust.jcommander.Parameter;

import some.domain.common.BaseOperations;
import some.domain.repository.WegoOps;
import some.domain.utils.Helper;
import some.domain.utils.TraceOps;
import some.domain.utils.TraceOps.LogLevel;

public class Scenario2 extends BaseOperations {

	public WebDriverWait wait;
	String testName;
	
	public Scenario2() throws IOException {
		super();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		wait = new WebDriverWait(driver, 10);
	}
	
    @BeforeMethod
    public void BeforeMethod(Method method)
    {
        testName = method.getName(); 
        TraceOps.printMessage(LogLevel.INFO, "Executing test: %s...", testName);
    }
    
    @AfterMethod
    public void AfterMethod()
    {
        TraceOps.printMessage(LogLevel.INFO, "Finished test execution: %s", testName);
    }
	
	@Test (priority = 0)
	@Parameters({"wego_home"})
	public void openHomePage(String url) throws InterruptedException{

		driver.get(url);

	}
	
	@Test (dependsOnMethods = { "openHomePage" })
	@Parameters({"wego_uid","wego_pwd"})
	public void signIn (String uid,String pwd) throws InterruptedException{
		
		By signInElement= By.cssSelector("button[class='sign_in_out sign_in_button']") ;
		By signBtn = By.xpath("//*[@id='sign-in-form']/div[2]/div[6]/form/fieldset[3]/button");
		WebElement elem;
		String idUID ="sign-in-email";
		String idPWD ="sign-in-password-encrypted";
		
		WegoOps.openMenu(wait, this);
		
		waitForElement(signInElement, 10, 1, false);
		
		elem = driver.findElement(signInElement);

		if(elem.getText().contains("Sign in")){
			TraceOps.printMessage(LogLevel.INFO,"Logging in...");
			elem.click();
			driver.switchTo().frame("here-account-sdk");
			waitForElement(By.id(idUID), 10, 1, true);
			
			driver.findElement(By.id(idUID)).sendKeys(uid);
			driver.findElement(By.id(idPWD)).sendKeys(pwd);
		
			waitForElement(signBtn, 10, 1, false);
			driver.findElement(signBtn).click();
		}
		else{
			TraceOps.printMessage(LogLevel.WARN, "Already signed in. Login procedure has been skipped.");
		}
}

	@Test (dependsOnMethods = { "signIn" },dataProvider = "Location",enabled = true)
	public void selectLocation (String Location){
		
		By cssCollectionInput = By.cssSelector("#searchbar > div > div.bar.bar_no_bottom > input");
		By cssSelectLocationFromDropdown = By.cssSelector("div[class='dropdown_list_item suggestion']");
		String xpathSuggestion = "//*[@id='searchbar']/div/div[2]/div";
				
		WegoOps.openMenu(wait, this);

		WegoOps.selectMenuItem(WegoOps.MENU_COLLECTIONS, this);
		
		TraceOps.printMessage(LogLevel.TRACE, "Entering and selecting a location...");

		waitForElement(cssCollectionInput,10, 1, false);
		driver.findElement(cssCollectionInput).clear();
		driver.findElement(cssCollectionInput).sendKeys(Location);

		int rndIndex = Helper.getRandomIndex(driver.findElements(cssSelectLocationFromDropdown));

		TraceOps.printMessage(LogLevel.INFO, "Selected: %s", driver.findElement(By.xpath(xpathSuggestion+"["+rndIndex+"]")).getText());
		driver.findElement(By.xpath(xpathSuggestion+"["+rndIndex+"]")).click();

	}
	
	@Test (dependsOnMethods = { "selectLocation" },enabled = true)
	public void clickCollect(){	

		By xpathCollectBtn = By.xpath("//button[@title='Collect']");
	
		waitForElement(xpathCollectBtn, 20, 1, false);
		
		driver.findElement(xpathCollectBtn).click();
		}
		
	@Test (dependsOnMethods = { "clickCollect" },dataProvider = "Location",enabled = true)
	public void createNewCollection(String collectionName){
		
		By cssStartCollectionBtn = By.cssSelector("div[class='collections_empty'] > button");
		By cssDoneBtn = By.cssSelector("div[class='collections_grid']> button:nth-child(2)");
		String xpathCreateBtn = "/html/body/div[1]/div[7]/div/div/div/div[2]/form/fieldset[2]/button[2]";
		By idCollectionInput = By.id("collection_name");
		
		waitForElement(cssStartCollectionBtn, 10, 1, true);
		wait.until(ExpectedConditions.elementToBeClickable(cssStartCollectionBtn));
		
		driver.findElement(cssStartCollectionBtn).click();
		
		TraceOps.printMessage(LogLevel.TRACE, "Entering collection name...");

		driver.findElement(idCollectionInput).clear();
		driver.findElement(idCollectionInput).sendKeys(collectionName);
		
		TraceOps.printMessage(LogLevel.TRACE, "Generating new collection...");
		
		waitForElement(By.xpath(xpathCreateBtn), 5, 1, false);
		driver.findElement(By.xpath(xpathCreateBtn)).click();
		
		TraceOps.printMessage(LogLevel.TRACE,"New collection has been created. Finishing ...");

		driver.findElement(cssDoneBtn).click();
		
	}

	@Test (dependsOnMethods = { "createNewCollection" },dataProvider = "Location",enabled = true)
	public void verifyCollectionExists(String location){
		
		SoftAssert softAssert = new SoftAssert();
		By cssCollectionList = By.cssSelector("h4[class='title']");
		
		TraceOps.printMessage(LogLevel.INFO,"Verifying collection is present...");
		
		//the element below overlaps with menu button. Needs to wait until it disappears. 
		By css = By.cssSelector("span[class='logo']");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(css));
		
		WegoOps.openMenu(wait, this);
		WegoOps.selectMenuItem(WegoOps.MENU_COLLECTIONS, this);

		TraceOps.printMessage(LogLevel.TRACE, "Verifying size of collection's list...");
		softAssert.assertTrue((getWebElementListSize(cssCollectionList)>0), "Collections list is empty");
		
		try {
			TraceOps.printMessage(LogLevel.TRACE,"Verifying content of collection's list");
			WebElement elem = getElementFromListByText(cssCollectionList, location);
			softAssert.assertEquals(elem.getText(), location,"Unexpected collection name was found");
		} catch (Exception e) {
			System.out.println("oops");
		}
	}
	

	@DataProvider(name="Location")
    public Object[][] getLo(){

        return new Object[][] {
            {"Tallinn"}
        };
	}
	
	@AfterClass
	public void onFinish(){
		TraceOps.printMessage(LogLevel.TRACE,"Clearing test data...");
		
		By cssEditBtn = By.cssSelector("button[title='Edit']");
		By cssDeleteBtn = By.cssSelector("button[class='btn_delete']");
		By cssConfirmDelete = By.cssSelector ("div[class ='delete_overlay']> button.btn_link.confirm");// div.delete_overlay > button.btn_link.confirm
		
		WegoOps.openMenu(wait, this);
		WegoOps.selectMenuItem(WegoOps.MENU_COLLECTIONS, this);
		
		driver.findElement(cssEditBtn).click();
		
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		driver.findElement(cssDeleteBtn).click();

		waitForElement(cssConfirmDelete, 15, 1, true);
		driver.findElement(cssConfirmDelete).click();

		TraceOps.printMessage(LogLevel.TRACE,"Test data clearing completed.");
		//super.onFinish();
	}
}
	

