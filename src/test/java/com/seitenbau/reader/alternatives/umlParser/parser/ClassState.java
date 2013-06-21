package com.seitenbau.reader.alternatives.umlParser.parser;

import static com.seitenbau.reader.alternatives.umlParser.parser.XMIParser.USE_NEW_LINE;
import static com.seitenbau.reader.alternatives.umlParser.parser.XMIParser.USE_SAME_LINE;
import static com.seitenbau.reader.alternatives.umlParser.parser.XMIParser.model;
import static com.seitenbau.reader.alternatives.umlParser.parser.XMIParser.state;

import com.seitenbau.reader.alternatives.umlParser.model.Class;

public class ClassState extends State {

	@Override
	public boolean processLine(String line) {

		if (line.contains("<UML:Class ")) {
			State.setActualClassName(this.extractName(line));
			model.addClassByName(new Class(State.getActualClassName()));
			return USE_NEW_LINE;

		} else if (line.contains("</UML:Class>")) {
			state = new ModelState();
			return USE_SAME_LINE;

		} else if (line.contains("<UML:Attribute ")) {
			state = new AttributeState();
			return USE_SAME_LINE;

		} else {
			return USE_NEW_LINE;
		}
	}

}
