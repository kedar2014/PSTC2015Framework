package com.pstc.framework.utilities;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Predicate;
import com.pstc.framework.FrameworkServices;


public class WebDriverWrapper {
	static Logger logger = Logger.getLogger(WebDriverWrapper.class.getName());
	WebDriver driver;

	public WebDriverWrapper(WebDriver driver) {
		this.driver = driver;

	}

	public static String getUniqueValue() {
		// get current date time with Date() to create unique file name
		char[] chars = "abcdefghijklmnopqrstuvwxyzABSDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
		Random r = new Random(System.currentTimeMillis());
		char[] id = new char[9];
		for (int i = 0; i < 9; i++) {
			id[i] = chars[r.nextInt(chars.length)];
		}
		return new String(id);
	}

	public static String getCurrentBrowserName(WebDriver driver) {
		Capabilities cp = ((RemoteWebDriver) driver).getCapabilities();
		return cp.getBrowserName();

	}

	public String captureScreenShot(File destinationFilePathLocation, String snapShotName) throws IOException {
		String fileName = new String();
		FrameworkServices frameworkServices = new FrameworkServices();
		if (isAlertPresentForScreenshot()) {
			String message = dismissAlert(2);
			frameworkServices.logMessage("<B> Found alert with mesage: " + message + "</B>", logger);
		}

		WebDriver augmentedDriver = driver;
		if (!(driver instanceof InternetExplorerDriver))
			augmentedDriver = new Augmenter().augment(driver);

		File screenshot = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
		fileName = snapShotName + ".png";
		File destinationFilePath = new File((destinationFilePathLocation + File.separator + fileName));
		FileUtils.copyFile(screenshot, destinationFilePath);
		return fileName;

	}

	public String getUniqueTimeStamp() {

		DateFormat dateFormat = new SimpleDateFormat("hh_mm_ssaadd_MMM_yyyy");
		Date date = new Date();
		return (dateFormat.format(date));
	}

	public String getCurrentBrowserName() {
		Capabilities cp = ((RemoteWebDriver) driver).getCapabilities();
		return cp.getBrowserName();

	}

	public Capabilities getCurrentCapability() {
		Capabilities cp = ((RemoteWebDriver) driver).getCapabilities();
		return cp;

	}

	public WebElement waitForElementToBeDisplayed(final WebElement element, int timeOutPeriod) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, timeOutPeriod);
		webDriverWait.pollingEvery(10, TimeUnit.MICROSECONDS);

		return webDriverWait.until(new ExpectedCondition<WebElement>() {

			public WebElement apply(WebDriver driver) {
				try {
					if (element.isDisplayed())
						return element;
					else
						return null;
				} catch (NoSuchElementException ex) {
					return null;
				} catch (StaleElementReferenceException ex) {
					return null;
				} catch (NullPointerException ex) {

					return null;
				}
			}

		});
	}

	public WebElement waitForElementToBeDisplayed(final By by, int timeOutPeriod) {
		// try{
		WebDriverWait webDriverWait = new WebDriverWait(driver, timeOutPeriod);
		webDriverWait.pollingEvery(10, TimeUnit.MICROSECONDS);
		return webDriverWait.until(new ExpectedCondition<WebElement>() {

			public WebElement apply(WebDriver driver) {
				try {
					WebElement element = driver.findElement(by);
					if (element.isDisplayed())
						return element;
					else
						return null;
				} catch (NoSuchElementException ex) {
					return null;
				} catch (StaleElementReferenceException ex) {
					return null;
				} catch (NullPointerException ex) {
					return null;
				}
			}

		});
		/*
		 * } catch(Exception e){ return null; }
		 */

	}

	public WebElement waitForElementToBeClickable(final By by, int timeOutPeriod) {

		WebDriverWait webDriverWait = new WebDriverWait(driver, timeOutPeriod);
		webDriverWait.pollingEvery(10, TimeUnit.MICROSECONDS);
		return webDriverWait.until(new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver driver) {
				try {
					WebElement element = driver.findElement(by);
					if (element.isEnabled() && element.isDisplayed())
						return element;
					else
						return null;
				} catch (NoSuchElementException ex) {
					return null;
				} catch (StaleElementReferenceException ex) {
					return null;
				} catch (NullPointerException ex) {
					return null;
				}
			}

		});

	}

	public WebElement waitForElementToBeClickable(final WebElement element, int timeOutPeriod) {

		WebDriverWait webDriverWait = new WebDriverWait(driver, timeOutPeriod);
		webDriverWait.pollingEvery(10, TimeUnit.MICROSECONDS);

		return webDriverWait.until(new ExpectedCondition<WebElement>() {

			public WebElement apply(WebDriver driver) {
				try {

					if (element.isEnabled() && element.isDisplayed())
						return element;
					else
						return null;
				} catch (NoSuchElementException ex) {
					return null;
				} catch (StaleElementReferenceException ex) {
					return null;
				} catch (NullPointerException ex) {
					return null;
				}
			}

		});

	}

	public WebElement waitForElementToBeEnabled(final WebElement element, int timeOutPeriod) {

		WebDriverWait webDriverWait = new WebDriverWait(driver, timeOutPeriod);
		webDriverWait.pollingEvery(10, TimeUnit.MICROSECONDS);

		return webDriverWait.until(new ExpectedCondition<WebElement>() {

			public WebElement apply(WebDriver driver) {
				try {

					if (element.isEnabled())
						return element;
					else
						return null;
				} catch (NoSuchElementException ex) {
					return null;
				} catch (StaleElementReferenceException ex) {
					return null;
				} catch (NullPointerException ex) {
					return null;
				}
			}

		});

	}

	public WebElement waitForElementToBeEnabled(final By by, int timeOutPeriod) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, timeOutPeriod);
		webDriverWait.pollingEvery(10, TimeUnit.MICROSECONDS);
		return webDriverWait.until(new ExpectedCondition<WebElement>() {

			public WebElement apply(WebDriver driver) {
				try {
					WebElement element = driver.findElement(by);
					if (element.isEnabled())
						return element;
					else
						return null;
				} catch (NoSuchElementException ex) {
					return null;
				} catch (StaleElementReferenceException ex) {
					return null;
				} catch (NullPointerException ex) {
					return null;
				}
			}

		});

	}

	public WebElement waitForOptionToBePopulatedInList(final WebElement dropdownList, int timeOutPeriod) {

		WebDriverWait webDriverWait = new WebDriverWait(driver, timeOutPeriod);
		webDriverWait.pollingEvery(10, TimeUnit.MICROSECONDS);
		return webDriverWait.until(new ExpectedCondition<WebElement>() {

			public WebElement apply(WebDriver driver) {
				try {
					List<WebElement> options = dropdownList.findElements(By.tagName("option"));
					if (options.size() >= 1) {
						return dropdownList;
					} else
						return null;

				} catch (NoSuchElementException ex) {
					return null;
				} catch (StaleElementReferenceException ex) {
					return null;
				} catch (NullPointerException ex) {
					return null;
				}
			}

		});

	}

	public WebElement waitForTextToAppearInTextField(final WebElement webElement, int timeOutPeriod) {

		WebDriverWait webDriverWait = new WebDriverWait(driver, timeOutPeriod);
		webDriverWait.pollingEvery(10, TimeUnit.MICROSECONDS);
		return webDriverWait.until(new ExpectedCondition<WebElement>() {

			public WebElement apply(WebDriver driver) {
				try {
					String text = webElement.getText();
					if (text != null && text.equals("")) {
						return webElement;
					} else
						return null;

				} catch (NoSuchElementException ex) {
					return null;
				} catch (StaleElementReferenceException ex) {
					return null;
				} catch (NullPointerException ex) {
					return null;
				}
			}

		});

	}

	public boolean waitForOptionToBePresentInList(final By by, String value, int timeOutPeriod) {

		WebDriverWait webDriverWait = new WebDriverWait(driver, timeOutPeriod);
		webDriverWait.pollingEvery(10, TimeUnit.MICROSECONDS);
		return webDriverWait.until(ExpectedConditions.textToBePresentInElement(by, value));
	}

	public void waitForElementToDisapper(final By by, int timeOutPeriod) {

		FluentWait<By> fluentWait = new FluentWait<By>(by);
		fluentWait.pollingEvery(10, TimeUnit.MICROSECONDS);
		fluentWait.withTimeout(timeOutPeriod, TimeUnit.SECONDS);
		fluentWait.until(new Predicate<By>() {
			public boolean apply(By by) {
				try {
					return !driver.findElement(by).isDisplayed();

				} catch (NoSuchElementException ex) {
					return true;
				} catch (StaleElementReferenceException ex) {
					return true;
				}
			}
		});

	}

	public void bringElementInView(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

	}

	public void waitForElementToDisapper(final WebElement element, int timeOutPeriod) {

		FluentWait<WebElement> fluentWait = new FluentWait<WebElement>(element);
		fluentWait.pollingEvery(10, TimeUnit.MICROSECONDS);
		fluentWait.withTimeout(timeOutPeriod, TimeUnit.SECONDS);
		fluentWait.until(new Predicate<WebElement>() {
			public boolean apply(WebElement element) {
				try {
					return !element.isDisplayed();
				} catch (NoSuchElementException ex) {
					return true;
				} catch (StaleElementReferenceException ex) {
					return true;
				}
			}
		});
	}

	public void waitForAlert(int timeOutPeriod) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, timeOutPeriod);
		webDriverWait.ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
				.pollingEvery(10, TimeUnit.MILLISECONDS).until(ExpectedConditions.alertIsPresent());
	}

	public void waitForAlert(int timeOutPeriod, int pollingTime) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, timeOutPeriod);
		webDriverWait.ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
				.pollingEvery(pollingTime, TimeUnit.MILLISECONDS).until(ExpectedConditions.alertIsPresent());
	}

	public String getAlertMessage(int timeOutPeriod) {
		waitForAlert(timeOutPeriod);
		Alert alert = driver.switchTo().alert();
		String AlertMessage = alert.getText();
		return AlertMessage;
	}

	public String acceptAlert(int timeOutPeriod, String AlertMessage) {
		waitForAlert(timeOutPeriod);
		Alert alert = driver.switchTo().alert();
		FrameworkServices frameworkServices = new FrameworkServices();
		frameworkServices.logMessage(AlertMessage, logger);
		alert.accept();
		return AlertMessage;
	}

	public String acceptAlert(int timeOutPeriod) {
		for (int checkAttempts = 1; checkAttempts <= 3; checkAttempts++) {
			waitForAlert(timeOutPeriod);
			if (isAlertPresent()) {
				break;
			}
		}
		Alert alert = driver.switchTo().alert();
		String AlertMessage = alert.getText();
		FrameworkServices frameworkServices = new FrameworkServices();
		frameworkServices.logMessage(AlertMessage, logger);
		alert.accept();
		return AlertMessage;
	}

	public String dismissAlert(int timeOutPeriod) {
		waitForAlert(timeOutPeriod);
		Alert alert = driver.switchTo().alert();
		String alertMessage = alert.getText();
		FrameworkServices frameworkServices = new FrameworkServices();
		frameworkServices.logMessage(alertMessage, logger);
		alert.dismiss();
		return alertMessage;
	}

	public void acceptAlertForFirefox() {
		if (getCurrentBrowserName().equalsIgnoreCase("firefox")) {
			try {
				waitForAlert(20);
				driver.switchTo().alert().accept();
			} catch (Exception e) {

			}
		}
	}

	public String getAlertMessage() {
		String alertMessage = new String("");
		try {
			waitForAlert(5);
			FrameworkServices frameworkServices = new FrameworkServices();
			alertMessage = driver.switchTo().alert().getText();
			frameworkServices.logMessage("fetched text: " + alertMessage + " from alert box.", logger);
		} catch (Exception e) {

		}
		return alertMessage;
	}

	public boolean isAlertPresentForScreenshot() {
		boolean isAlertPresent = false;
		try {
			driver.switchTo().alert();
			isAlertPresent = false;
		} catch (Exception e) {

		}
		return isAlertPresent;
	}

	public boolean isAlertPresent() {
		boolean isAlertPresent = false;
		try {
			waitForAlert(3);
			driver.switchTo().alert();
			isAlertPresent = true;
		} catch (Exception e) {

		}
		return isAlertPresent;
	}

	public boolean isAlertWithSpecifiedMessagePresent(String errorMessage) {
		boolean isAlertPresent = false;
		try {
			// waitForAlert(3);
			isAlertPresent = driver.switchTo().alert().getText().contains(errorMessage);
		} catch (Exception e) {

		}
		return isAlertPresent;
	}

	public boolean isAlertNotPresent() {
		boolean isAlertNotPresent = false;
		try {
			driver.switchTo().alert();
			isAlertNotPresent = false;
		} catch (Exception e) {
			isAlertNotPresent = true;
		}
		return isAlertNotPresent;
	}

	public void explicitWait(int waitTime) {

		try {
			Thread.sleep(waitTime * 1000);
		} catch (InterruptedException e) {

		}
	}

}
