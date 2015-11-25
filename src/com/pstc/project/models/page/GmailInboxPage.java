package com.pstc.project.models.page;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.pstc.framework.FrameworkServices;
import com.pstc.framework.utilities.Page;
import com.pstc.framework.utilities.PageElement;
import com.pstc.framework.utilities.WaitType;
import com.pstc.project.models.data.GmailLandingPageData;
import com.pstc.project.models.data.GmailLoginPageData;
import com.pstc.project.models.data.TestMap;

public class GmailInboxPage extends Page {
	static Logger logger = Logger.getLogger(GmailInboxPage.class
			.getName());
	GmailLandingPageData gmailLandingPageData;

	public GmailInboxPage(WebDriver driver,TestMap testMap) {
		super(driver, GmailInboxPage.class.getSimpleName(), logger);
		gmailLandingPageData = getInboxData(testMap);

	}
	
	public GmailLandingPageData getGmailInboxData(){
		return gmailLandingPageData;
	}

	public static GmailInboxPage getInboxPageInstance(WebDriver driver,TestMap testMap)
			throws Throwable {
		GmailInboxPage gmailInboxPage = new GmailInboxPage(driver,testMap);
		gmailInboxPage.waitForHomePageToLoad();
		return gmailInboxPage;

	}

	public void waitForHomePageToLoad() {
		waitForPageElement(gmailMailTable());
	}

	private PageElement gmailMailTable() {
		return new PageElement(
				By.xpath("//div[@role='main']"),
				"Table with mails.", true,
				WaitType.WAITFORELEMENTTOBEDISPLAYED, 15);
	}

	private PageElement gmailSearchTextField() {
		return new PageElement(
				By.xpath("//input[@id='gbqfq']"),
				"Gmail search text field", true,
				WaitType.WAITFORELEMENTTOBEDISPLAYED, 15);
	}

	private PageElement navigateToNextPageLink() {
		return new PageElement(
				By.xpath("//div[@data-tooltip='Older']"),
				"Next page link for mails", true,
				WaitType.WAITFORELEMENTTOBEDISPLAYED, 15);
	}

	public void enterSearchValue() {
		sendKeys(gmailSearchTextField(), gmailLandingPageData.getSearch()+"\n");
	}

	public void clickNextPageLink() {
		click(navigateToNextPageLink());
	}

	public List<String> getListOfAllSubjects() {
		List<String> listOfSubjects = new ArrayList<String>();

		try
		{
			do
			{
				for(WebElement element :getWebElements(By.xpath("//tr//td[6]//b"), driver))
				{
					listOfSubjects.add(element.getText().trim());
				}
				if(isElementEnabled(navigateToNextPageLink()))
				{
				      clickNextPageLink();
				}
				else
				{
					break;
				}
			}
			while(getWebElements(By.xpath("//tr//td[6]//b"), driver).size()>0);
		}
		catch(NoSuchElementException e)
		{

		}
		return  listOfSubjects;
	}
	
	private GmailLandingPageData getInboxData(TestMap testMap) {
		FrameworkServices frameworkServices = new FrameworkServices();
		return (GmailLandingPageData) frameworkServices.getPageDataByTestMapCode(
				testMap.getGmailLandingPage(), GmailLandingPageData.class);
	}


}
