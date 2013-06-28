package com.seitenbau;

import java.io.File;

import org.apache.torque.DatabaseType;
import org.eclipse.uml2.uml.Model;

import com.seitenbau.common.FileHelper;
import com.seitenbau.mapper.XMI2TorqueMapper;
import com.seitenbau.reader.eclipse.XmiReader;
import com.seitenbau.reader.eclipse.XmiTransformer;
import com.seitenbau.writer.torque.DatamodelWriter;

public class DatamodelCreatorTest {

	public static void main(String[] args) throws Exception {

		// transform Enterprise Architecture file
		File xmiFile = FileHelper
				.getXmiFile("src/test/resources/import/export.xmi");
		File typesFile = FileHelper
				.getXmiFile("src/main/resources/import/ea_extension_primitivetypes.xmi");
		File transformedFile = XmiTransformer.transform(xmiFile, typesFile);
		System.out.println("\n" + FileHelper.getContents(transformedFile));

		// read Enterprise Architecture file
		Model model = XmiReader.readUmlModel(transformedFile);

		// map Enterprise Architecture file to Torque file
		DatabaseType database = XMI2TorqueMapper.mapUmlModel(model);

		// write Torque file
		File resultFile = new File(
				"target/torque-schema.xml");
		DatamodelWriter.writeDatamodel(database, resultFile);
		System.out.println("\n" + FileHelper.getContents(resultFile));
	}
}
