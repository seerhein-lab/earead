package com.seitenbau;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.torque.DatabaseType;
import org.eclipse.uml2.uml.Model;

import com.seitenbau.common.FileHelper;
import com.seitenbau.mapper.XMI2TorqueMapper;
import com.seitenbau.reader.eclipse.XMIReader;
import com.seitenbau.writer.torque.DatamodelWriter;

public class DatamodelCreatorTest {

	public static void main(String[] args) throws ClassNotFoundException,
			IOException, JAXBException {

		String path = "src/test/resources/import/";
		String sourceFile = path + "export_all_transformed.xmi";
		String targetFile = path + "torque-schema.xml";
		File resultFile = new File(targetFile);

		File xmiFile = FileHelper.getXmiFile(sourceFile);
		Model model = XMIReader.readUmlModel(xmiFile);
		DatabaseType database = XMI2TorqueMapper.mapUmlModel(model);
		DatamodelWriter.writeDatamodel(database, resultFile);
		System.out.println("\n" + FileHelper.getContents(resultFile));
	}
}
