package com.pstc.framework.utilities;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.asserts.Assertion;

import com.pstc.framework.FrameworkServices;
import com.pstc.framework.FrameworkSetupSuiteLevel;
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

	public AssertClass(WebDriver driver, String testId, String iteration) throws Throwable {
		this.driver = driver;
		this.testID = testId;
		this.Iteration = iteration;
		actualexpected.append(" ");
		actualScenarioExpected.append("");
		Calendar cal = Calendar.getInstance();
		FrameworkServices frameworkServices = new FrameworkServices();
		frameworkServices.logMessage(cal.getTime().toString(), logger);
	}

	public boolean checkpointString(String checkpointInfo, String actualValue, String expectedValue) {
		++checkCounter;

		if (actualValue == null)
			actualValue = "DEFAULTED TO NULL OBJECT";
		if (expectedValue == null)
			expectedValue = "DEFAULTED TO NULL OBJECT";

		if (actualValue.equals(expectedValue)) {
			testStatus = true && testStatus;
		} else {
			testStatus = false;

		}
		actualexpected.append("<br>" + checkpointInfo + " Check " + checkCounter + ":Check String {" + expectedValue
				+ ":" + actualValue + "},<br>");
		return testStatus;
	}

	public boolean verifyValues(String assertNumber, Object actualValue, Object expectedValue, String messages) {
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

			frameworkServices.logMessage("Actual :-"+actualValue.toString()+
					";Expected :-"+expectedValue.toString()+" <br> "+messages,
					logger);
		} catch (AssertionError assertionError) {
			assertError = assertionError;
			scenarioTestStatus = false;

			frameworkServices.logMessage("<B>" + assertError.getMessage() +
					"<br> " + messages + "<B>", logger);

		}

		/*
		 * if (actualValue == expectedValue) { scenarioTestStatus = true &&
		 * scenarioTestStatus; } else { scenarioTestStatus = false;
		 * 
		 * }
		 */

		actualScenarioExpected.append(assertNumber + " : { " + expectedValue + " : " + actualValue + " },<br>");
		return scenarioTestStatus;

	}

	/*
	 * public void verifyScenarioResult(String assertNumber, Object actualValue,
	 * Object expectedValue, String messages) {
	 * 
	 * AssertionError assertError = null; FrameworkServices frameworkServices =
	 * new FrameworkServices(); try { Assert.assertEquals(actualValue,
	 * expectedValue, messages);
	 * 
	 * frameworkServices.logMessage("Actual:-"+actualValue.toString()+
	 * ";Expected:-"+expectedValue.toString()+messages, logger); } catch
	 * (AssertionError assertionError) { assertError = assertionError;
	 * frameworkServices.logMessage("<B>" + assertError.getMessage() + " " +
	 * messages + "<B>", logger); }
	 * 
	 * reportCurrentVerificationStatus(assertNumber, assertError,
	 * frameworkServices, messages);
	 * 
	 * }
	 */

	public void verifyScenarioResult(String assertNumber, String messages) throws Throwable {
		FrameworkServices frameworkServices = new FrameworkServices();
		AssertionError assertionError = null;
		try {
			Assert.assertEquals(scenarioTestStatus, true, actualScenarioExpected.toString());
			frameworkServices.logMessage(actualScenarioExpected.toString(),
			logger);
		} catch (AssertionError e) {
			assertionError = e;
			frameworkServices.logMessage(actualScenarioExpected.toString(),
			logger);
		}

	}

	public void verifyNotEqual(String assertNumber, Object actualValue, Object expectedValue, String messages) {

		AssertionError assertError = null;
		FrameworkServices frameworkServices = new FrameworkServices();
		try {

			if (actualValue == null)
				actualValue = "DEFAULTED TO NULL OBJECT";
			if (expectedValue == null)
				expectedValue = "DEFAULTED TO NULL OBJECT";

			Assert.assertNotEquals(actualValue, expectedValue, messages);
			
			frameworkServices.logMessage("Actual:-"+actualValue.toString()+
			";Expected:-"+expectedValue.toString()+"<br> "+messages, logger);
						 frameworkServices.logMessage(messages, logger);
		} catch (AssertionError assertionError) {
			assertError = assertionError;
			
			frameworkServices.logMessage("<B>" + assertError.getMessage() +
			"<br> " + messages + "<B>", logger);
			
		}



	}

	public boolean checkpointStringRegEx(String checkpointInfo, String actualvalue, String expectedRegExvalue) {
		++checkCounter;

		Pattern r = Pattern.compile(expectedRegExvalue);
		Matcher m = r.matcher(actualvalue);
		if (m.find()) {
			testStatus = true && testStatus;
		} else {
			testStatus = false;
		}
		actualexpected.append(checkpointInfo + " Check " + checkCounter + ":Check String with Regular Expression{"
				+ expectedRegExvalue + ":" + actualvalue + "},\n");
		return testStatus;
	}

	public boolean checkpointBoolean(String checkpointInfo, boolean actualvalue, boolean expectedvalue) {
		++checkCounter;

		if (actualvalue == expectedvalue) {
			testStatus = true && testStatus;
		} else {
			testStatus = false;

		}
		actualexpected
		.append(checkpointInfo + " Check" + checkCounter + ":{" + expectedvalue + ":" + actualvalue + "},\n");
		return testStatus;
	}

	public boolean checkpointIsItemPresentInGivenString(String checkpointInfo, String actualListValues,
			String expectedValue) {
		++checkCounter;

		List<String> expectedListValues = Arrays.asList(expectedValue.split(","));
		boolean localTestStatus = true;
		StringBuffer tempExpectedValue = new StringBuffer();
		tempExpectedValue.append(" ");
		StringBuffer tempActualValue = new StringBuffer();
		tempActualValue.append(" ");
		for (String listValue : expectedListValues) {
			if (actualListValues.contains(listValue)) {
				localTestStatus = true && localTestStatus;
				tempExpectedValue.append(listValue + ",");
				tempActualValue.append("Present,");
			} else {
				localTestStatus = false;
				tempExpectedValue.append(listValue + ",");
				tempActualValue.append("NotPresent,");
			}
		}

		if (localTestStatus) {
			testStatus = true && testStatus;
		} else {
			testStatus = false;
		}
		actualexpected.append(checkpointInfo + " Check " + checkCounter + ":DropDownValuesPresence:- {"
				+ tempExpectedValue + ":" + tempActualValue + "},\n");
		return testStatus;
	}

	public boolean checkpointMatchRow(String checkpointInfo, HashMap<String, String> actualRow, String expectedRow) {
		++checkCounter;
		if (actualRow != null) {
			String[] arrExpectedColumnValue = expectedRow.replace("[]", "").split(",");
			StringBuffer tempExpectedValue = new StringBuffer();
			tempExpectedValue.append(" ");
			StringBuffer tempActualValue = new StringBuffer();
			tempActualValue.append(" ");
			boolean localTestStatus = true;
			for (String expectedColumnValue : arrExpectedColumnValue) {
				String expectedColumn = expectedColumnValue.split("=")[0].trim();
				String expectedValue = expectedColumnValue.split("=")[1].trim();
				if (actualRow.containsKey(expectedColumn)) {
					if (actualRow.get(expectedColumn).equals(expectedValue)) {
						localTestStatus = true && localTestStatus;
						tempExpectedValue.append(expectedColumn + "=" + expectedValue + ",");
						tempActualValue.append(expectedColumn + "=" + actualRow.get(expectedColumn) + ",");
					} else {
						localTestStatus = false;
						tempExpectedValue.append(expectedColumn + "=" + expectedValue + ",");
						tempActualValue.append(expectedColumn + "=" + actualRow.get(expectedColumn) + ",");
					}
				} else {
					localTestStatus = false;
					tempExpectedValue.append(expectedColumn + "=" + expectedValue + ",");
					tempActualValue.append("ColumnNotPresent,");

				}
			}

			if (localTestStatus) {
				testStatus = true;
			} else {
				testStatus = false;
			}
			actualexpected.append(checkpointInfo + " Check:" + checkCounter + ":MatchRow:- {" + tempExpectedValue + ":"
					+ tempActualValue + "},\n");
			return testStatus;
		} else {
			actualexpected.append(
					checkpointInfo + " Check: " + checkCounter + ":MatchRow:- No row found on the Application \n");
			return false;

		}
	}

	public boolean checkpointIsRowPresent(String checkpointInfo, ArrayList<HashMap<String, String>> actualAllRows,
			String expectedRow) {
		++checkCounter;

		if (actualAllRows != null) {
			String[] arrExpectedColumnValue = expectedRow.replace("]", "").replace("[", "").replace("{", "")
					.replace("}", "").split(",");
			StringBuffer tempActualValue = new StringBuffer();
			tempActualValue.append(" ");
			int foundatRowindex = 0;
			boolean secondLocalTestStatus = false;
			for (int i = 0; i < actualAllRows.size(); i++) {
				HashMap<String, String> actualRow = actualAllRows.get(i);
				boolean firstLocalTestStatus = true;
				for (String expectedColumnValue : arrExpectedColumnValue) {
					String expectedColumn = expectedColumnValue.split("=")[0].trim();
					String expectedValue = expectedColumnValue.split("=")[1].trim();
					if (actualRow.containsKey(expectedColumn)) {
						if (actualRow.get(expectedColumn).equals(expectedValue)) {
							firstLocalTestStatus = true;
						} else {
							firstLocalTestStatus = false;
						}
					} else {
						firstLocalTestStatus = false;
					}
				}
				if (firstLocalTestStatus) {
					secondLocalTestStatus = true;
					foundatRowindex = i;
					break;
				} else {
					secondLocalTestStatus = false;
				}
			}
			if (secondLocalTestStatus) {
				testStatus = true && testStatus;
				tempActualValue.append("Found at Row No-" + foundatRowindex);
			} else {
				testStatus = false;
				tempActualValue.append("Row not found-" + foundatRowindex);
			}
			actualexpected.append(checkpointInfo + " Check:" + checkCounter + ":Row Present:- {" + expectedRow + ":"
					+ tempActualValue + "},\n");
			return testStatus;
		} else {
			actualexpected.append(
					checkpointInfo + " Check: " + checkCounter + ":MatchRow:- No row found on the Application \n");
			return false;

		}
	}






}
