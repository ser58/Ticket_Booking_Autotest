package test.ru.oooinex.autotests.presscenter.tests;

import static com.codeborne.selenide.Selenide.open;

import java.awt.AWTException;
import java.io.IOException;
import java.util.Hashtable;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;

import test.ru.oooinex.autotests.presscenter.testitself.NewsReleases;
import test.ru.oooinex.dataproviders.NewsReleasesDataProvider;
import test.ru.oooinex.listeners.CustomTest;
import test.ru.oooinex.utilities.Tools;

@Listeners({ CustomTest.class })

public class Content_NewsReleases {
	static NewsReleases adminPage;
	static WebDriver driver;
	static String url;
	
	@Parameters({ "baseUrlPortal", "baseUrl", "holdBrowser", "browser", "profile", "reportsFolder", "reportsUrl", "timeout" })
	@BeforeSuite
	public static void setUp(String baseUrlPortal, String baseUrl, String holdBrowser, String browser, String profile, String reportsFolder, String reportsUrl, String timeout) throws IOException {
		if (driver == null) {
			Configuration.holdBrowserOpen = holdBrowser.contentEquals("true") ? true : false;
			Configuration.timeout = Long.parseLong(timeout);
			Configuration.savePageSource = false;
			Configuration.browser = browser;
			Configuration.startMaximized = true;

			//Configuration.assertionMode = AssertionMode.STRICT;
			
			String baseURL = System.getProperty("PTK");

			if (!reportsFolder.isEmpty())
				Configuration.reportsFolder = reportsFolder;
			
			if (!reportsUrl.isEmpty())
				Configuration.reportsUrl = reportsUrl;
			
			url = baseURL == null ? baseUrl : baseURL;

			driver = Tools.createDriver(browser, profile);
			driver.manage().window().maximize();
			WebDriverRunner.setWebDriver(driver);
			
			adminPage = open(baseUrlPortal, NewsReleases.class);
		} 
	}

	@Parameters({ "login", "password", "reportsUrl" })
	@Test(description = "Логинимся на сайт")
	public void AdminLogin(String login, String password, String reportsUrl) {
		adminPage.adminLogin(login, password);
	}

	@Test(description = "Проверяем что залогинились правильно и открылась нужная страница", dependsOnMethods = { "AdminLogin" })
	public void EnterIntoPressCenter() {
		adminPage.enterIntoPressCenter();
	}
	
	@Test(description = "Создание новости в административном интерфейсе", 
		  dataProvider = "smiReview", dataProviderClass = NewsReleasesDataProvider.class, dependsOnMethods = { "EnterIntoPressCenter" })
	public void AddNewsToAdmin(Hashtable<String, Boolean> goals, Hashtable<String, Object> reviewOptions) {
		adminPage.addNews(goals, reviewOptions);
	}
	
	@Test(description = "Проверка новости в административном интерфейсе", dependsOnMethods = { "AddNewsToAdmin" })
	public void СheckNewsInAdmin() {
		adminPage.checkNewsAndGetID();
	}
	
	@Parameters({ "baseUrlNews" } )
	@Test(description = "Проверка новости в пользовательском интерфейте", dependsOnMethods = { "СheckNewsInAdmin" })
	public void CheckNewsOnUserInterface(String baseUrlNews) {
		adminPage.checkNewsOnUserInterface(baseUrlNews);
	}
	
	@Parameters({ "baseUrlPortal" } )
	@Test(description = "Проверка новости в пользовательском интерфейте", dependsOnMethods = { "CheckNewsOnUserInterface" })
	public void DeleteNews(String baseUrlPortal) throws AWTException {
		adminPage.deleteNews(baseUrlPortal);
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

