package com.seitenbau;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.torque.ColumnType;
import org.apache.torque.DatabaseType;
import org.apache.torque.ForeignKeyType;
import org.apache.torque.IdMethodType;
import org.apache.torque.ReferenceType;
import org.apache.torque.SqlDataType;
import org.apache.torque.TableType;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

import com.seitenbau.common.FileHelper;
import com.seitenbau.reader.eclipse.XMIReader;
import com.seitenbau.writer.torque.DatamodelWriter;

public class DatamodelCreator {

	public static void main(String[] args) throws ClassNotFoundException,
			IOException, JAXBException {

		String path = "src/test/resources/import/";
		String sourceFile = path + "export_all_transformed.xmi";
		String targetFile = path + "torque-schema.xml";
		File resultFile = new File(targetFile);
		

		File xmiFile = FileHelper.getXmiFile(sourceFile);

		Model model = XMIReader.readUmlModel(xmiFile);

		DatabaseType database = mapModel(model);

		DatamodelWriter.writeDatamodel(database, resultFile);

		System.out.println(FileHelper.getContents(resultFile));
	}

	private static DatabaseType mapModel(Model modelObject)
			throws JAXBException, IOException {

		DatabaseType database = new DatabaseType();
		database.setDefaultIdMethod(IdMethodType.NATIVE);

		List<TableType> tableList = new ArrayList<TableType>();

		if (modelObject == null) {
			System.out.println("Model is null.");
			return database;
		}

		for (Package packageObject : modelObject.getNestedPackages()) {
			database.setName(packageObject.getName());

			// print entities in package.
			for (Element element : packageObject.getOwnedElements()) {
				if (element instanceof Class) {
					Class classObject = (Class) element;

					TableType table = new TableType();
					table.setName(classObject.getName());
					table.setDescription("Description:" + classObject.getName());

					List<ColumnType> columnList = new ArrayList<ColumnType>();
					List<Serializable> foreignKeyList = new ArrayList<Serializable>();

					// print attributes of entities.
					for (Property property : classObject.getOwnedAttributes()) {

						Association associationObject = property
								.getAssociation();
						if (associationObject != null) {
							ForeignKeyType foreignKey = new ForeignKeyType();
							foreignKey.setForeignTable(property.getType()
									.getName());
							List<ReferenceType> referenceTypeList = new ArrayList<ReferenceType>();

							for (Property associationProperty : associationObject
									.getOwnedEnds()) {
								// TODO
								// System.out.println("\t\t\tLower: "
								// + associationProperty.getLower());
								// System.out.println("\t\t\tUpper: "
								// + associationProperty.getUpper());

								ReferenceType referenceType = new ReferenceType();
								// TODO
								referenceType.setForeign(property.getType()
										.getName());
								// TODO
								referenceType.setLocal(associationProperty
										.getType().getName());
								referenceTypeList.add(referenceType);
								break;
							}

							foreignKey.setReference(referenceTypeList);
							foreignKeyList.add(foreignKey);

						} else {

							ColumnType column = new ColumnType();
							column.setName(property.getName());
							if (property.getLower() == 1) {
								column.setRequired(Boolean.TRUE);
							} else {
								column.setRequired(Boolean.FALSE);
							}
							column.setPrimaryKey(property.isID());
							Type type = property.getType();
							if (type != null) {
								if (type.getName().equals("int")) {
									column.setType(SqlDataType.INTEGER);
								} else {
									// TODO
									column.setType(SqlDataType.VARCHAR);
								}
							}
							column.setDescription("Description:"
									+ property.getName());
							columnList.add(column);
						}

					}

					table.setColumn(columnList);
					table.setForeignKeyOrIndexOrUnique(foreignKeyList);
					tableList.add(table);
				} else if (element instanceof Association) {
					// this case is handled via attribute of a class element
				} else {
					System.err.println(element.toString());
				}
			}
		}

		database.setTable(tableList);
		return database;
	}
}
