package com.pstc.framework;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Reporter;

import com.pstc.framework.exception.IllegalEnvironmentValueException;
import com.pstc.project.models.data.TestMap;

@SuppressWarnings("unchecked")
public class FrameworkServices {

	public static final boolean DEBUGLEVEL = true;
	public static File screenShotFolderPath;
	public static File screenShotAddress;
	public static String resultServerLocation;
	public static String resultServerAddress;
	public static String resultpart;

	static Logger log = Logger.getLogger(FrameworkServices.class.getName());

	public synchronized static String getUniqueFileName() {
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMYYYYhhmmss");
		Date date = new Date();
		return (sdf.format(date));
	}

	public WebDriver getWebDriverInstance(EnvironmentParameter environmentParameter) throws Throwable {
		DesiredCapabilities capability = new DesiredCapabilities();

		if (environmentParameter.getBrowserName().equalsIgnoreCase("internet explorer")) {
			capability = DesiredCapabilities.internetExplorer();
			capability.setVersion(environmentParameter.getBrowserVersion());
			capability.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, false);
			capability.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, true);
		} else if (environmentParameter.getBrowserName().equalsIgnoreCase("firefox"))

			capability = DesiredCapabilities.firefox();

		else if (environmentParameter.getBrowserName().equalsIgnoreCase("chrome")) {
			capability = DesiredCapabilities.chrome();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-extensions");
			// options.addArguments("test-type"); //Working
			options.setExperimentalOption("excludeSwitches", Arrays.asList("ignore-certificate-errors"));
			capability.setCapability(ChromeOptions.CAPABILITY, options);

		} else if (environmentParameter.getBrowserName().equalsIgnoreCase("phantomjs")) {
			capability = DesiredCapabilities.phantomjs();
			capability.setCapability("phantomjs.binary.path", "D:/Grid/phantomjs.exe");
		} else if (environmentParameter.getBrowserName().equalsIgnoreCase("opera"))
			capability = DesiredCapabilities.opera();

		else if (environmentParameter.getBrowserName().equalsIgnoreCase("safari"))
			capability = DesiredCapabilities.safari();

		else if (environmentParameter.getBrowserName().equalsIgnoreCase("htmlunit")) {
			capability = DesiredCapabilities.htmlUnit();
			capability.setBrowserName("firefox");
		}

		else
			throw new IllegalEnvironmentValueException("Browser Name: " + environmentParameter.getBrowserName()
					+ " is Not valid Try any one of following \n1 internet explorer ,2 firefox,3 chrome 4 opera");

		if (environmentParameter.getPlatform().equalsIgnoreCase("windows"))
			capability.setPlatform(Platform.WINDOWS);

		else if (environmentParameter.getPlatform().equalsIgnoreCase("linux"))

			capability.setPlatform(Platform.LINUX);

		else if (environmentParameter.getPlatform().equalsIgnoreCase("mac"))

			capability.setPlatform(Platform.MAC);

		else
			throw new IllegalEnvironmentValueException("Platform Name: " + environmentParameter.getPlatform()
					+ " is Not valid Try any one of following\n1 windows ,2 linux,3 mac");

		ConfigurationProperties configurationProperties = new ConfigurationProperties();

		
		WebDriver driver = null;
		capability.setVersion(environmentParameter.getBrowserVersion());
		try {
			URL gridURL = new URL(configurationProperties.getProperty(ConfigurationProperties.REMOTE_SERVER_URL));
			driver = new RemoteWebDriver(gridURL, capability);
		} catch (Exception e) {
			e.printStackTrace();
			Reporter.log("Unable to launch browser instance due to following exception : " + e.getMessage());
		} catch (Error err) {
			err.printStackTrace();
			Reporter.log("Unable to launch browser instance due to following error : " + err.getMessage());
		}
		((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		return driver;

	}

	public Iterator<Object> getTestDataMap(String scenarioName) {
		SessionFactory sessionFactory;
		List<Object> objects = null;
		Session session = null;
		List<Object> dataToBeReturned = new ArrayList<>();
		try {
			objects = new ArrayList<Object>();
			sessionFactory = FrameworkSetupSuiteLevel.factory;
			session = sessionFactory.openSession();
			Transaction transaction = session.beginTransaction();

			Criteria criteria = session.createCriteria(TestMap.class);
			objects = criteria.add(Restrictions.like("scenarioName", scenarioName, MatchMode.ANYWHERE)).list();

			for (Object data : objects) {
				dataToBeReturned.add(new Object[] { data });
			}

			transaction.commit();
			if (objects.size() == 0) {
				Reporter.log("No data found or iteration selected for Automation ID " + scenarioName + " in Base Class "
						+ TestMap.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return dataToBeReturned.iterator();
	}

	public static List<Object> getEnvironmentParameterListFromEnvironmentString(String environmentString) {
		List<Object> environmentParameterList = new ArrayList<Object>();
		EnvironmentParameter environmentParameter = new EnvironmentParameter();
		StringTokenizer environmentTokens = new StringTokenizer(environmentString, ";");
		while (environmentTokens.hasMoreTokens()) {
			String environmentToken = environmentTokens.nextToken();
			StringTokenizer environmentValuesTokens = new StringTokenizer(environmentToken, ",");
			while (environmentValuesTokens.hasMoreTokens()) {
				String environmentValuesToken = environmentValuesTokens.nextToken();
				String environmentKey = environmentValuesToken.split(":")[0];
				String environmentValue = environmentValuesToken.split(":")[1];

				if (environmentKey.equalsIgnoreCase("browserName")) {
					environmentParameter.setBrowserName(environmentValue);
				} else if (environmentKey.equalsIgnoreCase("browserVersion")) {
					environmentParameter.setBrowserName(environmentValue);
				} else if (environmentKey.equalsIgnoreCase("platform")) {
					environmentParameter.setBrowserName(environmentValue);
				}

			}
			environmentParameterList.add(new Object[] { environmentParameter });

		}
		return environmentParameterList;
	}

	// MindTree Used
	public void setUpFrameworkForExecution() throws Throwable {
		try {

			/*
			 * screenShotFolderPath = System.getProperty("resultServerLocation")
			 * != null ? new File(System.getProperty("snapshotpath")) : new
			 * File("D://Execution"); screenShotAddress =
			 * System.getProperty("snapshotpath") != null ? new
			 * File(System.getProperty("resultServerAddress") + "/" +
			 * System.getProperty("resultpart")) : new
			 * File("D://Execution");
			 * 
			 * if (screenShotFolderPath.exists()) {
			 * screenShotFolderPath.delete(); } screenShotFolderPath.mkdir();
			 */

			ConfigurationProperties configurationProperties = new ConfigurationProperties();
			// PropertyConfigurator.configure("Config/log4j.properties");
			// PropertyConfigurator.configure("Config/configuration.properties");
			UploadTestDataToDataBase uploadTestDataToDataBase = new UploadTestDataToDataBase();
			if (configurationProperties.getProperty(ConfigurationProperties.GENERATE_ENTITY_BINS)
					.equalsIgnoreCase("true")) {
				HibernateEntityGenerator hibernateEntityGenerator = new HibernateEntityGenerator();
				hibernateEntityGenerator.readHeaderDetailsFromExcelAndGenerateHibernateEntities();
				System.exit(1);

			} else if (configurationProperties.getProperty(ConfigurationProperties.uploadCompleteDataSetToDbFlag)
					.equalsIgnoreCase("true")) {
				uploadTestDataToDataBase.readTestDataFromExcelAndUploadItToDatabase();
			} else if (configurationProperties.getProperty(ConfigurationProperties.uploadTestDataToDbFlag)
					.equalsIgnoreCase("true")) {
				uploadTestDataToDataBase.uploadModifiedDataToDataBase();

			} else
				log.info("Test Data not uploaded as flag in property file was false");
		} catch (Exception e) {
			log.fatal("Unable to upload data into database and system exited with error--" + e.getMessage());
			System.out.println("Unable to upload data into database and system exited with error--" + e.getMessage());
			System.exit(1);

		}

	}

	public void logMessage(String message, Logger logger) {
		logger.info(message);
		Reporter.log(message);
	}

	public static <T> String getProcessedTestData(String builderName, List<T> array, String parameter) {
		int rnd = new Random().nextInt(array.size());

		if (builderName.toLowerCase().contains("builder")) {
			List<String> arrList = new ArrayList<String>() {
				{
					add("kedar.barde2002@gmail.com");
					add("kedar.barde@mindtree.com");
					add("rjt@gmail.com123");
				}
			};
			return arrList.get(rnd);
		} else {
			return parameter;
		}

	}

	@SuppressWarnings("rawtypes")
	public Object getPageDataByTestMapCode(String code, Class clazz) {
		Session session = null;
		SessionFactory sessionFactory = null;
		Object object = null;
		try {
			sessionFactory = FrameworkSetupSuiteLevel.factory;
			session = sessionFactory.openSession();
			Transaction transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(clazz);
			object = criteria.add(Restrictions.eq("testDataCode", code)).list().get(0);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			session.close();
		}
		return object;

	}
	
	@SuppressWarnings("rawtypes")
	public static  List getProcessedDataListTestDataCode(String combinedCode, Class clazz) {
		
		Session session = null;
		SessionFactory sessionFactory = null;
		Object object = null;
		List<Object> dataList = new ArrayList<>();
		
		try {	
				
				sessionFactory = FrameworkSetupSuiteLevel.factory;
				
				String[] codes = combinedCode.split(",");
				
				for (String code : codes) {
					session = sessionFactory.openSession();
					Transaction transaction = session.beginTransaction();
					Criteria criteria = session.createCriteria(clazz);
					object = criteria.add(Restrictions.like("testDataCode", code,MatchMode.ANYWHERE)).list().get(0);
					transaction.commit();
					dataList.add(object);
				}
				
				
			
			
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			session.close();
		}
		return dataList;

	}
	
	

	public boolean assertCollectionsEqual(List<?> actualValues, List<?> expectedValues) {

		for (Object actualValue : actualValues) {
			boolean isObjectFound = false;
			for (Object expectedValue : expectedValues)
				try {
					if (expectedValue.equals(actualValue)) {
						isObjectFound = true;
					}
				} catch (AssertionError assertionError) {

				}
			if (!isObjectFound) {
				throw new AssertionError("; List" + expectedValues + " Does not contain: " + actualValue);
			}

		}
		for (Object expectedValue : expectedValues) {

			boolean isObjectFound = false;
			for (Object actualValue : actualValues)
				try {
					if (actualValue.equals(expectedValue)) {
						isObjectFound = true;
					}
				} catch (AssertionError assertionError) {

				}
			if (!isObjectFound) {
				throw new AssertionError("Actual List" + actualValues + " Does not contain: " + expectedValue);
			}

		}
		return true;
	}

	
	@SuppressWarnings("rawtypes")
	public String startTestVideoRecording(String testName) {
		return "";

	}

}
