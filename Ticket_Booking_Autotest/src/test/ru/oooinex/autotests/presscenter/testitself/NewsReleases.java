package test.ru.oooinex.autotests.presscenter.testitself;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

import java.awt.AWTException;
import java.util.Hashtable;

import org.openqa.selenium.By;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import test.ru.oooinex.utilities.Tools;

public class NewsReleases {
	static String newsHeader;
	static String newsText;
	static String newsReference;
	static String newsID;
	
	public void adminLogin(String login, String password) {
 		$("#userID").setValue(login);
		$("#password").setValue(password); 
		$(By.name("ns_Z7_CGAH47L0085810IAHU76SD20U1__login")).click();
	}

	public void enterIntoPressCenter() {
		$(By.xpath(".//*[@id='wptheme-topnav']/ul/li[23]/a")).should(exist).click();
		
		$(".AdminToolBar>tbody>tr>td>span").shouldHave(text("Пресс-Центр"));
		
		$(By.linkText("Новости / Релизы")).click();
		$(By.className("arm-title")).shouldHave(text(" Новости"));
	}

	public void addNews(Hashtable<String, Boolean> goals, Hashtable<String, Object> reviewOptions) {
		String temp;
		String dateTime = Tools.getNowDateTime();
		
		$(By.className("glyph")).click();																			// Нажать кнопку 'Добавить'
		
		// Параметры
		newsHeader = (String) reviewOptions.get("header");
		$(By.xpath(".//*[@id='NAME_id']")).setValue(newsHeader); 													// Заголовок
		$(By.xpath(".//*[@id='HIGHLIGHT_FLAG_id']")).setSelected((boolean) reviewOptions.get("highlightInColor")); 	// Выделить цветом
		
		Selenide.switchTo().frame("ANOUNCE_id_ifr");
		SelenideElement announce = $(By.cssSelector("body"));
		announce.setValue((String) reviewOptions.get("announcement")); 												// Анонс
		
		Selenide.switchTo().parentFrame();
		$(By.xpath(".//*[@id='ANNOUNCE_BLOCK_id']")).setValue((String) reviewOptions.get("position")); 				// Позиция в блоке анонсов (ID)
		
		Selenide.switchTo().frame("TEXT_id_ifr");
		SelenideElement text = $(By.cssSelector("body"));
		newsText = (String) reviewOptions.get("text");
		text.setValue(newsText); 																					// Текст
		
		Selenide.switchTo().parentFrame();
		Selenide.switchTo().frame("NOTE_id_ifr");
		SelenideElement reference = $(By.cssSelector("body"));
		newsReference = (String) reviewOptions.get("reference");
		reference.setValue(newsReference); 																			// Справка
		
		Selenide.switchTo().parentFrame();
		
		$(By.xpath(".//*[@id='BLOCK_DAY_id']")).setValue((String) reviewOptions.get("etbPosition")); 				// Позиция ЕТВ ( быв. тема дня) (ID)
		$(By.xpath(".//*[@id='MAINPASS_FLAG_id']")).setValue((String) reviewOptions.get("startingOfPasengers")); 	// Стартовая пассажиров (ID)

		// Выбор раздела
		$(By.xpath(".//*[@id='APP_STRUCTURE_PRESSRELIZ__PRESSRELIZ_ID_chooseTopicButton_id']")).click();
		Selenide.switchTo().window(1);
		
		$(By.xpath(".//*[@id='entitiesList']")).selectOption((String) reviewOptions.get("sections"));
		$(By.xpath("//input[@value='Выбрать']")).click();
		
		// Выбор рубрики
		Selenide.switchTo().window(0);
		$(By.xpath(".//*[@id='APP_RUBRICATOR_PRESSRELIZ__PRESSRELIZ_ID_chooseTopicButton_id']")).click();
		Selenide.switchTo().window(1);
		
		$(By.xpath(".//*[@id='entitiesList']")).selectOption((String) reviewOptions.get("headings"));
		$(By.xpath("//input[@value='Выбрать']")).click();
		
		Selenide.switchTo().window(0);
		
 		$(By.xpath(".//*[@id='PHOTO_REP_ID_id']")).setValue((String) reviewOptions.get("newsPhotoreportID")); 		// Фоторепортаж к новости (ID)
		
 		temp = (String) reviewOptions.get("publicationDate");														// Дата публикации (Дата)
 		
 		if (temp.contains("immediately")) {
 			$(By.xpath(".//*[@id='DATE_PUBLICATION_id']")).setValue(dateTime.split(" ")[0].replace(".", "-"));
 		} else {
 	 		$(By.xpath(".//*[@id='DATE_PUBLICATION_id']")).setValue(temp); 	
 		}
 			
 		$(By.xpath("//.//*[@id='DATE_PUBLICATION_hour_id']")).setValue(dateTime.split(" ")[1].split(":")[0]);		// Час публикации
 		$(By.xpath("//.//*[@id='DATE_PUBLICATION_minute_id']")).setValue(dateTime.split(" ")[1].split(":")[1]);		// Минута публикации
 		 		
		$(By.xpath(".//*[@id='DATE_CAL_id']")).setValue(dateTime.split(" ")[0].replace(".", "-")); 								// Дата для календаря (Дата)
		$(By.xpath(".//*[@id='PUBLISH_TYPE_ID_id']")).selectOption((String) reviewOptions.get("publicationStatus")); 			// Статус публикации
		$(By.xpath(".//*[@id='FILE_LINK_NAME_id']")).setValue((String) reviewOptions.get("pressFolderName")); 					// Название пресс-папки
		$(By.xpath(".//*[@id='COMMENTS_id']")).setValue((String) reviewOptions.get("comment")); 								// Комментарий
		$(By.xpath(".//*[@id='ACTION_NAME_id']")).setValue((String) reviewOptions.get("actionName")); 							// Наименование мероприятия с онлайн-аккредитацией
		$(By.xpath(".//*[@id='ACCREDITATION_FINISH_id']")).setValue((String) reviewOptions.get("accreditationCompletionDate")); // Завершение аккредитации (Дата)
		
		// Где показывать
		$(By.xpath(".//*[@id='PASS_FLAG_id']")).setSelected(goals.get("newsForPassengers"));						// Новости для пассажиров
		$(By.xpath(".//*[@id='GRUZ_FLAG_id']")).setSelected(goals.get("newsForTrucks"));							// Новости для грузовиков
		$(By.xpath(".//*[@id='INT_FLAG_id']")).setSelected(goals.get("internationalNews"));							// Международные новости
		$(By.xpath(".//*[@id='DZO_FLAG_id']")).setSelected(goals.get("dzoNews"));									// Новости ДЗО
		$(By.xpath(".//*[@id='IR_FLAG_id']")).setSelected(goals.get("investorsNews"));								// Новости Инвесторов
		$(By.xpath(".//*[@id='ACCREDITATION_FLAG_id']")).setSelected(goals.get("onlineAccreditation"));				// Онлайн аккредитация
		$(By.xpath(".//*[@id='PostIt_id']")).setSelected(goals.get("includeInNewsletter"));							// Включать в рассылку
		
		$(By.xpath("//button[@type='submit']")).click();
	}
	
	public void checkNewsAndGetID() {
		$(By.xpath(".//*[@id='PC_Z7_M71IFOI21GQ1802T5SHALE0S55000000_search_NAME_id']")).setValue(newsHeader);		// Вводим заголовок
		$(By.xpath("//button[@type='submit']")).click();															// Нажимаем найти
		
		// Вытаскиваем нашу новость
		SelenideElement news = $$(By.xpath(".//*[@id='PC_Z7_M71IFOI21GQ1802T5SHALE0S55000000_formList']/table/tbody/tr[1]/td/table/tbody/tr")).find(Condition.text(newsHeader));
		news.should(Condition.exist);
		
		// Вытаскиваем елемент содержащий ID
		SelenideElement ID = news.$(By.xpath("//form[@id='PC_Z7_M71IFOI21GQ1802T5SHALE0S55000000_formList']/table/tbody/tr/td/table/tbody/tr[2]/td[2]/a"));
		
		// Запоминаем ID
		newsID = ID.getText();													
	}
	
	public void checkNewsOnUserInterface(String baseUrlNews) {
		String newsURL = baseUrlNews + "?STRUCTURE_ID=654&layer_id=4069&refererLayerId=3307&id=" + newsID;
		
		// Открываем страничку с новостью
		Selenide.navigator.open(newsURL);
		
		// Убеждаемся что открылась именно она 
		$(By.xpath(".//*[@id='container']/tbody/tr/td[2]/h1")).shouldHave(text("Пресс-релизы"));
		// Проверяем заголовок
		$(By.xpath(".//*[@id='container']/tbody/tr/td[2]/table/tbody/tr/td/h2")).shouldHave(text(newsHeader));
		// Проверяем текст 
		$(By.xpath(".//*[@id='container']/tbody/tr/td[2]/table/tbody/tr/td/p[1]")).shouldHave(text(newsText));
		// Проверяем поле справки
		$(By.xpath(".//*[@id='container']/tbody/tr/td[2]/table/tbody/tr/td/div[3]/p[1]")).shouldHave(text(newsReference));
	}
	
	public void deleteNews(String baseUrlPortal) throws AWTException {
		Selenide.navigator.open(baseUrlPortal);
		
		$(By.xpath(".//*[@id='wptheme-topnav']/ul/li[23]/a")).should(exist).click();
		
		$(".AdminToolBar>tbody>tr>td>span").shouldHave(text("Пресс-Центр"));
		
		$(By.linkText("Новости / Релизы")).click();
		$(By.className("arm-title")).shouldHave(text(" Новости"));
		
		// Находим нашу новость
		$(By.xpath(".//*[@id='PC_Z7_M71IFOI21GQ1802T5SHALE0S55000000_search_NAME_id']")).setValue(newsHeader);		// Вводим заголовок
		$(By.xpath("//button[@type='submit']")).click();															// Нажимаем найти
		SelenideElement news = $$(By.xpath(".//*[@id='PC_Z7_M71IFOI21GQ1802T5SHALE0S55000000_formList']/table/tbody/tr[1]/td/table/tbody/tr")).find(Condition.text(newsHeader));
		news.should(Condition.exist);
		
		$(By.xpath("//td/table/tbody/tr[2]/td/input")).setSelected(true);
		
		$("button.glyph.arm-btn-remove").click();
		
		Selenide.switchTo().alert().accept();
		
		// Проверяем что новость удалилась и что отсутствуют ошибки
		$(By.xpath(".//*[@id='PC_Z7_M71IFOI21GQ1802T5SHALE0S55000000_search_NAME_id']")).setValue(newsHeader);		// Вводим заголовок
		$(By.xpath("//button[@type='submit']")).click();															// Нажимаем найти
		// Здесь нужно подождать пока не загрузится страница
		news = $$(By.xpath(".//*[@id='PC_Z7_M71IFOI21GQ1802T5SHALE0S55000000_formList']/table/tbody/tr[1]/td/table/tbody/tr")).find(Condition.text(newsHeader));
		news.shouldNot(Condition.exist);
	}
}
