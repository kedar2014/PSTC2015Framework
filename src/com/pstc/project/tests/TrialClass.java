package com.pstc.project.tests;

import java.lang.reflect.Method;
import java.util.Iterator;

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
import com.pstc.project.models.page.SurveyMonkeyHomePage;
import com.pstc.project.models.page.SurveyMonkeyLoggedInPage;
import com.pstc.project.models.page.SurveyMonkeyLoginPage;

public class TrialClass extends FrameworkSetupSuiteLevel{
	FrameworkServices frameworkServices = null;
	EnvironmentParameter env = new EnvironmentParameter();

	@Parameters({ "browserName", "browserVersion", "platform" })
	@BeforeTest(alwaysRun = true)
	public void TrialClass1(String browserName, String browserVersion,
			String platform) {
		frameworkServices = new FrameworkServices();
		env.setBrowserName(browserName);
		env.setBrowserVersion(browserVersion);
		env.setPlatform(platform);
	}

	@DataProvider(name = "SurveyMonkey_DP", parallel = true)
	public Iterator<Object> loginGmail_DP() {
		return frameworkServices.getTestDataMap("LoginSurveyMonkey");
	}

	@Test(testName = "LoginSurveyMonkey", groups = { "Regression",
			"LoginSurveyMonkey" }, dataProvider = "SurveyMonkey_DP")
	public void testExecution(TestMap testMap) throws Throwable {
		WebDriver driver = frameworkServices.getWebDriverInstance(env);
		Method currentMethod = Reporter.getCurrentTestResult().getMethod()
				.getConstructorOrMethod().getMethod();
		String scriptID = currentMethod.getAnnotation(Test.class).testName();
		AssertClass assertClass = new AssertClass(driver, scriptID,
				testMap.getScenarioName());
		driver.get(ConfigurationProperties.APPLICATION_URL);

		SurveyMonkeyHomePage surveyMonkeyHomePage = SurveyMonkeyHomePage
				.getHomePageInstance(driver);
		surveyMonkeyHomePage.signInToSurveyMonkey();

		SurveyMonkeyLoginPage surveyMonkeyLoginPage = SurveyMonkeyLoginPage
				.getLoginPageInstance(driver,testMap);
		assertClass.verifyValues("1", true,
				surveyMonkeyLoginPage.verifySurveyMonkeyLoginPageTitleExists(),
				"Verify Login Page title exists");
		surveyMonkeyLoginPage.loginToSurveyMonkey();

		SurveyMonkeyLoggedInPage surveyMonkeyLoggedInPage = SurveyMonkeyLoggedInPage
				.getLoggedInPageInstance(driver);
		assertClass.verifyValues("2", true, surveyMonkeyLoggedInPage
				.verifySurveyMonkeyLoggedInPageUserNameExists(),
				"Verify Username exists");
	}

}
