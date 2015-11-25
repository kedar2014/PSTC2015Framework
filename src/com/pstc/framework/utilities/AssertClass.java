package com.pstc.framework.utilities;

import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.asserts.Assertion;

import com.pstc.framework.FrameworkServices;

@SuppressWarnings("unchecked")
public class AssertClass extends Assertion {
	static Logger logger = Logger.getLogger(AssertClass.class);
	private boolean testStatus = true;
	private boolean scenarioTestStatus = true;
	HashMap<String, String> expectedMap;
	int checkCounter = 0;
	int scenarioCounter = 0;
	StringBuffer actualexpected = new StringBuffer();
	StringBuffer actualScenarioExpected = new StringBuffer();
	WebDriver driver;
	String testID;
	String Iteration;
	boolean isTestFailed = false;

	public AssertClass(WebDriver driver, String testId, String iteration)
			throws Throwable {
		this.driver = driver;
		this.testID = testId;
		this.Iteration = iteration;
		actualexpected.append(" ");
		actualScenarioExpected.append("");
		Calendar cal = Calendar.getInstance();
		FrameworkServices frameworkServices = new FrameworkServices();
		frameworkServices.logMessage(cal.getTime().toString(), logger);
	}

	public boolean verifyValues(String assertNumber, Object actualValue,
			Object expectedValue, String messages) throws Throwable {
		++scenarioCounter;
		AssertionError assertError = null;
		FrameworkServices frameworkServices = new FrameworkServices();
		try {
			if (actualValue == null)
				actualValue = "DEFAULTED TO NULL OBJECT";
			if (expectedValue == null)
				expectedValue = "DEFAULTED TO NULL OBJECT";

			Assert.assertEquals(actualValue, expectedValue, messages);
			scenarioTestStatus = true && scenarioTestStatus;

			frameworkServices.logMessage("Actual :-" + actualValue.toString()
					+ ";Expected :-" + expectedValue.toString() + " <br> "
					+ messages, logger);
		} catch (AssertionError assertionError) {
			assertError = assertionError;
			scenarioTestStatus = false;

			frameworkServices.logMessage("<B>" + assertError.getMessage()
					+ "<br> " + messages + "<B>", logger);
			frameworkServices.takeScreenshot(testID, driver, assertError,
					logger);

		}

		/*
		 * if (actualValue == expectedValue) { scenarioTestStatus = true &&
		 * scenarioTestStatus; } else { scenarioTestStatus = false;
		 * 
		 * }
		 */

		actualScenarioExpected.append(assertNumber + " : { " + expectedValue
				+ " : " + actualValue + " },<br>");
		return scenarioTestStatus;

	}

	public void verifyScenarioResult(String assertNumber, String messages)
			throws Throwable {
		FrameworkServices frameworkServices = new FrameworkServices();
		AssertionError assertionError = null;
		try {
			Assert.assertEquals(scenarioTestStatus, true,
					actualScenarioExpected.toString());
			frameworkServices.logMessage(actualScenarioExpected.toString(),
					logger);
		} catch (AssertionError e) {
			assertionError = e;
			frameworkServices.logMessage(actualScenarioExpected.toString(),
					logger);
		}

	}

	public void verifyNotEqual(String assertNumber, Object actualValue,
			Object expectedValue, String messages) throws Throwable {

		AssertionError assertError = null;
		FrameworkServices frameworkServices = new FrameworkServices();
		try {

			if (actualValue == null)
				actualValue = "DEFAULTED TO NULL OBJECT";
			if (expectedValue == null)
				expectedValue = "DEFAULTED TO NULL OBJECT";

			Assert.assertNotEquals(actualValue, expectedValue, messages);

			frameworkServices.logMessage("Actual:-" + actualValue.toString()
					+ ";Expected:-" + expectedValue.toString() + "<br> "
					+ messages, logger);
			frameworkServices.logMessage(messages, logger);
		} catch (AssertionError assertionError) {
			assertError = assertionError;

			frameworkServices.logMessage("<B>" + assertError.getMessage()
					+ "<br> " + messages + "<B>", logger);
			frameworkServices.takeScreenshot(testID, driver, assertError,
					logger);
		}

	}

	public boolean checkpointStringRegEx(String checkpointInfo,
			String actualvalue, String expectedRegExvalue) {
		++checkCounter;

		Pattern r = Pattern.compile(expectedRegExvalue);
		Matcher m = r.matcher(actualvalue);
		if (m.find()) {
			testStatus = true && testStatus;
		} else {
			testStatus = false;
		}
		actualexpected.append(checkpointInfo + " Check " + checkCounter
				+ ":Check String with Regular Expression{" + expectedRegExvalue
				+ ":" + actualvalue + "},\n");
		return testStatus;
	}

	public boolean checkpointBoolean(String checkpointInfo,
			boolean actualvalue, boolean expectedvalue) {
		++checkCounter;

		if (actualvalue == expectedvalue) {
			testStatus = true && testStatus;
		} else {
			testStatus = false;

		}
		actualexpected.append(checkpointInfo + " Check" + checkCounter + ":{"
				+ expectedvalue + ":" + actualvalue + "},\n");
		return testStatus;
	}

}
