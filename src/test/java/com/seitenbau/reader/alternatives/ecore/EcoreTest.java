package com.seitenbau.reader.alternatives.ecore;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

public class EcoreTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put( 
				"library", new EcoreResourceFactoryImpl());

		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put( 
				Resource.Factory.Registry.DEFAULT_EXTENSION, 
				new XMIResourceFactoryImpl());

		ResourceSet resourceSet = new ResourceSetImpl();
		
		URI fileURI = URI.createFileURI("src/test/java/com/seitenbau/reader/alternatives/ecore/export14_12.ecore");
		
		//get resource from ecore file.
		Resource resource = resourceSet.getResource(fileURI, true);
		
		EList<EObject> contents = resource.getContents();
		
		//print packages in resource.
		for (EObject pkge : contents) {
			if (pkge instanceof EPackage) {
				System.out.println("Package: " + ((EPackage) pkge).getName());
				
				//print entities in package.
				for (EObject clss : pkge.eContents()) {
					if (clss instanceof EClass) {
						System.out.println("\tClass: " + ((EClass) clss).getName());
						
						//print attributes of entities.
						for (EAttribute attr : ((EClass) clss).getEAllAttributes()) {
							if (attr.getEType() != null) {
								System.out.println("\t\tKey: " + attr.getName());
							} else {
								System.out.println("\t\tAttribute: " + attr.getName());
							}
						}

						//print references between entities.
						for (EReference ref : ((EClass) clss).getEAllReferences()) {
							System.out.println("\t\tOpposite Entity: " + ref.getEReferenceType().getName());
						}

					} else {
						System.err.println("Class error!");
					}
				}
			} else {
				System.err.println("Package error!");
			}
		}
	}

}
