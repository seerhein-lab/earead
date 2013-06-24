package com.seitenbau.reader.eclipse;

import java.io.IOException;

import org.eclipse.uml2.uml.Model;


public class XmiReaderTest {

	public static void main(String[] args) throws ClassNotFoundException,
			IOException {

		String filePath = "src/test/resources/import/export_transformed.xmi";
		Model model = XMIReader.readUmlModel(filePath);
		
		WriterHelper.writeModel(model);
	}

}
