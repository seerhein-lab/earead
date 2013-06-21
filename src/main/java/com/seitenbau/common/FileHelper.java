package com.seitenbau.common;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.log4j.Logger;

/**
 * Helper class for reading and writing files.
 * 
 * @author nkunstek
 */
public abstract class FileHelper {

	/** The logger. */
	private static final Logger LOG = Logger.getLogger(FileHelper.class);

	/**
	 * Returns the file of the given relative file path name.
	 * 
	 * @param filePath
	 *            the relative file path name.
	 * @return the file object.
	 * @throws FileNotFoundException
	 *             exception when file cannot be found.
	 */
	public static File getFile(String filePath) throws FileNotFoundException {
		String method = "getFile(): ";
		LOG.debug(method + "Start");

		File file = new File(filePath);
		if (!file.exists()) {
			String msg = "File does not exists: " + filePath;
			LOG.error(method + msg);
			throw new FileNotFoundException(msg);
		}

		LOG.debug(method + "End");
		return file;
	}

	/**
	 * Returns the xmi file of the given relative file path name.
	 * 
	 * @param filePath
	 *            the relative file path name.
	 * @return the file object.
	 * @throws FileNotFoundException
	 *             exception when file cannot be found.
	 */
	public static File getXmiFile(String filePath) throws FileNotFoundException {
		String method = "getXmiFile(): ";
		LOG.debug(method + "Start");

		if (!filePath.endsWith(".xmi")) {
			String msg = "File has no xmi extension: " + filePath;
			LOG.error(method + msg);
			throw new FileNotFoundException(msg);
		}

		File file = getFile(filePath);

		LOG.debug(method + "End");
		return file;
	}
}
