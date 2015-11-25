package com.pstc.project.models.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.pstc.framework.FrameworkServices;
import com.pstc.framework.utilities.Page;
import com.pstc.framework.utilities.PageElement;
import com.pstc.framework.utilities.WaitType;
import com.pstc.project.models.data.GmailLoginPageData;
import com.pstc.project.models.data.Login;
import com.pstc.project.models.data.TestMap;

public class GmailLoginPage extends Page {
	static Logger logger = Logger.getLogger(GmailLoginPage.class.getName());
	GmailLoginPageData gmailLoginData;

	public GmailLoginPage(WebDriver driver, TestMap testMap) {
		super(driver, GmailLoginPage.class.getSimpleName(), logger);
		gmailLoginData = getLoginData(testMap);
	}

	public static GmailLoginPage getGmailLoginPageInstance(WebDriver driver,
			TestMap testMap) throws Throwable {
		GmailLoginPage gmailLoginPage = new GmailLoginPage(driver, testMap);
		gmailLoginPage.waitForGmailLoginPageToLoad();
		return gmailLoginPage;

	}

	public void waitForGmailLoginPageToLoad() {
		waitForPageElement(gmailLoginPage());
	}

	private PageElement gmailLoginPage() {
		return new PageElement(
				By.xpath("//*[contains(text(),'One account. All of Google.')]"),
				"Gmail Login Page", true, WaitType.WAITFORELEMENTTOBEDISPLAYED,
				15);
	}

	private PageElement usernameTextField() {
		return new PageElement(By.id("Email"),
				"Username Text Field on Gmail Login page", true,
				WaitType.WAITFORELEMENTTOBEDISPLAYED, 15);
	}

	private PageElement nextButton() {
		return new PageElement(By.id("next"),
				"Next button on Gmail Login page", true,
				WaitType.WAITFORELEMENTTOBEDISPLAYED, 15);
	}

	private PageElement passwordTextField() {
		return new PageElement(By.id("Passwd"),
				"Password Text Field on Gmail Login page", true,
				WaitType.WAITFORELEMENTTOBEDISPLAYED, 15);
	}

	private PageElement signInButton() {
		return new PageElement(By.id("signIn"),
				"Sign In button on Gmail Login page", true,
				WaitType.WAITFORELEMENTTOBEDISPLAYED, 15);
	}

	public void signInToGmail() {
		sendKeys(usernameTextField(), gmailLoginData.getUserName());
		click(nextButton());
		sendKeys(passwordTextField(), gmailLoginData.getPassword());
		click(signInButton());
	}

	private GmailLoginPageData getLoginData(TestMap testMap) {
		FrameworkServices frameworkServices = new FrameworkServices();
		return (GmailLoginPageData) frameworkServices.getPageDataByTestMapCode(
				testMap.getGmailLoginPage(), GmailLoginPageData.class);
	}
}
