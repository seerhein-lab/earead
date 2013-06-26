package com.seitenbau.writer.torque;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.torque.ColumnType;
import org.apache.torque.DatabaseType;
import org.apache.torque.IdMethodType;
import org.apache.torque.SqlDataType;
import org.apache.torque.TableType;

import com.seitenbau.common.FileHelper;

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

		File file = new File("target/torque-schema.xml");
		DatamodelWriter.writeDatamodel(database, file);

		System.out.println(FileHelper.getContents(file));
	}

}
