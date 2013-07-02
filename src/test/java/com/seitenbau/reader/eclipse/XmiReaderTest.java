package com.seitenbau.reader.eclipse;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

import com.seitenbau.common.FileHelper;


public class XmiReaderTest {

	public static void main(String[] args) throws ClassNotFoundException,
			IOException {

		String filePath = "src/test/resources/import/export_transformed.xmi";
		//String filePath = "src/test/resources/import/associationTest_all_transformed.xmi";
		File xmiFile = FileHelper.getXmiFile(filePath);

		Model model = XmiReader.readUmlModel(xmiFile);
		writeModel(model);
	}
	
	private static void writeModel(Model modelObject) {
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
							attribute += " (" + property.isID() + ")";
							System.out.println(attribute);
						}
					}
				} else if (element instanceof Association) {
					// this case is handled via attribute of a class element
					Association associationObject = (Association) element;
					
					//General information about association
					System.out.println("\tAssociation:");
					EList<Property> ownedEnds = associationObject.getOwnedEnds();
					if (ownedEnds.size() > 0) {
						System.out.println("\t\tName: " + associationObject.getOwnedEnds().get(0).getName());
					}
					
					EList<Property> memberEnds = associationObject.getMemberEnds();
					EList<Type> endTypes = associationObject.getEndTypes();
					
					if (memberEnds.size() > 0) {
						//get source type and properties
						Type sourceType = endTypes.get(0);
						Property source = memberEnds.get(0);
						
						//Source properties
						System.out.println("\t\tSource:");
						System.out.println("\t\t\tSource: " + sourceType.getName());
						System.out.println("\t\t\tSource Lower: " + source.getLower());
						System.out.println("\t\t\tSource Upper: " + source.getUpper());
					}
					
					if (memberEnds.size() > 0) {
						//get target type and properties
						Type targetType = endTypes.get(1);
						Property targetValues = memberEnds.get(0).getOtherEnd();

						//Target properties
						System.out.println("\t\tTarget:");
						System.out.println("\t\t\tTarget: " + targetType.getName());
						System.out.println("\t\t\tTarget Lower: " + targetValues.getLower());
						System.out.println("\t\t\tTarget Upper: " + targetValues.getUpper());
					}

				} else {
					System.err.println(element.toString());
				}
			}
		}
	}

}
