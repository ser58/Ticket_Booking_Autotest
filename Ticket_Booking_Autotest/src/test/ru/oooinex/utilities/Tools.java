package test.ru.oooinex.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;
import io.github.bonigarcia.wdm.OperaDriverManager;
import io.github.bonigarcia.wdm.PhantomJsDriverManager;

public class Tools {
	public static void AddUTF8toReport() {
		String oldFile = "D:/hgtest/Ticket_Booking_Autotest/test-output/index.html";
		String newFile = "D:/hgtest/Ticket_Booking_Autotest/test-output/index.htm";

		File file = new File(oldFile);
		Scanner scanner = null;
		FileWriter writer = null;

		try {
			writer = new FileWriter(newFile);
			scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.contentEquals("<!DOCTYPE html>")) {
					writer.write(line + "\n");
					writer.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">" + "\n");
				} else {
					writer.write(line + "\n");
				}
			}

			// file.delete();
			writer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static WebDriver createDriver(String browser, String autotestProfile) {
		WebDriver driver = null; 
		if (browser.contains("firefox")) {
			//FirefoxDriverManager.getInstance().setup();
			ProfilesIni iniProfile = new ProfilesIni();
			FirefoxProfile profile = iniProfile.getProfile(autotestProfile);
			
			/*FirefoxProfile profile = new FirefoxProfile(new File(autotestProfile));
			profile.setAssumeUntrustedCertificateIssuer(false);
			profile.setAcceptUntrustedCertificates(true);

			DesiredCapabilities capabilities = DesiredCapabilities.firefox();
			//capabilities.setCapability(FirefoxDriver.PROFILE, profile);
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			capabilities.setCapability("marionette", true);*/
			
			driver = new FirefoxDriver(profile);
			
		} else if (browser.contains("chrome")) {
			ChromeDriverManager.getInstance().setup();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--ignore-certificate-errors");
			//options.addArguments("user-data-dir=D:\\Profiles\\Chrome_Profile");
			//Map<String, Object> prefs = new HashMap<String, Object>();
			//prefs.put("profile.default_content_settings.popups", 0);
			//options.setExperimentalOption("prefs", prefs);
			driver = new ChromeDriver(options);
			
		} else if (browser.contains("ie")) {
			InternetExplorerDriverManager.getInstance().setup();
			driver = new InternetExplorerDriver();
			//driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		} else if (browser.contains("opera")) {
			OperaDriverManager.getInstance().setup();
			driver = new OperaDriver();
			//driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		} else if (browser.contains("phantomjs")) {
			PhantomJsDriverManager.getInstance().setup();
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setJavascriptEnabled(true);
			capabilities.setCapability("takesScreenshot", true);
			capabilities.setCapability("phantomjs.cli.args", new String[] {"--ignore-ssl-errors=true"});
			driver = new PhantomJSDriver();
		}
		
		//EventFiringWebDriver highLightDriver = new EventFiringWebDriver(driver);  
		//highLightDriver.register(new ListenerThatHighlightsElements(1, 200, TimeUnit.MILLISECONDS));
		
		//return highLightDriver;
		return driver;
	}
	
	public static boolean selectFromDropDown(SelenideElement element, String selector) {
		String tmp = element.toString();
		String[] tmp1 = tmp.substring(tmp.indexOf(">") + 1, tmp.lastIndexOf("</")).split("\n");
		
		for (int i = 0; i < tmp1.length; i++) {						// Проматываем список вниз, ищем нужный элемент
			if (element.getText().contains(selector)) 
				return true;
			else 
				element.sendKeys(Keys.ARROW_DOWN);
			
			if (element.getText().contains(selector))
				return true;
		}
		
		for (int i = 0; i < tmp1.length; i++) {						// Если не находим проматываем список вверх и тоже ищем
			if (element.getText().contains(selector)) 
				return true;
			else 
				element.sendKeys(Keys.ARROW_UP);
			
			if (element.getText().contains(selector))
				return true;
		}
		
		return false;
	}
	
	public static void setCheckBox(SelenideElement element, boolean value) {
		String tmp = element.toString();
		
		if (tmp.contains("invalid") && !value)
			element.click();
	}
	
	public static void selectInsuranceCompany(ElementsCollection element, int passengerNum, String insuranceCompany) {
        SelenideElement insurance = element.findBy(Condition.text(insuranceCompany));
		insurance.$(By.className("trlist__insurance-cont__item-radio")).click();
	}
	
	public static String getBirthday(String departureDate, int countYears, int countMonth, int countDays) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	    LocalDate outDate = LocalDate.parse(departureDate, formatter).minusYears(countYears).minusMonths(countMonth).minusDays(countDays);
		return outDate.format(formatter);
	}
	
	public static String getNow() {
		return LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	}
	
	public static String getYear(String dateTime) { 
		String departureYear = "";
		
		departureYear = dateTime.split("\n")[1].split("\\.")[2];		
		return departureYear;
	}
	
	public static String getDayMonth(String dateTime) {
		String departureDayMonth = "";
		String[] tmp;
		
		tmp = dateTime.split("\n")[1].split("\\.");
		
		departureDayMonth = tmp[0] + "." + tmp[1];
		return departureDayMonth;
	}
	
	public static String getHoursMinutes(String dateTime) {
		String depatrureHourMinute = "";
		
		depatrureHourMinute = dateTime.split("\n")[2];
		
		return depatrureHourMinute;
	}
	
	public static String getDepartureDate(int dayOfWeek, int weekOffset) {
		LocalDate date = LocalDate.now();
		DayOfWeek currDayWeek = date.getDayOfWeek();
		int diffDays;
		
		if (date.getDayOfWeek().getValue() > dayOfWeek) {
			diffDays = currDayWeek.getValue() - dayOfWeek;
			date = date.minusDays(diffDays);
			if (weekOffset > 0) 
				date = date.plusWeeks(weekOffset);
			else 
				date = date.plusWeeks(1);
		} else if (date.getDayOfWeek().getValue() <= dayOfWeek) {
			diffDays = dayOfWeek - currDayWeek.getValue();
			date = date.plusDays(diffDays);
			if (weekOffset > 0)	
				date = date.plusWeeks(weekOffset);
		}
		
		return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	}
	
	public static String getTariff(String birthday, String departureDate, boolean andReturn) {
		String tariff = "";
		LocalDate birthDate;
		LocalDate depDate;
		int numYears;
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		birthDate = LocalDate.parse(birthday, formatter);
		depDate = LocalDate.parse(departureDate, formatter);
		
		numYears = depDate.compareTo(birthDate);
		
		if (numYears <= 10) 
			tariff = "Детский";
		else if (numYears > 10 & numYears <= 21)
			tariff = "JUNIOR (от 10 до 21 года)";
		else if (numYears > 21 & numYears < 60)
			tariff = "Полный";
		else if (numYears >= 60)
			tariff = "SENIOR (от 60 лет)";
		
		return tariff;
	}
	
	public static String getServiceClass(String coupe) {
		String wagonType;
		String serviceClass = "";
		String[] temp = coupe.split("\n");
		wagonType = temp[0].split(" ")[0];
		
		switch (wagonType) {
		case "Купе":
			serviceClass = temp[0].substring(coupe.indexOf("(") + 1, coupe.indexOf(")"));
			break;
		case "Плацкартный":
			serviceClass = temp[0].substring(coupe.indexOf("(") + 1, coupe.indexOf(")"));
			break;
		}
			
		return serviceClass;
	}
	
	public static String getWagonTypeEN(String wagonTypeRU) {
		String wagonTypeEN = "";

		switch (wagonTypeRU) {
		case "Купе":
			wagonTypeEN = "Compartment";
			break;
		case "Плацкартный":
			wagonTypeEN = "Reserved seat";
			break;
		case "Сидячий":
			wagonTypeEN = "";
			break;
		case "Люкс":
			wagonTypeEN = "Luxury";
			break;
		}

		return wagonTypeEN;
	}
	
	public static String generateRandomSymbolsRU(int number) {
		String symbols = "";
		Random rand = new Random();
		
		String[] stringArray = new String[] {"а", "б", "в", "г", "д", "е", "ё", "ж", "з", "и", "й", "к", "л", "м", "н", "о", "п", "р", "с", "т", "у", "ф", "х", "ц", "ч", "ш", "щ", "ъ", "ы", "ь", "э", "ю", "я"};
		
		for (int i = 0; i < number; i++) {
			symbols += stringArray[rand.nextInt(stringArray.length - 1)];
		}
		
		return symbols;
	}
	
	public static String generateRandomSymbolsEN(int number) {
		String symbols = "";
		Random rand = new Random();
		                       
		String[] stringArray = new String[] {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
		
		for (int i = 0; i < number; i++) {
			symbols += stringArray[rand.nextInt(stringArray.length - 1)];
		}
		
		return symbols;
	}
	
	public static String generateRandomPhrases(int numberOfWords, int maxWordLength) {
		String phrases = "";
		Random rand = new Random();
		
		for (int i = 0; i < numberOfWords; i++) {
			phrases += generateRandomSymbolsRU(rand.nextInt(maxWordLength));
		}
		
		return phrases;
	}
	
	public static String getPassengerInitials(String passengerInfo) {
		String initials;
		String[] fio = passengerInfo.split("\n");
		
		initials = fio[0] + " " + fio[1].substring(0,  1) + " " + fio[1].split(" ")[1].substring(0,  1);
		
		return initials;
	}
	
	public static String getPassportID(String passengerInfo) {
		String id;
		String[] pass = passengerInfo.split("\n");
		
		id = "";
		
		return id;
	}
	
	public static String[] getSeatInfo(String passengerInfo) {
		String[] seat = new String[2];
		String seatNum;
		String seatInfo;
		String upDown;
		String seatPrice = null;
		String[] arrayTemp;		
		
		arrayTemp = passengerInfo.split("\n");
		
		if (passengerInfo.contains("Страховой полис")) {
			seatInfo = arrayTemp[6].split(",")[0];
			seatNum = seatInfo.split(" ")[2];
			upDown = seatInfo.split(" ")[3];
			seatInfo = seatNum + " " + translate(upDown);
			
			seatPrice = arrayTemp[6].split(",")[1].replace("цена ", "").replace(" руб.", "").trim();
		} else {
			seatInfo = arrayTemp[4].split(",")[0];
			seatNum = seatInfo.split(" ")[2];
			upDown = seatInfo.split(" ")[3];
			seatInfo = seatNum + " " + translate(upDown);
			
			seatPrice = arrayTemp[4].split(",")[1].replace(" цена ", "").replace(" руб.", "").trim();    
		}
		
		seat[0] = seatInfo;
		seat[1] = seatPrice;
		
		return seat;
	}
	
	public static String getTarifInfo(String inString) {
		String tarifInfo = new String();
		String[] arrayTemp;
		
		arrayTemp = inString.split("\n");
		
		if (inString.contains("Страховой полис")) {
			tarifInfo = arrayTemp[8];
			
			switch (tarifInfo) {
				case "Школьный":
					tarifInfo += translate("Школьный") + " " + arrayTemp[9];
					break;
				case "Детский":
					tarifInfo += translate("Детский");
					break;
				case "Полный":
					tarifInfo += translate("Полный");
					break;
			}
		} else {
			tarifInfo = arrayTemp[6];
			
			switch (tarifInfo) {
				case "Школьный":
					tarifInfo += translate("Школьный") + " " + arrayTemp[7];
					break;
				case "Детский":
					tarifInfo += translate("Детский");
					break;
				case "Полный":
					tarifInfo += translate("Полный");
					break;
			}
		}
			
		return tarifInfo;
	}
	
	public static String translate(String abbr) {
		String ret = "";
		
		switch (abbr) {
			case "Н":
				ret = "НИЖНЕЕ / LOWER";
				break;
			case "В":
				ret = "ВЕРХНЕЕ / UPPER";
				break;
			case "Школьный":
				ret = " / Youth";
				break;
			case "Детский":
				ret = " / Child";
				break;
			case "Полный":
				ret = " / Full price";
				break;
			case "Пройдена электронная регистрация":
				ret = " Registered";
				break;
		}
		
		return ret;
	}
	
	public static String getCancelTime(String time) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy kk:mm");
		String dateTime = time.split("\n")[1] + " " + time.split("\n")[2];		
		
		LocalDateTime localTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("dd.MM.yyyy kk:mm")).minusHours(1);
		
		return localTime.format(formatter);
	}
	
	public static String getNowDateTime() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy kk:mm"));
	}
	
	/*
	public static SelenideElement findTrain(ElementsCollection trains, int trainNumber) {
		SelenideElement train = null;
		StringBuilder str = null;
		
		for (SelenideElement _train : trains) {
			SelenideElement e = _train.$(By.className("trlist__cell-pointdata__tr-num"));
			int i_train = Integer.parseInt(e.getText().split(" ")[1]);
			str.append(e.getText().split(" ")[1]);
			
			if (trainNumber >= i_train) {
				
			}
		}
		
		return train;
	}*/
}
