package test.ru.oooinex.autotests.suggestions.tests;

import static com.codeborne.selenide.Configuration.AssertionMode.STRICT;
import static com.codeborne.selenide.Selenide.open;
import static test.ru.oooinex.utilities.Tools.createDriver;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.testng.SoftAsserts;

import test.ru.oooinex.autotests.suggestions.testitself.SuggestionsPagesTest;
import test.ru.oooinex.dataproviders.SuggestionsDataProvider;
import test.ru.oooinex.listeners.CustomTest;

@Listeners({ CustomTest.class, SoftAsserts.class })
public class SuggestionsTest {
	static SuggestionsPagesTest suggestPage;
	static WebDriver driver;
	static String url;

	@Parameters({ "baseUrl", "holdBrowser", "browser", "profile", "reportsFolder", "reportsUrl", "timeout" })
	@BeforeClass
	public static void setUp(String baseUrl, String holdBrowser, String browser, String profile, String reportsFolder, String reportsUrl, String timeout) {
		if (driver == null) {
			Configuration.holdBrowserOpen = holdBrowser.contentEquals("true") ? true : false;
			Configuration.timeout = Long.parseLong(timeout);
			Configuration.savePageSource = false;
			//Configuration.browser = browser;
			Configuration.browserSize = "1344x1040";
			Configuration.pageLoadStrategy = "normal";			// Possible values: "none", "normal" and "eager".
			Configuration.startMaximized = true;
			Configuration.assertionMode = STRICT;
			
			String baseURL = System.getProperty("PTK");

			if (!reportsFolder.isEmpty())
				Configuration.reportsFolder = reportsFolder;
			
			if (!reportsUrl.isEmpty())
				Configuration.reportsUrl = reportsUrl;
			
			driver = createDriver(browser, profile);
			WebDriverRunner.setWebDriver(driver);
			
			url = baseURL == null ? baseUrl : baseURL;
			
			suggestPage = open(url, SuggestionsPagesTest.class);

			driver.manage().window().maximize();
		}
	}
	
	@Test(description = "Проверяем различные варианты подсказки станций отправления и прибытия", 
		  dataProvider = "suggestions", dataProviderClass = SuggestionsDataProvider.class)
	public void ProfferTest(String toEnter, String toSelect, String[] listTo, String fromEnter, String fromSelect, String[] listFrom) {
		suggestPage.suggestionsTest(toEnter, toSelect, listTo, fromEnter, fromSelect, listFrom);
	}
	
	@Parameters({ "holdBrowser" })
	@AfterSuite
	public static void tearDown(String holdBrowser) {
		if (driver != null) {
			if (!holdBrowser.contentEquals("true") ? true : false)
				driver.quit();
		}
	}
}
