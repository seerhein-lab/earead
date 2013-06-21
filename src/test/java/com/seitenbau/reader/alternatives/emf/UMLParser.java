package com.seitenbau.reader.alternatives.emf;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

public class UMLParser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		URI uri = URI
				.createURI("src/test/java/com/seitenbau/reader/alternatives/emf/export14_12.xmi");
		UMLModel.init();
		Resource res = UMLModel.load(uri);

		for (EObject e : res.getContents()) {
			System.out.println(e);
		}

	}

}
