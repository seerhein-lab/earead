package com.seitenbau.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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

		if (filePath == null) {
			String msg = "File should not be null.";
			LOG.error(method + msg);
			throw new IllegalArgumentException(msg);
		}

		File file = new File(filePath);
		if (!file.exists()) {
			String msg = "File does not exists: " + file;
			LOG.error(method + msg);
			throw new FileNotFoundException(msg);
		}
		if (!file.isFile()) {
			String msg = "Should not be a directory: " + file;
			LOG.error(method + msg);
			throw new IllegalArgumentException(msg);
		}

		LOG.debug(method + "End");
		return file;
	}

	/**
	 * Returns the xmi file of the given relative file path name.
	 * 
	 * @param filePath
	 *            the relative file path name, not null.
	 * @return the file object.
	 * @throws FileNotFoundException
	 *             exception when file cannot be found.
	 */
	public static File getXmiFile(String filePath) throws FileNotFoundException {
		String method = "getXmiFile(): ";
		LOG.debug(method + "Start");

		File file = getFile(filePath);
		if (!filePath.endsWith(".xmi")) {
			String msg = "File has no xmi extension: " + filePath;
			LOG.error(method + msg);
			throw new FileNotFoundException(msg);
		}

		LOG.debug(method + "End");
		return file;
	}

	/**
	 * Fetch the entire contents of a file, and return it in a String. This
	 * style of implementation does not throw Exceptions to the caller.
	 * 
	 * @param file
	 *            is a file which already exists and can be read.
	 */
	public static String getContents(File file) {
		String method = "getContents(): ";
		LOG.debug(method + "Start");

		StringBuilder contents = new StringBuilder();

		try {
			// use buffering reading one line at a time
			BufferedReader input = new BufferedReader(new FileReader(file));
			try {
				String line = null;
				while ((line = input.readLine()) != null) {
					contents.append(line);
					contents.append(System.getProperty("line.separator"));
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			LOG.debug(method + ex.getMessage());
		}

		LOG.debug(method + "End");
		return contents.toString();
	}
}
