package some.domain.utils;

import some.domain.utils.TraceOps.LogLevel;;

public class TraceOps{
	
	public static void printMessage (String loglevel, String message, Object... args) throws Exception{
		
		switch (loglevel){
		case LogLevel.WARN: System.out.println(ASNICode.ANSI_BLUE + String.format(message, args)+ASNICode.ANSI_RESET); break;
		case LogLevel.INFO: System.out.println(ASNICode.ANSI_BLACK + String.format(message, args)+ASNICode.ANSI_RESET); break;
		case LogLevel.ERROR: System.out.println(ASNICode.ANSI_RED + String.format(message, args)+ASNICode.ANSI_RESET); break;
		case LogLevel.TRACE: System.out.println(ASNICode.ANSI_GREY + String.format(message, args)+ASNICode.ANSI_RESET); break;
		case LogLevel.PASSED: System.out.println(ASNICode.ANSI_GREEN + String.format(message, args)+ASNICode.ANSI_RESET); break;
		default: throw new Exception("Unregonized log level is provided.");
		}
	}

	public static class LogLevel{
		public static final String WARN = "Warning";
		public static final String INFO = "Information";
		public static final String ERROR = "Error";
		public static final String TRACE = "Trace";
		public static final String PASSED = "Passed";
	}
	private static class ASNICode {	 
		final static String ANSI_RESET = "\u001B[0m";
		final static String ANSI_GREY = "\u001B[30;1m";
		final static String ANSI_BLACK = "\u001B[30m";
		final static String ANSI_RED = "\u001B[31;1m";
		final static String ANSI_GREEN = "\u001B[32;1m";
		final static String ANSI_BLUE = "\u001B[34;1m";
	}
}
