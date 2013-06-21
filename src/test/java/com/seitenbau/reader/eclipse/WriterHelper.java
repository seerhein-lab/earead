package com.seitenbau.reader.eclipse;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

/**
 * Helper class for reading and writing files.
 * 
 * @author nkunstek
 */
public abstract class WriterHelper {

	public static void writeModel(Model model) {

		if (model == null) {
			System.out.println("Model is null");
			return;
		}
		System.out.println("\tModel: " + model.getName());
		for (PackageableElement packageElement : model.getPackagedElements()) {
			writePackageableElement(packageElement);
		}
		System.out.println("");
	}

	private static void writePackageableElement(
			PackageableElement packageElement) {
		if (packageElement == null) {
			System.out.println("Package is null.");
			return;
		}
		System.out.println("\t\tPackage: " + packageElement.getName());
		for (Element element : packageElement.getOwnedElements()) {
			if (element instanceof PackageableElement) {
				writePackageableElement((PackageableElement) element);
			} else {
				System.out.println("\t\tElement: " + element.toString());
			}
		}
	}

	public static void writePackage(Model model) {
		if (model == null) {
			System.out.println("Model is null.");
			return;
		}
		System.out.println("Model: " + model.getName());
		System.out.println("------------");

		// TODO
		for (Package modelPackage : model.getNestedPackages()) {
			System.out.println("\nPackage: " + modelPackage.getName());

			// print entities in package.
			for (Element element : modelPackage.getOwnedElements()) {
				if (element instanceof Class) {
					Class entity = (Class) element;
					System.out.println("\tClass: " + entity.getName());

					// print attributes of entities.
					for (Property property : entity.getAllAttributes()) {
						if (property.getAssociation() != null) {
							Association association = property.getAssociation();
							System.out.println("\t\tAttribute for Association: ");
							System.out.println("\t\t\tTarget: " + property.getType().getName());
							


							for (Property test : association.getOwnedEnds()) {
								System.out.println(test.getLower());
								System.out.println(test.getUpper());
								System.out.println("\t\t\tSource: " + test.getType().getName());
								System.out.println("\t\t\tName: " + test.getName());
							}
							
						} else {
							String attribute = "\t\tAttribute: "
									+ property.getName();
							Type type = property.getType();
							if (type != null) {
								attribute += ":" + type.getName();
							}
							System.out.println(attribute);
						}
					}
				} else if (element instanceof Class) {
					// this case is handled via attribute of a class element
				} else {
					System.err.println(element.toString());
				}
			}
		}

	}
}
