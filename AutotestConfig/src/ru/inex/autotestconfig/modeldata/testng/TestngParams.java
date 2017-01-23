package ru.inex.autotestconfig.modeldata.testng;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class TestngParams extends BaseModelData {
	private static final long serialVersionUID = -7011165103987802872L;

	public TestngParams() {

	}

	public TestngParams(String baseUrl, String baseUrlPortal, String baseUrlNews, String profile, String login, 
						String password, String browser, String holdBrowser, String reportsFolder, 
						String reportsUrl, String timeout, String newVersion, String paymentGatewayPlug ) {
		setBaseUrl(baseUrl);
		setBaseUrlPortal(baseUrlPortal);
		setBaseUrlNews(baseUrlNews);
		setProfile(profile);
		setLogin(login);
		setPassword(password);
		setBrowser(browser);
		setHoldBrowser(holdBrowser);
		setReportsFolder(reportsFolder);
		setReportsUrl(reportsUrl);
		setTimeout(timeout);
		setNewVersion(newVersion);
		setPaymentGatewayPlug(paymentGatewayPlug);
	}

	public void setPaymentGatewayPlug(String paymentGatewayPlug) {
		set("paymentGatewayPlug", paymentGatewayPlug);
	}
	public String getPaymentGatewayPlug() {
		return get("paymentGatewayPlug");
	}

	public void setNewVersion(String newVersion) {
		set("newVersion", newVersion);
	}
	public String getNewVersion() {
		return get("newVersion");
	}

	public void setTimeout(String timeout) {
		set("timeout", timeout);
	}
	public String getTimeout() {
		return get("timeout");
	}

	public void setReportsUrl(String reportsUrl) {
		set("reportsUrl", reportsUrl);
	}
	public String getReportsUrl() {
		return get("reportsUrl");
	}

	public void setReportsFolder(String reportsFolder) {
		set("reportsFolder", reportsFolder);
	}
	public String getReportsFolder() {
		return get("reportsFolder");
	}

	public void setHoldBrowser(String holdBrowser) {
		set("holdBrowser", holdBrowser);
	}
	public String getHoldBrowser() {
		return get("holdBrowser");
	}

	public void setBrowser(String browser) {
		set("browser", browser);
	}
	public String getBrowser() {
		return get("browser");
	}

	public void setPassword(String password) {
		set("password", password);
	}
	public String getPassword() {
		return get("password");
	}

	public void setLogin(String login) {
		set("login", login);
	}
	public String getLogin() {
		return get("login");
	}

	public void setProfile(String profile) {
		set("profile", profile);
	}
	public String getProfile() {
		return get("profile");
	}

	public void setBaseUrlPortal(String baseUrlPortal) {
		set("baseUrlPortal", baseUrlPortal);
	}
	public String getBaseUrlPortal() {
		return get("baseUrlPortal");
	}

	public void setBaseUrlNews(String baseUrlNews) {
		set("baseUrlNews", baseUrlNews);
	}
	public String getBaseUrlNews() {
		return get("baseUrlNews");
	}
	
	public void setBaseUrl(String baseUrl) {
		set("baseUrl", baseUrl);
	}
	public String getBaseUrl() {
		return get("baseUrl");
	}
}
