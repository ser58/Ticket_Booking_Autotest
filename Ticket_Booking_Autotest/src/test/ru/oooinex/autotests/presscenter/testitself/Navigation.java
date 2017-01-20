package test.ru.oooinex.autotests.presscenter.testitself;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

import java.util.Hashtable;

import org.openqa.selenium.By;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

public class Navigation {
	static String newsHeader;
	static String newsText;
	static String newsReference;
	static String newsID;
	static Navigation selectPage;
	static String log;
	static String pass;
	
	public void adminLogin(String login, String password) {
		log = login;
		pass = password;
		
 		$("#userID").setValue(login);
		$("#password").setValue(password); 
		$(By.name("ns_Z7_CGAH47L0085810IAHU76SD20U1__login")).click();
	}

	public void enterIntoPressCenter() {
		$(By.xpath(".//*[@id='wptheme-topnav']/ul/li[23]/a")).should(exist).click();
		
		$(".AdminToolBar>tbody>tr>td>span").shouldHave(text("Пресс-Центр"));
		
		$(By.linkText("Навигация")).click();
		$(By.className("arm-title")).shouldHave(text("Навигация сайта"));
	}

	public void createFirstLevelTopic(Hashtable<String, Object>[] allParams) {
		Hashtable<String, Object> topicOptions = new Hashtable<String, Object>();
		
		for (int i = 0; i < allParams.length; i++) {
			$(By.xpath("//a[contains(text(),'Выход из системы')]")).click();
			$("#userID").setValue(log);
			$("#password").setValue(pass); 
			$(By.name("ns_Z7_M71IFOI21GN900I403BHMO3007__login")).click();
			enterIntoPressCenter();
			
			topicOptions = allParams[i];
			
			$(By.linkText("Создать новый раздел первого уровня")).click();
			
			$(By.id("PC_Z7_M71IFOI21GQ1802T5SHALE02S3000000_NAME_id")).setValue((String) topicOptions.get("name"));						// Название
			$(By.id("PC_Z7_M71IFOI21GQ1802T5SHALE02S3000000_SORT_ORDER_id")).setValue((String) topicOptions.get("sortOrder")); 			// Порядок
			$(By.id("PC_Z7_M71IFOI21GQ1802T5SHALE02S3000000_LINK_TYPE_ID_id")).selectOption((String) topicOptions.get("linkType"));		// Тип ссылки
			$(By.id("PC_Z7_M71IFOI21GQ1802T5SHALE02S3000000_TEMPLATE_ID_id")).selectOption((String) topicOptions.get("template"));		// Шаблон
			$(By.id("PC_Z7_M71IFOI21GQ1802T5SHALE02S3000000_URL_id")).setValue((String) topicOptions.get("template"));						// Переводы: URL
			$(By.id("PC_Z7_M71IFOI21GQ1802T5SHALE02S3000000_PUBLISH_TYPE_ID_id")).selectOption((String) topicOptions.get("publishType"));	// Статус публикации
			
			$(By.id("PC_Z7_M71IFOI21GQ1802T5SHALE02S3000000_PARENT_ID_chooseTopicButton_id")).click();									// Родительский узел
			
			Selenide.switchTo().window(1);
			
			$(By.id("entitiesList")).selectOption((String) topicOptions.get("parent"));
			$(By.xpath("//input[@value='Выбрать']")).click();
			
			Selenide.switchTo().window(0);
			
			$(By.id("PC_Z7_M71IFOI21GQ1802T5SHALE02S3000000_KEYWORDS_id")).setValue((String) topicOptions.get("keywords"));				// Ключевые слова
			$(By.id("PC_Z7_M71IFOI21GQ1802T5SHALE02S3000000_DESCRIPTION_id")).setValue((String) topicOptions.get("description"));		// Описание

			Selenide.switchTo().frame("PC_Z7_M71IFOI21GQ1802T5SHALE02S3000000_NAV_HEADER_id_ifr");
			SelenideElement sectionText = $(By.cssSelector("body"));
			sectionText.setValue((String) topicOptions.get("description"));																// Текст к разделу
			
			Selenide.switchTo().parentFrame();
			
			$(By.name("submit")).click();	
		}
	}
}
