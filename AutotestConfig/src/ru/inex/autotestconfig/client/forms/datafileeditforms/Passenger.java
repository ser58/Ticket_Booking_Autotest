package ru.inex.autotestconfig.client.forms.datafileeditforms;

import ru.inex.autotestconfig.modeldata.trainmodels.Country;
import ru.inex.autotestconfig.modeldata.trainmodels.TestData;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;

public class Passenger extends TabItem {
	public Passenger() {
		setLayout(new TableLayout(2));
		
		LabelField lblId = new LabelField("ID");
		TableData td_lblId = new TableData();
		td_lblId.setHorizontalAlign(HorizontalAlignment.RIGHT);
		td_lblId.setPadding(5);
		add(lblId, td_lblId);
		
		NumberField nmbId = new NumberField();
		TableData td_nmbId = new TableData();
		td_nmbId.setHorizontalAlign(HorizontalAlignment.LEFT);
		add(nmbId, td_nmbId);
		nmbId.setWidth("50");
		
		LabelField lblLastName = new LabelField("Last Name");
		TableData td_lblLastName = new TableData();
		td_lblLastName.setHorizontalAlign(HorizontalAlignment.RIGHT);
		td_lblLastName.setPadding(5);
		add(lblLastName, td_lblLastName);
		
		TextField<String> txtLastName = new TextField<String>();
		add(txtLastName);
		
		LabelField lblFirstName = new LabelField("First Name");
		TableData td_lblFirstName = new TableData();
		td_lblFirstName.setHorizontalAlign(HorizontalAlignment.RIGHT);
		td_lblFirstName.setPadding(5);
		add(lblFirstName, td_lblFirstName);
		
		TextField<String> txtFirstName = new TextField<String>();
		add(txtFirstName);
		txtFirstName.setFieldLabel("FirstName");
		
		LabelField lblMiddleName = new LabelField("Middle Name");
		TableData td_lblMiddleName = new TableData();
		td_lblMiddleName.setHorizontalAlign(HorizontalAlignment.RIGHT);
		td_lblMiddleName.setPadding(5);
		add(lblMiddleName, td_lblMiddleName);
		
		TextField<String> txtMiddleName = new TextField<String>();
		add(txtMiddleName);
		txtMiddleName.setFieldLabel("MiddleName");
		
		LabelField lblDocumentType = new LabelField("Document Type");
		TableData td_lblDocumentType = new TableData();
		td_lblDocumentType.setHorizontalAlign(HorizontalAlignment.RIGHT);
		td_lblDocumentType.setPadding(5);
		add(lblDocumentType, td_lblDocumentType);
		
		SimpleComboBox<String> cmbDocType = new SimpleComboBox<String>();
		cmbDocType.add("Паспорт РФ");
		cmbDocType.add("Паспорт формы СССР");
		cmbDocType.add("Заграничный паспорт");
		cmbDocType.add("Иностранный документ");
		cmbDocType.add("Паспорт моряка");
		cmbDocType.add("Свидетельство о рождении");
		cmbDocType.add("Военный билет");
		
		cmbDocType.setTriggerAction(TriggerAction.ALL);
		cmbDocType.setSimpleValue("Паспорт РФ");
		
		add(cmbDocType);
		cmbDocType.setWidth("200");
		
		LabelField lblDocumentNumber = new LabelField("Document Number");
		TableData td_lblDocumentNumber = new TableData();
		td_lblDocumentNumber.setHorizontalAlign(HorizontalAlignment.RIGHT);
		td_lblDocumentNumber.setPadding(5);
		add(lblDocumentNumber, td_lblDocumentNumber);
		
		TextField<String> txtDocNumber = new TextField<String>();
		add(txtDocNumber);
		txtDocNumber.setFieldLabel("DocNumber");
		
		LabelField lblTariff = new LabelField("Tariff");
		TableData td_lblTariff = new TableData();
		td_lblTariff.setPadding(5);
		td_lblTariff.setHorizontalAlign(HorizontalAlignment.RIGHT);
		add(lblTariff, td_lblTariff);
		
		SimpleComboBox<String> cmbTariff = new SimpleComboBox<String>();
		cmbTariff.add("Полный");
		cmbTariff.add("Детский");
		cmbTariff.add("Детский без места");
		cmbTariff.add("JUNIOR (от 12 до 26 лет)");
		cmbTariff.add("SENIOR (от 60 лет)");
		
		cmbTariff.setTriggerAction(TriggerAction.ALL);
		cmbTariff.setSimpleValue("Полный");
		
		add(cmbTariff);
		cmbTariff.setWidth("200");
		
		LabelField lblCitizenship = new LabelField("Citizenship");
		TableData td_lblCitizenship = new TableData();
		td_lblCitizenship.setHorizontalAlign(HorizontalAlignment.RIGHT);
		td_lblCitizenship.setPadding(5);
		add(lblCitizenship, td_lblCitizenship);
		
		ComboBox<Country> cmbCitizenship = new ComboBox<Country>();
		ListStore<Country> country;
		country = new ListStore<Country>();
		country.add(TestData.getCountries());
		country.setSortDir(SortDir.ASC);
		
		cmbCitizenship.setStateful(true);
		cmbCitizenship.setStateId("Country");
		cmbCitizenship.setTriggerAction(TriggerAction.ALL);
		cmbCitizenship.setEmptyText("Select a Country...");
		cmbCitizenship.setDisplayField("name");
		cmbCitizenship.setStore(country);
		
		cmbCitizenship.setTriggerAction(TriggerAction.ALL);
		cmbCitizenship.setForceSelection(true);
		
		add(cmbCitizenship);
		
		LabelField lblGender = new LabelField("Gender");
		TableData td_lblGender = new TableData();
		td_lblGender.setHorizontalAlign(HorizontalAlignment.RIGHT);
		td_lblGender.setPadding(5);
		add(lblGender, td_lblGender);
		
		SimpleComboBox<String> cmbGender = new SimpleComboBox<String>();
		cmbGender.add("Не выбрано");
		cmbGender.add("Женский");
		cmbGender.add("Мужской");
		
		cmbGender.setTriggerAction(TriggerAction.ALL);
		cmbGender.setSimpleValue("Не выбрано");
		
		add(cmbGender);
		
		LabelField lblDateOfBirth = new LabelField("DateOfBirth");
		TableData td_lblDateOfBirth = new TableData();
		td_lblDateOfBirth.setHorizontalAlign(HorizontalAlignment.RIGHT);
		td_lblDateOfBirth.setPadding(5);
		add(lblDateOfBirth, td_lblDateOfBirth);
		
		DateField dtDateOfBirth = new DateField();
		add(dtDateOfBirth);
		dtDateOfBirth.setFieldLabel("DateOfBirth");
		
		LabelField lblInsurance = new LabelField("Insurance");
		TableData td_lblInsurance = new TableData();
		td_lblInsurance.setHorizontalAlign(HorizontalAlignment.RIGHT);
		td_lblInsurance.setPadding(5);
		add(lblInsurance, td_lblInsurance);
		
		SimpleComboBox<String> cmbInsurance = new SimpleComboBox<String>();
		cmbInsurance.add("None");
		cmbInsurance.add("ОАО «ЖАСО»");
		cmbInsurance.add("ООО «СО «Сургутнефтегаз»");
		cmbInsurance.add("ООО «Росгосстрах»");
		cmbInsurance.add("ООО «Группа Ренессанс Страхование»");

		cmbInsurance.setTriggerAction(TriggerAction.ALL);
		cmbInsurance.setSimpleValue("None");
		
		add(cmbInsurance);
		cmbInsurance.setWidth("200");
		cmbInsurance.setFieldLabel("Insurance");
		
		LabelField lblPhoneNumber = new LabelField("Phone Number");
		TableData td_lblPhoneNumber = new TableData();
		td_lblPhoneNumber.setPadding(5);
		add(lblPhoneNumber, td_lblPhoneNumber);
		
		TextField<String> txtPhoneNumber = new TextField<String>();
		add(txtPhoneNumber);
	
	}
}
