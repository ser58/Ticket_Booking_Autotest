package test.ru.oooinex.autotests.buyticket.pages;

import static com.codeborne.selenide.Selenide.$$;

import com.codeborne.selenide.ElementsCollection;

public class TrainSelect {
	public ElementsCollection result() {
		ElementsCollection result;
		result = $$(".j-car-item.trlist__trlist-subhead");
		
		return result;
	}
}
