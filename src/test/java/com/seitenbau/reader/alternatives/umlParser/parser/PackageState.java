package com.seitenbau.reader.alternatives.umlParser.parser;

import static com.seitenbau.reader.alternatives.umlParser.parser.XMIParser.state;

public class PackageState extends State {

	@Override
	public boolean processLine(String line) {

		if (line.contains("<UML:Model ")) {
			state = new ModelState();
			return XMIParser.USE_SAME_LINE;

		} else {
			return XMIParser.USE_NEW_LINE;
		}
	}

}
