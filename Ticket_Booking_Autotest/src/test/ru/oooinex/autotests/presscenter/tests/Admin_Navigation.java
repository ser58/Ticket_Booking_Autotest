package test.ru.oooinex.autotests.presscenter.tests;

import static com.codeborne.selenide.Selenide.open;

import java.io.IOException;
import java.util.Hashtable;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;

import test.ru.oooinex.autotests.presscenter.testitself.Navigation;
import test.ru.oooinex.dataproviders.NavigationDataProvider;
import test.ru.oooinex.listeners.CustomTest;
import test.ru.oooinex.utilities.Tools;

@Listeners({ CustomTest.class })

public class Admin_Navigation {
	static Navigation navigationPage;
	static WebDriver driver;
	static String url;
	
	@Parameters({ "baseUrlPortal", "baseUrl", "holdBrowser", "browser", "profile", "reportsFolder", "reportsUrl", "timeout" })
	@BeforeSuite
	public static void setUp(String baseUrlPortal, String baseUrl, String holdBrowser, String browser, String profile, String reportsFolder, String reportsUrl, String timeout) throws IOException {
		if (driver == null) {
			Configuration.holdBrowserOpen = holdBrowser.contentEquals("true") ? true : false;
			Configuration.timeout = Long.parseLong(timeout);
			Configuration.savePageSource = false;
			//Configuration.browser = browser;
			//Configuration.browserSize = "1344x1040";
			Configuration.pageLoadStrategy = "normal";			// Possible values: "none", "normal" and "eager".
			
			String baseURL = System.getProperty("PTK");

			if (!reportsFolder.isEmpty())
				Configuration.reportsFolder = reportsFolder;
			
			if (!reportsUrl.isEmpty())
				Configuration.reportsUrl = reportsUrl;
			
			url = baseURL == null ? baseUrl : baseURL;

			driver = Tools.createDriver(browser, profile);
			driver.manage().window().setSize(new Dimension(1344, 1040));
			WebDriverRunner.setWebDriver(driver);
			
			navigationPage = open(baseUrlPortal, Navigation.class);
		} 
	}

	@Parameters({ "login", "password", "reportsUrl" })
	@Test(description = "Логинимся на сайт")
	public void AdminLogin(String login, String password, String reportsUrl) {
		navigationPage.adminLogin(login, password);
	}

	@Test(description = "Проверяем что залогинились правильно и открылась нужная страница", dependsOnMethods = { "AdminLogin" })
	public void EnterIntoPressCenter() {
		navigationPage.enterIntoPressCenter();
	}

	@Test(description = "Проверяем что залогинились правильно и открылась нужная страница", 
		  dataProvider = "navigation", dataProviderClass = NavigationDataProvider.class, dependsOnMethods = { "EnterIntoPressCenter" })
	public void CreateFirstLevelTopic(Hashtable<String, Object>[] allParams) {
		navigationPage.createFirstLevelTopic(allParams);
	}
}