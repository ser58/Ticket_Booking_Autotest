package test.ru.oooinex.autotests.buyticket.testitself;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.page;

import org.openqa.selenium.By;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import test.ru.oooinex.autotests.buyticket.pages.CancelRegistrationAndRefund;
import test.ru.oooinex.autotests.buyticket.pages.CompleteOrderProcessing;
import test.ru.oooinex.autotests.buyticket.pages.Login_Result;
import test.ru.oooinex.autotests.buyticket.pages.PassengerForm;
import test.ru.oooinex.autotests.buyticket.pages.PassengerValidationForm;
import test.ru.oooinex.autotests.buyticket.pages.PaymentForm;
import test.ru.oooinex.autotests.buyticket.pages.Results;
import test.ru.oooinex.autotests.buyticket.pages.Schedule_Received;
import test.ru.oooinex.autotests.buyticket.pages.TrainSelect;
import test.ru.oooinex.autotests.buyticket.pages.WagonSelect;
import test.ru.oooinex.utilities.Tools;

public class ReturnTrainPagesTest {
	String dateTo;
	String dateFrom;
	String reverseWagonType;
	boolean transfer;
	boolean andReturn;
	boolean foreignTrainTo;
	boolean foreignTrainBack;
	static int numPassengers;
	static String shippingCompany;
	String[] tarifs;
	String[] trainsType = new String[2];
	
	String[] specialBirthdays;
	String[] lastNames;
	
	public SelenideElement Is_Start_Page() {
		//return $(By.linkText("Вход"));
		return $(By.xpath(".//*[@id='headAuth']"));
	}
	
	public Login_Result login(String login, String password) {
		$(By.linkText("Вход")).click();
 		$(By.id("j_username")).setValue(login);
		$(By.id("j_password")).setValue(password); 
		$(By.xpath("//button[@name='action']")).click();
	
		return page(Login_Result.class);
	}
	
	public Schedule_Received trainShedule (String from, String to, String dateTo, String dateFrom, boolean onlyWithTickets, boolean localTrain, boolean andReturn, boolean foreignTrainTo, boolean foreignTrainBack) {
		this.dateTo = dateTo;
		this.dateFrom = dateFrom;

		$(By.xpath(".//*[@id='headNav2']/tbody/tr/td[1]/a")).click();  				// Кликнуть по ссылке "Пассажирам"
		
		$("#name0").setValue(from);													// Выбрать станцию отправления
		
		$("#name1").setValue(to);													// Выбрать станцию назначения
		
		$(By.xpath("//input[@id='date0']")).setValue(dateTo);						// Установить дату 'Туда'
		
		if (andReturn) {
			$("#DirToggle").setSelected(andReturn);									// Флажок 'Обратно'
			$(By.xpath("//input[@id='date1']")).setValue(dateFrom);					// Установить дату 'Обратно'
		}
		
		$("#NoSeats").setSelected(onlyWithTickets);									// Флажок 'Только с билетами'
		
		$("#SubTrains").setSelected(localTrain);									// Флажок 'Электрички'
		
		if (onlyWithTickets) {
			if ($("#Submit").isEnabled())
				$("#Submit").click();												// Нажать кнопку 'Купить билет' если она активна.
		} else {
			if ($("#Submit1").isEnabled())
				$("#Submit1").click();												// Нажать кнопку 'Расписание' если она активна.
		}
		
		$(By.id("Part0")).waitUntil(Condition.text("вариантов по прямому маршруту"), 60000);
		
		return page(Schedule_Received.class);
	}
	
	public TrainSelect trainWithReturnSelect (String trainNumber, String carCheckers, String trainNumberReturn, String carCheckersReturn) {
		ElementsCollection trains;
		SelenideElement train;
		int numTrainsTo = 0;
		
		// carChecker будет реализован позже. Имеется ввиду выбор типа вагона (все, ничего,	Сидячий Общий Плацкартный Купе Мягкий Люкс) 
		
		// Список поездов получили, теперь выбираем нужный и жмем "Продолжить"
		// Если поезд один, ничего не выбираем, а просто жмём батон продолжить
		// Вытаскиваем список поездов 'Туда' в массив и ищем нужный
		trains = $$(By.xpath(".//*[@id='Part0']/div[6]/table/tbody/tr"));
		if (!trains.isEmpty()) {
			numTrainsTo = trains.size();
			
			for (int i = 0; i < trains.size(); i++) {
				train = trains.get(i);
				if (train.has(Condition.text(trainNumber))) {
					trainsType[0] = $(By.xpath(".//*[@id='Part0']/div[6]/table/tbody/tr[" + (i + 1) + "]/td[2]/div[1]/span")).attr("title");
					$(By.xpath("(//input[@type='radio'])[" + (i + 1) + "]")).click();
					break;
				}
			}
		}
		
		// Вытаскиваем список поездов 'Обратно' в массив и ищем нужный
		trains = $$(By.xpath(".//*[@id='Part1']/div[6]/table/tbody/tr")).shouldHave(CollectionCondition.sizeGreaterThan(1));
		if (!trains.isEmpty()) {
			for (int i = 0; i < trains.size(); i++) {
				train = trains.get(i);
				if (train.getText().contains(trainNumberReturn)) {
					trainsType[1] = $(By.xpath(".//*[@id='Part1']/div[6]/table/tbody/tr[" + (i + 1) + "]/td[2]/div[1]/span")).attr("title");
					$(By.xpath("(//input[@type='radio'])[" + (i + 1 + numTrainsTo) + "]")).click();
					break;
				}
			}
		}
		
		$(By.xpath(".//*[@id='continueButton']")).waitUntil(Condition.enabled, 10000);
		$(By.xpath(".//*[@id='continueButton']")).click();
				
		return page(TrainSelect.class);
	}
	
	public WagonSelect wagonSelect(String wagonType, String wagonCategory, String servicesAndCapabilities, String returnWagonType, String returnWagonCategory, String returnServicesAndCapabilities) {
		SelenideElement wagon;
		ElementsCollection wagons;
		int numWagonsTo = 0;
		int trainIndex = 0;
		
		// Ждем пока не появится список вагонов
		$(By.xpath("//div[@id='Main']/div/div/table[2]")).waitUntil(Condition.appear, 30000);

		ElementsCollection trains = $$(By.className("jtrain-box"));
		
		// Здесь проверяем наличие электричек на маршруте, если есть электричка, пропускаем выбор вагона 
		if (!(trainsType[0].contains("Электричка")) & !(trainsType[1].contains("Электричка"))) {
			wagons = trains.get(trainIndex).$$(".j-car-item.trlist__trlist-row");
			numWagonsTo = wagons.size();

			for (int i = 0; i < wagons.size(); i++ ) {								// Выбор вагона в первом поезде
				wagon = wagons.get(i);
				if (wagon.has(Condition.text(wagonType + " " + wagonCategory + " " + servicesAndCapabilities))) {
					$(By.xpath("(//input[@type='radio'])[" + (i + 1) + "]")).click();
					break;
				}
			}
			
			trainIndex++;
			
			wagons = trains.get(trainIndex).$$(".j-car-item.trlist__trlist-row"); 		// Выбор вагона во втором поезде
			
			for (int i = 0; i < wagons.size(); i++ ) {
				wagon = wagons.get(i);
				if (wagon.has(Condition.text(returnWagonType + " " + returnWagonCategory + " " + returnServicesAndCapabilities))) {
					wagon.$(By.xpath("(//input[@type='radio'])[" + (i + 1 + numWagonsTo) + "]")).click();
					break;
				}
			}
			
		} else if ((trainsType[0].contains("Электричка")) & !(trainsType[1].contains("Электричка"))) {
			wagons = trains.get(trainIndex).$$(".j-car-item.trlist__trlist-row"); 		// Выбор вагона во втором поезде
			
			for (int i = 0; i < wagons.size(); i++ ) {
				wagon = wagons.get(i);
				if (wagon.has(Condition.text(returnWagonType + " " + returnWagonCategory + " " + returnServicesAndCapabilities))) {
					wagon.$(By.xpath("(//input[@type='radio'])[" + (i + 1 + numWagonsTo) + "]")).click();
					break;
				}
			}
			
		} else if (!(trainsType[0].contains("Электричка")) & (trainsType[1].contains("Электричка"))) {
			wagons = trains.get(trainIndex).$$(".j-car-item.trlist__trlist-row"); 		// Выбор вагона во втором поезде
			
			for (int i = 0; i < wagons.size(); i++ ) {
				wagon = wagons.get(i);
				if (wagon.has(Condition.text(wagonType + " " + wagonCategory + " " + servicesAndCapabilities))) {
					wagon.$(By.xpath("(//input[@type='radio'])[" + (i + 1 + numWagonsTo) + "]")).click();
					break;
				}
			}
		}		
		$("#continueButton").waitUntil(Condition.enabled, 30000).click();
		
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
		
		int sub = 0;
		
		// Убираем всех пассажиров
		passOptions = $$(".j-pass-item.pass-item.trlist-pass__pass-item");
		
		for (int i = 0; i < passOptions.size() - 2; i++) {
			element = $(By.xpath(".//*[@id='PassList']/div[1]/div[2]/div/h3"));
			if (element.has(text("Убрать")))
				$("a.pass_IU_PassInfo__addOrDel").click();
		}
		
		// Добавляем нужное количество пассажиров
		for (int i = 1; i < listPassenger.length; i++) {
			$(By.xpath("//div[" + (i + 1) + "]/div/h3/span/a")).click();
		}
		
		
		ElementsCollection passes = $$(By.className("trlist-pass__pass-wrap"));
		SelenideElement pass;
		
		// Заполняем в цикле все параметры каждого пассажира
		for (int i = 0; i < listPassenger.length; i++) {
			
			String specialBirthday;
			String[] passParams = listPassenger[i].replace("'", "").split(",");
			
			String lastName = passParams[0].trim() + Tools.generateRandomSymbolsRU(3);
			lastNames[i] = lastName;
			$(By.xpath("(//input[@name='lastName'])[" + (i + 1) + "]")).setValue(lastName); 								// Фамилия
			$(By.xpath("(//input[@name='firstName'])[" + (i + 1) + "]")).setValue(passParams[1].trim()); 					// Имя 
			
			// Если иностранный поезд сдвигаем все селекторы на один в меньшую сторону
			// По причине отсутствия отчества
			if (foreignTrainTo) {
				$(By.xpath("(//input[@name='midName'])[" + (i + 1) + "]")).shouldNotBe(visible);
				sub = 1;																					
			} else 
				$(By.xpath("(//input[@name='midName'])[" + (i + 1) + "]")).setValue(passParams[2].trim()); 					// Отчество
			
			SelenideElement birthday = $(By.xpath("//div[" + (i + 1) + "]/div/div/div[" + (4 - sub) + "]/div/input"));
			birthday.clear();
			
			
			if (passParams[5].trim().contains("Школьный")) {	// Проверяем школьный тариф, вычисляем дату рождения = дата отправления - 10 лет + 1 день
				specialBirthday = Tools.getBirthday(dateTo, 10, 0, 1);
				birthday.setValue(specialBirthday).pressEnter();															// Дата рождения
				
				$(By.xpath("//div[@id='PassList']/div/div/div/div/div[6]/label/input")).setSelected(true);					// Выбираем тариф 'Школьный'
				$(By.xpath("//div[" + (i + 1) + "]/div/div/div[" + (5 - sub) + "]/select")).shouldNotBe(enabled);	// Проверяем недоступность комбобокса 'Тариф'
				
			} else {
				switch (passParams[5].trim()) {																// Вычисляем дни рождений для всех остальных тарифов 
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
				
				birthday.setValue(specialBirthday).pressEnter();																	// Дата рождения
				$(By.xpath("(//select[@name='tariff'])[" + (i + 1) + "]")).selectOption(passParams[5].trim());						// Тариф
			}

			specialBirthdays[i] = specialBirthday;
			
			// Проверяем что выпадающий список 'Тип документа' автоматически установился в 'Свидетельство о рождении'
			// и что список содержит только три элемента 'Загран паспорт', 'Иностранный документ' и 'Свидетельство о рождении'
			if (passParams[5].trim().contentEquals("Детский") | passParams[5].trim().contentEquals("Детский без места")) {
				docType = $$(By.xpath(".//*[@id='PassList']/div[1]/div[" + (i + 1) + "]/div/div/div[7]/select/option"));
				docType.shouldHaveSize(3);
				docType.shouldHave(texts("Заграничный паспорт", "Иностранный документ", "Свидетельство о рождении"));
			}
			
			tarifs[i] = passParams[5].trim();					// Сохраняем все тарифы в массив
			
			$(By.xpath("(//select[@name='docType'])[" + (i + 1) + "]")).selectOption(passParams[3].trim());							// Документ тип
			$(By.xpath("(//input[@name='docNumber'])[" + (i + 1) + "]")).setValue(passParams[4].trim()); 							// Номер документа	
			$(By.xpath("(//select[@name='gender'])[" + (i + 1) + "]")).selectOption(passParams[7].trim());							// Пол
			$(By.xpath("(//select[@name='country'])[" + (i + 1) + "]")).selectOption(passParams[6].trim()); 						// Государство
			
			// Оформляем страховой полис
			
			element = $(By.xpath("(//input[@name='insCheck'])[" + (i + 1) + "]")); 												// Чекбокс 'Оформление страхового полиса'
			
			pass = passes.get(i);
			ElementsCollection insurances = pass.$$(By.className("j-ins-item"));
			strahovka = !passParams[9].trim().isEmpty() ? true : false;
			
			if (strahovka) {																				// Устанавливаем чекбокс 'Оформление страхового полиса'
				$(By.xpath(".//*[@id='PassList']/div[1]/div[1]/div/div/div[13]/div/label")).shouldHave(text("Оформление страхового полиса "));
				element.setSelected(true);
				Tools.selectInsuranceCompany(insurances, i + 1, passParams[9].trim());
			} else {
				if (element.exists())
					element.setSelected(false);
			}
		}
		
		return page(PassengerForm.class);
	}
	
	public PassengerForm fillPlaceSigns(Object[] directPlaces, Object[] returnPlaces) {
		SelenideElement element;
		String wagonType;
		String placeFrom;
		String placeTo;
		
		// Необходимо организовать проверку типа транспортного средства на маршруте с пересадкой и в зависимости от этого  устанавливать признаки мест
		// ******* trainsType ******* Поезд дальнего следования, электричка, паром или автобус
		
		// Выбираем места в вагоне в первом поезде
		
		wagonType = $(By.xpath("//div[@id='TrainsList']/div[1]/table/tbody/tr/td[4]")).getText().split("\n")[1].split(" ")[0];
		
		placeFrom = (String) directPlaces[2];
		placeTo = (String) directPlaces[3];
				
    	$(By.xpath("(//input[@name='range0'])[1]")).setValue(placeFrom);												// Граница мест 'С'
		$(By.xpath("(//input[@name='range1'])[1]")).setValue(placeTo);													// Граница мест 'По'
		
		switch (wagonType) {
	        case "Сидячий":  
	        	break;
	        case "Плацкартный":
	    		$(By.xpath("//input[@name='plBedding']")).setSelected((boolean) (directPlaces[4]));						// Оплатить постельное бельё
	    		$(By.xpath("//select[@name='plUpDown']")).selectOption((String) directPlaces[5]);						// Верхние нижние места
	    		$(By.xpath("//select[@name='plComp']")).selectOption((String) directPlaces[6]);							// Расположение мест
	        	break;	
	        case "Купе":	
	        	String plGender = (String) directPlaces[0];
	    		
	        	if ($(By.xpath(".//*[@id='ErrorBox']")).getText().contains("Не выбран признак купе")) 						// Если не выбран признак места, выбираем его 
	    			$(By.xpath(".//*[@id='TrainsList']/div/div[2]/table/tbody/tr/td[2]/div/select")).selectOption(plGender);
	    				
	    		element = $(By.xpath(".//*[@id='TrainsList']/div/div[2]/table/tbody/tr/td[3]/div/label/input"));

	    		if (numPassengers > 1) {
	    			// Устанавливаем чекбокс 'В одном купе'
	    			element.setSelected((boolean) directPlaces[1]);
	    		} else
	    			element.shouldNotBe(Condition.visible);
	    		
	        	break;
	        case "СВ":
	        	
	        	break;
	        case "Люкс":
	        	
	        	break;
		}
		
		// Выбираем места в вагоне во втором поезде
		wagonType = $(By.xpath("//div[@id='TrainsList']/div[2]/table/tbody/tr/td[4]")).getText().split("\n")[1].split(" ")[0];
		placeFrom = (String) returnPlaces[2];
		placeTo = (String) returnPlaces[3];
				
    	$(By.xpath("(//input[@name='range0'])[2]")).setValue(placeFrom);												// Граница мест 'С'
		$(By.xpath("(//input[@name='range1'])[2]")).setValue(placeTo);													// Граница мест 'По'
		
		switch (wagonType) {
	        case "Сидячий":  
	        	break;
	        case "Плацкартный":
	    		$(By.xpath("//input[@name='plBedding']")).setSelected((boolean) (returnPlaces[4]));						// Оплатить постельное бельё
	    		$(By.xpath("//select[@name='plUpDown']")).selectOption((String) returnPlaces[5]);						// Верхние нижние места
	    		$(By.xpath("//select[@name='plComp']")).selectOption((String) returnPlaces[6]);							// Расположение мест
	        	break;	
	        case "Купе":	
	        	String plGender = (String) returnPlaces[0];
	    		
	        	if ($(By.xpath(".//*[@id='ErrorBox']")).has(Condition.text("Не выбран признак купе"))) 						// Если не выбран признак места, выбираем его 
	    			$(By.xpath(".//*[@id='TrainsList']/div/div[2]/table/tbody/tr/td[2]/div/select")).selectOption(plGender);
	    				
	    		//element = $(By.xpath(".//*[@id='TrainsList']/div[2]/div[2]/table/tbody/tr/td[4]/div/label/input"));
	    		SelenideElement plKupe = $(By.name("plKupe"));
	    		
	    		if (numPassengers > 1) {
	    			// Устанавливаем чекбокс 'В одном купе'
	    			plKupe.setSelected((boolean) returnPlaces[1]);
	    		} else
	    			plKupe.shouldNotBe(Condition.visible);
	    		
	        	break;
	        case "СВ":
	        	
	        	break;
	        case "Люкс":
	        	
	        	break;
		}
		
		// Батон продолжить должен выть активным, если так, нажимаем его
		$("#continueButton").waitUntil(Condition.enabled, 10000).click();
		
		$(By.xpath("//div[@id='Main']")).waitUntil(Condition.appear, 30000);
				
		return page(PassengerForm.class);
	}
	
	public PassengerValidationForm passengerValidationForms() {
		SelenideElement element;
		element = $(By.xpath(".//*[@id='TrainOfferta']"));			// Достаём чекбокс 'С условиями ознакомлен'
		element.click();											// Устанавливаем его
		
		element = $(By.xpath("//*[@id='Ref']"));					// Проверяем если батон 'Оплатить' доступен, то кликаем на него.
		if (element.isEnabled())
			element.click();
		
		return page(PassengerValidationForm.class);
	}
	
	public PaymentForm payment(String cardNumber, String cardholder, String cvv2, String validityPeriod) {
		// Заполняем все необходимые поля для оплаты картой
		
		$(By.xpath(".//*[@id='cp-pan-decor']")).setValue(cardNumber); 			// Номер карты
		$(By.xpath(".//*[@id='cp-full-name']")).setValue(cardholder);			// Владелец карты
		$(By.xpath(".//*[@id='cvv2']")).setValue(cvv2);							// CVV2
		$(By.xpath(".//*[@id='cp-expiration-date']")).setValue(validityPeriod);	// Действительна до
		
		$(By.xpath(".//*[@id='OK']")).click();									// Нажать на батон 'Оплатить'
		
		$(By.xpath(".//*[@id='Main']/div/div")).shouldNotHave(Condition.text("Время, отведенное на оплату, истекло. Ваш заказ будет отменен, а деньги возвращены на карту."));

		return page(PaymentForm.class);
	}

	public CompleteOrderProcessing completeOrderProcessing() {
		// Кликаем на батон 'Завершить оформление заказа' если появилось окно 'Оплата прошла успешно'
		
		$(By.xpath(".//*[@id='headerPaymentOk']")).shouldHave(Condition.text("Оплата прошла успешно"));
		$(By.xpath(".//*[@id='Main']/div/h3")).shouldHave(Condition.text("Для завершения оформления заказов перейдите на следующий шаг."));
		$(By.xpath("//div[@id='Main']/div/div/a/span[2]")).click();  // Нажимаем кнопку 'Завершить оформление заказа'

		return page(CompleteOrderProcessing.class);
	}
	
	public Results results(String wagonCategory) {
		// Если категория вагона 'У1' проверяем появилось ли окно 'Предзаказанное питание'
		$(By.xpath(".//*[@id='fancybox-outer']")).waitUntil(Condition.appear, 15000);

		if (wagonCategory.contentEquals("У1")) {
			SelenideElement foodMessage = $(By.xpath(".//*[@id='foodMessageGeneral']"));
			
			if (foodMessage.isDisplayed()) {
				$(By.xpath(".//*[@id='fancybox-close']")).click();
				$(By.xpath(".//*[@id='fancybox-outer']")).waitUntil(Condition.visible, 15000);
			}
		}
		
		// Проверяем появилось ли окно 'Я понимаю условия, закрыть окно' и кликаем на батон 'Закрыть'
		
		$(By.xpath(".//*[@id='fancybox-close']")).click();
		
		// Проверяем правильность выбора тарифов
		
		ElementsCollection passData = $$(By.xpath(".//*[@id='MainBox']/div/div/div/div/table[2]/tbody/tr"));
		
		for (String tarif : tarifs) {
			SelenideElement ticketData = passData.find(Condition.text(tarif));
			ticketData.shouldHave(Condition.text("Пройдена электронная регистрация"));
		}

		return page(Results.class);
	}
	
	public CancelRegistrationAndRefund cancelRegistrationAndRefund(String trainNumber) {
		$(By.linkText("Перейти в Мои заказы")).click();
		$(By.xpath(".//*[@id='container']/tbody/tr/td[2]")).waitUntil(Condition.appear, 30000);	// Ждем пока не появится окно с заказами
		$(By.name("number")).setValue(trainNumber.replace("№", "").trim());						// Вводим номер поезда например '002Й'
		$(By.name("date0")).setValue(dateTo).pressEnter();										// Вводим дату отправления 'от'
		$(By.name("date1")).setValue(dateTo).pressEnter();										// Вводит дату отправления 'до'
		$(By.xpath("//button[@type='submit']")).click();										// Кликаем 'Найти'
		
		$(By.xpath(".//*[@id='WaitMsg']")).waitUntil(Condition.disappear, 30000);
		$(By.xpath(".//*[@id='CabBox']")).waitUntil(Condition.appear, 30000);
		
		ElementsCollection transactions = $$(By.className("j-order-list")); 						// Помещаем все транзакции в коллекцию 
		
		for (int j = 0; j < transactions.size(); j++) {												// Пробегаем по всем транзакциям и находим первый билет который можно вернуть
			boolean success = false;
			SelenideElement transaction = transactions.get(j);
			
			// Кликаем 'Запросить статус билета'
			transaction.$(By.xpath("//div[@id='CabBox']/div[" + (j + 1) + "]/div/div/div/table/tbody/tr[2]/td/div/div[2]/span/span/span/span/span")).click();	
			
			$(By.xpath(".//*[@id='CabBox']/div[" + (j + 1) + "]/div/div/div[3]/div/div")).waitUntil(Condition.visible, 30000);
			
			ElementsCollection orders = transactions.get(j).$$(By.className("cab-pass"));			// Помещаем все билеты в коллекцию
			 
			for (int i = 0; i < orders.size(); i ++) {
				if (!orders.get(i).getText().contains("Оформлен возврат")) {						// Если возврат уже оформлен, проверяем следующий билет
					// Получить поле со статусом
					SelenideElement status = orders.get(i).$(By.xpath("//tr[" + (i + 1) + "]/td/table/tbody/tr[2]/td/div/span/div/div/ul/li/b"));
					
					// Проверка электронной регистрации
					// Получить ссылку 'Электронная регистрация'
					SelenideElement regButton = orders.get(i).$(By.xpath("//tr[" + (i + 1) + "]/td/table/tbody/tr[2]/td/div/span/div/div/ul/li[2]/span/span/span"));
					
					if (status.getText().contains("Пройдена электронная регистрация")) { 				// Проверяем есть ли Электронная регистрация
						regButton.click();																// Если есть жмем на ссылку 'Отменить электронную регистрацию'
						status.shouldHave(Condition.text("Оплачен"));									// Проверяем статус билета
						regButton.waitUntil(Condition.text("Пройти электронную регистрацию"), 20000);	// И батон электронной регистрации
						regButton.click(); 																// Если есть ссылка, жмем на неё
						regButton.waitUntil(Condition.text("Отменить электронную регистрацию"), 20000);
					} else {
						regButton.click(); 																// Жмем на ссылку 'Пройти электронную регистрацию'
						regButton.waitUntil(Condition.text("Отменить электронную регистрацию"), 20000);
					}
					
					// Проверка возврата билета
					// Получить ссылку 'Оформить возврат'
					
					SelenideElement refundButton = $(By.xpath(".//*[@id='CabBox']/div[" + (j + 1) + "]/div/div/div[3]/div/div/table/tbody/tr[" + (i + 1) + "]/td/table/tbody/tr[2]/td/div[1]/span/div/div[1]/ul/li[3]"));
					refundButton.click();																// Кликаем батон и ждем появления окна с предупреждением
					$(By.xpath("html/body/div[10]")).waitUntil(Condition.appear, 20000);
					$(By.xpath("//div[10]/div[5]/button[2]")).click();									// Кликаем 'Отмена' и проверяем что возврата не произошло
					refundButton.should(Condition.exist);	
					refundButton.click();																// Кликаем еще 'Оформить возврат'
					$(By.xpath("html/body/div[10]")).waitUntil(Condition.appear, 20000);
					$(By.xpath("//div[10]/div[5]/button")).click();										// Кликаем 'Сдать билет'
					
					$(By.xpath(".//*[@id='CabBox']/div[" + (j + 1) + "]/div/div/div[3]/div/div/table/tbody/tr[" + (i + 1) + "]/td/table/tbody/tr[2]/td/div[1]/span")).shouldHave(Condition.text("Оформлен возврат"));
					success = true;
				}
			}
			
			if (success) break;
		}
		
		return page(CancelRegistrationAndRefund.class);
	}
}