package com.conceptapp.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

public class My {

	private My() {}

	public static String escapeHtml(String text) {
		if (text == null) return "";
		StringBuffer sb = new StringBuffer(text.length());
		int len = text.length();
		for (int i = 0; i < len; i++) {
			char c = text.charAt(i);
			if (c == '"')
				sb.append("&quot;");
			else if (c == '&')
				sb.append("&amp;");
			else if (c == '<')
				sb.append("&lt;");
			else if (c == '>')
				sb.append("&gt;");
			else
				sb.append(c);
		}
		return sb.toString();
	}

	public static String join(Iterable<? extends CharSequence> s, String delimiter) {
		int capacity = 0;
		int delimLength = delimiter.length();
		Iterator<? extends CharSequence> iter = s.iterator();
		if (iter.hasNext()) {
			capacity += iter.next().length() + delimLength;
		}
		StringBuilder buffer = new StringBuilder(capacity);
		iter = s.iterator();
		if (iter.hasNext()) {
			buffer.append(iter.next());
			while (iter.hasNext()) {
				buffer.append(delimiter);
				buffer.append(iter.next());
			}
		}
		return buffer.toString();
	}

	public static byte[] objectToBytes(Object object) {
		if (object == null) {
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
		} catch (IOException ex) {
			new Log(My.class.getName()).stackTrace(ex);
		}
		return baos.toByteArray();
	}

	public static Object bytesToObject(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		Object object = null;
		try {
			object = new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
		} catch (IOException ex) {
			new Log(My.class.getName()).stackTrace(ex);
		} catch (ClassNotFoundException ex) {
			new Log(My.class.getName()).stackTrace(ex);
		}
		return object;
	}

}
