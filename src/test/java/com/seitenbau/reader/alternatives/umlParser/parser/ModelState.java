package com.seitenbau.reader.alternatives.umlParser.parser;

import static com.seitenbau.reader.alternatives.umlParser.parser.XMIParser.USE_NEW_LINE;
import static com.seitenbau.reader.alternatives.umlParser.parser.XMIParser.USE_SAME_LINE;
import static com.seitenbau.reader.alternatives.umlParser.parser.XMIParser.model;
import static com.seitenbau.reader.alternatives.umlParser.parser.XMIParser.state;

import com.seitenbau.reader.alternatives.umlParser.model.Model;

public class ModelState extends State {

	@Override
	public boolean processLine(String line) {

		if (line.contains("<UML:Model ")) {
			model = new Model(this.extractName(line));
			return USE_NEW_LINE;

		} else if (line.contains("</UML:Model>")) {
			state = new PackageState();
			return USE_SAME_LINE;

		} else if (line.contains("<UML:Class ")) {
			state = new ClassState();
			return USE_SAME_LINE;

		} else if (line.contains("<UML:Association ")) {
			state = new AssociationState();
			return USE_SAME_LINE;

		} else {
			return USE_NEW_LINE;
		}

	}

}
