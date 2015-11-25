package com.pstc.project.models.page;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.pstc.framework.utilities.Page;
import com.pstc.framework.utilities.PageElement;
import com.pstc.framework.utilities.WaitType;

public class GmailHomePage extends Page {
	static Logger logger = Logger.getLogger(GmailHomePage.class.getName());

	public GmailHomePage(WebDriver driver) {
		super(driver, GmailHomePage.class.getSimpleName(), logger);

	}

	public static GmailHomePage getGmailHomePageInstance(WebDriver driver)
			throws Throwable {
		GmailHomePage gmailHomePage = new GmailHomePage(driver);
		gmailHomePage.waitForGmailHomePageToLoad();
		return gmailHomePage;

	}

	public void waitForGmailHomePageToLoad() {
		waitForPageElement(gmailHomePage());
	}

	private PageElement gmailHomePage() {
		return new PageElement(
				By.xpath("//*[contains(text(),'One account. All of Google.')]"),
				"Gmail Home Page", true, WaitType.WAITFORELEMENTTOBEDISPLAYED,
				15);
	}

	private PageElement signInButton() {
		return new PageElement(By.linkText("Sign in"),
				"Sign In button on Gmail home page", true,
				WaitType.WAITFORELEMENTTOBEDISPLAYED, 15);
	}

	public void signInToGmail() {
		click(signInButton());
	}

}
