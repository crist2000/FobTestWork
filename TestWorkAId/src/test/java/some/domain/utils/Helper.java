package some.domain.utils;

import java.util.List;
import java.util.Random;

import org.openqa.selenium.WebElement;

import some.domain.utils.TraceOps.LogLevel;

/**
 * Utility class containing fields and methods that are common for all scenarios.
 */
public class Helper {
	
	/**
	 * Calculates number of non empty args provided as methods params.
	 * @param args - args to be assessed.
	 * @return - int value of non empty args.
	 */
	public static int calculateNonEmptyArgs(String... args){

		int count=0;
		
		for (String s:args){
			if (s!="")
				count++;
		}
		return count;
	}

	/**
	 * Returns random int from range 1 and list.size()
	 * @param list - list to be processed.
	 * @return - random int.
	 */
	public static int getRandomIndex(List list){
		
		int elemCount = list.size();	
		Random random = new Random();
		int randomElem = random.nextInt(elemCount) + 1;
		TraceOps.printMessage(LogLevel.TRACE, "index %d has been randomly selected.", randomElem);
		
		return randomElem;
	}

	/**
	 * Defines if there is element which matches provided criteria. Search performed on linked text.
	 * @param elems - list to be assessed.
	 * @param criterias - pattern to be seeking for.
	 * @return TRUE if match is found in the list.
	 */
	public static boolean isMatchingElement(List<WebElement> elems, String[] criterias){
		
		boolean isMatch = false;
		
		//getting all text data from List of web elements
		String result ="";
		for(WebElement e:elems){
			
			String str = e.getText();
			result = result+str;
		}
		//Asserting data from List of web elements with pattern defined in criterias.
		for (int i =0; i<criterias.length;i++){
			if (result.contains(criterias[i])){
				TraceOps.printMessage(LogLevel.TRACE, "  Matching element was found: %s", criterias[i]);
				isMatch=true;
				break;
			}
		}

		return isMatch;
	}

}
