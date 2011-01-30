package com.conceptapp.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Logger;

public class Log {

	private Logger log;

	public Log(String name) {
		log = Logger.getLogger(name);
	}

	public void error(String err) {
		log.severe(err);
	}

	public void stackTrace(Throwable ex) {
		final Writer result = new StringWriter();
		final PrintWriter pw = new PrintWriter(result);
		ex.printStackTrace(pw);
		log.severe(result.toString());
	}

}
