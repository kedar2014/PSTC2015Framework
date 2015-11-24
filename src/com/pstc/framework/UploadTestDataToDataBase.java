package com.pstc.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.pstc.framework.exception.DataUploadException;

public class UploadTestDataToDataBase {

	static Logger log = Logger.getLogger(UploadTestDataToDataBase.class
			.getName());
	//Mindtree Used
	public void readTestDataFromExcelAndUploadItToDatabase() throws Throwable {
		ConfigurationProperties cfgProperty = new ConfigurationProperties();
		String testDataFolderPath = cfgProperty.getProperty(ConfigurationProperties.Test_Data_Folder_Path);
		dropAllTablesFromDataBase();
		Connection dataBaseConnection = ConnectionProvider.getDataBaseConnection();
		for (File testDataFile : new File(testDataFolderPath).listFiles()) {
			if (!testDataFile.getAbsolutePath().contains("$")) {
				FileInputStream file = new FileInputStream(testDataFile);
				Workbook workBook = WorkbookFactory.create(file);


				for (int i = 0; i < workBook.getNumberOfSheets(); i++) {
					Sheet testDataSheet = workBook.getSheetAt(i);
					
					try {
						int columnCount = createDataTable(testDataSheet, dataBaseConnection);
						insertDataIntoTable(testDataSheet, columnCount, dataBaseConnection);
					}
					catch (Exception e) {
						log.info("Failed to Upload Data from readTestDataFromExcelAndUploadItToDatabase() " + e.getMessage());
						throw new DataUploadException("Failled to Upload Data from readTestDataFromExcelAndUploadItToDatabase() "
								+ e.getMessage());

					}
					
				}

				workBook = null;
				file.close();
			}
		}

		try {
			dataBaseConnection.clearWarnings();
			dataBaseConnection.close();
		}
		catch (Exception e) {
			log.info("Failled to Close DB Connection in readTestDataFromExcelAndUploadItToDatabase() " + e.getMessage());
			throw new DataUploadException("Failled to Close DB Connection in readTestDataFromExcelAndUploadItToDatabase() " + e.getMessage());

		}
	}


//Mindtree Used
	public int createDataTable(Sheet testDataSheet, Connection dataBaseConnection) throws SQLException {
		String createTableQuery = new String();
		createTableQuery = "Create table " + "\"" + testDataSheet.getSheetName() + "\"";
		String columnNames = new String();
		Row headerRow = testDataSheet.getRow(4);
		int columnCount = 0;
		for (int j = 0; j < headerRow.getPhysicalNumberOfCells(); j++) {
			try{


				String headerCellValue = headerRow.getCell(j).getStringCellValue();
				if (headerCellValue.equalsIgnoreCase("")) {
					break;
				}
				columnCount = columnCount + 1;
				columnNames = columnNames + "," +  "\""+ headerCellValue + "\"" 
						+ " Varchar(6000)";

			}
			catch (Exception e) {
				e.printStackTrace();
				log.info("Failled to create table " + testDataSheet.getSheetName()
				+ " " + e.getMessage());
				throw new DataUploadException("Failled to create table "
						+ testDataSheet.getSheetName() + " " + e.getMessage());
			}
		}

		columnNames = columnNames.replaceFirst(",", "");
		createTableQuery = createTableQuery + " (" + columnNames + ");";
		try {
			PreparedStatement createTableQueryStatement = dataBaseConnection.prepareStatement(createTableQuery);
			createTableQueryStatement.executeUpdate();
			createTableQueryStatement.close();
			// dataBaseConnection.commit();
			log.info("created table " + testDataSheet.getSheetName());
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Failled to create table " + testDataSheet.getSheetName() + " " + e.getMessage());
			throw new DataUploadException("Failled to create table " + testDataSheet.getSheetName() + " " + e.getMessage());
		}

		System.out.print ("Created Table: " + testDataSheet.getSheetName() + ". ");
		return columnCount;
	}

	//Mindtree USed
	public void insertDataIntoTable(Sheet testDataSheet, int columnCount, Connection dataBaseConnection) throws SQLException {
		int noOfColumns = columnCount;
		for (int i = 5; i <= testDataSheet.getLastRowNum(); i++) {

			Row dataRow = testDataSheet.getRow(i);
			if (dataRow == null || !(dataRow.getCell(0)==null))
			{
				String insertDataQuery = " insert into "
						+ "\""+testDataSheet.getSheetName()+"\"" + " values(";
				String cloumnValues = new String();
				String columnValue = new String();
				Cell dataCell;
				for (int j = 0; j < noOfColumns; j++) {
					try {
						dataCell = dataRow.getCell(j);
						dataCell.setCellType(Cell.CELL_TYPE_STRING);
						columnValue = dataCell.getStringCellValue();
						columnValue = columnValue.equalsIgnoreCase("") ? null : columnValue;

					} catch (Exception e) {
						columnValue = null;
					}
					columnValue = columnValue == null ? columnValue
							: columnValue.replace("'", "''").trim();

					cloumnValues = cloumnValues + "," + "'" + columnValue + "'";
				}
				cloumnValues = cloumnValues.replace("'null'", "null");
				cloumnValues = cloumnValues.replaceFirst(",", "");
				insertDataQuery = insertDataQuery + cloumnValues + ");";
				try {
					PreparedStatement insertDataQueryStatement = dataBaseConnection.prepareStatement(insertDataQuery);
					insertDataQueryStatement.executeUpdate();
					insertDataQueryStatement.close();
					//dataBaseConnection.commit();
					log.info("Inserted Record  " + insertDataQuery + " into table " + testDataSheet.getSheetName());
				} catch (Exception e) {
					log.info("Failed to Insert Record  " + insertDataQuery + " into table" + testDataSheet.getSheetName() + " " + e.getMessage());
					throw new DataUploadException("Failed to Insert Record  " + insertDataQuery + " into table" + testDataSheet.getSheetName() + " " + e.getMessage());
				}
			}
			dataRow = null;
		}

		System.out.println("Data Upload Completed. ");
	}

	// Mindtree Used
	public void uploadModifiedDataToDataBase() throws Throwable {
		ConfigurationProperties cfgProperty = new ConfigurationProperties();
		String testDataFolderPath = cfgProperty.getProperty(ConfigurationProperties.Test_Data_Folder_Path);

		Connection dataBaseConnection = ConnectionProvider.getDataBaseConnection();
		for (File testDataFile : new File(testDataFolderPath).listFiles()) {
			if (!testDataFile.getAbsolutePath().contains("$")) {
				FileInputStream file = new FileInputStream(testDataFile);
				Workbook workBook = WorkbookFactory.create(file);
				for (int i = 0; i < workBook.getNumberOfSheets(); i++) {
					file = new FileInputStream(testDataFile);
					workBook = WorkbookFactory.create(file);
					Sheet testDataSheet = workBook.getSheetAt(i);

					System.out.println(testDataSheet.getSheetName());

					if (isDataModified(testDataSheet)) {
						dropTableByTableName(testDataSheet.getSheetName().trim());
						
						try {
							int columnCount = createDataTable(testDataSheet, dataBaseConnection);
							insertDataIntoTable(testDataSheet, columnCount, dataBaseConnection);
							setModifiedFlagToFalse(testDataSheet, testDataFile, workBook);
						}
						catch(Exception e) {
							log.info("Failed to Upload Data from uploadModifiedDataToDataBase() " + e.getMessage());
							throw new DataUploadException("Failled to Upload Data from uploadModifiedDataToDataBase() "
									+ e.getMessage());
						}
					}
				}
			}
		}

		try {
			dataBaseConnection.clearWarnings();
			dataBaseConnection.close();
		}
		catch(Exception e) {
			log.info("Failed to close DB Connection in uploadModifiedDataToDataBase() " + e.getMessage());
			throw new DataUploadException("Failed to close DB Connection in uploadModifiedDataToDataBase() " + e.getMessage());
		}
	}

	public boolean isDataModified(Sheet testDataSheet) {
		Row row = testDataSheet.getRow(2);
		return Boolean.parseBoolean(row.getCell(1).toString().replace("'", ""));

	}

	public void setModifiedFlagToFalse(Sheet testDataSheet, File testDataFile,
			Workbook workBook) throws Throwable {

		Row row = testDataSheet.getRow(2);
		row.createCell(1);
		row.getCell(1).setCellValue("FALSE");
		FileOutputStream out = new FileOutputStream(testDataFile);
		workBook.write(out);
		out.flush();
		out.close();

	}

	public void dropAllTablesFromDataBase() throws Exception {
		Connection dataBaseConnection = null;

		try {
			dataBaseConnection = ConnectionProvider.getDataBaseConnection();
			DatabaseMetaData md = dataBaseConnection.getMetaData();
			ResultSet rs = md.getTables(null,"dbo", "%",
					new String[] { "TABLE" });

			while (rs.next()) {
				try {
					System.out.println("Dropped Table: " + rs.getString("TABLE_NAME"));
					PreparedStatement psmt = dataBaseConnection.prepareStatement("drop table " + "\""+rs.getString(3)+ "\"");
					psmt.executeUpdate();
					log.info("Droped Table: " + rs.getString(3));
				} catch (Exception e) {
					log.severe("Failled to drop table" + rs.getString(3) + " "
							+ e.getMessage());
					throw new DataUploadException();
				}

			}
		} finally {

			dataBaseConnection.close();
		}
	}

	// Mindtree Used
	public void dropTableByTableName(String tableName) throws SQLException {
		Connection dataBaseConnection = null;
		try {

			dataBaseConnection = ConnectionProvider.getDataBaseConnection();

			try {
				PreparedStatement psmt = dataBaseConnection
						.prepareStatement("drop table " + tableName);
				psmt.executeUpdate();
				log.info("Dropped table " + tableName);
			} catch (SQLException e1) {
				log.info("Failed to drop table" + tableName + " "
						+ e1.getMessage());
			} catch (Exception e) {
				log.severe("Failed to drop table" + tableName + " "
						+ e.getMessage());
				throw new DataUploadException();
			}

		} finally {
			dataBaseConnection.close();
		}
	}

}
