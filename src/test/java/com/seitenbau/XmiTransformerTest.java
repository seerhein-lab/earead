package com.seitenbau;

import java.io.File;
import java.io.FileNotFoundException;

import com.seitenbau.common.FileHelper;
import com.seitenbau.reader.eclipse.XmiTransformer;

/**
 * This class offers methods for transforming Enterprise Architect UML xmi files
 * into eclipse readable xmi files (EMF project).
 * 
 * @author nkunstek
 */
public abstract class XmiTransformerTest {
	
	public static void main(String args[]) throws FileNotFoundException {

		// String path = "src/test/resources/import/";
		String path = "src/test/java/com/seitenbau/";
		String sourceFile = path + "test.xmi";
		File xmiFile = FileHelper.getXmiFile(sourceFile);

		String typesFilePath = "src/main/resources/import/ea_extension_primitivetypes.xmi";
		File typesFile = FileHelper.getXmiFile(typesFilePath);

		File transformedFile = XmiTransformer.transform(xmiFile, typesFile);

		System.out.println(FileHelper.getContents(transformedFile));

	}

}
