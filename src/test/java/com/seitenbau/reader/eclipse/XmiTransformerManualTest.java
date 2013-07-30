package com.seitenbau.reader.eclipse;

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
public abstract class XmiTransformerManualTest {
	
	public static void main(String args[]) throws FileNotFoundException {
		
		String sourceFile = "src/test/resources/import/export.xmi";
		File xmiFile = FileHelper.getXmiFile(sourceFile);
		String typesFilePath = "src/main/resources/import/ea_extension_primitivetypes.xmi";
		File typesFile = FileHelper.getXmiFile(typesFilePath);

		File transformedFile = XmiTransformer.transform(xmiFile, typesFile);

		System.out.println(FileHelper.getContents(transformedFile));

	}

}
