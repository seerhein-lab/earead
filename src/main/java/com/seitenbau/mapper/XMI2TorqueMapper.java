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
	 *            UML model to map.
	 * @return the Apache Torque schema object.
	 */
	public static DatabaseType mapUmlModel(Model modelObject) {
		String method = "mapUmlModel(): ";
		LOG.debug(method + "Start");

		if (modelObject == null) {
			LOG.warn(method + "Model is null.");
			return new DatabaseType();
		}

		DatabaseType database = new DatabaseType();
		database.setDefaultIdMethod(IdMethodType.NATIVE);
		List<TableType> tableList = new ArrayList<TableType>();

		for (Package packageObject : modelObject.getNestedPackages()) {
			database.setName(packageObject.getName());

			for (Element element : packageObject.getOwnedElements()) {
				if (element instanceof Class) {
					Class classObject = (Class) element;
					TableType table = mapUmlClass(classObject);
					tableList.add(table);
				} else if (element instanceof Association) {
					// this case is handled via the property of a class element
				} else {
					String msg = "Expected package is not a class: "
							+ element.toString();
					LOG.warn(method + msg);
				}
			}
		}
		database.setTable(tableList);

		LOG.debug(method + "End");
		return database;
	}

	/**
	 * Maps the UML class object (using the eclipse libraries ecore und uml from
	 * EMF project) to a Apache Torque table object.
	 * 
	 * @param classObject
	 *            the class to map.
	 * @return the Apache Torque table object.
	 */
	private static TableType mapUmlClass(Class classObject) {
		String method = "mapUmlClass(): ";
		LOG.debug(method + "Start");

		TableType table = new TableType();
		table.setName(classObject.getName());
		table.setDescription("Description:" + classObject.getName());

		List<ColumnType> columnList = new ArrayList<ColumnType>();
		List<Serializable> foreignKeyList = new ArrayList<Serializable>();

		for (Property property : classObject.getOwnedAttributes()) {
			if (property.getAssociation() == null) {
				ColumnType column = mapUmlProperty(property);
				columnList.add(column);
			} else {
				ForeignKeyType foreignKey = mapUmlAssociation(property);
				foreignKeyList.add(foreignKey);
			}
		}

		table.setColumn(columnList);
		table.setForeignKeyOrIndexOrUnique(foreignKeyList);

		LOG.debug(method + "End");
		return table;
	}

	/**
	 * Maps the UML property which contains the association object (using the
	 * eclipse libraries ecore und uml from EMF project) to a Apache Torque
	 * ForeignKeyType object.
	 * 
	 * @param propertyObject
	 *            the property which contains the association to map.
	 * @return the Apache Torque ForeignKeyType object.
	 */
	private static ForeignKeyType mapUmlAssociation(Property propertyObject) {
		String method = "mapUmlAssociation(): ";
		LOG.debug(method + "Start");

		Association associationObject = propertyObject.getAssociation();
		if (associationObject == null) {
			String msg = "Property is no association: " + propertyObject;
			throw new IllegalArgumentException(msg);
		}

		ForeignKeyType foreignKey = new ForeignKeyType();
		foreignKey.setForeignTable(propertyObject.getType().getName());

		List<ReferenceType> referenceTypeList = new ArrayList<ReferenceType>();
		for (Property associationProperty : associationObject.getOwnedEnds()) {

			// TODO Unterschiedliche Assoziation-Typen implementieren
			// System.out.println("\t\t\tLower: "
			// + associationProperty.getLower());
			// System.out.println("\t\t\tUpper: "
			// + associationProperty.getUpper());

			ReferenceType referenceType = new ReferenceType();
			// TODO Fremdschlüssel verwenden und nicht das Objekt
			referenceType.setForeign(propertyObject.getType().getName());
			// TODO Fremdschlüssel verwenden und nicht das Objekt
			referenceType.setLocal(associationProperty.getType().getName());
			referenceTypeList.add(referenceType);

			// only one reference, leave loop
			break;
		}
		foreignKey.setReference(referenceTypeList);

		LOG.debug(method + "End");
		return foreignKey;
	}

	/**
	 * Maps the UML property object (using the eclipse libraries ecore und uml
	 * from EMF project) to a Apache Torque column object.
	 * 
	 * @param propertyObject
	 *            the property to map.
	 * @return the Apache Torque column object.
	 */
	private static ColumnType mapUmlProperty(Property propertyObject) {
		String method = "mapUmlProperty(): ";
		LOG.debug(method + "Start");

		ColumnType column = new ColumnType();
		column.setPrimaryKey(propertyObject.isID());
		column.setName(propertyObject.getName());
		column.setDescription("Description:" + propertyObject.getName());

		if (propertyObject.getLower() == 1) {
			column.setRequired(Boolean.TRUE);
		} else {
			column.setRequired(Boolean.FALSE);
		}

		Type type = propertyObject.getType();
		if (type != null) {
			column.setType(mapUmlType(type));
		} else {
			String msg = "No type defined for property: "
					+ propertyObject.getName();
			LOG.warn(method + msg);
		}

		LOG.debug(method + "End");
		return column;
	}

	/**
	 * Maps the UML property type object (using the eclipse libraries ecore und
	 * uml from EMF project) to a Apache Torque SqlDataType.
	 * 
	 * @param typeObject
	 *            the property type object to map.
	 * @return the Apache Torque SqlDataType.
	 */
	private static SqlDataType mapUmlType(Type typeObject) {
		// TODO: andere Typen (am Besten durch eine konfigurierbare Map)
		if (typeObject.getName().equals("int")) {
			return SqlDataType.INTEGER;
		}
		return SqlDataType.VARCHAR;

	}
}
