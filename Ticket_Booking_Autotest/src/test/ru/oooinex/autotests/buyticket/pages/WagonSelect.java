package test.ru.oooinex.autotests.buyticket.pages;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;

public class WagonSelect {
	public SelenideElement result() {
		SelenideElement result;
		result = $("#PassList");
		
		return result;
	}
}
