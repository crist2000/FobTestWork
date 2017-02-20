package some.domain.scenarios;

import java.io.IOException;

import org.testng.annotations.Test;

import some.domain.common.BaseOperations;

public class Scenario1 extends BaseOperations {
	
	public Scenario1() throws IOException {
		super();
	}

	@Test 
	public void doSomething(){
		System.out.println("Did something!");
	}

}
