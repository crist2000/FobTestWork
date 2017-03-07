package some.domain.common;

import java.io.IOException;
import java.lang.reflect.Method;

import org.openqa.selenium.Point;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import some.domain.utils.TraceOps;
import some.domain.utils.TraceOps.LogLevel;

public class TestClassBaseSettings extends BaseOperations{

	public TestClassBaseSettings() throws IOException {
		super();
	}

	@BeforeClass
	public void baseBeforeClass(){
		
		driver.manage().window().setPosition(new Point(10, 10));
		
	}
	@AfterClass
	public void baseAfterClasss(){
		
		super.closeCurrentWindow();
		
	}
	
    @BeforeMethod
    public void baseBeforeMethod(Method method)
    {
        testName = method.getName(); 
        TraceOps.printMessage(LogLevel.INFO, "Executing test: %s...", testName);
    }
    @AfterMethod
    public void baseAfterMethod()
    {
        TraceOps.printMessage(LogLevel.INFO, "Finished test execution: %s", testName);
    }
  
}
