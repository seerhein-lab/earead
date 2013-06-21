package com.seitenbau.reader.alternatives.umlParser.model;

import java.util.HashMap;
import java.util.Map;

public class Class {

	private String name;
	private Map<String, String> attributes;

	public Class(String name) {
		this.name = name;
		this.attributes = new HashMap<String, String>();
	}

	public String getName() {
		return this.name;
	}

	public void addAttribute(String name, String value) {
		this.attributes.put(name, value);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\t").append(name).append("\n");

		for (Map.Entry<String, String> entry : this.attributes.entrySet()) {
			sb.append("\t\t").append(entry.getKey()).append("\n");
		}

		return sb.toString();
	}
}
