package com.seitenbau;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.seitenbau.common.FileHelper;

/**
 * This class offers methods for reading UML models in xmi files using the
 * eclipse libraries ecore und uml from EMF project.
 * 
 * @author nkunstek
 */
public abstract class XmiTransformator {
	
	private final static String PRIMARY_KEYS = "thecustomprofile:Schlüsselattribut";
	private final static String PRIMARY_KEYS_ATTRIBUT = "base_Attribute";
	
	final static String ATTRIBUTES = "UML:Attribute";
	final static String METHODS = "UML:Operation";
	final static String CLASSES = "UML:Classifier.feature"; 
	final static String COUPLINGS = "UML:AssociationEnd";
	final static String CLASS_INHERITANCES = "UML:GeneralizableElement.generalization";

	public static void main(String args[]) {
		try {
			
			//String path = "src/test/resources/import/";
			String path = "src/test/java/com/seitenbau/";
			String sourceFile = path + "test.xmi";
			File xmiFile = FileHelper.getXmiFile(sourceFile);

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(xmiFile);


			Element rootElement = doc.getDocumentElement();
			rootElement.normalize();
			System.out.println("==============================");
			System.out.println("Root element: "
					+ rootElement.getNodeName());

			NodeList primaryKeys = doc.getElementsByTagName(PRIMARY_KEYS);
			System.out.println("Primary keys total: " + primaryKeys.getLength());
			
			for (int i = 0; i < primaryKeys.getLength(); i++) {

				Element primaryKeyElement = (Element) primaryKeys.item(i);

				String umlPropertyId = primaryKeyElement.getAttribute(PRIMARY_KEYS_ATTRIBUT);
				System.out.println("uml:Property xmi:id= "
						+ umlPropertyId);
				primaryKeyElement.setAttribute(PRIMARY_KEYS_ATTRIBUT, umlPropertyId + "+++");
				
//				Element primaryKey = doc.getElementById(umlPropertyId);

//				System.out.println("uml:Property= "
//						+ primaryKey.toString());

			}

		} catch (SAXParseException err) {
			System.out.println("** Parsing error" + ", line "
					+ err.getLineNumber() + ", uri " + err.getSystemId());
			System.out.println(" " + err.getMessage());

		} catch (SAXException e) {
			Exception x = e.getException();
			((x == null) ? e : x).printStackTrace();

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
