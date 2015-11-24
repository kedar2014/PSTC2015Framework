package com.pstc.framework;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class FrameworkSetupSuiteLevel {
	public static String currentExecFolder;
	public static String log_level;
	public static String log_flag;
	public static String screenshot_flag;
	public static String applink;
	public static String appDB;
	public static String hubHost;
	public static String noProxyFor;
	public static String httpProxy;
	public static SessionFactory factory;
	public static String dBUser;
	


	static Logger logger = Logger.getLogger(FrameworkSetupSuiteLevel.class
			.getName());
	// public static Reporter LOGGER1;

	/*public ThreadLocal<String> threadedParentMethod = new ThreadLocal<>();
	public ThreadLocal<HashMap<String, String>> threadedPassDataChildToParent = new ThreadLocal<>();
	public ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	public ThreadLocal<DesiredCapabilities> Threadedcapabilities = new ThreadLocal<>();*/

	/*public void setParentMethod(String parentMethod) {
		this.threadedParentMethod.set(parentMethod);
	}

	public String parentMethod() {
		return threadedParentMethod.get();
	}

	public void setPassDataChildToParent(
			HashMap<String, String> passDataChildToParent) {
		this.threadedPassDataChildToParent.set(passDataChildToParent);
	}

	public HashMap<String, String> passDataChildToParent() {
		return threadedPassDataChildToParent.get();
	}

	public void setDriver(WebDriver driver) {
		this.driver.set(driver);
	}

	public WebDriver driver() {
		return driver.get();
	}
*/
	@BeforeSuite(alwaysRun = true, groups = { "Setup" })
	public void BeforeSuite() throws Throwable {

		FrameworkServices frameworkServices = new FrameworkServices();
		frameworkServices.setUpFrameworkForExecution();
		try {
			ConfigurationProperties configProperties = new ConfigurationProperties();
			currentExecFolder = configProperties
					.getProperty(ConfigurationProperties.Test_Data_Folder_Path);
			applink = configProperties
					.getProperty(ConfigurationProperties.APPLICATION_URL);
			hubHost = configProperties
					.getProperty(ConfigurationProperties.REMOTE_SERVER_URL);
			noProxyFor = configProperties
					.getProperty(ConfigurationProperties.NO_PROXY_CONNECTION_STRING);
			httpProxy = configProperties.getProperty("httpProxy");
			currentExecFolder = System.getProperty("currentExecFolder");
			
			log_level = configProperties
					.getProperty(ConfigurationProperties.LOG_LEVEL);
			log_flag = configProperties
					.getProperty(ConfigurationProperties.LOG_FLAG);
			screenshot_flag = configProperties
					.getProperty(ConfigurationProperties.SCREENSHOT_FLAG);
			dBUser = configProperties.getProperty(ConfigurationProperties.DB_USER);
			
			Reporter.log("App Link:-" + applink);

			// Create SesssionFactory
			Configuration configuration = new Configuration();
			configuration.configure("hibernate.cfg.xml");
			configuration.setProperty("hibernate.connection.url", configProperties.getProperty(ConfigurationProperties.DATABASE_CONNECTION_STRING));


			
			ServiceRegistryBuilder serviceRegistryBuilder = new ServiceRegistryBuilder().applySettings(configuration.getProperties());
			factory = configuration.buildSessionFactory(serviceRegistryBuilder
					.buildServiceRegistry());
			

			frameworkServices.logMessage("Before Suite Execution Finish",logger);



		} catch (Exception e) {
			frameworkServices.logMessage("Before Suite Failure-" + e.getMessage(), logger);
			System.out.println("Hibernate error:-" + e.getMessage());
			System.exit(1);
		}
	}

	@AfterSuite(alwaysRun = true, groups = { "Setup" })
	public void AfterSuite() throws SecurityException {
		System.out.println("After Suite Function Called");
		FrameworkServices frameworkServices = new FrameworkServices();
		try 
		{
			factory.close();
			frameworkServices.logMessage("After Suite Execution Finish", logger);
		} catch (Exception e) {
			frameworkServices.logMessage("After Suite Failure-" + e.getMessage(), logger);
		}
	}

}


