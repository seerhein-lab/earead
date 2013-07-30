package com.seitenbau.mapper;

import static org.junit.Assert.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.stub;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.List;

import org.apache.torque.ColumnType;
import org.apache.torque.DatabaseType;
import org.apache.torque.ForeignKeyType;
import org.apache.torque.SqlDataType;
import org.apache.torque.TableType;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Type;
import org.junit.Ignore;
import org.junit.Test;

import com.seitenbau.common.FileHelper;
import com.seitenbau.reader.eclipse.XmiReader;
import com.seitenbau.reader.eclipse.XmiTransformer;

public class XMI2TorqueMapperTest {

	@Test
	public void testMapUmlModel() throws FileNotFoundException {
		// transform Enterprise Architecture file
		File xmiFile = FileHelper
				.getXmiFile("src/test/resources/import/complex.xmi");
		File typesFile = FileHelper
				.getXmiFile("src/main/resources/import/ea_extension_primitivetypes.xmi");
		File transformedFile = XmiTransformer.transform(xmiFile, typesFile);
		System.out.println("\n" + FileHelper.getContents(transformedFile));

		// read Enterprise Architecture file
		Model model = XmiReader.readUmlModel(transformedFile);

		// map Enterprise Architecture file to Torque file
		DatabaseType database = XMI2TorqueMapper.mapUmlModel(model);
		
		//TODO: Prüfung ob Ausgabe korrekt oder ähnliches.
	}

	@Test
	public void testMapUmlModelFailure() throws FileNotFoundException {
		DatabaseType database = XMI2TorqueMapper.mapUmlModel(null);
		
		DatabaseType newDatabase = new DatabaseType();
		
		assertEquals(newDatabase, database);
	}

	@Test
	public void testMapUmlClassInheritance() {
		Class classObject = mock(Class.class);
		String name = "testObject";
		String description = "Description:" + name;
		String specificName = "specName";
		String genName = "genName";
		
		EList<Property> propertyList = new BasicEList<Property>();
		Property firstProperty = mock(Property.class);
		Property secondProperty = mock(Property.class);
		
		propertyList.add(firstProperty);
		propertyList.add(secondProperty);
		
		EList<Generalization> generalizationList = new BasicEList<Generalization>();
		Generalization generalization = mock(Generalization.class);
		Class specific = mock(Class.class);
		Class general = mock(Class.class);
		
		generalizationList.add(generalization);
		
		when(classObject.getName()).thenReturn(name);
		when(classObject.getOwnedAttributes()).thenReturn(propertyList);
		when(firstProperty.getAssociation()).thenReturn(null);
		when(secondProperty.getAssociation()).thenReturn(mock(Association.class));
		
		when(general.getName()).thenReturn(genName);
		when(specific.getName()).thenReturn(specificName);
		when(classObject.getGeneralizations()).thenReturn(generalizationList);
		when(generalization.getSpecific()).thenReturn(specific);
		when(generalization.getGeneral()).thenReturn(general);
		
		TableType table = XMI2TorqueMapper.mapUmlClass(classObject);
		
		assertEquals(name, table.getName());
		assertEquals(description, table.getDescription());
	}

	@Test
	public void testMapUmlClassNoInheritance() {
		Class classObject = mock(Class.class);
		String name = "testObject";
		String description = "Description:" + name;
		
		EList<Property> propertyList = new BasicEList<Property>();
		Property firstProperty = mock(Property.class);
		Property secondProperty = mock(Property.class);
		
		propertyList.add(firstProperty);
		propertyList.add(secondProperty);
		
		EList<Generalization> generalizationList = new BasicEList<Generalization>();
		
		when(classObject.getName()).thenReturn(name);
		when(classObject.getOwnedAttributes()).thenReturn(propertyList);
		when(firstProperty.getAssociation()).thenReturn(null);
		when(secondProperty.getAssociation()).thenReturn(mock(Association.class));

		when(classObject.getGeneralizations()).thenReturn(generalizationList);
		
		TableType table = XMI2TorqueMapper.mapUmlClass(classObject);
		
		assertEquals(name, table.getName());
		assertEquals(description, table.getDescription());
	}

	@Test
	public void testMapUmlPropertyLower1() {
		Property propertyObject = mock(Property.class);
		boolean isID = true;
		boolean isRequired = true;
		String name = "propName";
		String description = "Description:" + name;
		Type type = mock(Type.class);
		SqlDataType typeInt = SqlDataType.INTEGER;
		
		when(propertyObject.isID()).thenReturn(isID);
		when(propertyObject.getName()).thenReturn(name);
		when(propertyObject.getLower()).thenReturn(1);
		when(propertyObject.getType()).thenReturn(type);
		
		when(type.getName()).thenReturn("int");
		
		ColumnType column = XMI2TorqueMapper.mapUmlProperty(propertyObject);
		
		assertEquals(isID, column.isPrimaryKey());
		assertEquals(name, column.getName());
		assertEquals(description, column.getDescription());
		assertEquals(isRequired, column.isRequired());
		assertEquals(typeInt, column.getType());
	}

	@Test
	public void testMapUmlPropertyLowerNot1() {
		Property propertyObject = mock(Property.class);
		boolean isID = true;
		boolean isRequired = false;
		String name = "propName";
		String description = "Description:" + name;
		
		when(propertyObject.isID()).thenReturn(isID);
		when(propertyObject.getName()).thenReturn(name);
		when(propertyObject.getLower()).thenReturn(2);
		when(propertyObject.getType()).thenReturn(null);
		
		ColumnType column = XMI2TorqueMapper.mapUmlProperty(propertyObject);
		
		assertEquals(isID, column.isPrimaryKey());
		assertEquals(name, column.getName());
		assertEquals(description, column.getDescription());
		assertEquals(isRequired, column.isRequired());
	}

	@Test
	public void testMapUmlType() {
		Type intType = mock(Type.class);
		Type varcharType = mock(Type.class);
		
		SqlDataType intSqlType = SqlDataType.INTEGER;
		SqlDataType varcharSqlType = SqlDataType.VARCHAR;
		
		when(intType.getName()).thenReturn("int");
		when(varcharType.getName()).thenReturn("foo");
		
		SqlDataType intResult = XMI2TorqueMapper.mapUmlType(intType);
		SqlDataType varcharResult = XMI2TorqueMapper.mapUmlType(varcharType);
		
		assertEquals(intSqlType, intResult);
		assertEquals(varcharSqlType, varcharResult);
	}

	@Test
	public void testMapAssociation() throws FileNotFoundException {

		File xmiFile = FileHelper
				.getXmiFile("src/test/resources/import/complex.xmi");
		File typesFile = FileHelper
				.getXmiFile("src/main/resources/import/ea_extension_primitivetypes.xmi");
		File transformedFile = XmiTransformer.transform(xmiFile, typesFile);
		System.out.println("\n" + FileHelper.getContents(transformedFile));

		// read Enterprise Architecture file
		Model model = XmiReader.readUmlModel(transformedFile);

		// map Enterprise Architecture file to Torque file
		DatabaseType database = XMI2TorqueMapper.mapUmlModel(model);
		
		assertNotNull(database);
	}

	@Test
	public void testCreateForeignKeyAndReferenceIsID() {
		Type type = mock(Type.class);
		EList<Element> list = new BasicEList<Element>();
		Property property = mock(Property.class);
		Element nonProperty = mock(Element.class);
		
		String singleEntityName = "singleTableTestName";
		String primaryKeyName = "primaryKeyTestName";
		
		list.add(property);
		list.add(nonProperty);
		
		when(type.getOwnedElements()).thenReturn(list);
		when(property.isID()).thenReturn(true);
		when(type.getName()).thenReturn(singleEntityName);
		when(property.getName()).thenReturn(primaryKeyName);
		
		List<Serializable> resultList = XMI2TorqueMapper.createForeignKeyAndReference(type);
		
		assertEquals(singleEntityName, ((ForeignKeyType)resultList.get(0))
				.getForeignTable());
		assertEquals(primaryKeyName, ((ForeignKeyType)resultList.get(0))
				.getReference().get(0).getForeign());
		assertEquals(primaryKeyName, ((ForeignKeyType)resultList.get(0))
				.getReference().get(0).getLocal());
	}

	@Test
	public void testCreateForeignKeyAndReferenceIsNotID() {
		Type type = mock(Type.class);
		EList<Element> list = new BasicEList<Element>();
		Property property = mock(Property.class);

		String singleEntityName = "singleTableTestName";
		String primaryKeyName = null;
		
		list.add(property);
		
		when(type.getOwnedElements()).thenReturn(list);
		when(property.isID()).thenReturn(false);
		when(type.getName()).thenReturn(singleEntityName);
		when(property.getName()).thenReturn(primaryKeyName);
		
		List<Serializable> resultList = XMI2TorqueMapper.createForeignKeyAndReference(type);
		
		assertEquals(singleEntityName, ((ForeignKeyType)resultList.get(0))
				.getForeignTable());
		assertEquals(primaryKeyName, ((ForeignKeyType)resultList.get(0))
				.getReference().get(0).getForeign());
		assertEquals(primaryKeyName, ((ForeignKeyType)resultList.get(0))
				.getReference().get(0).getLocal());
	}

	@Test
	public void testCreateFKAttribute() {
		Type type = mock(Type.class);
		Property property = mock(Property.class);
		EList<TableType> list = new BasicEList<TableType>();
		TableType tableType = mock(TableType.class);
		
		String singleEntityName = "singleEntityName";
		String tableTypeName = "tableTypeName";
		boolean primaryKey = true;
		String primaryKeyName = "pkName";
		String description = "Description:" + primaryKeyName;
		boolean isRequired = true;
		SqlDataType pkType = SqlDataType.INTEGER;
		
		EList<ColumnType> columnList = new BasicEList<ColumnType>();
		ColumnType column = new ColumnType();
		column.setPrimaryKey(primaryKey);
		column.setName(primaryKeyName);
		column.setDescription(description);
		column.setType(pkType);
		
		list.add(tableType);
		columnList.add(column);
		
		when(property.getName()).thenReturn(singleEntityName);
		when(property.getLower()).thenReturn(1);
		when(tableType.getName()).thenReturn(tableTypeName);
		when(type.getName()).thenReturn(tableTypeName);
		
		when(tableType.getColumn()).thenReturn(columnList);
			
		ColumnType fkColumn = XMI2TorqueMapper.createFKAttribute(type, property, list);
		
		assertEquals(!primaryKey, fkColumn.isPrimaryKey());
		assertEquals(primaryKeyName, fkColumn.getName());
		assertEquals(description, fkColumn.getDescription());
		assertEquals(isRequired, fkColumn.isRequired());
		assertEquals(pkType, fkColumn.getType());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateFKAttributeNoPrimaryKey() {
		Type singleEntity = mock(Type.class);
		Property singleEntityProp = mock(Property.class);
		EList<TableType> list = new BasicEList<TableType>();
		TableType tableType = mock(TableType.class);
		
		String singleEntityName = "singleEntityName";
		String tableTypeName = "tableTypeName";
		boolean primaryKey = false;
		String primaryKeyName = "pkName";
		String description = "Description:" + primaryKeyName;
		SqlDataType pkType = SqlDataType.INTEGER;
		
		EList<ColumnType> columnList = new BasicEList<ColumnType>();
		ColumnType column = new ColumnType();
		column.setPrimaryKey(primaryKey);
		column.setName(primaryKeyName);
		column.setDescription(description);
		column.setType(pkType);
		
		list.add(tableType);
		columnList.add(column);
		
		when(singleEntityProp.getName()).thenReturn(singleEntityName);
		when(tableType.getName()).thenReturn(tableTypeName);
		when(singleEntity.getName()).thenReturn(tableTypeName);
		
		when(tableType.getColumn()).thenReturn(columnList);
			
		XMI2TorqueMapper.createFKAttribute(singleEntity, singleEntityProp, list);

	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateFKAttributeNoTableFound() {
		Type singleEntity = mock(Type.class);
		Property singleEntityProp = mock(Property.class);
		EList<TableType> list = new BasicEList<TableType>();
		TableType tableType = mock(TableType.class);
		
		String singleEntityName = "singleEntityName";
		String tableTypeName = "tableTypeName";
		String tableNameError = "foo";
		
		EList<ColumnType> columnList = new BasicEList<ColumnType>();
		
		list.add(tableType);
		
		when(singleEntityProp.getName()).thenReturn(singleEntityName);
		when(tableType.getName()).thenReturn(tableTypeName);
		when(singleEntity.getName()).thenReturn(tableNameError);
		
		when(tableType.getColumn()).thenReturn(columnList);
			
		XMI2TorqueMapper.createFKAttribute(singleEntity, singleEntityProp, list);

	}
	
	@Test
	public void testCreateFKAttributeNotRequired() {
		Type type = mock(Type.class);
		Property property = mock(Property.class);
		EList<TableType> list = new BasicEList<TableType>();
		TableType tableType = mock(TableType.class);
		
		String singleEntityName = "singleEntityName";
		String tableTypeName = "tableTypeName";
		boolean primaryKey = true;
		String primaryKeyName = "pkName";
		String description = "Description:" + primaryKeyName;
		boolean isRequired = false;
		SqlDataType pkType = SqlDataType.INTEGER;
		
		EList<ColumnType> columnList = new BasicEList<ColumnType>();
		ColumnType column = new ColumnType();
		column.setPrimaryKey(primaryKey);
		column.setName(primaryKeyName);
		column.setDescription(description);
		column.setType(pkType);
		
		list.add(tableType);
		columnList.add(column);
		
		when(property.getName()).thenReturn(singleEntityName);
		when(property.getLower()).thenReturn(0);
		when(tableType.getName()).thenReturn(tableTypeName);
		when(type.getName()).thenReturn(tableTypeName);
		
		when(tableType.getColumn()).thenReturn(columnList);
			
		ColumnType fkColumn = XMI2TorqueMapper.createFKAttribute(type, property, list);
		
		assertEquals(!primaryKey, fkColumn.isPrimaryKey());
		assertEquals(primaryKeyName, fkColumn.getName());
		assertEquals(description, fkColumn.getDescription());
		assertEquals(isRequired, fkColumn.isRequired());
		assertEquals(pkType, fkColumn.getType());
	}

}
