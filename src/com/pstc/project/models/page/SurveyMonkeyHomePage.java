package com.pstc.project.models.page;

import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.pstc.framework.utilities.Page;
import com.pstc.framework.utilities.PageElement;
import com.pstc.framework.utilities.WaitType;

public class SurveyMonkeyHomePage extends Page {
	static Logger logger = Logger.getLogger(SurveyMonkeyHomePage.class
			.getName());

	public SurveyMonkeyHomePage(WebDriver driver) {
		super(driver, SurveyMonkeyHomePage.class.getSimpleName(), logger);

	}

	public static SurveyMonkeyHomePage getHomePageInstance(WebDriver driver)
			throws Throwable {
		SurveyMonkeyHomePage surveyMonkeyHomePage = new SurveyMonkeyHomePage(
				driver);
		surveyMonkeyHomePage.waitForHomePageToLoad();
		return surveyMonkeyHomePage;

	}

	public void waitForHomePageToLoad() {
		waitForPageElement(surveyMonkeyHomePageTitle());
	}

	private PageElement surveyMonkeyHomePageTitle() {
		return new PageElement(
				By.xpath("//span[contains(text(),'SurveyMonkey')]"),
				"Survey monkey home page Title", true,
				WaitType.WAITFORELEMENTTOBEDISPLAYED, 15);
	}

	private PageElement signInButton() {
		return new PageElement(By.linkText("Sign In"),
				"Sign In button on Survey monkey home page", true,
				WaitType.WAITFORELEMENTTOBEDISPLAYED, 15);
	}

	public void signInToSurveyMonkey() {
		click(signInButton());
	}

}
