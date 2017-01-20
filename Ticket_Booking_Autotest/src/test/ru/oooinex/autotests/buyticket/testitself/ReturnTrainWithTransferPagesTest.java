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

public class ReturnTrainWithTransferPagesTest {
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
	String[] directTrainsType = new String[2];
	String[] returnTrainsType = new String[2];
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
		
		$(By.xpath("//input[@id='name0']")).setValue(from);							// Выбрать станцию отправления
		$(By.xpath("//input[@id='name1']")).setValue(to);							// Выбрать станцию назначения
		
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
	
	public TrainSelect trainWithReturnSelect (String[] directTrain, String[] directTransferTrain, String[] returnTrain, String[] returnTransferTrain) {
		ElementsCollection trains;
		SelenideElement train;
		SelenideElement passWithTransferLink;
		SelenideElement message;
		SelenideElement toolBar;
		
		String trainNumber;
		//String carCheckers;

		String transferTrainNumber;
		String transferStation;
		String transferTimeFrom;
		String transferTimeTo;
		
		// *********** Выбираем пару поездов (с пересадкой) в направлении 'Туда' ************
		
		trainNumber = directTrain[0];
		//carCheckers = directTrain[1];
		
		transferTrainNumber = directTransferTrain[0];
		transferStation = directTransferTrain[1];
		transferTimeFrom = directTransferTrain[2];
		transferTimeTo = directTransferTrain[3];
		
		message = $(By.xpath("//div[@id='Part0']/div[3]/div/div/b"));		// Блок сообщений (Найдены или не найдены поезда)
		message.shouldHave(Condition.text("Не найдено ни одного поезда"));
		
		passWithTransferLink = $(By.xpath("//div[@id='Part0']/div[2]/table/tbody/tr/td[4]/a"));	// Линк 'Маршруты с пересадками'
													
		if (passWithTransferLink.exists())									// Проверяем наличие линка 'Маршруты с пересадками'
			passWithTransferLink.click();									// Если линк присутствует нажимаем его
			
		$(By.xpath(".//*[@id='ajaxTrainTable']")).waitWhile(Condition.visible, 180000);   // Ждем пока не появится список поездов
		
		toolBar = $(By.xpath("//div[@id='Part0']/div[5]/div/div/div"));
		
		if (toolBar.exists()) {												// Устанавливаем пересадочную станцию и время пересадки
			if (!transferStation.isEmpty()) toolBar.$("select[name='MidPt']").selectOption(transferStation);
			if (!transferTimeFrom.isEmpty()) toolBar.$("input[name='T0']").setValue(transferTimeFrom);
			if (!transferTimeTo.isEmpty()) toolBar.$("input[name='T1']").setValue(transferTimeTo);
			toolBar.$(By.xpath("//div[@id='Part0']/div[5]/div/div/div/table/tbody/tr/td[4]/button")).click();	// Нажимаем батон 'Пересчитать'
		}
		
		trains = $$(By.xpath(".//*[@id='Part0']/div[5]/div"));
		trains.shouldHave(CollectionCondition.sizeGreaterThan(2));
		
		for (int i = 3; i < trains.size(); i++) { 							// Разворачиваем все поезда по очереди методом кликанья на батон 'Подробнее'
			SelenideElement element;
			train = trains.get(i);
			
			// Убрать getText
			element = train.$(By.xpath("(//a[@name='toggleParts'])[" + (i - 1) + "]"));
			if (element.getText().contains("Подробнее")) {
				element.click();
				$(By.xpath(".//*[@id='Part0']/div[5]/div[" + (i + 1) + "]/div[2]")).waitUntil(Condition.appear, 5000);
			}
			
			SelenideElement firstTrainNumber;
			SelenideElement secondTrainNumber;
			
			train = trains.get(i);
			firstTrainNumber = train.$(By.xpath("//div[@id='Part0']/div[5]/div[" + (i + 1) + "]/div[2]/table[1]/tbody/tr/td[2]/div"));
			secondTrainNumber = train.$(By.xpath("//div[@id='Part0']/div[5]/div[" + (i + 1) + "]/div[2]/table[2]/tbody/tr/td[2]/div"));
			
			// Ищем пару поездов туда и обратно. Если находим кликаем на радио батон
			if (firstTrainNumber.has(Condition.text(trainNumber)) & secondTrainNumber.has(Condition.text(transferTrainNumber))) {
				directTrainsType[0] = $(By.xpath("//div[@id='Part0']/div[5]/div[" + (i + 1) + "]/div[2]/table[1]/tbody/tr/td/span")).attr("title");
				directTrainsType[1] = $(By.xpath("//div[@id='Part0']/div[5]/div[" + (i + 1) + "]/div[2]/table[2]/tbody/tr/td/span")).attr("title");
				
				$(By.xpath("//div[@id='Part0']/div[5]/div[" + (i + 1) + "]/div/table/tbody/tr/td/input")).click();
				break;
			}
		}
		
		// *********** Выбираем пару поездов (с пересадкой) в направлении 'Обратно' ************
		
		trainNumber = returnTrain[0];
		//carCheckers = returnTrain[1];
		
		transferTrainNumber = returnTransferTrain[0];
		transferStation = returnTransferTrain[1];
		transferTimeFrom = returnTransferTrain[2];
		transferTimeTo = returnTransferTrain[3];
		
		message = $(By.xpath("//div[@id='Part1']/div[3]/div/div/b"));		// Блок сообщений (Найдены или не найдены поезда)
		message.shouldHave(Condition.text("Не найдено ни одного поезда"));
		
		passWithTransferLink = $(By.xpath("//div[@id='Part1']/div[2]/table/tbody/tr/td[4]/a"));	// Линк 'Маршруты с пересадками'
													
		if (passWithTransferLink.exists())									// Проверяем наличие линка 'Маршруты с пересадками'
			passWithTransferLink.click();									// Если линк присутствует нажимаем его
			
		$(By.xpath(".//*[@id='ajaxTrainTable']")).waitWhile(Condition.visible, 180000);   // Ждем пока не появится список поездов
		
		toolBar = $(By.xpath("//div[@id='Part1']/div[5]/div/div/div"));
		
		if (toolBar.exists()) {												// Устанавливаем пересадочную станцию и время пересадки
			if (!transferStation.isEmpty()) toolBar.$("select[name='MidPt']").selectOption(transferStation);
			if (!transferTimeFrom.isEmpty()) toolBar.$("input[name='T0']").setValue(transferTimeFrom);
			if (!transferTimeTo.isEmpty()) toolBar.$("input[name='T1']").setValue(transferTimeTo);
			toolBar.$(By.xpath("//div[@id='Part1']/div[5]/div/div/div/table/tbody/tr/td[4]/button")).click();	// Нажимаем батон 'Пересчитать'
		}
		
		trains = $$(By.xpath(".//*[@id='Part1']/div[5]/div"));
		trains.shouldHave(CollectionCondition.sizeGreaterThan(2));
		
		for (int i = 3; i < trains.size(); i++) { 							// Разворачиваем все поезда по очереди методом кликанья на батон 'Подробнее'
			SelenideElement element;
			train = trains.get(i);
			
			element = $(By.xpath("//div[@id='Part1']/div[5]/div[" + (i + 1) + "]/div/table/tbody/tr/td[2]/a"));

			if (element.getText().contains("Подробнее")) {
				element.click();
				$(By.xpath(".//*[@id='Part1']/div[5]/div[" + (i + 1) + "]/div[2]")).waitUntil(Condition.appear, 5000);
			}
			
			SelenideElement firstTrainNumber;
			SelenideElement secondTrainNumber;
			
			//train = trains.get(i);
			firstTrainNumber = train.$(By.xpath("//div[@id='Part1']/div[5]/div[" + (i + 1) + "]/div[2]/table[1]/tbody/tr/td[2]/div"));
			secondTrainNumber = train.$(By.xpath("//div[@id='Part1']/div[5]/div[" + (i + 1) + "]/div[2]/table[2]/tbody/tr/td[2]/div"));
			
			// Ищем пару поездов туда и обратно. Если находим кликаем на радио батон
			if (firstTrainNumber.has(Condition.text(trainNumber)) & secondTrainNumber.has(Condition.text(transferTrainNumber))) {
				returnTrainsType[0] = $(By.xpath("//div[@id='Part1']/div[5]/div[" + (i + 1) + "]/div[2]/table[1]/tbody/tr/td/span")).attr("title");
				returnTrainsType[1] = $(By.xpath("//div[@id='Part1']/div[5]/div[" + (i + 1) + "]/div[2]/table[2]/tbody/tr/td/span")).attr("title");
				
				$(By.xpath("//div[@id='Part1']/div[5]/div[" + (i + 1) + "]/div/table/tbody/tr/td/input")).click();
				break;
			}
		}
		
		$("#continueButton").shouldBe(Condition.enabled).click();
		
		return page(TrainSelect.class);
	}
	
	public WagonSelect wagonSelect(String[] directWagon, String[] directTransferWagon, String[] returnWagon, String[] returnTransferWagon) {
		SelenideElement wagon;
		ElementsCollection wagons;
		int numWagons = 0;
		int trainIndex = 0;
		
		String wagonType;
		String wagonCategory;
		String servicesAndCapabilities;
		String transferWagonType;
		String transferWagonCategory;
		String transferServicesAndCapabilities;
		
		// *********** Выбираем пару вагонов (с пересадкой) в направлении 'Туда' ************
		
		wagonType = directWagon[0];
		wagonCategory = directWagon[1];
		servicesAndCapabilities = directWagon[2];
		transferWagonType = directTransferWagon[0];
		transferWagonCategory = directTransferWagon[1];
		transferServicesAndCapabilities = directTransferWagon[2];
		 
		// Ждем пока не появится список вагонов
		$(By.xpath("//div[@id='Main']/div/div/table[2]")).waitUntil(Condition.appear, 30000);

		ElementsCollection trains = $$(By.className("jtrain-box"));
		
		// Здесь проверяем наличие электричек на маршруте, если есть электричка, пропускаем выбор вагона 
		if (!(directTrainsType[0].contains("Электричка"))) {
			
			wagons = trains.get(trainIndex).$$(".j-car-item.trlist__trlist-row");
			numWagons = wagons.size();

			for (int i = 0; i < wagons.size(); i++ ) {								// Выбор вагона в первом поезде
				wagon = wagons.get(i);
				if (wagon.has(Condition.text(wagonType + " " + wagonCategory + " " + servicesAndCapabilities))) {
					$(By.xpath("(//input[@type='radio'])[" + (i + 1) + "]")).click();
					break;
				}
			}
			
			trainIndex++;
		}
		
		if (!(directTrainsType[1].contains("Электричка"))) {
			
			wagons = trains.get(trainIndex).$$(".j-car-item.trlist__trlist-row"); 		// Выбор вагона во втором поезде
			
			for (int i = 0; i < wagons.size(); i++ ) {
				wagon = wagons.get(i);
				if (wagon.has(Condition.text(transferWagonType + " " + transferWagonCategory + " " + transferServicesAndCapabilities))) {
					wagon.$(By.xpath("(//input[@type='radio'])[" + (i + 1 + numWagons) + "]")).click();
					break;
				}
			}
			
			numWagons += wagons.size();
			trainIndex++;
		}

		
		// *********** Выбираем пару вагонов (с пересадкой) в направлении 'Обратно' ************
		
		wagonType = returnWagon[0];
		wagonCategory = returnWagon[1];
		servicesAndCapabilities = returnWagon[2];
		transferWagonType = returnTransferWagon[0];
		transferWagonCategory = returnTransferWagon[1];
		transferServicesAndCapabilities = returnTransferWagon[2];
		
		//trains = $$(By.className("jtrain-box"));
		
		// Здесь проверяем наличие электричек на маршруте, если есть электричка, пропускаем выбор вагона 
		if (!returnTrainsType[0].contains("Электричка")) {
			
			wagons = trains.get(trainIndex).$$(".j-car-item.trlist__trlist-row");

			for (int i = 0; i < wagons.size(); i++ ) {								// Выбор вагона в первом поезде
				wagon = wagons.get(i);
				if (wagon.has(Condition.text(wagonType + " " + wagonCategory + " " + servicesAndCapabilities))) {
					$(By.xpath("(//input[@type='radio'])[" + (i + 1 + numWagons) + "]")).click();
					break;
				}
			}
			
			numWagons += wagons.size();
			trainIndex++;
		}
		
		if (!returnTrainsType[1].contains("Электричка")) {
			wagons = trains.get(trainIndex).$$(".j-car-item.trlist__trlist-row"); 		// Выбор вагона во втором поезде
			
			for (int i = 0; i < wagons.size(); i++ ) {
				wagon = wagons.get(i);
				if (wagon.has(Condition.text(transferWagonType + " " + transferWagonCategory + " " + transferServicesAndCapabilities))) {
					wagon.$(By.xpath("(//input[@type='radio'])[" + (i + 1 + numWagons) + "]")).click();
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
	
	public PassengerForm fillPlaceSigns(Object[] directPlaces, Object[] transferPlaces, Object[] returnPlaces, Object[] returnTransferPlaces) {
		SelenideElement element;
		SelenideElement placesProp;
		String wagonType;
		String placeFrom;
		String placeTo;
		int trainIndex = 1;
		String plGender;
		
		// Необходимо организовать проверку типа транспортного средства на маршруте с пересадкой и в зависимости от этого  устанавливать признаки мест
		// ******* trainsType ******* Поезд дальнего следования, электричка, паром или автобус
		
		// Маршрут 'Туда'
		// Выбираем места в вагоне в первом поезде
		
		if (!(directTrainsType[0].contains("Электричка"))) {
			placesProp = $(By.xpath(".//*[@id='TrainsList']/div[" + trainIndex + "]"));
			
			wagonType = $(By.xpath("//div[@id='TrainsList']/div[" + trainIndex + "]/table/tbody/tr/td[4]")).getText().split("\n")[1].split(" ")[0];
			placeFrom = (String) directPlaces[2];
			placeTo = (String) directPlaces[3];
			
			placesProp.find(By.name("range0")).setValue(placeFrom);													// Граница мест 'С'
			placesProp.find(By.name("range1")).setValue(placeTo);													// Граница мест 'По'
			
			switch (wagonType) {
		        case "Сидячий":  
		        	break;
		        case "Плацкартный":
		        	placesProp.find(By.name("plBedding")).setSelected((boolean) (directPlaces[4]));					// Оплатить постельное бельё
		        	placesProp.find(By.name("plUpDown")).selectOption((String) directPlaces[5]);					// Верхние нижние места
		        	placesProp.find(By.name("plComp")).selectOption((String) directPlaces[6]);						// Расположение мест
		        	break;	
		        case "Купе":	
		        	plGender = (String) directPlaces[0];
		    		
		        	element = placesProp.find(By.name("plGender"));
		        	
		        	if (element.is(Condition.exist)) { 							        							// Если не выбран признак места, выбираем его 
		        		placesProp.find(By.name("plGender")).selectOption(plGender);
		        	}
		    			
		    		element = placesProp.find(By.name("plKupe"));

		    		if (numPassengers > 1) {
		    			// Устанавливаем чекбокс 'В одном купе'
		    			element.setSelected((boolean) directPlaces[1]);
		    		} else
		    			element.shouldNotBe(Condition.visible);
		    		
		        	break;
		        case "СВ":
		        	plGender = (String) directPlaces[0];
		    		
		        	element = placesProp.find(By.name("plGender"));
		        	
		        	if (element.is(Condition.exist)) { 							        							// Если не выбран признак места, выбираем его 
		        		placesProp.find(By.name("plGender")).selectOption(plGender);
		        	}
		    			
		    		element = placesProp.find(By.name("plKupe"));

		    		if (numPassengers > 1) {
		    			// Устанавливаем чекбокс 'В одном купе'
		    			element.setSelected((boolean) directPlaces[1]);
		    		} else
		    			element.shouldNotBe(Condition.visible);

		        	break;
		        case "Люкс":
		        	
		        	break;
			}
			
			trainIndex++;
		}
				
		// Выбираем места в вагоне во втором поезде
		
		if (!(directTrainsType[1].contains("Электричка"))) {
			placesProp = $(By.xpath(".//*[@id='TrainsList']/div[" + trainIndex + "]"));
			
			wagonType = $(By.xpath("//div[@id='TrainsList']/div[" + trainIndex + "]/table/tbody/tr/td[4]")).getText().split("\n")[1].split(" ")[0];
			placeFrom = (String) directPlaces[2];
			placeTo = (String) directPlaces[3];
			
			placesProp.find(By.name("range0")).setValue(placeFrom);													// Граница мест 'С'
			placesProp.find(By.name("range1")).setValue(placeTo);													// Граница мест 'По'
			
			switch (wagonType) {
		        case "Сидячий":  
		        	break;
		        case "Плацкартный":
		        	placesProp.find(By.name("plBedding")).setSelected((boolean) (directPlaces[4]));					// Оплатить постельное бельё
		        	placesProp.find(By.name("plUpDown")).selectOption((String) directPlaces[5]);					// Верхние нижние места
		        	placesProp.find(By.name("plComp")).selectOption((String) directPlaces[6]);						// Расположение мест
		        	break;	
		        case "Купе":	
		        	plGender = (String) directPlaces[0];
		    		
		        	element = placesProp.find(By.name("plGender"));
		        	
		        	if (element.is(Condition.exist)) { 							        							// Если не выбран признак места, выбираем его 
		        		placesProp.find(By.name("plGender")).selectOption(plGender);
		        	}
		    			
		    		element = placesProp.find(By.name("plKupe"));

		    		if (numPassengers > 1) {
		    			// Устанавливаем чекбокс 'В одном купе'
		    			element.setSelected((boolean) directPlaces[1]);
		    		} else
		    			element.shouldNotBe(Condition.visible);
		    		
		        	break;
		        case "СВ":
		        	plGender = (String) directPlaces[0];
		    		
		        	element = placesProp.find(By.name("plGender"));
		        	
		        	if (element.is(Condition.exist)) { 							        							// Если не выбран признак места, выбираем его 
		        		placesProp.find(By.name("plGender")).selectOption(plGender);
		        	}
		    			
		    		element = placesProp.find(By.name("plKupe"));

		    		if (numPassengers > 1) {
		    			// Устанавливаем чекбокс 'В одном купе'
		    			element.setSelected((boolean) directPlaces[1]);
		    		} else
		    			element.shouldNotBe(Condition.visible);

		        	break;
		        case "Люкс":
		        	
		        	break;
			}
			
			trainIndex++;
		}
		
		// Маршрут 'Обратно'
		// Выбираем места в вагоне в первом поезде
		
		if (!(returnTrainsType[0].contains("Электричка"))) {
			placesProp = $(By.xpath(".//*[@id='TrainsList']/div[" + trainIndex + "]"));
			
			wagonType = $(By.xpath("//div[@id='TrainsList']/div[" + trainIndex + "]/table/tbody/tr/td[4]")).getText().split("\n")[1].split(" ")[0];
			placeFrom = (String) directPlaces[2];
			placeTo = (String) directPlaces[3];
			
			placesProp.find(By.name("range0")).setValue(placeFrom);													// Граница мест 'С'
			placesProp.find(By.name("range1")).setValue(placeTo);													// Граница мест 'По'
			
			switch (wagonType) {
		        case "Сидячий":  
		        	break;
		        case "Плацкартный":
		        	placesProp.find(By.name("plBedding")).setSelected((boolean) (directPlaces[4]));					// Оплатить постельное бельё
		        	placesProp.find(By.name("plUpDown")).selectOption((String) directPlaces[5]);					// Верхние нижние места
		        	placesProp.find(By.name("plComp")).selectOption((String) directPlaces[6]);						// Расположение мест
		        	break;	
		        case "Купе":	
		        	plGender = (String) directPlaces[0];
		    		
		        	element = placesProp.find(By.name("plGender"));
		        	
		        	if (element.is(Condition.exist)) { 							        							// Если не выбран признак места, выбираем его 
		        		placesProp.find(By.name("plGender")).selectOption(plGender);
		        	}
		    			
		    		element = placesProp.find(By.name("plKupe"));

		    		if (numPassengers > 1) {
		    			// Устанавливаем чекбокс 'В одном купе'
		    			element.setSelected((boolean) directPlaces[1]);
		    		} else
		    			element.shouldNotBe(Condition.visible);
		    		
		        	break;
		        case "СВ":
		        	plGender = (String) directPlaces[0];
		    		
		        	element = placesProp.find(By.name("plGender"));
		        	
		        	if (element.is(Condition.exist)) { 							        							// Если не выбран признак места, выбираем его 
		        		placesProp.find(By.name("plGender")).selectOption(plGender);
		        	}
		    			
		    		element = placesProp.find(By.name("plKupe"));

		    		if (numPassengers > 1) {
		    			// Устанавливаем чекбокс 'В одном купе'
		    			element.setSelected((boolean) directPlaces[1]);
		    		} else
		    			element.shouldNotBe(Condition.visible);
		    		
		        	break;
		        case "Люкс":
		        	
		        	break;
			}
			
			trainIndex++;
		}
		
		// Выбираем места в вагоне во втором поезде

		if (!(returnTrainsType[1].contains("Электричка"))) {
			placesProp = $(By.xpath(".//*[@id='TrainsList']/div[" + trainIndex + "]"));
			
			wagonType = $(By.xpath("//div[@id='TrainsList']/div[" + trainIndex + "]/table/tbody/tr/td[4]")).getText().split("\n")[1].split(" ")[0];
			placeFrom = (String) directPlaces[2];
			placeTo = (String) directPlaces[3];
			
			placesProp.find(By.name("range0")).setValue(placeFrom);													// Граница мест 'С'
			placesProp.find(By.name("range1")).setValue(placeTo);													// Граница мест 'По'
			
			switch (wagonType) {
		        case "Сидячий":  
		        	break;
		        case "Плацкартный":
		        	placesProp.find(By.name("plBedding")).setSelected((boolean) (directPlaces[4]));					// Оплатить постельное бельё
		        	placesProp.find(By.name("plUpDown")).selectOption((String) directPlaces[5]);					// Верхние нижние места
		        	placesProp.find(By.name("plComp")).selectOption((String) directPlaces[6]);						// Расположение мест
		        	break;	
		        case "Купе":	
		        	plGender = (String) directPlaces[0];
		    		
		        	element = placesProp.find(By.name("plGender"));
		        	
		        	if (element.is(Condition.exist)) { 							        							// Если не выбран признак места, выбираем его 
		        		placesProp.find(By.name("plGender")).selectOption(plGender);
		        	}
		    			
		    		element = placesProp.find(By.name("plKupe"));

		    		if (numPassengers > 1) {
		    			// Устанавливаем чекбокс 'В одном купе'
		    			element.setSelected((boolean) directPlaces[1]);
		    		} else
		    			element.shouldNotBe(Condition.visible);
		    		
		        	break;
		        case "СВ":
		        	plGender = (String) directPlaces[0];
		    		
		        	element = placesProp.find(By.name("plGender"));
		        	
		        	if (element.is(Condition.exist)) { 							        							// Если не выбран признак места, выбираем его 
		        		placesProp.find(By.name("plGender")).selectOption(plGender);
		        	}
		    			
		    		element = placesProp.find(By.name("plKupe"));

		    		if (numPassengers > 1) {
		    			// Устанавливаем чекбокс 'В одном купе'
		    			element.setSelected((boolean) directPlaces[1]);
		    		} else
		    			element.shouldNotBe(Condition.visible);

		        	break;
		        case "Люкс":
		        	
		        	break;
			}
			
			trainIndex++;
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
		
		$(By.xpath(".//*[@id='Main']/div/div/a")).waitUntil(Condition.enabled, 60000);
		
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