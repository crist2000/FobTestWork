package some.domain;

import java.util.List;

import org.testng.TestNG;
import org.testng.xml.Parser;
import org.testng.xml.XmlSuite;

import some.domain.scenarios.Scenario1;

public class MainApp {

	public static void main(String[] args) {

		TestNG testng = new TestNG();
		String xmlFileName = "src/test/java/scenario1.xml";
		List<XmlSuite> suite;
		
	    try {
			suite = (List <XmlSuite>)(new Parser(xmlFileName).parse());
		    testng.setXmlSuites(suite);
		    testng.run();
		} catch (Exception e) {

			e.printStackTrace();
		}

		//HowTo run specific test classes:
//        TestNG testng = new TestNG();
//        Class[] classes = new Class[]{Scenario1.class};
//        testng.setTestClasses(classes);
//        testng.run();
		
	}

}
