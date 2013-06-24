package com.seitenbau.reader.eclipse;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
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

	public static void writePackage(Model modelObject) {
		if (modelObject == null) {
			System.out.println("Model is null.");
			return;
		}
		System.out.println("Model: " + modelObject.getName());
		System.out.println("------------");
		
		for (Package packageObject : modelObject.getNestedPackages()) {
			System.out.println("Package: " + packageObject.getName());

			// print entities in package.
			for (Element element : packageObject.getOwnedElements()) {
				if (element instanceof Class) {
					Class classObject = (Class) element;
					System.out.println("\tClass: " + classObject.getName());

					// print attributes of entities.
					for (Property property : classObject.getOwnedAttributes()) {
						Association associationObject = property.getAssociation();
						if (associationObject != null) {
							System.out.println("\t\tAttribute for Association: ");
							for (Property associationProperty : associationObject.getOwnedEnds()) {
								System.out.println("\t\t\tLower: " + associationProperty.getLower());
								System.out.println("\t\t\tUpper: " + associationProperty.getUpper());
								System.out.println("\t\t\tName: " + associationProperty.getName());
								System.out.println("\t\t\tSource: " + associationProperty.getType().getName());
							}
							System.out.println("\t\t\tTarget: " + property.getType().getName());
							
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
				} else if (element instanceof Association) {
					// this case is handled via attribute of a class element
				} else {
					System.err.println(element.toString());
				}
			}
		}
	}
}
