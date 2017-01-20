package test.ru.oooinex.autotests.buyticket.tests;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.open;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
//import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;

import test.ru.oooinex.autotests.buyticket.testitself.DirectTrainPagesTest;
import test.ru.oooinex.dataproviders.PaymentDataProvider;
import test.ru.oooinex.dataproviders.TrainDataProvider;
import test.ru.oooinex.listeners.CustomTest;
import test.ru.oooinex.utilities.Tools;

@Listeners({ CustomTest.class })

public class DirectTrainTest {
	static DirectTrainPagesTest rzdPage;
	static WebDriver driver;
	static boolean paymentGatewayPlug;
	static String url;
	static String paymentMethod;
	
	@Parameters({ "baseUrl", "holdBrowser", "browser", "profile", "reportsFolder", "reportsUrl", "timeout", "paymentGatewayPlug" })
	@BeforeClass
	public static void setUp(String baseUrl, String holdBrowser, String browser, String profile, String reportsFolder, String reportsUrl, String timeout, String paymentPlug) throws IOException {
		System.setProperty("webdriver.chrome.driver", "D:/hgtest/Drivers/chromedriver.exe");
		System.setProperty("webdriver.opera.driver", "D:/hgtest/Drivers/operadriver.exe");
		System.setProperty("webdriver.ie.driver", "D:/hgtest/Drivers/IEDriverServer.exe");
		System.setProperty("phantomjs.binary.path", "D:/hgtest/Drivers/phantomjs.exe");
		System.setProperty("webdriver.gecko.driver", "D:/hgtest/Drivers/geckodriver.exe");
		
		if (driver == null) {
			Configuration.holdBrowserOpen = holdBrowser.contentEquals("true") ? true : false;
			Configuration.timeout = Long.parseLong(timeout);
			Configuration.pageLoadStrategy = "normal";					// Possible values: "none", "normal" and "eager".
			Configuration.savePageSource = false;

			if (!reportsFolder.isEmpty())
				Configuration.reportsFolder = reportsFolder;
			
			if (!reportsUrl.isEmpty())
				Configuration.reportsUrl = reportsUrl;
			
			//Configuration.browser = browser;
			//Configuration.startMaximized = true;

			driver = Tools.createDriver(browser, profile);
			driver.manage().window().maximize();
			WebDriverRunner.setWebDriver(driver);
			
			paymentGatewayPlug = paymentPlug.contentEquals("true") ? true : false;

			String baseURL = System.getProperty("PTK");
			url = baseURL == null ? baseUrl : baseURL;
	
			rzdPage = open(url, DirectTrainPagesTest.class);
		}
	}
	
	@Parameters({ "login", "password", "reportsUrl" })
	@Test(description = "Логинимся на сайт")
	public void Login(String login, String password, String reportsUrl) {
		Cookie cookie = driver.manage().getCookieNamed("BetaTest");					// Переключаемся на старую версию с помощью изменения куки
		String value = cookie.getValue();
		
		if (value.equals("3") | value.equals("1")) {
			Cookie newCookie = new Cookie("BetaTest", "0"); 
			driver.manage().addCookie(newCookie);
			driver.navigate().refresh();
		}
		
		SelenideElement enter = rzdPage.Is_Start_Page().$(By.linkText("Вход"));
		SelenideElement exit = rzdPage.Is_Start_Page().$(By.linkText("Выход"));
		
		if (enter.isDisplayed())
			rzdPage.login(login, password).result().shouldHave(text("Выход"));
		else if (exit.isDisplayed()){ } 
	}

	@Test(description = "Покупаем электронную карту и запоминаем её номер для дальнейшего использования", 
		  dataProvider = "DirectTrain", dataProviderClass = TrainDataProvider.class)
		public void PayElectronicCard(String from, String to, Long dayOfWeek, boolean noSeats, boolean localTrain, boolean andReturn, boolean foreignTrainTo, boolean foreignTrainBack) {
		
	}
	
	@Test(description = "Заполняем стартовую форму и нажимаем 'Купить билет' или 'Расписание' в зависимости от флага 'Только с билетами'", 
		  dataProvider = "DirectTrain", dataProviderClass = TrainDataProvider.class, 
		  dependsOnMethods = { "Login" })
	public void FillStartForm(String _dateTo, String _dateFrom, String _from, String _to, String _noSeats, String _localTrain, String _andReturn, String _foreignTrainTo, String _foreignTrainBack) {
		// Заполняем стартовую форму и нажимаем 'Купить билет' или 'Расписание'
		// в зависимости от флага 'Только с билетами'
		String from = _from.split(": ")[1];
		String to = _to.split(": ")[1];
		String dateTo = _dateTo.split(": ")[1];
		boolean noSeats = _noSeats.split(": ")[1].contains("true") ? true : false;
		boolean localTrain = _localTrain.split(": ")[1].contains("true") ? true : false;
		boolean andReturn = _andReturn.split(": ")[1].contains("true") ? true : false;
		boolean foreignTrainTo = _foreignTrainTo.split(": ")[1].contains("true") ? true : false;
		boolean foreignTrainBack = _foreignTrainBack.split(": ")[1].contains("true") ? true : false;
		
		rzdPage.trainShedule(from, to, dateTo, noSeats, localTrain, andReturn, foreignTrainTo, foreignTrainBack); //.result().shouldBe(CollectionCondition.sizeGreaterThan(0));
	}

	@Test(description = "Выбираем прямой поезд", dataProvider = "DirectTrain", dataProviderClass = TrainDataProvider.class, 
		  dependsOnMethods = { "FillStartForm" })
	public void SelectDirectTrain(String trainNumber, String carCheckers) {
		// Если список поездов появился, выбираем нужный и нажимаем 'Продолжить'
		rzdPage.selectDirectTrain(trainNumber, carCheckers).result().shouldBe(CollectionCondition.sizeGreaterThan(0));
	}
	
	@Test(description = "Выбираем нужный вагон по номеру, в прямом поезде и нажимаем 'Продолжить'. Должен появиться список пассажиров", 
		  dataProvider = "DirectTrain", dataProviderClass = TrainDataProvider.class, 
		  dependsOnMethods = { "SelectDirectTrain" })
	public void SelectWagonDirectTrain(String wagonType, String wagonCategory, String servicesAndCapabilities) {
		// Если список вагонов появился выбираем нужный и продолжить
		rzdPage.wagonSelectDirectTrain(wagonType, wagonCategory, servicesAndCapabilities).result().shouldHave(text("Список пассажиров"));
	}

	@Test(description = "Заполняем все формы пассажиров необходимыми параметрами", 
		  dataProvider = "DirectTrain", dataProviderClass = TrainDataProvider.class, 
		  dependsOnMethods = { "SelectWagonDirectTrain" })
	public void FillPassengerForms(String passengers) {
		// Если список пассажиров появился заполняем все формы
		rzdPage.fillPassengerForms(passengers); //.result().shouldNotHave(text("Не выбран признак купе"));
	}

	@Test(description = "Заполняем параметры мест 'Границы мест'; 'Признак места'; 'В одном купе'", 
		  dataProvider = "DirectTrain", dataProviderClass = TrainDataProvider.class, 
		  dependsOnMethods = { "FillPassengerForms" })
	public void FillPlaceSigns(Object[] places) {
		// Если список пассажиров появился заполняем все формы
		rzdPage.fillPlaceSigns(places); //.result().shouldHave(text("Проверка данных пассажиров"));
	}
	
	@Test()
	public void CheckBedding(Object[] places) {
		// Если список пассажиров появился заполняем все формы
		rzdPage.checkBadding(); //.result().shouldHave(text("Проверка данных пассажиров"));
	}

	@Test(description = "Проверка тарифов", dependsOnMethods = { "FillPlaceSigns" }, 
		  dataProvider = "DirectTrain", dataProviderClass = TrainDataProvider.class)
	public void CheckTariffs(String passengerNames) {
		// Проверка тарифов присланных из Экспресса
		rzdPage.checkTariffs(passengerNames);
	}
	
	@Test(description = "Проверка данных пассажира", 
		  dependsOnMethods = { "CheckTariffs" })
	@Parameters({ "payMethod" })
	public void PassengerDataValidation(String payMethod) {
		paymentMethod = payMethod;
		// Проверка данных пассажира, если все правильно, ставим галку 'Подтверждаю' и жмем оплатить
		rzdPage.passengerValidation(payMethod); //.result().shouldHave(text("Оплатить"));
	}

	@Test(description = "Оплата через Банк", dataProvider = "payment",
			dataProviderClass = PaymentDataProvider.class, dependsOnMethods = { "PassengerDataValidation" })
	public void Payment(String cardNumber, String cardholder, String cvv2, String validityPeriod, String yandexLogin, String yandexPassword, String paymentPassword) {
		// Заполняем параметры карты и жмем 'Оплатить'
		
		if (paymentMethod.contains("bank")) {
			if (!paymentGatewayPlug)			// Проверяем не установлена ли заглушка
				rzdPage.paymentBank(cardNumber, cardholder, cvv2, validityPeriod).result().waitUntil(Condition.text("Оплата прошла успешно"), 120000);
		} else if (paymentMethod.contains("yandex")) {
			rzdPage.paymentYandex(yandexLogin, yandexPassword, paymentPassword).result().waitUntil(Condition.text("Оплата прошла успешно"), 120000);
		}
	}
	
	@Test(description = "Завершить оформление заказа",
		  dataProvider = "DirectTrain", dataProviderClass = TrainDataProvider.class, dependsOnMethods = { "Payment" })
	public void CompleteOrderProcessing() {
		rzdPage.completeOrderProcessing().result();
	}

	@Test(description = "Проверить результаты", dependsOnMethods = { "CompleteOrderProcessing" },
		  dataProvider = "DirectTrain", dataProviderClass = TrainDataProvider.class)
	public void Results(String wagonCategory) {
		rzdPage.results(wagonCategory).result();
	}
	
	@Test(description = "Проверить результирующие тарифы", dependsOnMethods = { "Results" } )
	public void CheckResultTariffs() {
		rzdPage.checkResultTariffs();
	}

	
	@Test(description = "Проверить бланк заказа без оформленных возвратов", dependsOnMethods = { "CheckResultTariffs" })
	public void CheckOrderFormsWithoutRefund() {
		rzdPage.checkOrderFormsWithoutRefund();
	}
	
	@Test(description = "Отмена электронной регистрации и возврат билета", 
		  dataProvider = "DirectTrain", dataProviderClass = TrainDataProvider.class, dependsOnMethods = { "CheckOrderFormsWithoutRefund" })
	public void CancelRegistrationAndRefund(String trainNumber) {
		rzdPage.cancelRegistrationAndRefund(trainNumber);
	}
	
	@Test(description = "Проверить бланк заказа c оформленными возвратами", 
		  dataProvider = "DirectTrain", dataProviderClass = TrainDataProvider.class)
	public void CheckOrderFormsWithRefund(String trainNumber) {
		rzdPage.checkOrderFormsWithRefund(trainNumber);
	}
	
	/*
	private static final String HREF = "Last URL: <a href=\"%s\">%s</a>";
	private static final String HREF_IMG = "&lt;a href=\"%s\"&gt;&lt;img src=\"%s\" alt=\"\" width=\"100\" height=\"100\" /&gt;&lt;/a&gt;";
	
	@AfterMethod(alwaysRun = true)
	public void logResult(final ITestContext context, final ITestResult result) throws IOException {
		Reporter.setCurrentTestResult(result);
		if (!result.isSuccess()) {
			Reporter.log(String.format(HREF, driver.getCurrentUrl(), driver.getCurrentUrl()));
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			String imagePath = result.getName() + ".jpg";
			File file = new File(imagePath);
			FileUtils.copyFile(scrFile, file);
			Reporter.log(String.format(HREF_IMG, file.getName(), file.getName()));
		}
	}*/
	
	@Parameters({ "holdBrowser" })
	@AfterSuite
	public static void tearDown(String holdBrowser) {
		if (driver != null) {
			if (!holdBrowser.contentEquals("true") ? true : false)
				driver.quit();
		}
	}
}