package some.domain.scenarios;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.beust.jcommander.Parameter;

import some.domain.common.BaseOperations;
import some.domain.utils.TraceOps;
import some.domain.utils.TraceOps.LogLevel;

public class Scenario2 extends BaseOperations {

	final String testingURL = "https://wego.here.com";
	
	public Scenario2() throws IOException {
		super();
	}

	@Test 
	@Parameters({"url"})
	public void openHomePage(String url) throws InterruptedException{
		
		driver.get(url);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		System.out.println("Scenario1: Did something!");
		Thread.sleep(2000);
	}
	
	@AfterClass
	public void onFinish(){
		super.onFinish();
	}; 

}
