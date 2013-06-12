package com.seitenbau.test;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.uml2.common.util.UML2Util;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Package;

public class Test {

	public static void main(String[] args) throws ClassNotFoundException,
			IOException {

		// ResourceSet resourceSet = ReaderHelper.getResourceSetXmi();

		ResourceSet resourceSet = ReaderHelper.getResourceSetUml();

		String path = "src/main/java/com/seitenbau/test/";
		//
		// String file = "test_UML_2_1.xmi";
		// System.out.println("File: " + file);
		// Model model = UML2Util.load(resourceSet, URI.createURI(path + file),
		// UMLPackage.Literals.MODEL);
		// ReaderHelper.writeModel(model);
		//
		// String file = "testAusdemNetz.xmi";
		// System.out.println("File: " + file);
		// Model model = UML2Util.load(resourceSet, URI.createURI(path + file),
		// UMLPackage.Literals.MODEL);
		// ReaderHelper.writeModel(model);

		String file = "test_UML_2_1.xmi";
		System.out.println("File: " + file);
		Package packageElement = UML2Util.load(resourceSet,
				URI.createURI(path + file), UMLPackage.Literals.PACKAGE);
		ReaderHelper.writePackage(packageElement);

		// file = "testAusdemNetz.xmi";
		// System.out.println("File: " + file);
		// packageElement = UML2Util.load(resourceSet, URI.createURI(path +
		// file),
		// UMLPackage.Literals.PACKAGE);
		// ReaderHelper.writePackage(packageElement);
	}

}
