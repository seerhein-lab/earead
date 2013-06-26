package com.seitenbau.mapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
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

/**
 * This class maps a UML model (using the eclipse libraries ecore und uml from
 * EMF project) to the Apache Torque schema object.
 * 
 * @author nkunstek
 */
public abstract class XMI2TorqueMapper {

	/** The logger. */
	private static final Logger LOG = Logger.getLogger(XMI2TorqueMapper.class);

	/**
	 * Maps the UML model (using the eclipse libraries ecore und uml from EMF
	 * project) to a Apache Torque schema object.
	 * 
	 * @param modelObject
	 *            UML model to mao
	 * @return the Apache Torque schema object.
	 */
	public static DatabaseType mapUmlModel(Model modelObject) {

		DatabaseType database = new DatabaseType();
		database.setDefaultIdMethod(IdMethodType.NATIVE);

		List<TableType> tableList = new ArrayList<TableType>();

		if (modelObject == null) {
			LOG.warn("Model is null.");
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
					LOG.error(element.toString());
				}
			}
		}

		database.setTable(tableList);
		return database;
	}
}
