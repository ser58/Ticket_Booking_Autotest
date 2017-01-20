package test.ru.oooinex.autotests.buyticket.testitself;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.or;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.by;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selectors.byLinkText;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.page;

import java.util.Hashtable;

import org.openqa.selenium.By;

//import org.openqa.selenium.By;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import test.ru.oooinex.autotests.buyticket.pages.CancelRegistrationAndRefund;
import test.ru.oooinex.autotests.buyticket.pages.CompleteOrderProcessing;
import test.ru.oooinex.autotests.buyticket.pages.Login_Result;
import test.ru.oooinex.autotests.buyticket.pages.PassengerForm;
import test.ru.oooinex.autotests.buyticket.pages.PaymentForm;
import test.ru.oooinex.autotests.buyticket.pages.Results;
import test.ru.oooinex.autotests.buyticket.pages.Schedule_Received;
import test.ru.oooinex.autotests.buyticket.pages.TrainSelect;
import test.ru.oooinex.autotests.buyticket.pages.WagonSelect;
import test.ru.oooinex.utilities.Tools;

public class DirectTrainPagesTest {
	String dateTo;
	String reverseWagonType;
	boolean transfer;
	boolean foreignTrainTo;
	boolean foreignTrainBack;
	static int numPassengers;
	static String shippingCompany;
	String[] tarifs;
	String[] trainsType = new String[2];
	String[] specialBirthdays;
	String[] lastNames;
	boolean newVersion;
	
	// Переменные для данных о поездке
	Hashtable<String, String> tripDetails = new Hashtable<String, String>();
	String[] passengerDetails;
	
	public SelenideElement Is_Start_Page() {
		return $(byXpath(".//*[@id='header']"));
	}
	
	public Login_Result login(String login, String password) {
		$(byLinkText("Вход")).click();
 		$(byId("j_username")).setValue(login);
		$(byId("j_password")).setValue(password); 
		$(byXpath("//button[@name='action']")).click();
		
		return page(Login_Result.class);
	}
	
	public Schedule_Received trainShedule (String from, String to, String dateTo, boolean onlyWithTickets, boolean localTrain, boolean andReturn, boolean foreignTrainTo, boolean foreignTrainBack) {
		this.foreignTrainTo = foreignTrainTo;
		this.dateTo = dateTo;
		
		$(byXpath(".//*[@id='headNav2']/tbody/tr/td[1]/a")).click();  				// Кликнуть по ссылке "Пассажирам"
		
		$(byClassName("box-form__tabz")).waitUntil(Condition.appears, 30000);		// Ждем когда загрузится форма
		
		$(byXpath(".//*[@id='name0']")).clear();
		$(byXpath(".//*[@id='name0']")).setValue(from);								// Выбрать станцию отправления
		$(byXpath(".//*[@id='name1']")).clear();
		$(byXpath(".//*[@id='name1']")).setValue(to);								// Выбрать станцию назначения
		
		$("#date0").setValue(dateTo);												// Установить дату 'Туда'
		
		$("#NoSeats").setSelected(onlyWithTickets);									// Флажок 'Только с билетами'
		
		$("#SubTrains").setSelected(localTrain);									// Флажок 'Электрички' 
		
		if (onlyWithTickets) {
			if ($("#Submit").isEnabled())
				$("#Submit").click();												// Нажать кнопку 'Купить билет' если она активна.
		} else {
			if ($("#Submit1").isEnabled())
				$("#Submit1").click();												// Нажать кнопку 'Расписание' если она активна.
		}
		
		$(byXpath("//div[@id='Part0']/div[4]")).waitUntil(text("вариантов по прямому маршруту"), 120000);
		
		return page(Schedule_Received.class);
	}
	
	public TrainSelect selectDirectTrain (String trainNumber, String carCheckers) {
		SelenideElement train = null;
		ElementsCollection trains;
		SelenideElement continueButton;
				
		// carChecker будет реализован позже. Имеется ввиду выбор типа вагона (все, ничего,	Сидячий Общий Плацкартный Купе Мягкий Люкс) 

		// Список поездов получили, теперь выбираем нужный и жмем "Продолжить"
		// Если поезд один, ничего не выбираем, а просто жмём батон продолжить
		
		trains = $$(byXpath(".//*[@id='Part0']/div[5]/table/tbody/tr"));
		
		train = trains.findBy(Condition.text(trainNumber));
		
		if (train.exists())
			train.$(byClassName("j-train-radio")).click();;
		
		continueButton = $(byId("continueButton")).waitUntil(enabled, 20000);
		if (continueButton.isEnabled())
			continueButton.click();
		
		return page(TrainSelect.class);
	}
	
	public WagonSelect wagonSelectDirectTrain(String wagonType, String wagonCategory, String servicesAndCapabilities) {
		SelenideElement wagon;
		ElementsCollection wagons;
		SelenideElement continueButton;
		
		// Ждем пока не появится список вагонов
		$(byXpath("//div[@id='Main']/div/div/table[2]")).waitUntil(appear, 30000);
		
		wagons = $$(".j-car-item.trlist__trlist-row");
		
		for (int i = 0; i < wagons.size(); i++ ) {
			wagon = wagons.get(i);
			if (wagon.has(Condition.text(wagonType + " " + wagonCategory + " " + servicesAndCapabilities))) {
				$(byXpath("(//input[@type='radio'])[" + (i + 1) + "]")).click();
				break;
			}
		}
		
		wagon = wagons.findBy(text(wagonType + " " + wagonCategory + " " + servicesAndCapabilities));
		wagon.$("input[type='radio']").click();
	
		continueButton = $(byId("continueButton")).waitUntil(enabled, 20000);
		if (continueButton.isEnabled())
			continueButton.click();	
		
		return page(WagonSelect.class);
	}
	
	public PassengerForm fillPassengerForms(String passengers) {
		String[] listPassenger = passengers.split(";");
		numPassengers = listPassenger.length;
		tarifs = new String[listPassenger.length];
		specialBirthdays = new String[listPassenger.length];
		lastNames = new String[listPassenger.length];
		
		SelenideElement element;
		ElementsCollection passOptions;
		ElementsCollection docType;
		boolean strahovka;
		String lastName = "";
		
		// Убираем всех пассажиров
		passOptions = $$(".j-pass-item.pass-item.trlist-pass__pass-item");
		
		for (int i = 0; i < passOptions.size() - 2; i++) {
			element = $(byXpath(".//*[@id='PassList']/div[1]/div[2]/div/h3"));
			if (element.has(text("Убрать")))
				$("a.pass_IU_PassInfo__addOrDel").click();
		}
		
		// Добавляем нужное количество пассажиров
		for (int i = 1; i < listPassenger.length; i++) {
			$(byXpath("//div[" + (i + 1) + "]/div/h3/span/a")).click();
		}
		
		ElementsCollection passItems = $$(byClassName("j-pass-item "));
		
		// Заполняем в цикле все параметры каждого пассажира
		for (int i = 0; i < listPassenger.length; i++) {
			SelenideElement passItem = passItems.get(i);
			
			String specialBirthday;
			String[] passParams = listPassenger[i].replace("'", "").split(",");
			
			// Если иностранный поезд сдвигаем все селекторы на один в меньшую сторону
			// По причине отсутствия отчества
			SelenideElement midName = passItem.$(byAttribute("name", "midName"));
			if (foreignTrainTo) {
				midName.shouldNotBe(visible);
				lastName = passParams[0].trim() + Tools.generateRandomSymbolsEN(3);
			} else {
				lastName = passParams[0].trim() + Tools.generateRandomSymbolsRU(3);
				midName.setValue(passParams[2].trim()); 					// Отчество
			}
			
			lastNames[i] = lastName;
			passItem.$(byAttribute("name", "lastName")).setValue(lastName); 				// Фамилия
			passItem.$(byAttribute("name", "firstName")).setValue(passParams[1].trim()); 	// Имя 
			
			SelenideElement birthday = passItem.$(byAttribute("name", "birthdate"));
			birthday.clear();
			
			if (passParams[5].trim().contains("Школьный")) {	// Проверяем школьный тариф, вычисляем дату рождения = дата отправления - 10 лет + 1 день
				specialBirthday = Tools.getBirthday(dateTo, 10, 0, 1);
				birthday.setValue(specialBirthday).pressEnter();							// Дата рождения
				Selenide.sleep(1000);
				
				passItem.$(byAttribute("name", "schoolboy")).setSelected(true);				// Выбираем тариф 'Школьный'
				passItem.$(byAttribute("name", "tariff")).shouldNotBe(enabled);				// Проверяем недоступность комбобокса 'Тариф'
				
			} else {
				switch (passParams[5].trim()) {												// Вычисляем дни рождений для всех остальных тарифов 
		            case "SENIOR (от 60 лет)":  
		            	specialBirthday = Tools.getBirthday(dateTo, 60, 0, 1);
		            	break;
		            case "JUNIOR (от 12 до 26 лет)":
		            	specialBirthday = Tools.getBirthday(dateTo, 12, 0, 1);
		            	break;
		            case "JUNIOR (от 10 до 21 года)":
		            	specialBirthday = Tools.getBirthday(dateTo, 10, 0, 1);
		            	break;
		            case "Детский":
		            	specialBirthday = Tools.getBirthday(dateTo, 5, 0, 1);
		            	break;
		            case "Детский без места":
		            	specialBirthday = Tools.getBirthday(dateTo, 1, 0, -1);
		            	break;
		            default:
		            	specialBirthday = passParams[8].trim();
		            	break;
				}
				
				birthday.setValue(specialBirthday).pressEnter();								// Дата рождения
				passItem.$(byAttribute("name", "tariff")).selectOption(passParams[5].trim());	// Тариф
			}

			specialBirthdays[i] = specialBirthday;
			
			// Проверяем что выпадающий список 'Тип документа' автоматически установился в 'Свидетельство о рождении'
			// и что список содержит только три элемента 'Загран паспорт', 'Иностранный документ' и 'Свидетельство о рождении'
			docType = passItem.$(byAttribute("name", "docType")).$$(By.tagName("option"));
			
			if (!foreignTrainTo) {
				if (passParams[5].trim().contentEquals("Детский") | passParams[5].trim().contentEquals("Детский без места")) {
					docType.shouldHaveSize(3);
					docType.shouldHave(texts("Заграничный паспорт", "Иностранный документ", "Свидетельство о рождении"));
				}
			} else {
				docType.shouldHaveSize(2);
				docType.shouldHave(texts("Заграничный паспорт", "Иностранный документ"));
			}
			
			tarifs[i] = passParams[5].trim();					// Сохраняем все тарифы в массив, необходимо для следующих тестов
			
			passItem.$(byAttribute("name", "docType")).selectOption(passParams[3].trim());		// Документ тип
			passItem.$(byAttribute("name", "docNumber")).setValue(passParams[4].trim()); 		// Номер документа	
			passItem.$(byAttribute("name", "gender")).selectOption(passParams[7].trim());		// Пол
			passItem.$(byAttribute("name", "country")).selectOption(passParams[6].trim()); 		// Государство
			
			// Оформляем страховой полис
			
			element = passItem.$(byAttribute("name", "insCheck"));								// Чекбокс 'Оформление страхового полиса'
			passItem.$(byClassName("j-ins-panel")).$(byClassName("jfield-box")).shouldHave(text("Оформление страхового полиса "));
			
			strahovka = !passParams[9].trim().isEmpty() ? true : false;
			
			if (strahovka) {																	// Устанавливаем чекбокс 'Оформление страхового полиса'
				element.setSelected(true);
				ElementsCollection insurances = passItem.$$(byClassName("j-ins-item"));
				Tools.selectInsuranceCompany(insurances, i + 1, passParams[9].trim());
			} else {
				if (element.exists())
					element.setSelected(false);
			}
		}
		
		return page(PassengerForm.class);
	}
	
	public PassengerForm fillPlaceSigns(Object[] places) {
		SelenideElement element;
		String plGender;
		
		String wagonType = $(byXpath(".//*[@id='TrainsList']/div/table/tbody/tr/td[4]")).getText().split("\n")[1].split(" ")[0];
		
		String placeFrom = (String) Long.toString((long) places[2]);
		String placeTo = (String) Long.toString((long) places[3]);
				
    	$(byXpath(".//*[@id='TrainsList']/div/div[2]/table/tbody/tr/td[1]/span[1]/input")).setValue(placeFrom);	// Граница мест 'С'
		$(byXpath(".//*[@id='TrainsList']/div/div[2]/table/tbody/tr/td[1]/span[2]/input")).setValue(placeTo);		// Граница мест 'По'
		
		switch (wagonType) {
	        case "Сидячий":  
	        	break;
	        case "Плацкартный":
	    		$(byXpath("//input[@name='plBedding']")).setSelected((boolean) (places[4]));						// Оплатить постельное бельё
	    		$(byXpath("//select[@name='plUpDown']")).selectOption((String) places[5]);							// Верхние нижние места
	    		$(byXpath("//select[@name='plComp']")).selectOption((String) places[6]);							// Расположение мест
	        	break;	
	        case "Купе":	
	        	plGender = (String) places[0];
	    		
	        	if ($(byXpath(".//*[@id='ErrorBox']")).getText().contains("Не выбран признак купе")) 						// Если не выбран признак места, выбираем его 
	    			$(byXpath(".//*[@id='TrainsList']/div/div[2]/table/tbody/tr/td[2]/div/select")).selectOption(plGender);
	    				
	    		element = $(byXpath(".//*[@id='TrainsList']/div/div[2]/table/tbody/tr/td[3]/div/label/input"));

	    		if (numPassengers > 1) {
	    			// Устанавливаем чекбокс 'В одном купе'
	    			element.setSelected((boolean) places[1]);
	    		} else
	    			element.shouldNotBe(visible);
	    		
	        	break;
	        case "СВ":
	        	plGender = (String) places[0];
	    		
	        	if ($(byXpath(".//*[@id='ErrorBox']")).getText().contains("Не выбран признак купе")) 						// Если не выбран признак места, выбираем его 
	    			$(byXpath(".//*[@id='TrainsList']/div/div[2]/table/tbody/tr/td[2]/div/select")).selectOption(plGender);
	    				
	    		element = $(byXpath(".//*[@id='TrainsList']/div/div[2]/table/tbody/tr/td[3]/div/label/input"));			// В одном купе

	    		if (numPassengers > 1) {
	    			// Устанавливаем чекбокс 'В одном купе'
	    			element.setSelected((boolean) places[1]);
	    		} else
	    			element.shouldNotBe(visible);
	        	break;
	        case "Люкс":
	        	
	        	break;
		}
		
		// Батон продолжить должен выть активным, если так, нажимаем его
		$("#continueButton").shouldBe(enabled);
		$("#continueButton").click();
		
		$(byXpath("//div[@id='Main']")).waitUntil(appear, 60000);
				
		return page(PassengerForm.class);
	}
	
	public void checkBadding() {
		//String wagonType = $(byXpath(".//*[@id='TrainsList']/div/table/tbody/tr/td[4]")).getText().split("\n")[1].split(" ")[0];
	}
	
	public void checkTariffs(String passengerNames) {
		String[] listPassenger = passengerNames.split(";");
		ElementsCollection ticketData;
		
		ticketData = $$(byClassName("trlist-passlist__passenger__ticketdata"));
		
		//tripDetails = GetOrderInfo($(byXpath(".//*[@id='Main']")));
		//passengerDetails = GetPassengerInfo(ticketData);
		
		for (int i = 0; i < ticketData.size(); i++) {
			SelenideElement element = ticketData.get(i);
			
			SelenideElement tariffName = element.$("div.trlist-passlist__passenger__tariffname");
			
			if (element.isDisplayed()) {
				//tariff = Utilities.getTariff(specialBirthdays[i], dateTo, false);
				
				String lastName = listPassenger[i].split(",")[0].replace("'", "").trim();
				String firstName =  listPassenger[i].split(",")[1].replace("'", "").trim();
				String middleName = listPassenger[i].split(",")[2].replace("'", "").trim();

				if (element.has(text(lastName)) & element.has(text(firstName)) & element.has(text(middleName))) {
					//tariffName.shouldHave(exactText(tariff));
					tariffName.shouldHave(exactText(tarifs[i]));
				}
			}
		}
	}
	
	public void passengerValidation(String payMethod) {
		tripDetails = GetOrderInfo($(byXpath(".//*[@id='Main']")));
		passengerDetails = GetPassengerInfo($$(byClassName("trlist-passlist__passenger__ticketdata")));
		
		SelenideElement element;
		element = $(byXpath(".//*[@id='TrainOfferta']"));						// Достаём чекбокс 'С условиями ознакомлен'
		element.click();														// Устанавливаем его
		
		// Выбираем способ оплаты Yandex или Банк
		
		ElementsCollection payments = $$(byClassName("pass_IU_payment-methods__list-item"));
		
		if (payments.size() > 1) {
			if (payMethod.contains("bank")) {
				$(by("data-paymethod","BANK")).click();   	// Bank
			} else {
				$(by("data-paymethod","YANDEX")).click();  	// Yandex
			}
		}
					
		element = $(byXpath("//*[@id='Ref']"));								// Проверяем если батон 'Оплатить' доступен, то кликаем на него.
		if (element.isEnabled())
			element.click();
		
		//return page(PassengerValidationForm.class);
	}
	
	public PaymentForm paymentBank(String cardNumber, String cardholder, String cvv2, String validityPeriod) {
		// Заполняем все необходимые поля для оплаты картой
		
		$(byXpath(".//*[@id='cp-pan-decor']")).setValue(cardNumber); 			// Номер карты
		$(byXpath(".//*[@id='cp-full-name']")).setValue(cardholder);			// Владелец карты
		$(byXpath(".//*[@id='cvv2']")).setValue(cvv2);							// CVV2
		$(byXpath(".//*[@id='cp-expiration-date']")).setValue(validityPeriod);	// Действительна до
		
		$(byXpath(".//*[@id='OK']")).click();									// Нажать на батон 'Оплатить'
		
		$(byXpath(".//*[@id='Main']/div/div")).shouldNotHave(text("Время, отведенное на оплату, истекло. Ваш заказ будет отменен, а деньги возвращены на карту."));

		return page(PaymentForm.class);
	}

	public PaymentForm paymentYandex(String yandexLogin, String yandexPassword, String paymentPassword) {
		ElementsCollection elements = $(byXpath(".//*[@id='js']/body")).findAll(byName("login"));
		SelenideElement element = elements.findBy(Condition.attribute("class", "b-form-input__input"));
		element.setValue(yandexLogin);
		
		elements = $(byXpath(".//*[@id='js']/body")).findAll(byName("passwd"));
		element = elements.findBy(Condition.attribute("class", "b-form-input__input"));
		element.setValue(yandexPassword);
		
		$(byClassName("b-domik__button")).click();
		$(byXpath(".//*[@id='challenge-submit']/td[2]/span")).click();
		$(byXpath(".//*[@id='passwd']")).setValue(paymentPassword);
		$(byXpath(".//*[@id='challenge-submit']/td[2]/span/span")).click();
		$(byXpath(".//*[@id='js']/body/table/tbody/tr/td[3]/table/tbody/tr/td[1]/div[2]/div[2]/a")).click();

		return page(PaymentForm.class);
	}
	
	public CompleteOrderProcessing completeOrderProcessing() {
		
		$(byXpath(".//*[@id='ajaxTrainTable']")).waitWhile(visible, 60000);
		
		// Кликаем на батон 'Завершить оформление заказа' если появилось окно 'Оплата прошла успешно'
		
		$(byXpath(".//*[@id='headerPaymentOk']")).shouldHave(text("Оплата прошла успешно"));
		$(byXpath(".//*[@id='Main']/div/h3")).shouldHave(text("Для завершения оформления заказов перейдите на следующий шаг."));
		$(byXpath("//div[@id='Main']/div/div/a/span[2]")).click();

		return page(CompleteOrderProcessing.class);
	}
	
	public Results results(String wagonCategory) {
		// Если категория вагона 'У1' проверяем появилось ли окно 'Предзаказанное питание'
		$(byXpath(".//*[@id='fancybox-outer']")).waitUntil(appear, 120000);

		if (wagonCategory.contains("У1")) {
			SelenideElement foodMessage = $(byXpath(".//*[@id='foodMessageGeneral']"));
			
			if (foodMessage.isDisplayed()) {
				$(byXpath(".//*[@id='fancybox-close']")).click();
				$(byXpath(".//*[@id='fancybox-outer']")).waitUntil(visible, 15000);
			}
		}
		
		// Проверяем появилось ли окно 'Я понимаю условия, закрыть окно' и кликаем на батон 'Закрыть'
		
		$(byXpath(".//*[@id='fancybox-close']")).click();
		$(byXpath(".//*[@id='fancybox-close']")).waitUntil(disappear, 10000);
		
		return page(Results.class);
	}
	
	public void checkResultTariffs() {
		// Проверяем правильность выбора тарифов
		ElementsCollection passData = $$(byXpath(".//*[@id='MainBox']/div/div/div/div/table[2]/tbody/tr"));
		
		for (String tarif : tarifs) {
			SelenideElement ticketData = passData.find(text(tarif));
			ticketData.shouldHave(text("Пройдена электронная регистрация"));
		}
		
		Selenide.sleep(1500);
		$(byLinkText("Перейти в Мои заказы")).click();
		//$(byXpath("//div[@id='CabBox']")).waitUntil(appear, 20000);					// Ждем пока не появится окно с заказами
	}
	
	public void checkOrderFormsWithoutRefund() {
		SelenideElement transaction;
		ElementsCollection orders;
		SelenideElement order;
		SelenideElement orderFormRef;
		String year;
		String dayMonth;
		String hourMinute;
		String temp;
		
		ElementsCollection tickets;
		String[] statuses;
		
		$(byXpath(".//*[@id='container']/tbody/tr/td[2]")).waitUntil(appear, 70000);					// Ждем пока не появится окно с заказами
		
		// Если не указана начальная дата, устанавливаем с сегодняшней
		if (dateTo == null) dateTo = Tools.getNow();
		
		// Ищем подходящую транзакцию (Параметры - номер поезда, начальная - конечная даты, был ли возврат)
		transaction = FindTransaction(tripDetails.get("trainNum"), dateTo, dateTo);
		
		// Достаём статусы билетов 'Электронная регистрация'
		tickets = transaction.$$(byClassName("cab-pass"));
		
		statuses = new String[tickets.size()];
		
		for (int i = 0; i < tickets.size(); i++) {
			SelenideElement ticket = tickets.get(i);
			statuses[i] = ticket.$$(byClassName("action-list__item-item")).get(0).getText().replace("Статус: ", "");
		}
		
//***************** Проверяем бланк заказа со всеми билетами ********************
		orderFormRef = transaction.find(".di-html.di-download-btn");										// Достаём ссылку на бланк
		
		Selenide.navigator.open(orderFormRef.attr("href"));													// Открываем бланк заказов

		orders = $$(byClassName("order-cont"));
				
		for (int i = 0; i < orders.size(); i++) {
			ElementsCollection collection;
			SelenideElement element;
			
			order = orders.get(i);
			
			//****************** TOPINFO *********************
			SelenideElement topinfo = order.$(byClassName("topinfo"));
			collection = topinfo.$$(byClassName("topinfo-etickethdr"));
			
			collection.get(0).$(byAttribute("alt", "Российские железные дороги")).should(exist);
			collection.get(1).shouldHave(text("Электронный билет (номер)"));
			collection.get(1).shouldHave(text("E-ticket number"));
			
			//****************** BOARDING ********************
			SelenideElement boarding = order.$(byClassName("boarding"));
			element = boarding.$(byClassName("boarding__heading"));
			
			element.shouldHave(text("КОНТРОЛЬНЫЙ КУПОН"));
			element.shouldHave(text("CHECK COUPON"));
			
			year = Tools.getYear(tripDetails.get("departure"));
			element.shouldHave(text("Год совершения поездки: " + year));	// Год совершения поездки
			
			temp = Tools.getPassengerInitials(passengerDetails[i]); 		// Фамилия и инициалы пассажира
			element = boarding.$(byClassName("boarding__pass-data"));
			element.shouldHave(text(temp));
			element.shouldHave(text("Кол-во пассажиров/Number of passengers:"));
			
			//****************** ROUTE ***********************
			SelenideElement route = order.$(byClassName("route"));
			element = route.$(byClassName("route-header"));
			
			element.shouldHave(text("Маршрут следования"));					// Маршрут следования
			element.shouldHave(text("От / From"));
			element.shouldHave(text("- > До / To"));
			element.shouldHave(text("Класс обслуживания Class"));			// Класс обслуживания 
			
			collection = route.$(byClassName("route-data")).$$(byClassName("date-time"));	// Время отправления и прибытия
			
			collection.get(0).shouldHave(text("Отправление Departure"));

			dayMonth = Tools.getDayMonth(tripDetails.get("departure"));
			collection.get(0).$(byClassName("route-date")).shouldHave(text(dayMonth)); 		// День и месяц отправления

			hourMinute = Tools.getHoursMinutes(tripDetails.get("departure"));
			collection.get(0).$(byClassName("route-time")).shouldHave(text(hourMinute)); 	// Время отправления
			
			collection.get(1).shouldHave(text("Прибытие Arrival"));

			dayMonth = Tools.getDayMonth(tripDetails.get("arrival"));
			collection.get(1).shouldHave(text(dayMonth)); 									// День и месяц прибытия

			hourMinute = Tools.getHoursMinutes(tripDetails.get("arrival"));
			collection.get(1).shouldHave(text(hourMinute)); 								// Время прибытия

			element = route.$(byClassName("route-data")).$(byClassName("route-points"));
			
			temp = tripDetails.get("departure").split("\n")[0];
			element.shouldHave(text(temp));													// Станция отправления на русском

			temp = "- > " + tripDetails.get("arrival").split("\n")[0];
			element.shouldHave(text(temp));													// Станция назначения на русском
			
			temp = Tools.getServiceClass(tripDetails.get("coupe"));							// Класс обслуживания
			route.$(byClassName("route-data")).$$(byClassName("cell-pillow")).get(1).shouldHave(text(temp));

			//****************** TRAINDATA *******************
			SelenideElement traindata = order.$(byClassName("traindata"));

			traindata.$(byClassName("traindata-tname")).shouldHave(text("Поезд Train"));							
			traindata.$(byClassName("traindata-tdat")).shouldHave(text(tripDetails.get("trainNum")));	// Поезд
		
			// Формируем строку 'Номер вагона', 'Тип вагона', 'Тип Вагона на английском'
			temp = tripDetails.get("wagon").split(" ")[2] + " " + tripDetails.get("coupe").split(" ")[0] + " / " + Tools.getWagonTypeEN(tripDetails.get("coupe").split(" ")[0]);
			traindata.$(byClassName("traindata-cname")).shouldHave(text("Вагон Coach"));
			traindata.$(byClassName("traindata-cdat")).shouldHave(text(temp));
			
			// Формируем строку 'Место №', 'Нижнее / Верхнее' + английское
			temp = Tools.getSeatInfo(passengerDetails[i])[0];
			traindata.$(byClassName("traindata-plname")).shouldHave(text("Место Seat"));
			traindata.$(byClassName("traindata-pldat")).shouldHave(text(temp));

			//****************** TRAINFIN ********************
			SelenideElement trainfin = order.$(byClassName("trainfin"));
			
			// Проверяем тариф + дополнительная информация (При посадке обязательно....)
			temp = Tools.getTarifInfo(passengerDetails[i]);
			trainfin.$(byClassName("trainfin-type")).shouldHave(text(temp));					// Тариф + дополнительная информация
			
			// Проверяем сервис (У0 с бельём, например)
			temp = tripDetails.get("coupe").split("\n")[1].replace("(", "").replace(")", "");
			
			SelenideElement service = $(byClassName("trainfin-carr"));							// Сервисные услуги
			boolean food = service.has(or("services", text("Питание"), text("С БЕЛЬЕМ")));
			
			trainfin.$(byClassName("trainfin-carr")).shouldHave(text(temp));
			
			// Тариф, цена, сборы
			element = trainfin.$(byClassName("trainfin-sum"));
			element.shouldHave(text("Тариф (билет, плацкарта), руб.	Fare (ticket/reservation), RUB"));
			element.shouldHave(text("Цена, руб. Price, RUB"));
			
			temp = Tools.getSeatInfo(passengerDetails[i])[1].replace(" ", "");
			element.shouldHave(text(temp));

			if (!food)																			// Сервисные услуги
				element.shouldHave(text("Сервисные услуги, руб. Services, RUB"));
			
			element.shouldHave(text("Сборы, руб. Сommission, RUB"));							// Сборы

			//****************** MONEY ***********************
			SelenideElement money = order.$(byClassName("money"));
			
			element = money.$$(By.tagName("table")).get(0);
			// Статус электронного билета
			element.shouldHave(text("Статус электронного билета E-ticket status"));
			
			temp = statuses[i] + Tools.translate(statuses[i]);
			element.shouldHave(text(temp));
			
			// Отмена электронной регистрации
			element.shouldHave(text("Отмена электронной регистрации возможна до Cancel of E-registration is available till"));
			temp = Tools.getCancelTime(tripDetails.get("departure"));
			element.shouldHave(text(temp));
			
			// Дополнительная информация
			temp = tripDetails.get("additionInfo");
			element.shouldHave(text("Дополнительная информация Additional information"));
			element.shouldHave(text(temp));
			
			// Дата и время оформления заказа
			temp = tripDetails.get("orderDateTime").substring(21);
			element.shouldHave(text("Дата и время оформления Date and Time of Booking"));
			element.shouldHave(text(temp));
			
			// Перевозчик (ИНН)
			element.shouldHave(text("Перевозчик (ИНН) Carrier (VAT number)"));
			
			// Форма оплаты
			element.shouldHave(text("Форма оплаты Payment method"));
			
			// Служебная информация
			element.shouldHave(text("Служебная информация"));
			
			// Подтверждаю что с правилами .......
			money.$(byClassName("money-inside")).shouldHave(text("Подтверждаю, что с правилами и особенностями оформления, оплаты, "
															   + "возврата неиспользованного электронного билета, заказанного через Интернет"
															   + " и проезда по электронному билету, а также с офертой, ознакомлен."
															   + " I confirm that I have read and agree with the rules and conditions of the order, "
															   + "e-ticket payment, refund of unused e-ticket and the offer contract."
															   + " Я согласен с реквизитами поездки и подтверждаю, что персональные данные пассажиров верны."
															   + " I agree with the travel details and confirm that all personal data are correct."));
			
			// Распечатайте данный купон .......
			money.$(byClassName("codebar")).shouldHave(text("Распечатайте данный купон или сохраните на мобильном устройстве"
														  + " и предъявите при посадке вместе с документом, удостоверяющим личность, "
														  + "указанным при покупке электронного билета. "
														  + "Если Вы хотите сохранить посадочный купон на мобильном устройстве, "
														  + "дождитесь полной загрузки изображения и убедитесь, что 2D-код отображается на экране."
														  + " Please print this coupon or save it on your mobile device and present it when boarding the train. "
														  + "You must bring your identification document, as mentioned on the e-ticket form, with you. "
														  + "If you need to save your boarding coupon on your mobile device, "
														  + "please wait till the image is completely loaded and make sure 2D-code is seen on the screen."));
			
			// Дата формирования купона
			temp = "Дата формирования купона : " + Tools.getNow();
			order.$(byClassName("creation-date")).shouldHave(text(temp));
			
			// Кнопка 'Печатать'
			order.$(byClassName("button-print")).should(exist);
		}
		
		Selenide.navigator.back();
		//**********************************************************************************

		//***************** Проверяем бланк заказа с отдельными билетами ********************
		/*
		// Помещаем все билеты из транзакции в коллекцию
		orders = transaction.$$(byClassName("cab-pass"));

		//************ Проверем все билеты по очереди *************
		orders.forEach(item -> {
			SelenideElement ticketFormRef = item.find(".di-html.di-download-btn");
			ticketFormRef.click();
		});*/
		
	}
	
	public CancelRegistrationAndRefund cancelRegistrationAndRefund(String trainNumber) {
		SelenideElement transaction = FindTransaction(tripDetails.get("trainNum"), dateTo, dateTo);
		System.out.println("'CancelRegistrationAndRefund' - TransactionInfo: " + transaction.getText());
		
		ElementsCollection orders = transaction.$$(byClassName("cab-pass"));

		for (int i = 0; i < orders.size(); i++) {
			SelenideElement order = orders.get(i);
			SelenideElement status = order.$(byClassName("action-list__item-item"));
			SelenideElement regCancelButton = order.$(byClassName("btn-reg-cancel"));
			SelenideElement regButton;
			SelenideElement refundButton = order.$(byClassName("btn-refund"));
			SelenideElement refundBox = $(byXpath("//body/div[10]"));

			// Проверка электронной регистрации
			if (status.has(text("Пройдена электронная регистрация"))) { 	// Проверяем есть ли электронная регистрация
				regCancelButton.click(); 									// Если есть жмем на ссылку 'Отменить электронную регистрацию'
				status.shouldHave(text("Оплачен")); 						// Проверяем статус билета
				regButton = order.$(byClassName("btn-reg"));
				regButton.click(); 											// Если есть ссылка, жмем на неё
				order.waitUntil(text("Отменить электронную регистрацию"), 20000);
			} else {
				regCancelButton.click(); 									// Жмем на ссылку 'Пройти электронную регистрацию'
				order.waitUntil(text("Отменить электронную регистрацию"), 20000);
			}

			// Проверка возврата билета

			refundButton.click(); 											// Кликаем батон и ждем появления окна с предупреждением
			//refundBox = $(byXpath("//body/div[10]"));
			refundBox.waitUntil(Condition.visible, 20000);
			//refundBox.$(byClassName("Cancel")).click(); 					// Кликаем 'Отмена' и проверяем что возврата не произошло
			//refundButton.should(exist);
			//refundButton.click(); 										// Кликаем еще раз 'Оформить возврат'
			//refundBox.waitUntil(Condition.visible, 20000);
			refundBox.$(byClassName("OK")).click(); 						// Кликаем 'Сдать билет'

			order.shouldHave(text("Оформлен возврат"));
			
			if (tripDetails.get("coupe").contains("Люкс")) break;
		}

 		return page(CancelRegistrationAndRefund.class);
	}
	

	public void checkOrderFormsWithRefund(String trainNumber) {

	}
	
	private SelenideElement FindTransaction(String trainNumber, String dateTo, String dateFrom) {
		ElementsCollection transactions;
		SelenideElement transaction = null;
		ElementsCollection orders;
		SelenideElement order;
		
		// Ищем билеты с нужной датой и номером поезда
		$(byXpath("//input[@name='number']")).setValue(trainNumber.replace("№", "").trim());	// Вводим номер поезда например '002Й'
		$(byName("date0")).setValue(dateTo).pressEnter();										// Вводим дату отправления 'от'
		
		Selenide.sleep(1000);
		
		if (dateFrom == null || dateFrom.isEmpty()) 											// Вводим дату отправления 'до'
			$(byName("date1")).clear();	
		else 
			$(byName("date1")).setValue(dateFrom).pressEnter();
		
		Selenide.sleep(1000);
		
		$(byXpath("//button[@type='submit']")).click();											// Кликаем 'Найти'
		$(byXpath("//div[@id='CabBox']")).waitUntil(appear, 30000);
		
		SelenideElement waitBox = $(byXpath(".//*[@id='WaitMsg']"));
		SelenideElement moreButton = $(byXpath(".//*[@id='MoreButton']/span[2]"));
		
		if (moreButton.isDisplayed()) {
			do {
				moreButton.click();
				waitBox.waitWhile(visible, 3000);
			} while (moreButton.isDisplayed());
		}
						
		transactions = $$(byXpath(".//*[@id='CabBox']/div"));

		for (int i = 0; i < transactions.size(); i++) {								// Пробегаем по всем транзакциям и находим билет по уникальной фамилии
			SelenideElement tmp = transactions.get(i);
			
			// Кликаем 'Запросить статус билета'
			$(byXpath("//div[@id='CabBox']/div[" + (i + 1) + "]/div/div/div/table/tbody/tr[2]/td/div/div[2]/span/span/span/span/span")).click();	
			
			// Ждем пока не появится информация о билетах
			$(byXpath(".//*[@id='CabBox']/div[" + (i + 1) + "]/div/div/div[3]/div")).waitUntil(visible, 60000);
			
			// Помещаем все билеты в коллекцию
			orders = $$(byXpath(".//*[@id='CabBox']/div[" + (i + 1) + "]/div/div/div[3]/div/div/table/tbody/tr"));
			
			// Ищем нужные билеты по уникальной фамилии
			for (int j = 0; j < lastNames.length; j++) {
				order = orders.findBy(text(lastNames[j]));
				if (order.exists()) {
					transaction = tmp;
					break;
				}
			}
		}
		
		return transaction;
	}
	
	private Hashtable<String, String> GetOrderInfo(SelenideElement passengerData) {
		Hashtable<String, String> tripDetail = new Hashtable<String, String>();
		
		tripDetail.put("trainNum", passengerData.$(byClassName("trlist-brief__train-num")).getText());
		tripDetail.put("route", passengerData.$(byClassName("trlist-brief__route")).getText());
		tripDetail.put("departure", passengerData.$(byXpath("//div[2]/div/table/tbody/tr/td[2]/div[2]")).getText() + "\n" + 						// Информация об отправлении поезда - Станция
									passengerData.$(byXpath("//div[@id='Main']/div[2]/div/table/tbody/tr/td[2]/div[3]/b")).getText() + "\n" +  	// Дата
									passengerData.$(byXpath("//div[@id='Main']/div[2]/div/table/tbody/tr/td[2]/div[3]/span")).getText());			// Время
		
		tripDetail.put("arrival", passengerData.$(byXpath("//div[@id='Main']/div[2]/div/table/tbody/tr/td[3]/div[2]")).getText() + "\n" + 			// Информация о прибытии поезда - Станция
								  passengerData.$(byXpath("//div[@id='Main']/div[2]/div/table/tbody/tr/td[3]/div[3]/b")).getText() + "\n" +  		// Дата
								  passengerData.$(byXpath("//div[@id='Main']/div[2]/div/table/tbody/tr/td[3]/div[3]/span")).getText());			// Время)
		
		tripDetail.put("wagon", passengerData.$(byXpath("//div[@id='Main']/div[2]/div/table/tbody/tr/td[4]/div")).getText());						// Номер вагона
		tripDetail.put("coupe", passengerData.$(byXpath("//div[@id='Main']/div[2]/div/table/tbody/tr/td[4]/div[2]")).getText());					// Купе
		tripDetail.put("additionInfo", passengerData.$(byXpath("//div[@id='Main']/div[2]/div/table/tbody/tr/td[5]")).getText());					// Дополнительная информация - 'Курить запрещено и т.д.'
		tripDetail.put("orderDateTime", passengerData.$(byXpath("//div[@id='Main']/div[2]/div/div")).getText());									// Дата и время заказа
		tripDetail.put("orderCost", passengerData.$(byXpath("//div[@id='Main']/div[2]/div[2]")).getText());										// Стоимость заказа
		tripDetail.put("paymentAmount", passengerData.$(byXpath("//div[@id='Main']/div[3]")).getText());											// Сумма к оплате

		return tripDetail;
	}
	
	private String[] GetPassengerInfo(ElementsCollection passengerData) {
		String[] tempParams = new String[passengerData.size()];
		String temp = "";
		
		for (int i = 0; i < passengerData.size(); i++) {
			SelenideElement element = passengerData.get(i);

			if (element.isDisplayed()) {
				temp = element.getText();
				tempParams[i] = temp;
			} 
		}
		
		return tempParams;
	}
}