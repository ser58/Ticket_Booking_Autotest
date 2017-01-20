package test.ru.oooinex.autotests.suggestions.testitself;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Condition.*;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.Reporter;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

public class SuggestionsPagesTest {
	boolean newVersion;
	
	public void suggestionsTest(String toEnter, String toSelect, String[] listTo, String fromEnter, String fromSelect, String[] listFrom) {
		int timeout = 500;
		
		$(By.xpath("//a[contains(text(),'Пассажирам')]")).click();
		Selenide.sleep(timeout);
		
		$(By.id("tab0_Output")).should(Condition.appear);
		Reporter.log("Загрузилась страница: 'Купить билет и посмотреть расписание'");
		
		SelenideElement submit = $(By.xpath(".//*[@id='Submit']"));
		
		SelenideElement departure = $(By.xpath(".//*[@id='name0']"));
		ElementsCollection departureList = $$(By.xpath("html/body/div[6]/div"));
		SelenideElement departureBox = $(By.xpath("html/body/div[6]"));
		
		SelenideElement arrival = $(By.xpath(".//*[@id='name1']"));
		ElementsCollection arrivalList;
		SelenideElement arrivalBox = $(By.xpath("html/body/div[7]"));
		
		// Проверка подсказки 'Туда'
		
		departure.clear();													// Очищаем станцию отправления
		
		departure.sendKeys(toEnter.substring(0, 1));						// Вводим первую букву станции отправления и убеждаемся что подсказки нет
		departureBox.shouldNotBe(visible);									// Проверяем что списка станций нет
		departureList = $$(By.xpath("html/body/div[6]/div"));
		//stations =  departureList.getTexts();
		//System.out.println("Введена одна буква: "); printList(stations); 
		departureList.shouldHave(CollectionCondition.empty);
		
		departure.sendKeys(toEnter.substring(1, 2));					 	// Вводим вторую букву станции отправления и убеждаемся что подсказка появилась
		departureBox.shouldBe(visible);
		departureList = $$(By.xpath("html/body/div[6]/div"));
		//stations =  departureList.getTexts();
		//System.out.println("Введены две буквы: "); printList(stations); 
		departureList.shouldHave(CollectionCondition.exactTexts(listTo));	// Проверяем что подсказка содержит ожидаемое количество станций
		
		departure.sendKeys(Keys.BACK_SPACE);								// Убираем одну букву и убеждаемся что подсказка исчезла
		departureBox.shouldNotBe(visible);
		departureList = $$(By.xpath("html/body/div[6]/div"));
		//stations =  departureList.getTexts();
		//System.out.println("Стёрли одну букву: "); printList(stations);
		departureList.forEach(element -> element.shouldBe(empty));

		departure.sendKeys(toEnter.substring(1, 2));						// Снова вводим вторую букву станции отправления и убеждаемся что подсказка опять появилась
		departureBox.shouldBe(visible);
		departureList = $$(By.xpath("html/body/div[6]/div"));
		//stations =  departureList.getTexts();
		//System.out.println("Опять ввели вторую букву: "); printList(stations);
		departureList.shouldHave(CollectionCondition.exactTexts(listTo));
		
		departure.clear();
		
		for (int i = 0; i < toEnter.length(); i++) {
			departure.sendKeys(String.valueOf(toEnter.charAt(i)));			// Вводим полностью название станции отправления (побуквенно с задержкой)
			Selenide.sleep(timeout);										// и убеждаемся что все строки списка содержат станцию отправления
		}
		
		departureBox.shouldBe(visible);
		departureList = $$(By.xpath("html/body/div[6]/div"));
		//stations =  departureList.getTexts();
		//System.out.println("Опять ввели вторую букву: "); printList(stations);
		departureList.forEach(element -> element.shouldHave(text(toEnter)));
		
		for (int i = 0; i < departureList.size(); i++) {
			departureBox.sendKeys(Keys.DOWN);
			SelenideElement element = departureList.get(i);
			if (element.has(text(toSelect))) {
				element.click();
				break;
			}
			Selenide.sleep(timeout);
		}
		
		// Проверка подсказки 'Обратно'
		
		arrival.clear();													// Очищаем станцию назначения
		
		arrival.sendKeys(fromEnter.substring(0, 1));						// Вводим первую букву станции назначения и убеждаемся что подсказки нет
		arrivalBox.shouldNotBe(visible);									// Проверяем что списка станций нет
		arrivalList = $$(By.xpath("html/body/div[7]/div"));
		//stations =  arrivalList.getTexts();
		//System.out.println("Введена одна буква: "); printList(stations); 
		arrivalList.shouldHave(CollectionCondition.empty);
		
		arrival.sendKeys(fromEnter.substring(1, 2));					 	// Вводим вторую букву станции назначения и убеждаемся что подсказка появилась
		arrivalBox.shouldBe(visible);
		arrivalList = $$(By.xpath("html/body/div[7]/div"));
		//stations =  arrivalList.getTexts();
		//System.out.println("Введены две буквы: "); printList(stations); 
		arrivalList.shouldHave(CollectionCondition.exactTexts(listFrom));	// Проверяем что подсказка содержит ожидаемое количество станций
		
		arrival.sendKeys(Keys.BACK_SPACE);									// Убираем одну букву и убеждаемся что подсказка исчезла
		arrivalBox.shouldNotBe(visible);
		arrivalList = $$(By.xpath("html/body/div[7]/div"));
		//stations =  arrivalList.getTexts();
		//System.out.println("Стёрли одну букву: "); printList(stations);
		arrivalList.forEach(element -> element.shouldBe(empty));

		arrival.sendKeys(fromEnter.substring(1, 2));						// Снова вводим вторую букву станции назначения и убеждаемся что подсказка опять появилась
		arrivalBox.shouldBe(visible);
		arrivalList = $$(By.xpath("html/body/div[7]/div"));
		//stations =  arrivalList.getTexts();
		//System.out.println("Опять ввели вторую букву: "); printList(stations);
		arrivalList.shouldHave(CollectionCondition.exactTexts(listFrom));
		
		arrival.clear();
		
		for (int i = 0; i < toEnter.length(); i++) {
			arrival.sendKeys(String.valueOf(fromEnter.charAt(i)));			// Вводим полностью название станции назначения (побуквенно с задержкой)
			Selenide.sleep(timeout);											// и убеждаемся что все строки списка содержат станцию назначения
		}
		
		arrivalBox.shouldBe(visible);
		arrivalList = $$(By.xpath("html/body/div[7]/div"));
		//stations =  arrivalList.getTexts();
		//System.out.println("Опять ввели вторую букву: "); printList(stations);
		arrivalList.forEach(element -> element.shouldHave(text(fromEnter)));
		
		for (int i = 0; i < arrivalList.size(); i++) {
			arrivalBox.sendKeys(Keys.DOWN);
			SelenideElement element = arrivalList.get(i);
			if (element.has(text(fromSelect))) {
				element.click();
				break;
			}
			Selenide.sleep(timeout);
		}
		
		submit.should(exist);
		submit.click();
		
		departure = $(By.xpath(".//*[@id='name0']"));
		arrival = $(By.xpath(".//*[@id='name1']"));
		
		departure.shouldHave(value(toSelect));
		arrival.shouldHave(value(fromSelect));
	}
	
	void printList(String[] list) {
		for (String item : list) 
			System.out.println(item);
	}
}
