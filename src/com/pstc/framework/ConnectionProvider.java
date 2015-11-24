package com.pstc.framework;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;

public class ConnectionProvider {
	static Logger log = Logger.getLogger(UploadTestDataToDataBase.class.getName());

	public static Connection getDataBaseConnection() {
		FrameworkServices frameworkServices = new FrameworkServices();
		ConfigurationProperties configurationProperties = new ConfigurationProperties();
		ConnectionProvider connServer = new ConnectionProvider();
		Connection dataBaseConnection = null;
		try {
			dataBaseConnection = connServer.dbConnect(
					configurationProperties.getProperty(ConfigurationProperties.DATABASE_CONNECTION_STRING),
					configurationProperties.getProperty(ConfigurationProperties.DB_USER), "admin@123");
		} catch (Exception e) {

			System.out.println(e.getMessage());
			System.exit(1);
		}

		return dataBaseConnection;

	}

	public Connection dbConnect(String db_connect_string, String db_userid, String db_password) {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection dataBaseConnection = DriverManager.getConnection(db_connect_string, db_userid, db_password);
			System.out.println("connected");
			return dataBaseConnection;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
