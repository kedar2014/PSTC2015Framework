package com.pstc.framework.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.pstc.framework.exception.UnsuccessfulServiceException;

public class CalendarComponent extends Page {

	static Logger logger = Logger.getLogger(CalendarComponent.class);

	public CalendarComponent(WebDriver driver) {
		super(driver, CalendarComponent.class.getSimpleName(), logger);
	}

	public static CalendarComponent getInstance(WebDriver driver) {
		return PageFactory.initElements(driver, CalendarComponent.class);

	}

	/**
	 * * Provide date in format "dd/MM/yyyy"
	 * 
	 * @throws InterruptedException
	 *             ****
	 */
	public void selectDateFromCalendar(String date) throws InterruptedException {
		try {
			PageElement nextButton = new PageElement(By.xpath("//a[@title='Next']"), "Next Button");

			PageElement previousButton = new PageElement(By.xpath("//a[@title='Prev']"), "Previous Button");

			PageElement headerYear = new PageElement(By.xpath("//select[contains(@class,'year')]"), "year on header",
					true, WaitType.WAITFORELEMENTTOBEENABLED, 30);
			PageElement headerMonth = new PageElement(By.xpath("//select[contains(@class,'month')]"), "month on header",
					true, WaitType.WAITFORELEMENTTOBEENABLED, 30);

			Thread.sleep(500);

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Calendar dateToBeSelected = Calendar.getInstance();
			dateToBeSelected.setTime(sdf.parse(date));
			PageElement dayOfMonth = new PageElement(
					By.xpath("//a[text()='" + dateToBeSelected.get(Calendar.DATE) + "']"), "month on header", true,
					WaitType.WAITFORELEMENTTOBEENABLED, 30);

			Calendar currentDate = getCurrentDateOnCalendar(headerYear, headerMonth);

			Thread.sleep(500);

			dateToBeSelected.set(Calendar.DATE, 01);
			mouseOver(nextButton);

			Thread.sleep(500);

			if (dateToBeSelected.after(currentDate))
				while (!dateToBeSelected.equals(getCurrentDateOnCalendar(headerYear, headerMonth))) {
					/*
					 * System.out.println("Current Date:" +
					 * currentDate.getTime().toString()); System.out.println(
					 * "Date to be selected Date:" +
					 * dateToBeSelected.getTime().toString());
					 */

					Thread.sleep(500);
					clickWithoutBringInView(nextButton);
					Thread.sleep(500);
				}
			else
				while (!getCurrentDateOnCalendar(headerYear, headerMonth).equals(dateToBeSelected)) {
					/*
					 * System.out.println("Current Date:" +
					 * currentDate.getTime().toString()); System.out.println(
					 * "Date to be selected Date:" +
					 * dateToBeSelected.getTime().toString());
					 */
					Thread.sleep(500);
					clickWithoutBringInView(previousButton);
					Thread.sleep(500);
				}
			clickWithoutBringInView(dayOfMonth);
			nextButton.setSlowLoadableComponent(true);
			nextButton.setWaitType(WaitType.WAITFORELEMENTTODISAPPEAR);
			nextButton.setTimeOut(30);
			waitForPageElement(nextButton);
		} catch (Exception e) {
			throw new UnsuccessfulServiceException("Failed to select date:" + date + " from calendar", e);
		}
	}

	public void waitForCalendarVisiblity() throws UnsuccessfulServiceException {
		PageElement nextButton = new PageElement(By.xpath("//a[@title='Next']"), "Next Button");

		PageElement previousButton = new PageElement(By.xpath("//a[@title='Prev']"), "Previous Button");

		PageElement headerYear = new PageElement(By.xpath("//select[contains(@class,'year')]"), "year on header", true,
				WaitType.WAITFORELEMENTTOBEENABLED, 30);

		PageElement headerMonth = new PageElement(By.xpath("//select[contains(@class,'month')]"), "month on header",
				true, WaitType.WAITFORELEMENTTOBEENABLED, 30);

		if (!(isElementDisplayed(nextButton) && isElementDisplayed(previousButton) && isElementDisplayed(headerYear)
				&& isElementDisplayed(headerMonth))) {
			throw new UnsuccessfulServiceException("Calendar Component was not displayed. ");
		}
	}

	private Calendar getCurrentDateOnCalendar(PageElement headerYear, PageElement headerMonth) throws ParseException {
		SimpleDateFormat sdf;
		String headerMonthText = getSelectedValueFromListWithoutBringInView(headerMonth);
		String headerYearText = getSelectedValueFromListWithoutBringInView(headerYear);
		sdf = new SimpleDateFormat("dd/MMMM/yyyy");
		Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(sdf.parse("01/" + headerMonthText + "/" + headerYearText));
		return currentDate;
	}

	public void selectTimeFromTimePicker(String timeToSelect) {

		try {
			PageElement timePicker = new PageElement(By.id("ui-datepicker-div"), "Time Picker");

			PageElement timeSelectedElement = new PageElement(By.xpath("//dd[contains(@class,'ui_tpicker_time')]"),
					"Choosen Time");

			// div[contains(@id,'ui-datepicker-div')]

			PageElement hourSlider = new PageElement(By.xpath("//div[contains(@class,'hour')]/a"), "Hour Slider");

			PageElement minuteSlider = new PageElement(By.xpath("//div[contains(@class,'minute')]/a"), "Hour Slider");

			PageElement secondSlider = new PageElement(By.xpath("//div[contains(@class,'second')]/a"), "Hour Slider");

			PageElement doneButton = new PageElement(By.xpath("//button[contains(text(),'Done')]"), "Done button");
			String time[] = timeToSelect.split(":");
			int hourPart = Integer.parseInt(time[0]);
			int minutePart = Integer.parseInt(time[1]);
			int secondPart = Integer.parseInt(time[2]);

			String timeSelected[] = getText(timeSelectedElement).split(":");

			int hour = Integer.parseInt(timeSelected[0]);
			int minute = Integer.parseInt(timeSelected[1]);
			int second = Integer.parseInt(timeSelected[2]);

			// to reset sliders
			for (int i = 0; i < hour; i++) {
				click(hourSlider);
				sendKeys(hourSlider, Keys.ARROW_LEFT);
			}
			for (int i = 0; i < minute; i++) {
				click(minuteSlider);
				sendKeys(minuteSlider, Keys.ARROW_LEFT);
			}
			for (int i = 0; i < second; i++) {
				click(secondSlider);
				sendKeys(secondSlider, Keys.ARROW_LEFT);
			}

			// to Select desired time
			for (int i = 0; i < hourPart; i++) {
				click(hourSlider);
				sendKeys(hourSlider, Keys.ARROW_RIGHT);
			}
			for (int i = 0; i < minutePart; i++) {
				click(minuteSlider);
				sendKeys(minuteSlider, Keys.ARROW_RIGHT);
			}
			for (int i = 0; i < secondPart; i++) {
				click(secondSlider);
				sendKeys(secondSlider, Keys.ARROW_RIGHT);
			}
			click(doneButton);
		} catch (Exception e) {
			throw new UnsuccessfulServiceException("Failed to select time:" + timeToSelect + " from Time Picker.", e);
		}
	}

	public void setAttribute(WebDriver driver, WebElement element) {
		// for (int i = 0; i < 2; i++) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
				"left: 0%; background-color: rgb(230, 230, 230);");
				/*
				 * js.executeScript(
				 * "arguments[0].setAttribute('style', arguments[1]);", element,
				 * "");
				 */
				// }

		/*
		 * JavascriptExecutor js = (JavascriptExecutor) driver; //
		 * js.executeScript(
		 * "document.getElementByXPATH('//div[contains(@class,'hour')]/a').setAttribute('style', 'left: 0%; background-color: rgb(230, 230, 230);')"
		 * ); js.executeScript(
		 * "document.getElementByXpath(\"//div[contains(@class,'hour')]/a\").setAttribute('style', 'left: 0%; background-color: rgb(230, 230, 230);')"
		 * );
		 */
	}

}
