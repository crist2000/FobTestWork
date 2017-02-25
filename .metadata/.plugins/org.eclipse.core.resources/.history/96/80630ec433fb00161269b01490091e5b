package some.domain.utils;

import java.util.List;
import java.util.Random;

import some.domain.utils.TraceOps.LogLevel;

public class Helper {
	
	public static int calculateNonEmptyArgs(String... args){

		int count=0;
		
		for (String s:args){
			if (s!="")
				count++;
		}
		return count;
	}

	public static int getRandomIndex(List list){
		
		int elemCount = list.size();	
		Random random = new Random();
		int randomElem = random.nextInt(elemCount) + 1;
		TraceOps.printMessage(LogLevel.TRACE, "index %d has been randomly selected.", randomElem);
		
		return randomElem;
	}
}
