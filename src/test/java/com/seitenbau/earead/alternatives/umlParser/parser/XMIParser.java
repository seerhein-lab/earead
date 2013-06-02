package com.seitenbau.earead.alternatives.umlParser.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.seitenbau.earead.alternatives.umlParser.model.Model;

public class XMIParser {

	static State state;
	static final boolean USE_SAME_LINE = false;
	static final boolean USE_NEW_LINE = true;
	
	static Model model;

	public static void main(final String[] args) throws IOException {

		state = new PackageState();
		BufferedReader in = loadXMI("src/test/java/com/seitenbau/earead/alternatives/umlParser/export14_12.xmi");

		String line = null;

		while ((line = in.readLine()) != null) {
			while (!state.processLine(line)) {
			}
		}
		
		System.out.println(model);
		
		in.close();
	}

	private static BufferedReader loadXMI(String path) {
		BufferedReader in = null;

		try {

			in = new BufferedReader(new FileReader(path));

		} catch (FileNotFoundException e) {
			System.err.println("Load File failed!");
		}

		return in;
	}

}
