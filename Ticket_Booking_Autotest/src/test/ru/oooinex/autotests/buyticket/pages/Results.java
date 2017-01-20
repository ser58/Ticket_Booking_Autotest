package test.ru.oooinex.autotests.buyticket.pages;

import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;

import com.codeborne.selenide.SelenideElement;

public class Results {
	public SelenideElement result() {
		SelenideElement result;
		result = $(By.xpath(".//*[@id='container']/tbody/tr/td[2]"));
		
		return result;
	}
}
