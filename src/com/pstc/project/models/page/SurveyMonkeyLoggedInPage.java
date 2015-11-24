package com.pstc.project.models.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.pstc.framework.utilities.Page;
import com.pstc.framework.utilities.PageElement;
import com.pstc.framework.utilities.WaitType;

public class SurveyMonkeyLoggedInPage extends Page {
	static Logger logger = Logger.getLogger(SurveyMonkeyLoggedInPage.class
			.getName());

	public SurveyMonkeyLoggedInPage(WebDriver driver) {
		super(driver, SurveyMonkeyLoggedInPage.class.getSimpleName(), logger);

	}

	public static SurveyMonkeyLoggedInPage getLoggedInPageInstance(WebDriver driver)
			throws Throwable {
		SurveyMonkeyLoggedInPage surveyMonkeyLoggedInPage = new SurveyMonkeyLoggedInPage(
				driver);
		surveyMonkeyLoggedInPage.waitForLoggedInPageToLoad();
		return surveyMonkeyLoggedInPage;

	}

	public void waitForLoggedInPageToLoad() {
		waitForPageElement(surveyMonkeyLoggedInPageUserName());
	}

	private PageElement surveyMonkeyLoggedInPageUserName() {
		return new PageElement(
				By.xpath(""),
				"Survey monkey Loggedin page Username", true,
				WaitType.WAITFORELEMENTTOBEDISPLAYED, 15);
	}

	public boolean verifySurveyMonkeyLoggedInPageUserNameExists() {
		return isElementDisplayed(surveyMonkeyLoggedInPageUserName());
	}

}
