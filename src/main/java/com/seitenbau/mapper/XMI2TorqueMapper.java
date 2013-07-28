package com.seitenbau.mapper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.torque.ColumnType;
import org.apache.torque.DatabaseType;
import org.apache.torque.ForeignKeyType;
import org.apache.torque.IdMethodType;
import org.apache.torque.InheritanceType;
import org.apache.torque.ReferenceType;
import org.apache.torque.SqlDataType;
import org.apache.torque.TableType;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Generalization;
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
		
		// Iteration through model for association mapping.
		for (Package packageObject : modelObject.getNestedPackages()) {
			database.setName(packageObject.getName());

			for (Element element : packageObject.getOwnedElements()) {
				if (element instanceof Association) {
					mapAssociation(element, tableList);
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
			}
		}
		
		//Generalizations
		InheritanceType inheritanceType = null;
		for (Generalization gen : classObject.getGeneralizations()) {
			inheritanceType = new InheritanceType();
			inheritanceType.setKey(gen.getSpecific().getName());
			inheritanceType.setClazz(gen.getSpecific().getName());
			inheritanceType.setExtends(gen.getGeneral().getName());
			System.err.println(inheritanceType);
		}
		
		if (inheritanceType != null) {
			ColumnType inheritanceColumn = new ColumnType();
			List<InheritanceType> inheritanceList = new ArrayList<InheritanceType>();
			
			inheritanceList.add(inheritanceType);
			inheritanceColumn.setInheritance(inheritanceList);
			
			columnList.add(inheritanceColumn);
		}
		
		table.setColumn(columnList);
		table.setForeignKeyOrIndexOrUnique(foreignKeyList);

		LOG.debug(method + "End");
		return table;
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
		column.setSize(new BigDecimal(1));

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
	
	/**
	 * Maps the UML associations to Apache Torque Foreign Keys for the specific Table Types.
	 * Supported association types: 1 - 1, 1 - 1..*, 1 - 0..1, 1 - 0..* and vice versa.
	 * 
	 * @param element the UML association element.
	 * @param tables List of existing tables to map specific foreign keys.
	 */
	private static void mapAssociation(Element element, List<TableType> tables) {
		String method = "mapAssociation(): ";
		LOG.debug(method + "Start");
		
		Association associationObject = (Association) element;
		List<TableType> tableList = tables;
		
		EList<Property> memberEnds = associationObject.getMemberEnds();
		EList<Type> endTypes = associationObject.getEndTypes();
		
		Type sourceType = null;
		Type targetType = null;
		Property sourceProperty = null;
		Property targetProperty = null;
		int sourceLower = 0;
		int sourceUpper = 0;
		int targetLower = 0;
		int targetUpper = 0;
		String sourceName = null;
		String targetName = null;
		String fkAssignedTable = null;
		
		if (memberEnds.size() > 0 && endTypes.size() > 1) {
			//get source type and properties
			sourceType = endTypes.get(0);
			sourceProperty = memberEnds.get(0);
			sourceName = sourceType.getName();
			sourceLower = sourceProperty.getLower();
			sourceUpper = sourceProperty.getUpper();

			//get target type and properties
			targetType = endTypes.get(1);
			targetProperty = memberEnds.get(0).getOtherEnd();
			targetName = targetType.getName();
			targetLower = targetProperty.getLower();
			targetUpper = targetProperty.getUpper();

		}

		List<Serializable> foreignKeyList = null;
		ColumnType newFKColumn = null;
		
		switch (sourceLower) {
			case 0: 
				
				foreignKeyList = createForeignKeyAndReference(targetType);
				newFKColumn = createFKAttribute(targetType, targetProperty, tableList);
				fkAssignedTable = sourceName;
				
				switch (sourceUpper) {

					case 1:
						// source: 0..1
						// target should only be 1
						if (targetLower == 1 && targetUpper == 1) {
							// 0..1 - 1
							System.err.println("Assoziation: " + sourceName + " 0..1 - 1 " + targetName);

						} else {
							System.err.println("Error: only 1 - 0..1");
						}
						break;
						
					case -1: 
						// source: 0..*
						// target should only be 1
						if (targetLower == 1 && targetUpper == 1) {
							//0..* - 1
							System.err.println("Assoziation: " + sourceName	+ " 0..* - 1 " + targetName);
						} else {
							System.err.println("Error: only 1 - 0..*");
						}
						break;
						
					default:
						System.err.println("Error: only 1 - 0..1");
				}
				
				break;

			case 1: 
				
				foreignKeyList = createForeignKeyAndReference(sourceType);
				newFKColumn = createFKAttribute(sourceType, sourceProperty, tableList);
				fkAssignedTable = targetName;
				
				switch (sourceUpper) {
					case 1:
						// source 1
						// target can be 1, 1..n, n, 0..1, 0..n
						switch (targetLower) {
							case 0:
								if (targetUpper == 1) {
									// 1 - 0..1
									System.err.println("Assoziation: " + sourceName	+ " 1 - 0..1 " + targetName);
								} else if (targetUpper == -1) {
									// 1 - 0..*
									System.err.println("Assoziation: " + sourceName	+ " 1 - 0..* " + targetName);
								} else {
									System.err.println("Error: only 1 - 0..1 or 1 - 0..* or 1 - 1 or 1 - 1..*");
								}
								
								break;
								
							case 1:
								if (targetUpper == 1) {
									// 1 - 1
									System.err.println("Assoziation: " + sourceName	+ " 1 - 1 " + targetName);
								} else if (targetUpper == -1) {
									// 1 - 1..*
									System.err.println("Assoziation: " + sourceName	+ " 1 - 1..* " + targetName);
								} else {
									System.err.println("Error: only 1 - 0..1 or 1 - 0..* or 1 - 1 or 1 - 1..*");
								}
								
								break;
								
							case -1:
								if (targetUpper == -1) {
									// 1 - *
									System.err.println("Assoziation: " + sourceName	+ " 1 - * " + targetName);
								} else {
									System.err.println("Error: only 1 - 0..1 or 1 - 0..* or 1 - 1 or 1 - 1..*");
								}
								
								break;
								
							default:
								System.err.println("Error: only 1 - 0..1 or 1 - 0..* or 1 - 1 or 1 - 1..*");
						}

						break;
						
					case -1:
						
						foreignKeyList = createForeignKeyAndReference(targetType);
						newFKColumn = createFKAttribute(targetType, targetProperty, tableList);
						fkAssignedTable = sourceName;
						
						// source 1..n
						// target should only be 1
						if (targetLower == 1 && targetUpper == 1) {
							// 1..* - 1
							System.err.println("Assoziation: " + sourceName + " 1..* - 1 " + targetName);
						} else {
							System.err.println("Error: only 1..* - 1");
						}
						
						break;
				}
				
				break;
				
			case -1:
				
				foreignKeyList = createForeignKeyAndReference(targetType);
				newFKColumn = createFKAttribute(targetType, targetProperty, tableList);
				fkAssignedTable = sourceName;
				
				// source n
				// target should only be 1
				if (targetLower == 1 && targetUpper == 1) {
					// * - 1
					System.err.println("Assoziation: " + sourceName + " * - 1 " + targetName);
				} else {
					System.err.println("Error: only * - 1");
				}
				
				break;
		}
		
		for (TableType table : tableList) {

			if (table.getName().equals(fkAssignedTable)) {
				for (Serializable fk : foreignKeyList) {
					table.getForeignKeyOrIndexOrUnique().add(fk);
					table.getColumn().add(newFKColumn);
				}
			}
		}
			
		LOG.debug(method + "End");
	}
	
	private static List<Serializable> createForeignKeyAndReference(Type singleEntity) {
		String method = "assignForeignKeyAndReference(): ";
		LOG.debug(method + "Start");

		List<Element> list = singleEntity.getOwnedElements();
		String primaryKeyName = null;
		
		for (Element elem : list) {
			
			Property prop = null;
			
			if (elem instanceof Property) {
				prop = (Property) elem;
			}
			
			if (prop.isID()) {
				primaryKeyName = prop.getName();
				System.err.println(prop.getName());
			}
			
		}
		
		ForeignKeyType foreignKey = new ForeignKeyType();
		foreignKey.setForeignTable(singleEntity.getName());
		
		ReferenceType referenceType = new ReferenceType();
		referenceType.setLocal(primaryKeyName);
		referenceType.setForeign(primaryKeyName);
		
		List<ReferenceType> referenceTypeList = new ArrayList<ReferenceType>();
		referenceTypeList.add(referenceType);
		
		foreignKey.setReference(referenceTypeList);
		
		List<Serializable> foreignKeyList = new ArrayList<Serializable>();
		foreignKeyList.add(foreignKey);

		LOG.debug(method + "End");

		return foreignKeyList;
	}
	
	private static ColumnType createFKAttribute(Type singleEntity, Property singleEntityProp, List<TableType> tableList) {
		String method = "addFKAttribute(): ";
		LOG.debug(method + "Start");
		
		TableType singleEntityTable = null;
		
		for (TableType table : tableList) {
			
			if (table.getName().equals(singleEntity.getName())) {
				singleEntityTable = table;
			}
		}
		
		ColumnType fkColumn = new ColumnType();

		List<ColumnType> pkColumn = singleEntityTable.getColumn();
		ColumnType primaryKey = null;
				
		for (ColumnType column : pkColumn) {
			if (column.isPrimaryKey()) {
				primaryKey = column;
			}
		}
		
		if (primaryKey == null) {
			System.out.println("Entity needs a primary key!");
		}
		
		fkColumn.setPrimaryKey(false);
		fkColumn.setName(primaryKey.getName());
		fkColumn.setDescription(primaryKey.getDescription());
		
		if (singleEntityProp.getLower() == 1) {
			fkColumn.setRequired(true);
		} else {
			fkColumn.setRequired(false);
		}
		
		fkColumn.setType(primaryKey.getType());
		
		LOG.debug(method + "End");
		
		return fkColumn;
	}
}