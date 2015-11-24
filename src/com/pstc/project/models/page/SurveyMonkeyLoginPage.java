package com.pstc.project.models.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.pstc.framework.FrameworkServices;
import com.pstc.framework.utilities.Page;
import com.pstc.framework.utilities.PageElement;
import com.pstc.framework.utilities.WaitType;
import com.pstc.project.models.data.Login;
import com.pstc.project.models.data.TestMap;

public class SurveyMonkeyLoginPage extends Page {
	static Logger logger = Logger.getLogger(SurveyMonkeyLoginPage.class
			.getName());
	Login loginData;

	public SurveyMonkeyLoginPage(WebDriver driver, TestMap testMap) {
		super(driver, SurveyMonkeyLoginPage.class.getSimpleName(), logger);
		loginData = getLoginData(testMap);
	}

	public static SurveyMonkeyLoginPage getLoginPageInstance(WebDriver driver,
			TestMap testMap) throws Throwable {
		SurveyMonkeyLoginPage surveyMonkeyLoginPage = new SurveyMonkeyLoginPage(
				driver, testMap);
		surveyMonkeyLoginPage.waitForLoginPageToLoad();
		return surveyMonkeyLoginPage;

	}

	public void waitForLoginPageToLoad() {
		waitForPageElement(surveyMonkeyLoginPageTitle());
	}

	private PageElement surveyMonkeyLoginPageTitle() {
		return new PageElement(By.xpath(""), "Survey monkey Login page Title",
				true, WaitType.WAITFORELEMENTTOBEDISPLAYED, 15);
	}

	private PageElement usernameTextField() {
		return new PageElement(By.name("username"), "Username Text Field",
				true, WaitType.WAITFORELEMENTTOBEDISPLAYED, 15);
	}

	private PageElement passwordTextField() {
		return new PageElement(By.name("password"), "Password Text Field",
				true, WaitType.WAITFORELEMENTTOBEDISPLAYED, 15);
	}

	private PageElement signInButton() {
		return new PageElement(By.xpath("//button[text()='Sign In']"),
				"Signin Button", true, WaitType.WAITFORELEMENTTOBEENABLED, 15);
	}

	public void loginToSurveyMonkey() {
		sendKeys(usernameTextField(), loginData.getUserName());
		sendKeys(passwordTextField(), loginData.getPassword());
		click(signInButton());
	}

	public boolean verifySurveyMonkeyLoginPageTitleExists() {
		return isElementDisplayed(surveyMonkeyLoginPageTitle());
	}

	private Login getLoginData(TestMap testMap) {
		FrameworkServices frameworkServices = new FrameworkServices();
		return (Login) frameworkServices.getPageDataByTestMapCode(
				testMap.getPage_Login(), Login.class);
	}

}
