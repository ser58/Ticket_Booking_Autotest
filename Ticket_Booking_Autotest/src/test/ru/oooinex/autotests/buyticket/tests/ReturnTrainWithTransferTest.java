package test.ru.oooinex.autotests.buyticket.tests;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.open;

import java.io.IOException;

import org.openqa.selenium.By;
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

import test.ru.oooinex.autotests.buyticket.testitself.ReturnTrainWithTransferPagesTest;
import test.ru.oooinex.dataproviders.PaymentDataProvider;
import test.ru.oooinex.dataproviders.TrainDataProvider;
import test.ru.oooinex.listeners.CustomTest;
import test.ru.oooinex.utilities.Tools;

@Listeners({ CustomTest.class })

public class ReturnTrainWithTransferTest {
	static ReturnTrainWithTransferPagesTest rzdPage;
	static WebDriver driver;
	static boolean paymentGatewayPlug;
	static String url;
	
	@Parameters({ "baseUrl", "holdBrowser", "browser", "profile", "reportsFolder", "reportsUrl", "timeout", "paymentGatewayPlug" })
	@BeforeClass
	public static void setUp(String baseUrl, String holdBrowser, String browser, String profile, String reportsFolder, String reportsUrl, String timeout, String paymentPlug) throws IOException {
		if (driver == null) {
			paymentGatewayPlug = paymentPlug.contentEquals("true") ? true : false;
			Configuration.holdBrowserOpen = holdBrowser.contentEquals("true") ? true : false;
			Configuration.timeout = Long.parseLong(timeout);
			Configuration.savePageSource = false;
			//Configuration.browser = browser;
			Configuration.browserSize = "1344x1040";
			Configuration.pageLoadStrategy = "normal";			// Possible values: "none", "normal" and "eager".
			Configuration.startMaximized = true;
			String baseURL = System.getProperty("PTK");

			if (!reportsFolder.isEmpty())
				Configuration.reportsFolder = reportsFolder;
			
			if (!reportsUrl.isEmpty())
				Configuration.reportsUrl = reportsUrl;
			
			driver = Tools.createDriver(browser, profile);
			WebDriverRunner.setWebDriver(driver);
			
			url = baseURL == null ? baseUrl : baseURL;
			
			rzdPage = open(url, ReturnTrainWithTransferPagesTest.class);
			
			//driver = WebDriverRunner.getWebDriver();
			driver.manage().window().maximize();
			//driver.manage().window().setSize(new Dimension(1344, 1040));
		}
	}

	@Parameters({ "login", "password", "reportsUrl" })
	@Test(description = "Логинимся на сайт")
	public void Login(String login, String password, String reportsUrl) {
		SelenideElement enter = rzdPage.Is_Start_Page().$(By.linkText("Вход"));
		SelenideElement exit = rzdPage.Is_Start_Page().$(By.linkText("Выход"));
		
		if (enter.isDisplayed())
			rzdPage.login(login, password).result().shouldHave(text("Выход"));
		else if (exit.isDisplayed()){ } 
	}

	@Test(description = "Заполняем стартовую форму и нажимаем 'Купить билет' или 'Расписание' в зависимости от флага 'Только с билетами'", 
		  dataProvider = "ReturnTrainWithTransfer", dataProviderClass = TrainDataProvider.class, dependsOnMethods = { "Login" })
	public void FillStartForm(String _dateTo, String _dateFrom, String _from, String _to, String _noSeats, String _localTrain, String _andReturn, String _foreignTrainTo, String _foreignTrainBack) {
		// Заполняем стартовую форму и нажимаем 'Купить билет' или 'Расписание'
		// в зависимости от флага 'Только с билетами'
		String from = _from.split(": ")[1];
		String to = _to.split(": ")[1];
		String dateTo = _dateTo.split(": ")[1];
		String dateFrom = _dateFrom.split(": ")[1];
		
		boolean noSeats = _noSeats.split(": ")[1].contains("true") ? true : false;
		boolean localTrain = _localTrain.split(": ")[1].contains("true") ? true : false;
		boolean andReturn = _andReturn.split(": ")[1].contains("true") ? true : false;
		boolean foreignTrainTo = _foreignTrainTo.split(": ")[1].contains("true") ? true : false;
		boolean foreignTrainBack = _foreignTrainBack.split(": ")[1].contains("true") ? true : false;

		rzdPage.trainShedule(from, to, dateTo, dateFrom, noSeats, localTrain, andReturn, foreignTrainTo, foreignTrainBack); //.result().shouldBe(CollectionCondition.sizeGreaterThan(0));
	}

	@Test(description = "Выбираем нужный поезд по номеру и нажимаем 'Продолжить'. Должен появиться список вагонов", 
		  dataProvider = "ReturnTrainWithTransfer", dataProviderClass = TrainDataProvider.class, 
		  dependsOnMethods = { "FillStartForm" })
	public void SelectReturnTrainWithTransfer(String[] directTrain, String[] directTransferTrain, String[] returnTrain, String[] returnTransferTrain) {
		// Если список поездов появился, выбираем нужный и нажимаем 'Продолжить'
		rzdPage.trainWithReturnSelect(directTrain, directTransferTrain, returnTrain, returnTransferTrain).result().shouldBe(CollectionCondition.sizeGreaterThan(0));
	}

	@Test(description = "Выбираем нужный вагон по номеру и нажимаем 'Продолжить'. Должен появиться список пассажиров", 
		  dataProvider = "ReturnTrainWithTransfer", dataProviderClass = TrainDataProvider.class, 
		  dependsOnMethods = { "SelectReturnTrainWithTransfer" })
	public void SelectWagonReturnTrainWithTransfer(String[] directWagon, String[] directTransferWagon, String[] returnWagon, String[] returnTransferWagon) {
		// Если список вагонов появился выбираем нужный и продолжить
		rzdPage.wagonSelect(directWagon, directTransferWagon, returnWagon, returnTransferWagon).result().shouldHave(text("Список пассажиров"));
	}

	@Test(description = "Заполняем все формы пассажиров необходимыми параметрами", 
		  dataProvider = "ReturnTrainWithTransfer", dataProviderClass = TrainDataProvider.class, 
		  dependsOnMethods = { "SelectWagonReturnTrainWithTransfer" })
	public void FillPassengerForms(String passengers) {
		// Если список пассажиров появился заполняем все формы
		rzdPage.fillPassengerForms(passengers); // .result().shouldNotHave(text("Не выбран признак купе"));
	}

	@Test(description = "Заполняем параметры мест 'Границы мест'; 'Признак места'; 'В одном купе'", 
		  dataProvider = "ReturnTrainWithTransfer", dataProviderClass = TrainDataProvider.class, 
		  dependsOnMethods = { "FillPassengerForms" })
	public void FillPlaceSigns(Object[] directPlaces, Object[] transferPlaces, Object[] returnPlaces, Object[] returnTransferPlaces) {
		// Если список пассажиров появился заполняем все формы
		rzdPage.fillPlaceSigns(directPlaces, transferPlaces, returnPlaces, returnTransferPlaces).result().shouldHave(text("Проверка данных пассажиров"));
	}

	@Test(description = "Проверка данных пассажира", dependsOnMethods = { "FillPassengerForms" })
	public void PassengerDataValidation() {
		// Проверка данных пассажира, если все правильно, ставим галку 'Подтверждаю' и жмем оплатить
		rzdPage.passengerValidationForms(); //.result().shouldHave(text("Оплатить"));
	}

	@Test(description = "Оплата", dataProvider = "payment", 
		  dataProviderClass = PaymentDataProvider.class, dependsOnMethods = { "PassengerDataValidation" })
	public void Payment(String cardNumber, String cardholder, String cvv2, String validityPeriod) {
		// Заполняем параметры карты и жмем 'Оплатить'
		// Проверяем не установлена ли заглушка
		if (!paymentGatewayPlug)
			rzdPage.payment(cardNumber, cardholder, cvv2, validityPeriod).result().waitUntil(Condition.text("Оплата прошла успешно"), 120000);
	}

	@Test(description = "Завершить оформление заказа", dependsOnMethods = {	"Payment" })
	public void CompleteOrderProcessing() {
		SelenideElement element = rzdPage.completeOrderProcessing().result();
		element.getText();
	}

	@Test(description = "Проверить результаты", dependsOnMethods = { "CompleteOrderProcessing" }, 
		  dataProvider = "ReturnTrainWithTransfer", dataProviderClass = TrainDataProvider.class)
	public void Results(String wagonCategory) {
		rzdPage.results(wagonCategory).result();
	}

	@Test(description = "Отмена электронной регистрации и возврат билета",  dependsOnMethods = { "CompleteOrderProcessing" },
		  dataProvider = "ReturnTrainWithTransfer", dataProviderClass = TrainDataProvider.class)
	public void CancelRegistrationAndRefund(String trainNumber) {
		rzdPage.cancelRegistrationAndRefund(trainNumber);
	}

	@Parameters({ "holdBrowser" })
	@AfterSuite
	public static void tearDown(String holdBrowser) {
		if (driver != null) {
			if (!holdBrowser.contentEquals("true") ? true : false)
				driver.quit();
		}
	}
}
