package test.ru.oooinex.autotests.buyticket.pages;

import static com.codeborne.selenide.Selenide.$$;

import org.openqa.selenium.By;

import com.codeborne.selenide.ElementsCollection;

public class Schedule_Received {
	public ElementsCollection result() {
		ElementsCollection result;
		result = $$(By.xpath(".//*[@id='Part0']/div[5]/table/tbody/tr"));
		
		return result;
	}
}
