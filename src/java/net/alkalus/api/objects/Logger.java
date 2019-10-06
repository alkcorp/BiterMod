package net.alkalus.api.objects;

import org.apache.logging.log4j.LogManager;

public class Logger {

	public Logger(String string) {

	}

	// Logging Functions
	public static final org.apache.logging.log4j.Logger modLogger = Logger.makeLogger();

	// Generate GT++ Logger
	public static org.apache.logging.log4j.Logger makeLogger() {
		final org.apache.logging.log4j.Logger gtPlusPlusLogger = LogManager.getLogger("BiterMod");
		return gtPlusPlusLogger;
	}

	private static final boolean enabled = true;

	public static final org.apache.logging.log4j.Logger getLogger(){
		return modLogger;
	}

	// Non-Dev Comments
	public static void INFO(final String s) {
		if (enabled) {
			modLogger.info(s);
		}
	}


	// Developer Comments
	public static void WARNING(final String s) {
		if (enabled) {
			modLogger.warn(s);			
		}
	}

	// Errors
	public static void ERROR(final String s) {
		if (enabled) {
				modLogger.fatal(s);
		}
	}

	public static void REFLECTION(String string) {
		WARNING(string);	
	}

}
