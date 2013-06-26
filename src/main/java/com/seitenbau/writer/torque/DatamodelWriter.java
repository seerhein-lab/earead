package com.seitenbau.writer.torque;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.apache.torque.DatabaseType;

/**
 * This class offers methods for creating and writing xml schema files for
 * Apache Torque.
 * 
 * @author nkunstek
 */
public abstract class DatamodelWriter {

	/** The logger. */
	private static final Logger LOG = Logger.getLogger(DatamodelWriter.class);

	/**
	 * Writes the database content to the given xml file.
	 * 
	 * @param database
	 *            The database representing the model, not null.
	 * @param file
	 *            xml file, not null.
	 * @throws JAXBException
	 *             Error when creating XML file.
	 */
	public static void writeDatamodel(DatabaseType database, File file)
			throws JAXBException {
		String method = "writeDatamodel(): ";
		LOG.debug(method + "Start");

		// create JAXB context and instantiate marshaller
		JAXBContext context = JAXBContext.newInstance(DatabaseType.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		// Write to file
		m.marshal(database, file);

		LOG.debug(method + "End");
	}

}
