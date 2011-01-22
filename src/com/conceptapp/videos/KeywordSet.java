package com.conceptapp.videos;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.regex.Pattern;

@SuppressWarnings("serial")
public class KeywordSet extends LinkedHashSet<String> {

	private static String[] notKeywords = {
		"a",
		"an",
		"and",
		"are",
		"as",
		"clip",
		"feat",
		"featuring",
		"ft",
		"is",
		"or",
		"that",
		"the",
		"these",
		"this",
		"those",
		"video",
		"videoclip",
		"vs"
	};

	private static String[] keywordReplacements = {
		"zero",
		"0",
		"one",
		"1",
		"two",
		"2",
		"three",
		"3",
		"four",
		"4",
		"five",
		"5",
		"six",
		"6",
		"seven",
		"7",
		"eight",
		"8",
		"nine",
		"9",
		"for",
		"4",
		"to",
		"2",
		"too",
		"2",
		"you",
		"u",
		"version",
		"ver"
	};

	public KeywordSet(String title) {
		super();
		title = title.toLowerCase();
		// Remove '
		title = Pattern.compile("'(s|ve|m|re|ll|d)", Pattern.CASE_INSENSITIVE).matcher(title).replaceAll("");
		title = title.replaceAll("'", "");
		title = title.replaceAll("(\\d)\\W*(st|nd|th)", "$1");
		// Split
		String[] tokens = title.split("[\\p{Cntrl}\\p{Punct}\\p{Space}]+");
		// Get keywords joining single characters into words and removing
		// duplicates
		String word = "";
		boolean isNumber = false;
		for (String token : tokens) {
			if (token.length() > 0) {
				if (token.length() == 1) {
					char c = token.charAt(0);
					if (c >= '0' && c <= '9') {
						// Numeric
						if (!isNumber) {
							isNumber = true;
							if (word.length() > 0) {
								add(word);
								word = "";
							}
						}
					} else {
						// String
						if (isNumber) {
							isNumber = false;
							if (word.length() > 0) {
								add(word);
								word = "";
							}
						}
					}
					word += token;
				} else {
					if (word.length() > 0) {
						add(word);
						word = "";
					}
					add(token);
				}
			}
		}
		if (word.length() > 0) {
			add(word);
		}
	}

	public boolean add(String keyword) {
		for (String notKeyword : notKeywords) {
			if (keyword.equals(notKeyword)) {
				return false;
			}
		}
		for (int i = 0; i < keywordReplacements.length; i+=2) {
			if (keyword.equals(keywordReplacements[i])) {
				keyword = keywordReplacements[i + 1];
				break;
			}
		}
		return super.add(keyword);
	}

	public String join(String delimiter) {
		if (isEmpty()) {
			return "";
		}
		Iterator<String> iter = iterator();
		StringBuffer buffer = new StringBuffer(iter.next());
		while (iter.hasNext()) {
			buffer.append(delimiter).append(iter.next());
		}
		return buffer.toString();
	}

}
