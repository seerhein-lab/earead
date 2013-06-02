package com.seitenbau.earead.alternatives.umlParser.model;

import java.util.HashMap;
import java.util.Map;

public class Model {

	private String name;
	private Map<String, Class> classes;
	
	public Model(String name) {
		this.name = name;
		this.classes = new HashMap<String, Class>();
	}
	
	public void addClassByName(Class class_) {
		this.classes.put(class_.getName(), class_);
	}
	
	public Class getClassByName(String classname) {
		return this.classes.get(classname);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append("\n");
		
		for (Map.Entry<String, Class> entry : this.classes.entrySet()) {
			sb.append(entry.getValue().toString());
		}

		return sb.toString();
	}
}
