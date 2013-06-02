package com.seitenbau.earead.alternatives.umlParser.parser;

import static com.seitenbau.earead.alternatives.umlParser.parser.XMIParser.USE_NEW_LINE;
import static com.seitenbau.earead.alternatives.umlParser.parser.XMIParser.USE_SAME_LINE;
import static com.seitenbau.earead.alternatives.umlParser.parser.XMIParser.state;
import static com.seitenbau.earead.alternatives.umlParser.parser.XMIParser.model;

public class AttributeState extends State{

	@Override
	public boolean processLine(String line) {

		if (line.contains("<UML:Attribute ")) {
			model.getClassByName(State.getActualClassName()).addAttribute(this.extractName(line), "TEST");
			return USE_NEW_LINE;

		} else if (line.contains("</UML:Attribute>")) {
			state = new ClassState();
			return USE_SAME_LINE;

		} else {
			return USE_NEW_LINE;
		}
	}

}
