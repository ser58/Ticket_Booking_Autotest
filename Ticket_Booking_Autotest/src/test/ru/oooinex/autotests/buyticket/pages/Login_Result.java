package test.ru.oooinex.autotests.buyticket.pages;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;

public class Login_Result {
	public SelenideElement result() {
		return $(".orng.j-profile-logout");
	}
}
