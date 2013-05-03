package com.seitenbau.earead;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.torque.ColumnType;
import org.apache.torque.DatabaseType;
import org.apache.torque.IdMethodType;
import org.apache.torque.SqlDataType;
import org.apache.torque.TableType;

public class TorqueWriterTest {

	public static void main(String[] argv) throws JAXBException, IOException {

		DatabaseType database = new DatabaseType();
		database.setDefaultIdMethod(IdMethodType.NATIVE);

		TableType bookTable = new TableType();
		bookTable.setName("book");
		bookTable.setDescription("Book table");

		ColumnType bookIdColumn = new ColumnType();
		bookIdColumn.setName("book_id");
		bookIdColumn.setRequired(Boolean.TRUE);
		bookIdColumn.setPrimaryKey(Boolean.TRUE);
		bookIdColumn.setType(SqlDataType.INTEGER);
		bookIdColumn.setDescription("Book Id");

		List<ColumnType> columnList = new ArrayList<ColumnType>();
		columnList.add(bookIdColumn);
		bookTable.setColumn(columnList);

		List<TableType> tableList = new ArrayList<TableType>();
		tableList.add(bookTable);
		database.setTable(tableList);

		// create JAXB context and instantiate marshaller
		JAXBContext context = JAXBContext.newInstance(DatabaseType.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		// Write to System.out
		// m.marshal(database, System.out);
	}

}
