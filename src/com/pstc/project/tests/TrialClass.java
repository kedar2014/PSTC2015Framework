package com.pstc.project.tests;

import java.lang.reflect.Method;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.pstc.framework.ConfigurationProperties;
import com.pstc.framework.EnvironmentParameter;
import com.pstc.framework.FrameworkServices;
import com.pstc.framework.FrameworkSetupSuiteLevel;
import com.pstc.framework.utilities.AssertClass;
import com.pstc.project.models.data.TestMap;
import com.pstc.project.models.page.GmailHomePage;
import com.pstc.project.models.page.GmailInboxPage;
import com.pstc.project.models.page.GmailLoginPage;
import com.pstc.project.models.page.SurveyMonkeyHomePage;
import com.pstc.project.models.page.SurveyMonkeyLoggedInPage;
import com.pstc.project.models.page.SurveyMonkeyLoginPage;

public class TrialClass extends FrameworkSetupSuiteLevel {
	FrameworkServices frameworkServices = null;
	EnvironmentParameter env = new EnvironmentParameter();
	ConfigurationProperties configurationProperties = null;
	Logger logger = Logger.getLogger(TrialClass.class);

	@Parameters({ "browserName", "browserVersion", "platform" })
	@BeforeTest(alwaysRun = true)
	public void TrialClass1(String browserName, String browserVersion,
			String platform) {
		configurationProperties = new ConfigurationProperties();
		frameworkServices = new FrameworkServices();
		env.setBrowserName(browserName);
		env.setBrowserVersion(browserVersion);
		env.setPlatform(platform);
	}

	@DataProvider(name = "GmailSubjectSearch_DP", parallel = true)
	public Iterator<Object> loginGmail_DP() {
		return frameworkServices.getTestDataMap("GmailSubjectSearch");
	}

	@Test(testName = "GmailSubjectSearch", groups = { "Regression",
			"LoginSurveyMonkey" }, dataProvider = "GmailSubjectSearch_DP")
	public void testExecution(TestMap testMap) throws Throwable {
		WebDriver driver=null;
		try {
			
			driver = frameworkServices.getWebDriverInstance(env);
			Method currentMethod = Reporter.getCurrentTestResult().getMethod()
					.getConstructorOrMethod().getMethod();
			String scriptID = currentMethod.getAnnotation(Test.class)
					.testName();
			AssertClass assertClass = new AssertClass(driver, scriptID,
					testMap.getScenarioName());
			driver.get("http://mail.google.com");

			
			GmailHomePage gmailHomePage = GmailHomePage.getGmailHomePageInstance(driver);
			
			gmailHomePage.signInToGmail();
			
			GmailLoginPage gmailLoginPage = GmailLoginPage.getGmailLoginPageInstance(driver, testMap);
			gmailLoginPage.signInToGmail();
			
			GmailInboxPage gmailInboxPage = GmailInboxPage.getInboxPageInstance(driver,testMap);
			
			gmailInboxPage.enterSearchValue();
			assertClass.verifyValues("2", gmailInboxPage.getGmailInboxData().getCountToVerify(), gmailInboxPage.getListOfAllSubjects().size(),
					"Verify Username exists");
			// FrameworkServices.videoServiceStop(drgmiver);
		} catch (AssertionError assertionError) {
			// if (FrameworkSetupSuiteLevel.screenshot_flag.equals("true")) {
			frameworkServices.takeScreenshot(testMap.getScenarioName(), driver,
					assertionError, logger);
			;
			// }
			throw assertionError;

		} catch (Exception exception) {

			// if (FrameworkSetupSuiteLevel.screenshot_flag.equals("true")) {
			frameworkServices.takeScreenshot(testMap.getScenarioName(), driver,
					exception, logger);
			;
			// }

			/*
			 * if (!(driver == null)) { driver.quit(); }
			 */
			throw exception;
		} finally {
			if (!(driver == null)) {
				driver.quit();
			}
		}

	}

}
