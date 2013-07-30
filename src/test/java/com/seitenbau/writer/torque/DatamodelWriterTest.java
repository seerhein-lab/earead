package com.seitenbau.writer.torque;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import org.apache.torque.DatabaseType;
import org.eclipse.uml2.uml.Model;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.seitenbau.common.FileHelper;
import com.seitenbau.mapper.XMI2TorqueMapper;
import com.seitenbau.reader.eclipse.XmiReader;
import com.seitenbau.reader.eclipse.XmiTransformer;

public class DatamodelWriterTest {
	
	private static File file;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		String filePath = "src/test/resources/import/writerTest.xml";
		file = new File(filePath);
	}

	@Test
	public void testWriteDatamodel() throws FileNotFoundException, JAXBException {

		// transform Enterprise Architecture file
		File xmiFile = FileHelper
				.getXmiFile("src/test/resources/import/complex.xmi");
		File typesFile = FileHelper
				.getXmiFile("src/main/resources/import/ea_extension_primitivetypes.xmi");
		File transformedFile = XmiTransformer.transform(xmiFile, typesFile);
		System.out.println("\n" + FileHelper.getContents(transformedFile));

		// read Enterprise Architecture file
		Model model = XmiReader.readUmlModel(transformedFile);

		// map Enterprise Architecture file to Torque file
		DatabaseType database = XMI2TorqueMapper.mapUmlModel(model);
		
		DatamodelWriter.writeDatamodel(database, transformedFile);
		
		assertNotNull(database);
	}
	
	@AfterClass
	public static void tearDownAfterClass() {
		file.delete();
	}

}
