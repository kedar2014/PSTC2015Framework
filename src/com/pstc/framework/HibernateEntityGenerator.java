package com.pstc.framework;

import java.beans.Introspector;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class HibernateEntityGenerator {

	String newLineCharacter = "\n";

	static Logger log = Logger.getLogger(HibernateEntityGenerator.class.getName());

	// Mindtree Used
	public void readHeaderDetailsFromExcelAndGenerateHibernateEntities() throws Throwable {

		ConfigurationProperties cfgProperty = new ConfigurationProperties();
		String testDataFolderPath = cfgProperty.getProperty(ConfigurationProperties.ENTITY_BINS_INPUT_FOLDER);
		File entityBinsOutPutFolder = new File(
				cfgProperty.getProperty(ConfigurationProperties.ENTITY_BINS_OUTPUT_FOLDER));
		if (entityBinsOutPutFolder.exists())
			entityBinsOutPutFolder.delete();
		entityBinsOutPutFolder.mkdirs();
		for (File testDataFile : new File(testDataFolderPath).listFiles()) {

			if (!testDataFile.getAbsolutePath().contains("$")) {
				FileInputStream file = new FileInputStream(testDataFile);
				Workbook workBook = WorkbookFactory.create(file);
				for (int i = 0; i < workBook.getNumberOfSheets(); i++) {
					Sheet testDataSheet = workBook.getSheetAt(i);
					System.out.println(testDataSheet.getSheetName());
					generateEntityBins(testDataSheet);

				}
			}
		}
	}

	// Mindtree Used
	public void generateEntityBins(Sheet testDataSheet) throws IOException {

		if (isDataModified(testDataSheet)) {

			String columnNames = new String();
			Row headerRow = testDataSheet.getRow(4);
			for (int j = 0; j < headerRow.getPhysicalNumberOfCells(); j++) {

				String headerCellValue = headerRow.getCell(j).getStringCellValue();
				if (headerCellValue.equalsIgnoreCase("")) {
					break;
				}

				columnNames = columnNames + "," + headerCellValue;
			}
			columnNames = columnNames.replaceFirst(",", "");
			generateEntityClass(columnNames, testDataSheet.getSheetName());
		}

	}

	// Mindtree Used
	public boolean isDataModified(Sheet testDataSheet) {
		Row row = testDataSheet.getRow(2);
		return Boolean.parseBoolean(row.getCell(1).toString().replace("'", ""));

	}

	// Mindtree Used
	public void generateEntityClass(String columnNames, String tableName) throws IOException {

		FileUtilities fileUtilities = new FileUtilities();
		ConfigurationProperties configurationProperties = new ConfigurationProperties();

		String sheetName[] = tableName.split("_");
		String entityClassName = sheetName[sheetName.length - 1];
		File file = new File(configurationProperties.getProperty(ConfigurationProperties.ENTITY_BINS_OUTPUT_FOLDER)
				+ File.separator + entityClassName + ".java");
		if (file.exists())
			file.delete();
		file.createNewFile();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("package com.pstc.project.models.data;").append(newLineCharacter);
		stringBuilder.append("import java.io.Serializable;").append(newLineCharacter);
		stringBuilder.append("import javax.persistence.Column;").append(newLineCharacter);
		stringBuilder.append("import javax.persistence.Entity;").append(newLineCharacter);
		stringBuilder.append("import javax.persistence.Table;").append(newLineCharacter);

		stringBuilder.append("import javax.persistence.Id;").append(newLineCharacter);

		stringBuilder.append("@Entity").append(newLineCharacter);
		stringBuilder.append("@Table(name = \"" + tableName + "\")").append(newLineCharacter);
		stringBuilder.append("public class " + entityClassName + " implements Serializable {")
				.append(newLineCharacter);
		stringBuilder.append("private static final long serialVersionUID = 1L;").append(newLineCharacter);
		stringBuilder.append("@Id").append(newLineCharacter);
		List<String> columnNameList = Arrays.asList(columnNames.split(","));
		for (String columnName : columnNameList) {
			stringBuilder.append("@Column(name =\"" + columnName + "\")").append(newLineCharacter);
			stringBuilder.append("private String " + Introspector.decapitalize(columnName) + ";")
					.append(newLineCharacter);

		}

		stringBuilder.append("}").append(newLineCharacter);
		fileUtilities.setContents(file, stringBuilder.toString());
	}

}
