package com.seitenbau.reader.eclipse;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;

import org.eclipse.uml2.uml.Model;
import org.junit.Test;

import com.seitenbau.common.FileHelper;

public class XmiTransformerTest {

	@Test
	public void testTransform() throws FileNotFoundException {
		// transform Enterprise Architecture file
		File xmiFile = FileHelper
				.getXmiFile("src/test/resources/import/complex.xmi");
		File typesFile = FileHelper
				.getXmiFile("src/main/resources/import/ea_extension_primitivetypes.xmi");
		File transformedFile = XmiTransformer.transform(xmiFile, typesFile);
				
		Model model = XmiReader.readUmlModel(transformedFile);
				
		assertNotNull(model);
	}

}
