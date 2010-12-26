package com.conceptapp.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class IsoDateFormat {

	private IsoDateFormat() {}

	private final static DateFormat dateFormat;

	static {
		try {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw (Error) new InstantiationError().initCause(e);
		}
	}

	public static synchronized String format(Date date) {
		if (date == null) {
			return "null";
		}
		String s = dateFormat.format(date);
		if (s.length() > 22) {
			char c = s.charAt(19);
			if (c == '-' || c == '+') {
				s = s.substring(0, 22) + ":" + s.substring(22, 24);
			}
		}
		return s;
	}

	/**Parses text from the beginning of the given string to produce a date.
	 * Parsing may or may not be {@link DateFormat#setLenient(boolean) lenient}
	 *
	 * @param string  the String representation of a date.
	 * @param lenient whether date/time parsing is to be lenient.
	 * @return a {@link Date}.
	 * #see @link DateFormat#parse(String)
	 */
	public static synchronized Date parse(String string,boolean lenient) throws ParseException {
		dateFormat.setLenient(lenient);
		string=string.trim();
		if (string.length() > 22) {
			char c = string.charAt(19);
			if (c == '-' || c == '+') {
				string = string.substring(0,22) + string.substring(23);
			}
		}
		return dateFormat.parse(string);
	}

}