package ru.inex.autotestconfig.client.forms;

import java.util.Map;

import ru.inex.autotestconfig.modeldata.testng.TestngParams;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.state.StateManager;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;

public class CommonParameters extends LayoutContainer {
	static SimpleComboBox<String> cmbBrowser;
	static CheckBox chkHoldBrowser;
	static TextField<String> txtProfile;
	static TextField<String> txtReportsFolder;
	static TextField<String> txtReportsURL;
	static NumberField nmbDefaultTimeout;
	static CheckBox chkNewVersion;
	static CheckBox chkPaymentGatewayPlug;
	static SimpleComboBox<String> cmbBaseURL;
	static SimpleComboBox<String> cmbBaseUrlPortal;
	static SimpleComboBox<String> cmbBaseUrlNews;
	static TextField<String> txtLogin;
	static TextField<String> txtPassword;
	
	public CommonParameters() {
		TableLayout tl_layoutContainer_4 = new TableLayout(2);
		tl_layoutContainer_4.setCellHorizontalAlign(HorizontalAlignment.RIGHT);
		tl_layoutContainer_4.setCellPadding(3);
		setLayout(tl_layoutContainer_4);
		
		LabelField lblBaseURL = new LabelField("Base Url");
		add(lblBaseURL);
		
		cmbBaseURL = new SimpleComboBox<String>() {
			@Override
			protected void applyState(Map<String, Object> state) {
				setSimpleValue((String) state.get("BaseURL"));
			}
		};

		cmbBaseURL.addListener(Events.Select, new Listener<FieldEvent>() {
			public void handleEvent(FieldEvent e) {
				cmbBaseURL.getState().put("BaseURL", cmbBaseURL.getRawValue());
				cmbBaseURL.saveState();
			}
		});

		cmbBaseURL.add("https://pass.alpha.oooinex.ru");
		cmbBaseURL.add("https://pass.beta.oooinex.ru");
		cmbBaseURL.add("https://pass.gamma.oooinex.ru");
		cmbBaseURL.setSimpleValue("https://pass.gamma.oooinex.ru");
		cmbBaseURL.setTriggerAction(TriggerAction.ALL);
		cmbBaseURL.setStateful(true);
		cmbBaseURL.setStateId("BaseURL");
		cmbBaseURL.setWidth("470");
		
		TableData td_cmbBaseURL = new TableData();
		td_cmbBaseURL.setHorizontalAlign(HorizontalAlignment.LEFT);
		add(cmbBaseURL, td_cmbBaseURL);
				
		LabelField lblBaseURLPortal = new LabelField("Base Url Portal");
		add(lblBaseURLPortal);
		
		cmbBaseUrlPortal = new SimpleComboBox<String>() {
			@Override
			protected void applyState(Map<String, Object> state) {
				setSimpleValue((String) state.get("BaseUrlPortal"));
			}
		};

		cmbBaseUrlPortal.addListener(Events.Select, new Listener<FieldEvent>() {
			public void handleEvent(FieldEvent e) {
				cmbBaseUrlPortal.getState().put("BaseUrlPortal", cmbBaseUrlPortal.getRawValue());
				cmbBaseUrlPortal.saveState();
			}
		});

		cmbBaseUrlPortal.add("https://172.22.2.232/wps/myportal/");
		cmbBaseUrlPortal.add("https://172.22.2.235/wps/myportal/");
		cmbBaseUrlPortal.add("https://172.22.2.238/wps/myportal/");
		cmbBaseUrlPortal.setSimpleValue("https://172.22.2.238/wps/myportal/");
		cmbBaseUrlPortal.setTriggerAction(TriggerAction.ALL);
		cmbBaseUrlPortal.setStateful(true);
		cmbBaseUrlPortal.setStateId("BaseUrlPortal");
		add(cmbBaseUrlPortal);
		cmbBaseUrlPortal.setWidth("470");
		
		LabelField lblBaseURLNews = new LabelField("Base Url News");
		add(lblBaseURLNews);
		
		cmbBaseUrlNews = new SimpleComboBox<String>() {
			@Override
			protected void applyState(Map<String, Object> state) {
				setSimpleValue((String) state.get("BaseUrlNews"));
			}
		};

		cmbBaseUrlNews.addListener(Events.Select, new Listener<FieldEvent>() {
			public void handleEvent(FieldEvent e) {
				cmbBaseUrlNews.getState().put("BaseUrlNews", cmbBaseUrlNews.getRawValue());
				cmbBaseUrlNews.saveState();
			}
		});

		cmbBaseUrlNews.add("http://press.alpha.oooinex.ru/news/public/ru");
		cmbBaseUrlNews.add("http://press.beta.oooinex.ru/news/public/ru");
		cmbBaseUrlNews.add("http://press.gamma.oooinex.ru/news/public/ru");
		cmbBaseUrlNews.setSimpleValue("http://press.gamma.oooinex.ru/news/public/ru");
		cmbBaseUrlNews.setTriggerAction(TriggerAction.ALL);
		cmbBaseUrlNews.setStateful(true);
		cmbBaseUrlNews.setStateId("BaseUrlNews");
		add(cmbBaseUrlNews);
		cmbBaseUrlNews.setWidth("470");
		
		LabelField lblBrowser = new LabelField("Browser");
		add(lblBrowser);

		cmbBrowser = new SimpleComboBox<String>() {
			@Override
			protected void applyState(Map<String, Object> state) {
				setSimpleValue((String) state.get("Browser"));
			}
		};

		cmbBrowser.addListener(Events.Select, new Listener<FieldEvent>() {
			public void handleEvent(FieldEvent e) {
				cmbBrowser.getState().put("Browser", cmbBrowser.getRawValue());
				cmbBrowser.saveState();
			}
		});

		cmbBrowser.add("chrome");
		cmbBrowser.add("firefox");
		cmbBrowser.add("ie");
		cmbBrowser.add("htmlunit");
		cmbBrowser.add("phantomjs");
		cmbBrowser.add("opera");
		cmbBrowser.add("marionette");
		cmbBrowser.setTypeAheadDelay(100);
		cmbBrowser.setMinListWidth(40);
		cmbBrowser.setSimpleValue("firefox");
		cmbBrowser.setTriggerAction(TriggerAction.ALL);
		cmbBrowser.setSize("100", "22px");
		cmbBrowser.setStateful(true);
		cmbBrowser.setStateId("Browser");
		
		TableData td_cmbBrowser = new TableData();
		td_cmbBrowser.setHorizontalAlign(HorizontalAlignment.LEFT);
		add(cmbBrowser, td_cmbBrowser);
		
		LabelField lblHoldBrowser = new LabelField("Hold Browser");
		add(lblHoldBrowser);
		
		chkHoldBrowser = new CheckBox();
		chkHoldBrowser.setHideLabel(true);
		chkHoldBrowser.setValue((Boolean) StateManager.get().get("HoldBrowser"));
		chkHoldBrowser.addListener(Events.OnChange, new Listener<FieldEvent>() {
			public void handleEvent(FieldEvent e) {
				StateManager.get().set("HoldBrowser", chkHoldBrowser.getValue());
			}
		});
		chkHoldBrowser.setSize("16px", "16px");
		TableData td_chkHoldBrowser = new TableData();
		td_chkHoldBrowser.setHorizontalAlign(HorizontalAlignment.LEFT);
		add(chkHoldBrowser, td_chkHoldBrowser);
		
		LabelField lblProfile = new LabelField("Profile");
		add(lblProfile);

		txtProfile = new TextField<String>();
		txtProfile.setValue(StateManager.get().getString("Profile"));
		txtProfile.addListener(Events.OnChange, new Listener<FieldEvent>() {
			public void handleEvent(FieldEvent e) {
				StateManager.get().set("Profile", txtProfile.getRawValue());
			}
		});
		txtProfile.setWidth("470");
		add(txtProfile);
		
		LabelField lblReportsFolder = new LabelField("Reports Folder");
		add(lblReportsFolder);
		
		txtReportsFolder = new TextField<String>();
		txtReportsFolder.setValue(StateManager.get().getString("ReportsFolder"));
		txtReportsFolder.addListener(Events.OnChange, new Listener<FieldEvent>() {
			public void handleEvent(FieldEvent e) {
				StateManager.get().set("ReportsFolder", txtReportsFolder.getRawValue());
			}
		});
		txtReportsFolder.setWidth("470");
		add(txtReportsFolder);
		
		LabelField lblReportsURL = new LabelField("Reports Url");
		add(lblReportsURL);
		
		txtReportsURL = new TextField<String>();
		txtReportsURL.setValue(StateManager.get().getString("ReportsURL"));
		txtReportsURL.addListener(Events.OnChange, new Listener<FieldEvent>() {
			public void handleEvent(FieldEvent e) {
				StateManager.get().set("ReportsURL", txtReportsURL.getRawValue());
			}
		});
		txtReportsURL.setWidth("470");
		add(txtReportsURL);
		
		LabelField lblDefaultTimeout = new LabelField("Default Timeout");
		add(lblDefaultTimeout);
		lblDefaultTimeout.setWidth("107px");
		
		nmbDefaultTimeout = new NumberField() {
			@Override
			protected void applyState(Map<String, Object> state) {
				String tmp = ((String) state.get("DefaultTimeout"));
				if (tmp != null) setValue(Double.valueOf(tmp).longValue());
			}
		};

		nmbDefaultTimeout.addListener(Events.Change, new Listener<FieldEvent>() {
			public void handleEvent(FieldEvent e) {
				nmbDefaultTimeout.getState().put("DefaultTimeout", nmbDefaultTimeout.getValue());
				nmbDefaultTimeout.saveState();
			}
		});
		nmbDefaultTimeout.setStateful(true);
		nmbDefaultTimeout.setStateId("DefaultTimeout");
		nmbDefaultTimeout.setWidth("100");
		TableData td_nmbDefaultTimeout = new TableData();
		td_nmbDefaultTimeout.setHorizontalAlign(HorizontalAlignment.LEFT);
		add(nmbDefaultTimeout, td_nmbDefaultTimeout);
		
		LabelField lblNewVersion = new LabelField("New Version");
		add(lblNewVersion);
		
		chkNewVersion = new CheckBox();
		chkNewVersion.setHideLabel(true);
		chkNewVersion.setValue((Boolean) StateManager.get().get("NewVersion"));
		chkNewVersion.addListener(Events.OnChange, new Listener<FieldEvent>() {
			public void handleEvent(FieldEvent e) {
				StateManager.get().set("NewVersion", chkNewVersion.getValue());
			}
		});
		chkNewVersion.setHideLabel(true);
		chkNewVersion.setSize("16px", "16px");
		TableData td_chkNewVersion = new TableData();
		td_chkNewVersion.setVerticalAlign(VerticalAlignment.MIDDLE);
		td_chkNewVersion.setHorizontalAlign(HorizontalAlignment.LEFT);
		add(chkNewVersion, td_chkNewVersion);
		
		LabelField lblPaymentGatewayPlug = new LabelField("Payment Gateway Plug");
		add(lblPaymentGatewayPlug);
		lblPaymentGatewayPlug.setWidth("137px");
		
		chkPaymentGatewayPlug = new CheckBox();
		chkPaymentGatewayPlug.setHideLabel(true);
		chkPaymentGatewayPlug.setValue((Boolean) StateManager.get().get("PaymentGatewayPlug"));
		chkPaymentGatewayPlug.addListener(Events.OnChange, new Listener<FieldEvent>() {
			public void handleEvent(FieldEvent e) {
				StateManager.get().set("PaymentGatewayPlug", chkPaymentGatewayPlug.getValue());
			}
		});
		chkPaymentGatewayPlug.setHideLabel(true);
		chkPaymentGatewayPlug.setSize("16px", "16px");
		TableData td_chkPaymentGatewayPlug = new TableData();
		td_chkPaymentGatewayPlug.setHorizontalAlign(HorizontalAlignment.LEFT);
		add(chkPaymentGatewayPlug, td_chkPaymentGatewayPlug);
		
		LabelField lbllogin;
		lbllogin = new LabelField("Login");
		add(lbllogin);
		
		txtLogin = new TextField<String>();
		TableData td_txtLogin = new TableData();
		td_txtLogin.setHorizontalAlign(HorizontalAlignment.LEFT);
		add(txtLogin, td_txtLogin);
		txtLogin.setFieldLabel("tstLogin");
		
		LabelField lblPassword;
		lblPassword = new LabelField("Password");
		add(lblPassword);
		
		txtPassword = new TextField<String>();
		txtPassword.setPassword(true);
		TableData td_txtPassword = new TableData();
		td_txtPassword.setHorizontalAlign(HorizontalAlignment.LEFT);
		add(txtPassword, td_txtPassword);
		txtPassword.setFieldLabel("txtPassword");
	}
	
	public static void SetParameters(TestngParams testngParams) {
		cmbBrowser.setSimpleValue(testngParams.getBrowser());
		chkHoldBrowser.setValue(testngParams.getHoldBrowser().contains("true") ? true : false );
		txtProfile.setValue(testngParams.getProfile());
		txtReportsFolder.setValue(testngParams.getReportsFolder());
		txtReportsURL.setValue(testngParams.getReportsUrl());
		nmbDefaultTimeout.setValue(Integer.parseInt(testngParams.getTimeout()));
		chkNewVersion.setValue(testngParams.getNewVersion().contains("true") ? true : false );
		chkPaymentGatewayPlug.setValue(testngParams.getPaymentGatewayPlug().contains("true") ? true : false );
		cmbBaseURL.setSimpleValue(testngParams.getBaseUrl());
		cmbBaseUrlPortal.setSimpleValue(testngParams.getBaseUrlPortal());
		cmbBaseUrlNews.setSimpleValue(testngParams.getBaseUrlNews());
		txtLogin.setValue(testngParams.getLogin());
		txtPassword.setValue(testngParams.getPassword());
	}
}
