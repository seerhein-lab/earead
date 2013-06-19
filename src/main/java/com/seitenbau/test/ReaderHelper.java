package com.seitenbau.test;

import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl;
import org.eclipse.uml2.uml.resource.XMI2UMLResource;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * Helper class for reading and writing files.
 * 
 * @author nkunstek
 */
public abstract class ReaderHelper {

	/** The logger. */
	private static final Logger LOG = Logger.getLogger(ReaderHelper.class);

	public static ResourceSet getResourceSetXmi() {

		ResourceSet resourceSet = getResourceSet();

		Map<String, Object> extensionToFactoryMap = resourceSet
				.getResourceFactoryRegistry().getExtensionToFactoryMap();
		extensionToFactoryMap.put(XMI2UMLResource.FILE_EXTENSION,
				new XMIResourceFactoryImpl());
		// read also xml files additionally to xmi files
		// extensionToFactoryMap.put("xml", new XMIResourceFactoryImpl());

		return resourceSet;
	}

	public static ResourceSet getResourceSetUml() {

		ResourceSet resourceSet = getResourceSet();

		Map<String, Object> extensionToFactoryMap = resourceSet
				.getResourceFactoryRegistry().getExtensionToFactoryMap();
		extensionToFactoryMap.put(XMI2UMLResource.FILE_EXTENSION,
				new UMLResourceFactoryImpl());
		// read also xml files additionally to xmi files
		// extensionToFactoryMap.put("xml", new XMIResourceFactoryImpl());

		return resourceSet;
	}

	private static ResourceSet getResourceSet() {

		ResourceSet resourceSet = new ResourceSetImpl();
		Registry packageRegistry = resourceSet.getPackageRegistry();
		packageRegistry.put("http://schema.omg.org/spec/XMI/2.1",
				UMLPackage.eINSTANCE);
		packageRegistry.put("http://schema.omg.org/spec/UML/2.1",
				UMLPackage.eINSTANCE);

		resourceSet.getLoadOptions().put(
				XMLResource.OPTION_RECORD_UNKNOWN_FEATURE, Boolean.TRUE);

		return resourceSet;
	}

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

	public static void writePackage(Package packageElement) {
		if (packageElement == null) {
			System.out.println("Package is null.");
			return;
		}
		System.out.println("Package: " + packageElement.getName());
				Package package1 = packageElement.getNestedPackage("MKO_Bookstore");
				System.out.println("Package: " + package1.getName());

				// print entities in package.
				for (EObject clss : package1.eContents()) {
					if (clss instanceof Class) {
						Class class1 = (Class) clss;
						System.out.println("\tClass: " + class1.getName());

						// print attributes of entities.
						for (Property attr : class1.getAllAttributes()) {
							if (attr.getType() != null) {
								System.out
										.println("\t\tKey: " + attr.getName());
							} else {
								String attribute = "\t\tAttribute: "
										+ attr.getName();
								if ("ATT_Name".equals(attr.getName())) {
									if (attr.getType() != null) {
										attribute += ":"
												+ attr.getType().getName();
									}
								}
								System.out.println(attribute);
							}
						}

						// print references between entities.
//						for (EReference ref : ((EClass) clss)
//								.getEAllReferences()) {
//							System.out.println("\t\tOpposite Entity: "
//									+ ref.getEReferenceType().getName());
//						}

					} else {
						System.err.println("Class error!");
					}
				}

		
	}
}
