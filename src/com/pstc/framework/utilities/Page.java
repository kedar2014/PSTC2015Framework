package com.pstc.framework.utilities;

import java.util.ArrayList;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.pstc.framework.ConfigurationProperties;
import com.pstc.framework.FrameworkServices;
import com.pstc.framework.exception.ElementNotDisappearedInSpecifiedTimeException;
import com.pstc.framework.exception.ElementNotLoadedInSpecifiedTimeException;
import com.pstc.framework.exception.UnsuccessfulServiceException;

public abstract class Page {
	protected WebDriver driver;
	protected WebDriverWrapper webDriverWrapper;
	protected FrameworkServices frameworkServices;
	protected String pageName;
	protected Logger logger;
	protected String masterEntityPassword = "auto01";

	/*
	 * protected String masterEntityPassword;
	 * 
	 * public String getMasterEntityPassword() { return masterEntityPassword; }
	 * 
	 * public void setMasterEntityPassword(String masterEntityPassword) {
	 * this.masterEntityPassword = masterEntityPassword; }
	 */

	public Page(WebDriver driver, String pageName, Logger logger) {

		this.driver = driver;
		this.pageName = pageName;
		webDriverWrapper = new WebDriverWrapper(driver);
		frameworkServices = new FrameworkServices();
		this.logger = logger;
		// waitForLoaderInvisibility();
	}

	public Page() {
	}

	private WebElement waitForElementAndReturnElement(PageElement pageElement) {
		switch (pageElement.getWaitType()) {
		case WAITFORELEMENTTOBECLICKABLE:

			try {
				return webDriverWrapper.waitForElementToBeClickable(
						pageElement.getBy(), pageElement.getTimeOut());
			} catch (TimeoutException e) {
				throw new ElementNotLoadedInSpecifiedTimeException(
						pageElement.getName(), pageElement.getTimeOut(), e);
			}

		case WAITFORELEMENTTOBEENABLED:

			try {
				return webDriverWrapper.waitForElementToBeEnabled(
						pageElement.getBy(), pageElement.getTimeOut());
			} catch (TimeoutException e) {
				throw new ElementNotLoadedInSpecifiedTimeException(
						pageElement.getName(), pageElement.getTimeOut(), e);
			}

		case WAITFORELEMENTTOBEDISPLAYED:

			try {
				return webDriverWrapper.waitForElementToBeDisplayed(
						pageElement.getBy(), pageElement.getTimeOut());
			} catch (TimeoutException e) {
				throw new ElementNotLoadedInSpecifiedTimeException(
						pageElement.getName(), pageElement.getTimeOut(), e);
			}

		default:
			return driver.findElement(pageElement.getBy());
		}
	}

	protected void waitForPageElement(PageElement pageElement) {

		boolean isWebElementAvailableInPageElement = isWebElementAvailableInPageElement(pageElement);
		switch (pageElement.getWaitType()) {
		case WAITFORELEMENTTOBECLICKABLE:
			try {
				if (!isWebElementAvailableInPageElement)
					webDriverWrapper.waitForElementToBeClickable(
							pageElement.getBy(), pageElement.getTimeOut());
				else
					webDriverWrapper.waitForElementToBeClickable(
							pageElement.getWebElement(),
							pageElement.getTimeOut());

			} catch (TimeoutException e) {
				throw new ElementNotLoadedInSpecifiedTimeException(
						pageElement.getName(), pageElement.getTimeOut(), e);
			}
			break;
		case WAITFORELEMENTTOBEENABLED:

			try {
				if (!isWebElementAvailableInPageElement)
					webDriverWrapper.waitForElementToBeEnabled(
							pageElement.getBy(), pageElement.getTimeOut());
				else
					webDriverWrapper.waitForElementToBeEnabled(
							pageElement.getWebElement(),
							pageElement.getTimeOut());

			} catch (TimeoutException e) {
				throw new ElementNotLoadedInSpecifiedTimeException(
						pageElement.getName(), pageElement.getTimeOut(), e);
			}
			break;
		case WAITFORELEMENTTOBEDISPLAYED:

			try {
				if (!isWebElementAvailableInPageElement)
					webDriverWrapper.waitForElementToBeDisplayed(
							pageElement.getBy(), pageElement.getTimeOut());
				else
					webDriverWrapper.waitForElementToBeDisplayed(
							pageElement.getWebElement(),
							pageElement.getTimeOut());

			} catch (TimeoutException e) {
				throw new ElementNotLoadedInSpecifiedTimeException(
						pageElement.getName(), pageElement.getTimeOut(), e);
			}
			break;

		case WAITFORELEMENTTODISAPPEAR:
			try {
				if (!isWebElementAvailableInPageElement)
					webDriverWrapper.waitForElementToDisapper(
							pageElement.getBy(), pageElement.getTimeOut());
				else {
					webDriverWrapper.waitForElementToDisapper(
							pageElement.getWebElement(),
							pageElement.getTimeOut());
				}

			} catch (TimeoutException e) {
				throw new ElementNotDisappearedInSpecifiedTimeException(
						pageElement.getName(), pageElement.getTimeOut(), e);
			}
			break;
		default:
			break;
		}

	}

	private boolean isWebElementAvailableInPageElement(PageElement pageElement) {
		return !(pageElement.getWebElement() == null);
	}

	protected WebElement getWebElement(PageElement pageElement) {
		if (pageElement.isSlowLoadableComponent()) {
			return waitForElementAndReturnElement(pageElement);

		} else
			return driver.findElement(pageElement.getBy());

	}

	protected void sendKeys(PageElement pageElement, String value) {
		try {
			waitForLoaderInvisibility();
			value = (value == null) ? "" : value;
			if (!isWebElementAvailableInPageElement(pageElement))
				getWebElement(pageElement).sendKeys(value);
			else
				pageElement.getWebElement().sendKeys(value);
			frameworkServices.logMessage("Typed Value: " + value + "' in "
					+ pageElement.getName(), logger);
		} catch (Exception exception) {
			throw new UnsuccessfulServiceException("Failed to type value: '"
					+ value + "' in " + pageElement.getName() + " on : '"
					+ pageName + "'", exception);
		} finally {
			pageElement = null;
		}
	}

	protected void sendKeys(PageElement pageElement, Keys key) {
		try {
			waitForLoaderInvisibility();
			if (!isWebElementAvailableInPageElement(pageElement))
				getWebElement(pageElement).sendKeys(key);
			else
				pageElement.getWebElement().sendKeys(key);
			frameworkServices.logMessage("Typed Value: " + key + "' in "
					+ pageElement.getName(), logger);
		} catch (Exception exception) {
			throw new UnsuccessfulServiceException("Failed to press : '" + key
					+ "' in " + pageElement.getName() + " on : '" + pageName
					+ "'", exception);
		} finally {
			pageElement = null;
		}
	}

	protected void clearAndSendKeys(PageElement pageElement, String value) {

		try {
			waitForLoaderInvisibility();
			value = (value == null) ? "" : value;
			WebElement element;

			for (int i = 1; i <= 5; i++) {
				bringElementInViewByDrag(getWebElement(pageElement));
				if (!isWebElementAvailableInPageElement(pageElement))
					element = getWebElement(pageElement);
				else
					element = pageElement.getWebElement();

				element.clear();
				element.sendKeys(value);
				if (element.getAttribute("value").equalsIgnoreCase(value)
						|| element.getAttribute("value").toUpperCase()
								.contains(value.toUpperCase())) {
					break;
				}
			}
			frameworkServices.logMessage("Cleared and Typed Value: " + value
					+ "' in " + pageElement.getName(), logger);
		} catch (Exception exception) {
			throw new UnsuccessfulServiceException(
					"Failed to clear and  type value: '" + value + "' in "
							+ pageElement.getName() + " on : '" + pageName
							+ "'", exception);

		} finally {
			pageElement = null;
		}
	}

	protected void waitForLoaderInvisibility() {
		try {
			/*
			 * //ORIGINAL CODE WebDriverWait webDriverWait=new
			 * WebDriverWait(driver, 300);
			 * webDriverWait.until(ExpectedConditions.
			 * invisibilityOfElementLocated(By.xpath(
			 * "//div[contains(@class,'ajaxbg')]")));
			 */

			/*
			 * //EDITED CODE by NBS to allow wait for 3 attempts
			 */
			WebDriverWait webDriverWait = new WebDriverWait(driver, 300);
			WebElement element = driver.findElement(By
					.xpath("//div[contains(@class,'ajaxbg')]"));
			for (int counter = 0; counter < 3; counter++) {
				webDriverWait.until(ExpectedConditions
						.invisibilityOfElementLocated(By
								.xpath("//div[contains(@class,'ajaxbg')]")));
				if (!element.isDisplayed())
					break;
			}
		} catch (Exception exception) {
			// throw new UnsuccessfulServiceException("Could not handle loader
			// properly :", exception);
		}
	}

	protected void click(PageElement pageElement) {
		try {
			waitForLoaderInvisibility();
			pageElement.setWebElement(getWebElement(pageElement));

			WebElement element;
			if (!isWebElementAvailableInPageElement(pageElement))
				element = getWebElement(pageElement);
			else
				element = pageElement.getWebElement();

			for (int i = 1; i <= 5; i++) {
				bringElementInViewByDrag(element);
				Thread.sleep(2000);
				if (isElementDisplayed(pageElement)) {
					break;
				}

				/*
				 * if(!isElementDisplayed(pageElement)) {
				 * bringElementInViewByDrag(element); Thread.sleep(2000); } else
				 * { break; }
				 */
				i++;
			}
			element.click();
			webDriverWrapper.explicitWait(1);

			frameworkServices.logMessage(
					"Clicked on: " + pageElement.getName(), logger);
		} catch (Exception exception) {
			throw new UnsuccessfulServiceException("Failed to click on : '"
					+ pageElement.getName() + "' on : '" + pageName + "' ",
					exception);
		} finally {
			pageElement = null;
		}
	}

	protected void clickAndRetry(PageElement pageElement) {
		try {
			waitForLoaderInvisibility();
			WebDriverWait wait = new WebDriverWait(driver, 5);
			wait.until(ExpectedConditions.elementToBeClickable(pageElement
					.getBy()));
			for (int i = 0; i <= 2; i++) {
				try {
					click(pageElement);
					break;

				} catch (Exception e) {
					webDriverWrapper.wait(1);
				}
			}
			frameworkServices.logMessage(
					"Clicked on: " + pageElement.getName(), logger);
		} catch (Exception exception) {
			throw new UnsuccessfulServiceException("Failed to click on : '"
					+ pageElement.getName() + "' on : '" + pageName + "' ",
					exception);
		} finally {
			pageElement = null;
		}
	}

	protected void clickWithoutBringInView(PageElement pageElement) {
		try {
			waitForLoaderInvisibility();
			pageElement.setWebElement(getWebElement(pageElement));
			WebElement element;

			if (!isWebElementAvailableInPageElement(pageElement))
				element = getWebElement(pageElement);
			else
				element = pageElement.getWebElement();

			element.click();
			frameworkServices.logMessage(
					"Clicked on: " + pageElement.getName(), logger);
		} catch (Exception exception) {
			throw new UnsuccessfulServiceException("Failed to click on : '"
					+ pageElement.getName() + "' on : '" + pageName + "' ",
					exception);
		} finally {
			pageElement = null;
		}
	}

	protected void check(PageElement pageElement) {
		try {
			waitForLoaderInvisibility();
			WebElement webElement;
			if (!isWebElementAvailableInPageElement(pageElement))
				webElement = getWebElement(pageElement);
			else
				webElement = pageElement.getWebElement();
			if (!webElement.isSelected())
				webElement.click();
			frameworkServices.logMessage("Checked  " + pageElement.getName(),
					logger);
		} catch (Exception exception) {
			throw new UnsuccessfulServiceException("Failed to check: '"
					+ pageElement.getName() + " on : '" + pageName + "' ",
					exception);
		} finally {
			pageElement = null;
		}
	}

	protected void checkOrUncheck(String config, PageElement pageElement) {
		try {
			waitForLoaderInvisibility();
			WebElement webElement;
			if (!isWebElementAvailableInPageElement(pageElement))
				webElement = getWebElement(pageElement);
			else
				webElement = pageElement.getWebElement();

			if (config.equalsIgnoreCase("yes") && !webElement.isSelected())
				webElement.click();
			else if (config.equalsIgnoreCase("no") && webElement.isSelected())
				webElement.click();

			frameworkServices.logMessage("Checked  " + pageElement.getName(),
					logger);
		} catch (Exception exception) {
			throw new UnsuccessfulServiceException("Failed to check: '"
					+ pageElement.getName() + " on : '" + pageName + "' ",
					exception);
		} finally {
			pageElement = null;
		}
	}

	protected void checkOrUncheckAndRetry(String config, PageElement pageElement) {
		try {
			waitForLoaderInvisibility();
			WebElement webElement;
			if (!isWebElementAvailableInPageElement(pageElement))
				webElement = getWebElement(pageElement);
			else
				webElement = pageElement.getWebElement();

			for (int i = 0; i < 3; i++) {
				try {
					if (webElement.isDisplayed() && webElement.isEnabled()) {
						if (config.equalsIgnoreCase("yes")
								&& !webElement.isSelected()) {
							webElement.click();
							frameworkServices.logMessage("Checked  "
									+ pageElement.getName(), logger);
						} else if (config.equalsIgnoreCase("no")
								&& webElement.isSelected()) {
							webElement.click();
							frameworkServices.logMessage("Unchecked  "
									+ pageElement.getName(), logger);
						} else
							break;
					}
				} catch (Exception e) {
					webDriverWrapper.explicitWait(2);
				}
			}

		} catch (Exception exception) {
			throw new UnsuccessfulServiceException(
					"Failed to click Checkfield : '" + pageElement.getName()
							+ " on : '" + pageName + "' ", exception);
		} finally {
			pageElement = null;
		}
	}

	protected void checkAndRetry(PageElement pageElement) {
		try {
			waitForLoaderInvisibility();
			WebElement webElement;
			if (!isWebElementAvailableInPageElement(pageElement))
				webElement = getWebElement(pageElement);
			else
				webElement = pageElement.getWebElement();
			for (int i = 0; i <= 9; i++) {
				if (!webElement.isSelected())
					webElement.click();
				else
					break;
			}
			frameworkServices.logMessage("Checked  " + pageElement.getName(),
					logger);
		} catch (Exception exception) {
			throw new UnsuccessfulServiceException("Failed to check: '"
					+ pageElement.getName() + " on : '" + pageName + "' ",
					exception);
		} finally {
			pageElement = null;
		}
	}

	protected void unCheck(PageElement pageElement) {
		try {
			waitForLoaderInvisibility();
			WebElement webElement;
			if (!isWebElementAvailableInPageElement(pageElement))
				webElement = getWebElement(pageElement);
			else
				webElement = pageElement.getWebElement();
			if (webElement.isSelected())
				webElement.click();
			frameworkServices.logMessage("Unchecked  " + pageElement.getName(),
					logger);
		} catch (Exception exception) {
			throw new UnsuccessfulServiceException("Failed to un check: '"
					+ pageElement.getName() + " on : '" + pageName + "' ",
					exception);
		} finally {
			pageElement = null;
		}
	}

	@Deprecated
	protected String acceptAlertAndReturnConfirmationMessage() {
		try {

			String confirmationMessage = driver.switchTo().alert().getText();
			driver.switchTo().alert().accept();
			frameworkServices.logMessage("fetched text: " + confirmationMessage
					+ " from alert box and accepted", logger);
			return confirmationMessage;
		} catch (Exception exception) {
			throw new UnsuccessfulServiceException(
					"failed to  accept alert on " + pageName + "' ", exception);
		}
	}

	protected String acceptAlertAndReturnConfirmationMessage(int timeOut) {
		try {
			timeOut = 75;
			String confirmationMessage = webDriverWrapper.acceptAlert(timeOut);
			frameworkServices.logMessage("fetched text: " + confirmationMessage
					+ " from alert box and accepted", logger);
			return confirmationMessage;
		} catch (Exception exception) {
			throw new UnsuccessfulServiceException(
					"failed to  accept alert on " + pageName + "' ", exception);
		}
	}

	protected String dismissAlertAndReturnConfirmationMessage() {
		try {
			String confirmationMessage = driver.switchTo().alert().getText();
			driver.switchTo().alert().dismiss();
			frameworkServices.logMessage("fetched text: " + confirmationMessage
					+ " from alert box and accepted", logger);
			return confirmationMessage;
		} catch (Exception exception) {
			throw new UnsuccessfulServiceException(
					"failed to  dismiss alert on " + pageName + "' ", exception);
		}
	}

	protected void doubleClick(PageElement pageElement) {
		try {
			bringElementInViewByDrag(getWebElement(pageElement));
			WebElement webElement;
			if (!isWebElementAvailableInPageElement(pageElement))
				webElement = getWebElement(pageElement);
			else
				webElement = pageElement.getWebElement();
			Actions actionBuilder = new Actions(driver);
			actionBuilder.doubleClick(webElement).build().perform();
			frameworkServices.logMessage(
					"Double Clicked on: " + pageElement.getName(), logger);
		} catch (Exception exception) {
			throw new UnsuccessfulServiceException("Failed to doubleclick: '"
					+ "' on " + pageElement.getName() + " on : '" + pageName
					+ "' ", exception);
		} finally {
			pageElement = null;
		}
	}

	protected String getText(PageElement pageElement) {
		String text = new String();
		try {
			bringElementInViewByDrag(getWebElement(pageElement));
			WebElement webElement;
			if (!isWebElementAvailableInPageElement(pageElement))
				webElement = getWebElement(pageElement);
			else
				webElement = pageElement.getWebElement();

			text = webElement.getText().trim();
			frameworkServices.logMessage("Fetched text: " + text + " of "
					+ pageElement.getName(), logger);
		} catch (Exception exception) {
			throw new UnsuccessfulServiceException("Failed to fetch text: '"
					+ "' of " + pageElement.getName() + " on : '" + pageName
					+ "' ", exception);

		} finally {
			pageElement = null;
		}
		return text;
	}

	protected String getAttribute(PageElement pageElement, String attributeName) {
		String atributeValue = new String("");
		try {
			bringElementInViewByDrag(getWebElement(pageElement));
			WebElement webElement;
			if (!isWebElementAvailableInPageElement(pageElement))
				webElement = getWebElement(pageElement);
			else
				webElement = pageElement.getWebElement();
			atributeValue = webElement.getAttribute(attributeName).trim();
			frameworkServices.logMessage("Fetched " + attributeName + ":"
					+ atributeValue + " of " + pageElement.getName(), logger);

		} catch (Exception exception) {
			frameworkServices.logMessage("<B>Failed to fetch '" + attributeName
					+ "' of " + pageElement.getName() + " on : '" + pageName
					+ "'</B> ", logger);

		} finally {
			pageElement = null;
		}
		return atributeValue;
	}

	protected boolean isElementDisplayed(PageElement pageElement) {

		boolean isElementDisplayed = false;
		try {
			WebElement webElement;
			if (!isWebElementAvailableInPageElement(pageElement))
				webElement = getWebElement(pageElement);
			else
				webElement = pageElement.getWebElement();

			isElementDisplayed = webElement.isDisplayed();
			/*
			 * frameworkServices.logMessage(pageElement.getName() +
			 * " is Displayed ", logger);
			 */
		} catch (Exception e) {
			frameworkServices.logMessage(pageElement.getName()
					+ " is not Displayed ", logger);
		} finally {
			pageElement = null;
		}
		return isElementDisplayed;

	}

	public static boolean isConfigTrue(String config) {
		if (config != null && config.toLowerCase().contains("y"))
			return true;
		else
			return false;

	}

	public static boolean isVerifyTrue(String config) {
		return (config != null && config.toLowerCase().contains("verify"));

	}

	/*
	 * public static boolean isHelperConfigTrue(Object pageObject, String
	 * iteration) { DataBin bin = (DataBin) pageObject; return
	 * bin.getIterationNo().equals(iteration) &&
	 * !bin.getDataType().toLowerCase().contains("verify") &&
	 * bin.getExecuteFlag().toLowerCase().contains("yes");
	 * 
	 * }
	 */

	/*
	 * public static boolean isHelperConfigTrue(Object pageObject, String
	 * iteration, String groupName) { DataBin bin = (DataBin) pageObject; if
	 * (groupName == null || groupName.equals("")) { return
	 * bin.getIterationNo().equals(iteration) &&
	 * !bin.getDataType().toLowerCase().contains("verify") &&
	 * bin.getExecuteFlag().toLowerCase().contains("yes"); } else {
	 * 
	 * 
	 * if(bin.getIterationNo().equals(iteration) &&
	 * !bin.getDataType().toLowerCase().contains("verify") &&
	 * bin.getExecuteFlag().toLowerCase().contains("yes")){
	 * 
	 * String groupNameOfTestData = bin.getDataType().split(":")[1]
	 * .toLowerCase(); return
	 * groupNameOfTestData.equals(groupName.toLowerCase()); }else { return
	 * false; } String groupNameOfTestData = bin.getDataType().split(":")[1]
	 * .toLowerCase(); return
	 * groupNameOfTestData.equals(groupName.toLowerCase()) &&
	 * bin.getIterationNo().equals(iteration) &&
	 * !bin.getDataType().toLowerCase().contains("verify") &&
	 * bin.getExecuteFlag().toLowerCase().contains("yes");
	 * 
	 * } }
	 */

	/*
	 * public static boolean isVerifyConfigTrue(Object pageObject, String
	 * iteration, String groupName) { DataBin bin = (DataBin) pageObject; if
	 * (groupName == null || groupName.equals("")) { return
	 * bin.getIterationNo().equals(iteration) &&
	 * bin.getDataType().toLowerCase().contains("verify") &&
	 * bin.getExecuteFlag().toLowerCase().contains("yes"); } else { String
	 * groupNameOfTestData = bin.getDataType().split(":")[1] .toLowerCase();
	 * return groupNameOfTestData.equals(groupName.toLowerCase()) &&
	 * bin.getIterationNo().equals(iteration) &&
	 * bin.getDataType().toLowerCase().contains("verify") &&
	 * bin.getExecuteFlag().toLowerCase().contains("yes");
	 * 
	 * } }
	 */
	public boolean isActionTrue(String config) {
		return (config.toLowerCase().equals("add")
				|| config.toLowerCase().contains("edit:") || config
				.toLowerCase().contains("verify:"));

	}

	public boolean isDeleteActionTrue(String config) {
		return (config.toLowerCase().contains("delete:"));

	}

	public boolean isActionAddAndIgnoreSaveTrue(String config) {
		return (config.toLowerCase().equals("addandignoresave"));
	}

	protected boolean isElementSelected(PageElement pageElement) {

		boolean isElementDisplayed = false;

		try {
			WebElement webElement;
			if (!isWebElementAvailableInPageElement(pageElement))
				webElement = getWebElement(pageElement);
			else
				webElement = pageElement.getWebElement();
			isElementDisplayed = webElement.isSelected();
			frameworkServices.logMessage(pageElement.getName()
					+ " is Displayed ", logger);
		} catch (Exception e) {
			frameworkServices.logMessage(pageElement.getName()
					+ " is not Displayed ", logger);
		} finally {
			pageElement = null;
		}
		return isElementDisplayed;

	}

	protected boolean isElementEnabled(PageElement pageElement) {

		boolean isElementEnabled = false;

		try {
			WebElement webElement;
			if (!isWebElementAvailableInPageElement(pageElement))
				webElement = getWebElement(pageElement);
			else
				webElement = pageElement.getWebElement();
			isElementEnabled = webElement.isEnabled();
			frameworkServices.logMessage(
					pageElement.getName() + " is Enabled ", logger);
		} catch (Exception e) {
			frameworkServices.logMessage(pageElement.getName()
					+ " is not Enabled ", logger);
		} finally {
			pageElement = null;
		}
		return isElementEnabled;

	}

	protected void selectValueFromList(PageElement pageElement, String value) {
		try {
			waitForLoaderInvisibility();
			bringElementInViewByDrag(getWebElement(pageElement));

			WebElement webElement;
			if (!isWebElementAvailableInPageElement(pageElement))
				webElement = getWebElement(pageElement);
			else
				webElement = pageElement.getWebElement();

			webDriverWrapper.waitForOptionToBePopulatedInList(webElement, 60);
			webDriverWrapper.waitForOptionToBePresentInList(
					pageElement.getBy(), value, 60);
			Select select = new Select(webElement);
			select.selectByVisibleText(value);

			frameworkServices.logMessage("Selected Value: " + value + "' in "
					+ pageElement.getName(), logger);
		} catch (Exception exception) {
			throw new UnsuccessfulServiceException("Failed to Select value: '"
					+ value + "' of " + pageElement.getName() + " on : '"
					+ pageName + "'", exception);
		} finally {
			pageElement = null;
		}
	}

	protected void selectValueFromListAndRetry(PageElement pageElement,
			String value) {
		try {
			waitForLoaderInvisibility();
			WebElement webElement;
			if (!isWebElementAvailableInPageElement(pageElement))
				webElement = getWebElement(pageElement);
			else
				webElement = pageElement.getWebElement();

			webDriverWrapper.waitForOptionToBePopulatedInList(webElement, 60);

			Select select = new Select(webElement);
			for (int i = 0; i <= 2; i++) {
				try {
					select.selectByVisibleText(value);
					break;
				} catch (Exception exception) {
					if (i == 2)
						throw new UnsuccessfulServiceException(
								"Failed to Select value: '" + value + "' of "
										+ pageElement.getName() + " on : '"
										+ pageName + "'", exception);
				}
			}
			frameworkServices.logMessage("Selected Value: " + value + "' in "
					+ pageElement.getName(), logger);
		} catch (Exception exception) {
			throw new UnsuccessfulServiceException("Failed to Select value: '"
					+ value + "' of " + pageElement.getName() + " on : '"
					+ pageName + "'", exception);
		} finally {
			pageElement = null;
		}
	}

	protected void selectDateAndRetry(PageElement pageElement, String value) {
		String date = "";
		try {
			waitForLoaderInvisibility();
			value = (value == null) ? "" : value;
			WebElement element;
			date = RandomCodeGenerator.dateGenerator(value, "dd-MMM-yyyy");
			for (int i = 1; i <= 3; i++) {
				bringElementInViewByDrag(getWebElement(pageElement));
				if (!isWebElementAvailableInPageElement(pageElement))
					element = getWebElement(pageElement);
				else
					element = pageElement.getWebElement();

				element.clear();
				if (webDriverWrapper.isAlertPresent()) {
					acceptAlertAndReturnConfirmationMessage(2);
				}
				// sendKeys(pageElement,date);
				// sendKeys(pageElement,Keys.TAB);
				element.sendKeys(date);
				element.sendKeys(Keys.TAB);
				if (!webDriverWrapper.isAlertPresent()) {
					if (element.getAttribute("value").equalsIgnoreCase(date)) {
						break;
					}
				} else {
					break;
				}
			}
			frameworkServices.logMessage("Cleared and Typed Date: " + date
					+ "' in " + pageElement.getName(), logger);
		} catch (Exception exception) {
			throw new UnsuccessfulServiceException(
					"Failed to clear and  type Date: '" + date + "' in "
							+ pageElement.getName() + " on : '" + pageName
							+ "'", exception);
		} finally {
			pageElement = null;
		}

		/*
		 * String date=""; try { waitForLoaderInvisibility(); CalendarComponent
		 * calendarComponent = new CalendarComponent(driver); for (int i = 0; i
		 * <= 2; i++) { try { date=RandomCodeGenerator.dateGenerator(value);
		 * clickAndRetry(pageElement); //click(pageElement); Thread.sleep(3000);
		 * calendarComponent.waitForCalendarVisiblity();
		 * calendarComponent.selectDateFromCalendar(date); break; } catch
		 * (Exception e) { } } frameworkServices.logMessage("Selected date: '" +
		 * date + "' in " + pageElement.getName(), logger); } catch (Exception
		 * exception) { throw new UnsuccessfulServiceException(
		 * "Failed to Select date: '" + date + "' of " + pageElement.getName() +
		 * " on : '" + pageName + "'", exception); } finally { pageElement =
		 * null; }
		 */
	}

	protected void selectDateFromCalendarWindowAndRetry(
			PageElement pageElement, String value) {
		String date = "";
		try {
			waitForLoaderInvisibility();
			CalendarComponent calendarComponent = new CalendarComponent(driver);
			for (int i = 0; i <= 2; i++) {
				try {
					date = RandomCodeGenerator.dateGenerator(value);
					clickAndRetry(pageElement);
					// click(pageElement);
					Thread.sleep(3000);
					calendarComponent.waitForCalendarVisiblity();
					calendarComponent.selectDateFromCalendar(date);
					break;
				} catch (Exception e) {
				}
			}
			frameworkServices.logMessage("Selected date: '" + date + "' in "
					+ pageElement.getName(), logger);
		} catch (Exception exception) {
			throw new UnsuccessfulServiceException("Failed to Select date: '"
					+ date + "' of " + pageElement.getName() + " on : '"
					+ pageName + "'", exception);
		} finally {
			pageElement = null;
		}
	}

	protected void selectPanelDateAndRetry(String value) {
		String date = "";
		try {
			Thread.sleep(3000);
			waitForLoaderInvisibility();
			CalendarComponent calendarComponent = new CalendarComponent(driver);
			for (int i = 0; i <= 2; i++) {
				try {
					date = RandomCodeGenerator.dateGenerator(value);
					calendarComponent.waitForCalendarVisiblity();
					calendarComponent.selectDateFromCalendar(date);
					break;
				} catch (Exception e) {
				}
			}
			frameworkServices.logMessage("Selected Date: '" + date + "'",
					logger);
		} catch (Exception exception) {
			throw new UnsuccessfulServiceException("Failed to Select date: '"
					+ date + "' on : '" + pageName + "'", exception);
		}
	}

	protected void mouseOver(PageElement pageElement) {
		try {

			WebElement webElement;
			if (!isWebElementAvailableInPageElement(pageElement))
				webElement = getWebElement(pageElement);
			else
				webElement = pageElement.getWebElement();

			Actions actionBuilder = new Actions(driver);
			actionBuilder.moveToElement(webElement).build().perform();
			frameworkServices.logMessage(
					"Hoverd mouse on: " + pageElement.getName(), logger);

		} catch (Exception exception) {
			throw new UnsuccessfulServiceException("Failed to hover mouse: '"
					+ "' on " + pageElement.getName() + " on : '" + pageName
					+ "' ", exception);
		} finally {
			pageElement = null;
		}
	}

	protected String getSelectedValueFromList(PageElement pageElement) {

		try {
			bringElementInViewByDrag(getWebElement(pageElement));
			WebElement webElement;
			if (!isWebElementAvailableInPageElement(pageElement))
				webElement = getWebElement(pageElement);
			else
				webElement = pageElement.getWebElement();

			Select selectType = new Select(webElement);
			String selectedValue = selectType.getFirstSelectedOption()
					.getText();

			frameworkServices.logMessage("Fetched " + selectedValue + "  "
					+ " of " + pageElement.getName(), logger);
			return selectedValue;
		} catch (Exception exception) {
			throw new UnsuccessfulServiceException("Failed to fetch "
					+ "Selected Value" + "' of " + pageElement.getName()
					+ " on : '" + pageName + "' ", exception);
		} finally {
			pageElement = null;
		}
	}

	protected String getSelectedValueFromListWithoutBringInView(
			PageElement pageElement) {

		try {
			WebElement webElement;
			if (!isWebElementAvailableInPageElement(pageElement))
				webElement = getWebElement(pageElement);
			else
				webElement = pageElement.getWebElement();

			Select selectType = new Select(webElement);
			String selectedValue = selectType.getFirstSelectedOption()
					.getText();

			frameworkServices.logMessage("Fetched " + selectedValue + "  "
					+ " of " + pageElement.getName(), logger);
			return selectedValue;
		} catch (Exception exception) {
			throw new UnsuccessfulServiceException("Failed to fetch "
					+ "Selected Value" + "' of " + pageElement.getName()
					+ " on : '" + pageName + "' ", exception);
		} finally {
			pageElement = null;
		}
	}

	protected ArrayList<String> getAllOptionsInList(PageElement pageElement) {

		try {
			bringElementInViewByDrag(getWebElement(pageElement));
			WebElement webElement;
			if (!isWebElementAvailableInPageElement(pageElement))
				webElement = getWebElement(pageElement);
			else
				webElement = pageElement.getWebElement();
			Select selectList = new Select(webElement);
			ArrayList<String> optionList = new ArrayList<String>();
			for (WebElement option : selectList.getOptions()) {
				optionList.add(option.getText().trim());
			}

			frameworkServices.logMessage("Fetched " + optionList + "  "
					+ " of " + pageElement.getName(), logger);
			return optionList;
		} catch (Exception exception) {
			throw new UnsuccessfulServiceException("Failed to fetch "
					+ "option Value" + "' of " + pageElement.getName()
					+ " on : '" + pageName + "' ", exception);
		} finally {
			pageElement = null;
		}
	}

	protected void bringElementInView(PageElement pageElement) {

		try {
			WebElement webElement;
			if (!isWebElementAvailableInPageElement(pageElement))
				webElement = getWebElement(pageElement);
			else
				webElement = pageElement.getWebElement();

			((JavascriptExecutor) driver).executeScript(
					"arguments[0].scrollIntoView(true);", webElement);

			frameworkServices.logMessage("Brought  " + pageElement.getName()
					+ "  " + " in the current browser view ", logger);

		} catch (Exception exception) {
			throw new UnsuccessfulServiceException("Failed to bring Element  "
					+ pageElement.getName() + "  "
					+ " in the current browser view " + " on : '" + pageName
					+ "' ", exception);
		} finally {
			pageElement = null;
		}

	}

	private PageElement pageLoader() {
		return new PageElement(By.xpath("//div[contains(@class,'ajaxbg')]"),
				"Page Loader", false, WaitType.WAITFORELEMENTTOBEDISPLAYED, 2);
	}

	private PageElement pageScroll() {
		return new PageElement(
				By.xpath("//div[contains(@class,'dragger_bar')]"),
				// By.xpath("//p[contains(text(),'LOB:')]"),
				// By.xpath("//div[contains(@class,'_draggerContainer')]//div[contains(@class,'_dragger')]"),
				"Scroll Down", false, WaitType.WAITFORELEMENTTOBEDISPLAYED, 2);
	}

	private PageElement pageScrollDown() {
		return new PageElement(
				By.xpath("//a[contains(@class,'mCSB_buttonDown')]"),
				// By.xpath("//div[contains(@class,'_draggerContainer')]//div[contains(@class,'_dragger')]"),
				"Scroll Down", false, WaitType.WAITFORELEMENTTOBEENABLED, 5);
	}

	private PageElement NEWROW() {
		return new PageElement(By.className("jq-icon-plus"),
		// By.xpath("//div[contains(@class,'_draggerContainer')]//div[contains(@class,'_dragger')]"),
				"Scroll Down", false, WaitType.WAITFORELEMENTTOBEDISPLAYED, 2);
	}

	private PageElement keyboardShortcutDivTag() {
		return new PageElement(
				By.xpath("//div[contains(@class,'keyboardlist')]"),
				"Scroll Down", false, WaitType.WAITFORELEMENTTOBEDISPLAYED, 2);
	}

	private PageElement keyboardListLabel() {
		return new PageElement(
				By.xpath("//a[contains(label,'Keyboard Shortcut')]"),
				"Keyboard shortcut Link ", false,
				WaitType.WAITFORELEMENTTOBEENABLED, 10);
	}

	public void removeKeyboardShortcutAndClick(PageElement pageElement)
			throws InterruptedException {
		for (int j = 0; j <= 5; j++) {
			bringElementInViewByDrag(getWebElement(pageElement));
			// String styleValue = getAttribute(keyboardListLabel(),"style");
			// mouseOver(keyboardListLabel());
			String styleValue = getAttribute(keyboardShortcutDivTag(), "style");
			if (styleValue.contains("block")) {
				Thread.sleep(2000);
				mouseOver(keyboardListLabel());
				// styleValue = getAttribute(keyboardListLabel(),"style");
				styleValue = getAttribute(keyboardShortcutDivTag(), "style");
				if (styleValue.contains("none") || styleValue.equals("")) {
					clickWithoutBringInView(pageElement);
					Thread.sleep(2000);
					break;
				} else {
					// wait.wait();
					continue;
				}
			} else {
				clickWithoutBringInView(pageElement);
				break;
			}

		}

		/*
		 * try { ((JavascriptExecutor)driver).executeScript(
		 * "document.getElementsByClassName('keyboardmenu')[0].setAttribute('style', ' ')"
		 * ); waitForLoaderInvisibility(); WebDriverWait wait=new
		 * WebDriverWait(driver, 5);
		 * wait.until(ExpectedConditions.elementToBeClickable(pageElement.getBy(
		 * ))); for (int i = 0; i <= 2; i++) { try { click(pageElement); break;
		 * 
		 * } catch (Exception e) { webDriverWrapper.wait(1); } }
		 * frameworkServices.logMessage( "Clicked on: " + pageElement.getName(),
		 * logger); } catch (Exception exception) { throw new
		 * UnsuccessfulServiceException("Failed to click on : '" +
		 * pageElement.getName() + "' on : '" + pageName + "' ", exception); }
		 * finally { pageElement = null; }
		 */
	}

	public void clickScrollDownButton(int times) {
		for (int i = 0; i < times; i++) {

			clickWithoutBringInView(pageScrollDown());

		}

	}

	public void bringElementInViewByDrag(WebElement targetElement) {
		try {
			if (isElementDisplayed(pageScroll())) {
				/*
				 * Locatable locatableElement=(Locatable) targetElement; int
				 * x=locatableElement.getCoordinates().onPage().x; int
				 * y=locatableElement.getCoordinates().onPage().y;
				 */

				webDriverWrapper.explicitWait(2);

				// while(!targetElement.isDisplayed()){
				// Selenium selenium = new WebDriverBackedSelenium(driver,"");
				// FrameworkServices.getWebDriverBackedSeleniumInstance(driver).dragAndDropToObject("xpath="+getWebElement(pageScroll()).toString(),targetElement.toString());
				// if(!targetElement.isDisplayed())

				Actions dragdrop = new Actions(driver);
				dragdrop.dragAndDrop(getWebElement(pageScroll()), targetElement)
						.build().perform();

				// dragdrop.moveToElement(targetElement).build().perform();

				// dragdrop.moveToElement(targetElement).build().perform();
				// try {
				webDriverWrapper.explicitWait(2);
				
			}

		} catch (Exception exception) {
			throw new UnsuccessfulServiceException("Failed to bring Element  "
					+ pageScroll().getName() + "  "
					+ " in the current browser view " + " on : '" + pageName
					+ "' ", exception);
		} finally {
			// pageScrollDown() = null;
		}

	}

	/*
	 * protected void mouseOverByFireEvent(PageElement pageElement) {
	 * 
	 * try { WebElement webElement; if
	 * (!isWebElementAvailableInPageElement(pageElement)) webElement =
	 * getWebElement(pageElement); else webElement =
	 * pageElement.getWebElement();
	 * 
	 * Selenium selenium = FrameworkServices
	 * .getWebDriverBackedSeleniumInstance(driver);
	 * selenium.fireEvent(pageElement.getIdentifierString(), "mousemove");
	 * 
	 * frameworkServices.logMessage( "Moved mouse on  " + pageElement.getName()
	 * + "  " + "  ", logger);
	 * 
	 * } catch (Exception exception) { throw new UnsuccessfulServiceException(
	 * "Failed to mouse move Element  " + pageElement.getName() + "  " + "  " +
	 * " on : '" + pageName + "' ", exception); } finally { pageElement = null;
	 * }
	 * 
	 * }
	 */

	protected PageElement autoSuggestLinkByText(String text) {
		webDriverWrapper.explicitWait(3);
		return new PageElement(
				By.xpath("//a[contains(text(),'" + text + "')]"),
				"Auto Suggest Link for " + text, true,
				WaitType.WAITFORELEMENTTOBEENABLED, 45);
	}

	protected PageElement getEarlyLoadedPageElement(PageElement pageElement) {
		pageElement.setSlowLoadableComponent(false);
		return pageElement;
	}

	public void expandPanel(PageElement panel) {
		webDriverWrapper.explicitWait(2);
		if (getAttribute(panel, "class").equalsIgnoreCase("plus"))
			click(panel);
		webDriverWrapper.explicitWait(3);
	}

	public void collapsePanel(PageElement panel) {
		try {
			webDriverWrapper.explicitWait(2);
			if (getAttribute(panel, "class").equalsIgnoreCase("minus")
					|| getAttribute(panel, "class").equalsIgnoreCase(
							"plus minus"))
				click(panel);
			webDriverWrapper.explicitWait(2);
		} catch (Exception e) {
		}
	}

	public void expandSubPanel(PageElement panel) {
		webDriverWrapper.explicitWait(2);
		if (getAttribute(panel, "class").equalsIgnoreCase("plus"))
			click(panel);
		webDriverWrapper.explicitWait(2);
	}

	public void collapseSubPanel(PageElement panel) {
		try {
			webDriverWrapper.explicitWait(2);
			if (getAttribute(panel, "class").equalsIgnoreCase("minus")
					|| getAttribute(panel, "class").equalsIgnoreCase(
							"plus minus"))
				click(panel);
			webDriverWrapper.explicitWait(2);
		} catch (Exception e) {
		}
	}

	public String fetchSelectedValueFromList(PageElement pageElement) {
		try {

			return getSelectedValueFromList(getEarlyLoadedPageElement(pageElement));
		} catch (Exception e) {
			return "";

		}

	}

	public String fetchSelectedValueFromListWithoutBringInView(
			PageElement pageElement) {
		try {

			return getSelectedValueFromListWithoutBringInView(getEarlyLoadedPageElement(pageElement));
		} catch (Exception e) {
			return "";

		}

	}

	public String fetchValueFromTextField(PageElement pageElement) {
		try {
			webDriverWrapper.waitForTextToAppearInTextField(
					getWebElement(pageElement), 10);
			return getAttribute(getEarlyLoadedPageElement(pageElement), "value");
		} catch (Exception e) {
			return "";

		}

	}

	public String fetchValueFromCheckBox(PageElement pageElement) {
		try {
			if (isElementSelected(getEarlyLoadedPageElement(pageElement)))
				return "yes";
			else
				return "no";
		} catch (Exception e) {
			return "";

		}

	}

	public String fetchTextFromLabel(PageElement pageElement) {
		try {

			return getText(pageElement);
		} catch (Exception e) {
			return "";

		}

	}

	public void switchToWindow(String windowTitle) throws Exception, Throwable {

		for (int i = 1; i <= 5; i++) {
			try {
				Set<String> windows = driver.getWindowHandles();
				Thread.sleep(4000);

				for (String window : windows) {
					driver.switchTo().window(window);
					driver.manage().window().maximize();
					if (driver.getTitle().contains(windowTitle)) {
						// driver.manage().window().maximize();
						return;
					}
				}
			} catch (WebDriverException e) {
				if (i != 5) {
					continue;
				} else
					throw new UnsuccessfulServiceException(
							"Could not switch to   " + windowTitle + " Screen",
							e);
			}
		}

	}

	public void closeWindow(String windowTitle) throws Exception, Throwable {
		switchToWindow(windowTitle);
		driver.close();
	}

	public void handleUnexpectedErrorPopups() {
		ConfigurationProperties configurationProperties = new ConfigurationProperties();
		String configHandleUnexpectedErrorPopups = configurationProperties
				.getProperty(ConfigurationProperties.Handle_Unexpected_Error_Popups);
		if (configHandleUnexpectedErrorPopups.equals("yes")) {
			while (webDriverWrapper
					.isAlertWithSpecifiedMessagePresent("Something seems Wrong")
					|| webDriverWrapper
							.isAlertWithSpecifiedMessagePresent("Something special seems Wrong")) {
				acceptAlertAndReturnConfirmationMessage();
			}
		}

	}

}
