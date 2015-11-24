package com.pstc.framework.utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.TimeZone;

public class RandomCodeGenerator {

	public static String randomNameGenerator() {
		Date date = new Date();
		SimpleDateFormat f = new SimpleDateFormat("dd/MMM/YYYY hh:mm:ss:ms");
		String newRandomString = f.format(date).replace("/", "").replace(" ", "").replace(":", "");

		String lastChars = "0123456789";
		Random random = new Random();
		StringBuilder randomNumber = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		for (int i = 0; i < 3; i++) {
			sb1.append(lastChars.charAt(random.nextInt(lastChars.length())));
		}
		randomNumber.append(newRandomString);
		randomNumber.append(sb1);

		return randomNumber.toString();
	}

	public static String getTimeStamp() {
		Date date = new Date();
		SimpleDateFormat f = new SimpleDateFormat("dd/MMM/YYYY hh:mm:ss:ms");
		return f.format(date).replace("/", "").replace(" ", "").replace(":", "");
	}

	public static String getCurrentDateInFormatDDMMMYYYY() {
		Date newDate = new Date();
		String returnDate = "";
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		SimpleDateFormat f = new SimpleDateFormat("dd-MMM-YYYY");
		newDate = cal.getTime();
		returnDate = f.format(newDate).toString();
		return returnDate;
	}

	public static String randomNumberGenerator(int length) {
		String Chars = "123456789";
		Random random = new Random();
		StringBuilder randomNumber = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb1.append(Chars.charAt(random.nextInt(Chars.length())));
		}
		randomNumber.append(sb1);
		return randomNumber.toString();
	}

	/*
	 * public void highlightElement(WebDriver driver, WebElement element) {
	 * 
	 * for (int i = 0; i < 2; i++) {
	 * 
	 * JavascriptExecutor js = (JavascriptExecutor) driver;
	 * 
	 * js.executeScript("arguments[0].setAttribute('style', arguments[1]);",
	 * 
	 * element, "color: yellow; border: 2px solid yellow;");
	 * 
	 * js.executeScript("arguments[0].setAttribute('style', arguments[1]);",
	 * 
	 * element, "");
	 * 
	 * }
	 * 
	 * }
	 */

	public static String randomPANGenerator() {
		String firstFiveChars = "abcdefghijklmnopqrstuvwxyz";
		String secondFourChars = "0123456789";
		String lastChar = "abcdefghijklmnopqrstuvwxyz";
		Random random = new Random();
		StringBuilder panNumber = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		StringBuilder sb3 = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			sb1.append(firstFiveChars.charAt(random.nextInt(firstFiveChars.length())));
		}
		for (int i = 0; i < 4; i++) {
			sb2.append(secondFourChars.charAt(random.nextInt(secondFourChars.length())));
		}
		for (int i = 0; i < 1; i++) {
			sb3.append(lastChar.charAt(random.nextInt(lastChar.length())));
		}
		panNumber.append(sb1);
		panNumber.append(sb2);
		panNumber.append(sb3);
		return panNumber.toString();
	}

	public static String randomTANGenerator() {
		String firstFiveChars = "abcdefghijklmnopqrstuvwxyz";
		String secondFourChars = "0123456789";
		String lastChar = "abcdefghijklmnopqrstuvwxyz";
		Random random = new Random();
		StringBuilder tanNumber = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		StringBuilder sb3 = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			sb1.append(firstFiveChars.charAt(random.nextInt(firstFiveChars.length())));
		}
		for (int i = 0; i < 5; i++) {
			sb2.append(secondFourChars.charAt(random.nextInt(secondFourChars.length())));
		}
		for (int i = 0; i < 1; i++) {
			sb3.append(lastChar.charAt(random.nextInt(lastChar.length())));
		}
		tanNumber.append(sb1);
		tanNumber.append(sb2);
		tanNumber.append(sb3);
		return tanNumber.toString();
	}

	public static String randomNameGeneratorUsingCharacter() {
		String firstFourChars = "AUTO";
		String secondSevenChars = "abcdefghijklmnopqrstuvwxyz";
		Random random = new Random();
		StringBuilder sb1 = new StringBuilder();
		sb1.append(firstFourChars);
		for (int i = 0; i < 10; i++) {

			sb1.append(secondSevenChars.charAt(random.nextInt(secondSevenChars.length())));
		}
		return sb1.toString();

	}

	public static String randomNameGeneratorUsingCharacter(int numOfChars) {
		String allChars = "abcdefghijklmnopqrstuvwxyz";
		Random random = new Random();
		StringBuilder sb1 = new StringBuilder();
		for (int i = 1; i <= numOfChars; i++) {
			sb1.append(allChars.charAt(random.nextInt(allChars.length())));
		}
		return sb1.toString();
	}

	public static String dateGenerator(String date) {
		Date newDate = new Date();
		String returnDate = "";
		if (date.equals("futureDate")) {

			Calendar cal = Calendar.getInstance();
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1);
			SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
			newDate = cal.getTime();
			returnDate = f.format(newDate).toString();
		} else if (date.equals("oldDate")) {

			Calendar cal = Calendar.getInstance();
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) - 1);
			SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
			newDate = cal.getTime();
			returnDate = f.format(newDate).toString();

		} else if (date.equals("now")) {

			Calendar cal = Calendar.getInstance();
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
			SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
			newDate = cal.getTime();
			returnDate = f.format(newDate).toString();

		} else if (date.contains("oldDate:")) {
			int counter = Integer.parseInt(date.split(":")[1]);
			Calendar cal = Calendar.getInstance();
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) - counter);
			SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
			newDate = cal.getTime();
			returnDate = f.format(newDate).toString();

		} else if (date.contains("futureDate:")) {
			int counter = Integer.parseInt(date.split(":")[1]);
			Calendar cal = Calendar.getInstance();
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + counter);
			SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
			f.setTimeZone(TimeZone.getDefault());

			newDate = cal.getTime();
			returnDate = f.format(newDate).toString();

		}
		return returnDate;
	}

	public static String dateGenerator(String date, String dateFormat) {
		Date newDate = new Date();
		String returnDate = "";
		if (date.equals("futureDate")) {
			Calendar cal = Calendar.getInstance();
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1);
			SimpleDateFormat f = new SimpleDateFormat(dateFormat);
			newDate = cal.getTime();
			returnDate = f.format(newDate).toString();
		} else if (date.equals("oldDate")) {
			Calendar cal = Calendar.getInstance();
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) - 1);
			SimpleDateFormat f = new SimpleDateFormat(dateFormat);
			newDate = cal.getTime();
			returnDate = f.format(newDate).toString();
		} else if (date.equals("now")) {
			Calendar cal = Calendar.getInstance();
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
			SimpleDateFormat f = new SimpleDateFormat(dateFormat);
			newDate = cal.getTime();
			returnDate = f.format(newDate).toString();
		} else if (date.contains("oldDate:")) {
			int counter = Integer.parseInt(date.split(":")[1]);
			Calendar cal = Calendar.getInstance();
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) - counter);
			SimpleDateFormat f = new SimpleDateFormat(dateFormat);
			newDate = cal.getTime();
			returnDate = f.format(newDate).toString();
		} else if (date.contains("futureDate:")) {
			int counter = Integer.parseInt(date.split(":")[1]);
			Calendar cal = Calendar.getInstance();
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + counter);
			SimpleDateFormat f = new SimpleDateFormat(dateFormat);
			newDate = cal.getTime();
			returnDate = f.format(newDate).toString();
		} else if (date.contains("lastWeekDay:")) {
			String dayName = date.split(":")[1];
			String monthName = date.split(":")[2];

			int mon = -1;
			Calendar tempCal = Calendar.getInstance();

			switch (monthName.toUpperCase()) {
			case "CURRENTMONTH":
				mon = tempCal.get(Calendar.MONTH);
				break;

			case "NEXTMONTH":
				mon = tempCal.get(Calendar.MONTH) + 1;
				break;

			case "FARNEXTMONTH":
				mon = tempCal.get(Calendar.MONTH) + 2;
				break;
			}

			int year = tempCal.get(Calendar.YEAR);

			if (mon > tempCal.DECEMBER) {
				mon = mon - 11;
				year = year + 1;
			}

			Calendar cal = new GregorianCalendar(year, mon, 1);
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

			int checkDay = -1;

			switch (dayName.toUpperCase()) {
			case "SUNDAY":
			case "SUN":
			case "0":
				checkDay = Calendar.SUNDAY;
				break;

			case "MONDAY":
			case "MON":
			case "1":
				checkDay = Calendar.MONDAY;
				break;

			case "TUESDAY":
			case "TUE":
			case "2":
				checkDay = Calendar.TUESDAY;
				break;

			case "WEDNESDAY":
			case "WED":
			case "3":
				checkDay = Calendar.WEDNESDAY;
				break;

			case "THURSDAY":
			case "THUR":
			case "4":
				checkDay = Calendar.THURSDAY;
				break;

			case "FRIDAY":
			case "FRI":
			case "5":
				checkDay = Calendar.FRIDAY;
				break;

			case "SATURDAY":
			case "SAT":
			case "6":
				checkDay = Calendar.SATURDAY;
				break;
			}

			int diff = checkDay - cal.get(Calendar.DAY_OF_WEEK);
			if (diff > 0) {
				diff -= 7;
			}

			cal.add(Calendar.DAY_OF_MONTH, diff);

			SimpleDateFormat f = new SimpleDateFormat(dateFormat);
			newDate = cal.getTime();
			returnDate = f.format(newDate).toString();
		}
		return returnDate;
	}
}
