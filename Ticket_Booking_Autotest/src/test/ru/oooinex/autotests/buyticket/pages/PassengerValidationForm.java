package test.ru.oooinex.autotests.buyticket.pages;

import static com.codeborne.selenide.Selenide.$;
import org.openqa.selenium.By;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

public class PassengerValidationForm {
	public SelenideElement result() {
		SelenideElement result;
		result = $(By.xpath(".//*[@id='wrapper']/div[2]/div[3]")).waitUntil(Condition.appear, 60000); 	// Форма оформления заказа с кнопкой 'Оплатить'
		
		return result;
	}
}