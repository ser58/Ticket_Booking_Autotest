package test.ru.oooinex.autotests.buyticket.pages;

import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;

import com.codeborne.selenide.SelenideElement;

public class CompleteOrderProcessing {
	public SelenideElement result() {
		SelenideElement result;
		result = $(By.xpath(".//*[@id='MainBox']"));
		
		return result;
	}
}
