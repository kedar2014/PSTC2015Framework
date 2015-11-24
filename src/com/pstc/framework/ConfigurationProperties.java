package com.pstc.framework;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationProperties {

	public final static String PROPERTY_FILENAME = "config/configuration.properties";
	private Properties configurationProperties = new Properties();
	public final static String Test_Data_Folder_Path = "Test_Data_File_Path";
	public final static String Handle_Unexpected_Error_Popups = "Handle_Unexpected_Error_Popups";
	public final static String ENVIRONMENT_SETTINGS = "ENVIRONMENT_SETTINGS";
	public final static String REMOTE_SERVER_URL = "REMOTE_SERVER_URL";
	public final static String APPLICATION_URL = "ApplicationUrl";
	public final static String DATABASE_CONNECTION_STRING = "DBConnectionString";
	public final static String NO_PROXY_CONNECTION_STRING = "noProxyFor";
	public final static String HTTP_PROXY = "httpProxy";
	public final static String LOG_LEVEL = "log_level";
	public final static String LOG_FLAG = "log_flag";
	public final static String SCREENSHOT_FLAG = "screenshot_flag";
	public final static String HISTORICALEXEC_RESULT = "historicalExecResult_flag";
	public final static String CURRENTEXEC_RESULT = "currentExecResult_flag";
	public final static String CURRENTRELEASE = "currentRelease";
	public final static String CURRENTSPRINT = "currentSprint";
	public final static String uploadTestDataToDbFlag = "UploadTestDataToDbFlag";
	public final static String uploadCompleteDataSetToDbFlag = "UploadCompleteDataSetToDbFlag";
	public final static String MAIL_IDs = "EmailAddress";
	public final static String GENERATE_ENTITY_BINS = "GenerateEntityBins";
	public final static String ENTITY_BINS_OUTPUT_FOLDER = "EntityBinsOutputFolder";
	public final static String ENTITY_BINS_INPUT_FOLDER = "EntityBinsInputFolder";
	public final static String DB_USER = "DbUser";
	public final static String DATABASE_CONNECTION_STRING_KB = "DBConnectionStringKB";
	public final static String DATABASE_CONNECTION_STRING_RT = "DBConnectionStringRT";
	public final static String DATABASE_CONNECTION_STRING_SP = "DBConnectionStringSP";
	public final static String DATABASE_CONNECTION_STRING_F = "DBConnectionStringF";
	public final static String DATABASE_CONNECTION_STRING_NBS = "DBConnectionStringNBS";
	public final static String DATABASE_CONNECTION_STRING_RMK = "DBConnectionStringRMK";
	public final static String DEBUG_ITERATION = "debugIteration";
	public final static String scenarioMisUpdate = "updateScenarioMis";
	public final static String REMOTEUPLOADTEMPLATEFILECOPYLOCATION = "RemoteUploadTemplateFileCopyLocation";
	public final static String LOCALUPLOADTEMPLATEFILELocation = "LocalUploadTemplateTemplateFileLocation";
	public final static String DATAMIGRATIONFLAG = "DataMigrationFlag";
	public final static String DBCONNECTIONECT = "DBConnectionStringECT";
	public final static String DATABASE_CONNECTION_STRING_PY = "DBConnectionStringPY";
	public final static String Tables_To_Export_In_Excel = "TablesToExportInExcel";
	public final static String runPendingScenariosfromLastExecution = "runPendingScenariosfromLastExecution";
	public final static String runDebugMode = "runDebugMode";

	public ConfigurationProperties() {
		try {

			configurationProperties.load(new FileInputStream(PROPERTY_FILENAME));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		assert!configurationProperties.isEmpty();
	}

	public String getProperty(final String key) {
		String property = configurationProperties.getProperty(key);
		return property != null ? property.trim() : property;
	}

}
