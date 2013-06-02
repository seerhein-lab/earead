package com.seitenbau.earead.alternatives.umlParser.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class State {
	
	private static String actualClassName;

	public abstract boolean processLine(String line);
	
	protected String extractName(String line) {
		Pattern pattern = Pattern.compile(".*name=\"(.*?)\".*");
		Matcher matcher = pattern.matcher(line);

		if (matcher.matches()) {
			return matcher.group(1);
		} else {
			return null;
		}
	}
	
	protected static String getActualClassName() {
		return actualClassName;
	}
	
	protected static void setActualClassName(String name) {
		actualClassName = name;
	}

}
