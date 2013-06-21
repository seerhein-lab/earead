package com.seitenbau.reader.alternatives.umlParser.parser;

import static com.seitenbau.reader.alternatives.umlParser.parser.XMIParser.USE_NEW_LINE;
import static com.seitenbau.reader.alternatives.umlParser.parser.XMIParser.USE_SAME_LINE;
import static com.seitenbau.reader.alternatives.umlParser.parser.XMIParser.state;


public class AssociationState extends State {

	@Override
	public boolean processLine(String line) {

		if (line.contains("</UML:Association>")) {
			state = new ModelState();
			return USE_SAME_LINE;

		} else {
			return USE_NEW_LINE;
		}
	}

}
