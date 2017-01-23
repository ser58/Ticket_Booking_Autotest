package ru.inex.autotestconfig.client.forms;

import java.util.ArrayList;
import java.util.List;

import ru.inex.autotestconfig.client.AutotestConfig;
import ru.inex.autotestconfig.client.forms.datafileeditforms.Passenger;
import ru.inex.autotestconfig.client.forms.datafileeditforms.TicketDataFile;
import ru.inex.autotestconfig.modeldata.suites.Parameter;
import ru.inex.autotestconfig.modeldata.suites.SuiteTestsList;
import ru.inex.autotestconfig.modeldata.suites.TestMethodsList;
import ru.inex.autotestconfig.utilities.Utilities;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Window;
import com.google.gwt.xml.client.DOMException;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class cntSuiteFileEdit extends LayoutContainer {
	static TextField<String> txtSuiteName;
	static TextField<String> txtVerbose;
	static TextField<String> txtDataFileName;
	static TextField<String> txtDataFilePath;
	static String xmlSuite;
	static TextField<String> txtClass = new TextField<String>();
	
	static TicketDataFile ticketDataFile = new TicketDataFile();
	
	private static EditorGrid<SuiteTestsList> gridTestsList;
	private static ListStore<SuiteTestsList> storeTestsList;
	
	private static EditorGrid<Parameter> gridTestParameter;
	private static ListStore<Parameter> storeTestParameter;
	
	private static EditorGrid<TestMethodsList> gridTestMethods;
	private static ListStore<TestMethodsList> storeTestMethods;
	
	public cntSuiteFileEdit() {
		/*
		addAttachHandler(new Handler() {
			public void onAttachOrDetach(AttachEvent event) {
				GridEvent<SuiteTestsList> gridEvent = new GridEvent<SuiteTestsList>(gridTestsList);
				gridEvent.setRowIndex(0);
				gridTestsList.fireEvent(Events.RowClick, gridEvent);
			}
		});*/
		
		setLayout(new BorderLayout());
		
		//*************** Заполнить грид со списком тестов ******************
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig column = new ColumnConfig();
		column.setAlignment(HorizontalAlignment.LEFT);
		column.setId("select");
		column.setWidth(20);
		configs.add(column);

		column = new ColumnConfig();
		column.setAlignment(HorizontalAlignment.LEFT);
		column.setId("name");
		column.setHeader("Test Name");
		column.setWidth(400);

		TextField<String> text = new TextField<String>();
		text.setAllowBlank(false);
		column.setEditor(new CellEditor(text));
		configs.add(column);

		CheckColumnConfig checkColumn = new CheckColumnConfig("preserveOrder", "Preserev Order", 65);
		checkColumn.setWidth(90);
		checkColumn.setAlignment(HorizontalAlignment.CENTER);
		CellEditor checkBoxEditor = new CellEditor(new CheckBox());
		checkColumn.setEditor(checkBoxEditor);
		configs.add(checkColumn);
		
		checkColumn = new CheckColumnConfig("enabled", "Enabled", 55);
		checkColumn.setAlignment(HorizontalAlignment.CENTER);
		checkBoxEditor = new CellEditor(new CheckBox());
		checkColumn.setEditor(checkBoxEditor);
		configs.add(checkColumn);

		storeTestsList = new ListStore<SuiteTestsList>();

		ColumnModel cm = new ColumnModel(configs);

		GridSelectionModel<SuiteTestsList> sm = new GridSelectionModel<SuiteTestsList>();
		sm.setSelectionMode(SelectionMode.SINGLE);

		gridTestsList = new EditorGrid<SuiteTestsList>(storeTestsList, cm);
		
		gridTestsList.addListener(Events.RowClick, new Listener<GridEvent<SuiteTestsList>>() {
			public void handleEvent(GridEvent<SuiteTestsList> e) {
				Document suiteDom = XMLParser.parse(xmlSuite);
				Node test = suiteDom.getElementsByTagName("test").item(e.getRowIndex());
				
				List <Parameter> testParameters = Utilities.getTestParameters(test);
				gridTestParameter.stopEditing();
				storeTestParameter.removeAll();
				storeTestParameter.add(testParameters);
				gridTestParameter.startEditing(0, 0);

				String testName = Utilities.getTestClassName(test);
				txtClass.setValue(testName);
				
				List <TestMethodsList> testMethods = Utilities.getTestMethods(test);
				gridTestMethods.stopEditing();
				storeTestMethods.removeAll();
				storeTestMethods.add(testMethods);
				gridTestMethods.startEditing(0, 0);
				
				//String dataFileName = txtDataFileName.getValue();
				//if (dataFileName.contains("directTrain") | dataFileName.contains("returnTrain")) {
				//	AutotestConfig.tabDataFileEdit.remove(ticketDataFile);
					AutotestConfig.tabDataFileEdit.add(ticketDataFile);
				//}
					
			}
		});
		
		gridTestsList.setSelectionModel(sm);
		gridTestsList.addPlugin(checkColumn);
		gridTestsList.setSize("82px", "178px");
		
		ContentPanel cntListOfTests = new ContentPanel();
		cntListOfTests.setHeading("List of Tests");
		cntListOfTests.setLayout(new FitLayout());
		add(cntListOfTests, new BorderLayoutData(LayoutRegion.WEST, 570.0f));
		cntListOfTests.add(gridTestsList);
		//***********************************************************************************
		
		ContentPanel cntTestParameters = new ContentPanel();
		cntTestParameters.setHeading("Test Parameters");
		cntTestParameters.setLayout(new BorderLayout());
		
		//**************** Заполнить грид с параметрами теста ******************
		List<ColumnConfig> configsTestParam = new ArrayList<ColumnConfig>();

		column = new ColumnConfig();
		column.setAlignment(HorizontalAlignment.LEFT);
		column.setId("name");
		column.setHeader("Parameter Name");
		column.setWidth(200);
		configsTestParam.add(column);
		
		column = new ColumnConfig();
		column.setAlignment(HorizontalAlignment.CENTER);
		column.setId("value");
		column.setHeader("Value");
		column.setWidth(45);

		text = new TextField<String>();
		text.setAllowBlank(false);
		column.setEditor(new CellEditor(text));
		configsTestParam.add(column);
		
		storeTestParameter = new ListStore<Parameter>();

		ColumnModel cmTestParameter = new ColumnModel(configsTestParam);

		GridSelectionModel<Parameter> smTestParameter = new GridSelectionModel<Parameter>();
		smTestParameter.setSelectionMode(SelectionMode.SINGLE);

		gridTestParameter = new EditorGrid<Parameter>(storeTestParameter, cmTestParameter);
		gridTestParameter.setSelectionModel(smTestParameter);
		gridTestParameter.addPlugin(checkColumn);
		gridTestParameter.setSize("82px", "178px");
		
		LayoutContainer layTestParams = new LayoutContainer();
		layTestParams.setLayout(new FitLayout());
		layTestParams.add(gridTestParameter);
		cntTestParameters.add(layTestParams, new BorderLayoutData(LayoutRegion.NORTH, 135.0f));
		//***********************************************************************************
		
		LayoutContainer layoutContainer_1 = new LayoutContainer();
		
		LayoutContainer layoutContainer = new LayoutContainer();
		layoutContainer.setLayout(new HBoxLayout());
		
		LabelField lblClass = new LabelField("Class");
		layoutContainer.add(lblClass, new HBoxLayoutData(5, 0, 0, 10));
		
		HBoxLayoutData hbld_txtClass = new HBoxLayoutData(5, 0, 0, 5);
		hbld_txtClass.setFlex(9.0);
		layoutContainer.add(txtClass, hbld_txtClass);
		layoutContainer_1.add(layoutContainer, new BorderLayoutData(LayoutRegion.NORTH, 35.0f));
		
		//******************* Заполнить грид со списком классов ***********************
		List<ColumnConfig> configsTestMethods = new ArrayList<ColumnConfig>();

		column = new ColumnConfig();
		column.setAlignment(HorizontalAlignment.LEFT);
		column.setId("name");
		column.setHeader("Test Method Name");
		column.setWidth(300);
		configsTestMethods.add(column);

		CheckColumnConfig check = new CheckColumnConfig("include", "Include", 55);
		check.setAlignment(HorizontalAlignment.CENTER);
		checkBoxEditor = new CellEditor(new CheckBox());
		check.setEditor(checkBoxEditor);
		configsTestMethods.add(check);
		
		storeTestMethods = new ListStore<TestMethodsList>();

		ColumnModel cmTestMethods = new ColumnModel(configsTestMethods);

		GridSelectionModel<TestMethodsList> smTestMethods = new GridSelectionModel<TestMethodsList>();
		smTestParameter.setSelectionMode(SelectionMode.SINGLE);

		gridTestMethods = new EditorGrid<TestMethodsList>(storeTestMethods, cmTestMethods);
		gridTestMethods.setSelectionModel(smTestMethods);
		gridTestMethods.addPlugin(checkColumn);
		gridTestMethods.setSize("82px", "178px");
		
		LayoutContainer layMethodsList = new LayoutContainer();
		layMethodsList.add(gridTestMethods);
		layMethodsList.setLayout(new FitLayout());
		layoutContainer_1.add(layMethodsList, new BorderLayoutData(LayoutRegion.CENTER));
		
		//*****************************************************************************
		layoutContainer_1.setLayout(new BorderLayout());
		cntTestParameters.add(layoutContainer_1, new BorderLayoutData(LayoutRegion.CENTER));
		add(cntTestParameters, new BorderLayoutData(LayoutRegion.CENTER));
		
		LayoutContainer cntName = new LayoutContainer();
		cntName.setLayout(new RowLayout(Orientation.VERTICAL));
		
		LayoutContainer layoutContainer_2 = new LayoutContainer();
		layoutContainer_2.setLayout(new HBoxLayout());
		
		LabelField lblSuiteName = new LabelField("Suite Name");
		layoutContainer_2.add(lblSuiteName, new HBoxLayoutData(5, 0, 0, 10));
		
		txtSuiteName = new TextField<String>();
		layoutContainer_2.add(txtSuiteName, new HBoxLayoutData(5, 0, 0, 5));
		txtSuiteName.setWidth("500");
		
		LabelField lblfldNewLabelfield = new LabelField("Verbose");
		layoutContainer_2.add(lblfldNewLabelfield, new HBoxLayoutData(5, 0, 0, 10));
		
		txtVerbose = new TextField<String>();
		layoutContainer_2.add(txtVerbose, new HBoxLayoutData(5, 0, 0, 5));
		txtVerbose.setWidth("30");
		cntName.add(layoutContainer_2, new RowData(Style.DEFAULT, 30.0, new Margins()));
		
		LayoutContainer layoutContainer_3 = new LayoutContainer();
		
		FieldSet fldstNewFieldset = new FieldSet();
		fldstNewFieldset.setLayout(new HBoxLayout());
		
		LabelField lblDataFileName = new LabelField("Name");
		fldstNewFieldset.add(lblDataFileName);
		
		txtDataFileName = new TextField<String>();
		fldstNewFieldset.add(txtDataFileName, new HBoxLayoutData(0, 0, 0, 5));
		txtDataFileName.setWidth("300");
		
		LabelField lblDataFilePath = new LabelField("Path");
		fldstNewFieldset.add(lblDataFilePath, new HBoxLayoutData(0, 0, 0, 5));
		
		txtDataFilePath = new TextField<String>();
		fldstNewFieldset.add(txtDataFilePath, new HBoxLayoutData(0, 0, 0, 5));
		txtDataFilePath.setWidth("500");
		
		Button btnShow = new Button("Show");
		fldstNewFieldset.add(btnShow, new HBoxLayoutData(0, 0, 0, 5));
		
		layoutContainer_3.add(fldstNewFieldset);
		fldstNewFieldset.setSize("975px", "61px");
		fldstNewFieldset.setHeading("Data File");
		layoutContainer_3.setLayout(new FitLayout());
		cntName.add(layoutContainer_3, new RowData(Style.DEFAULT, 70.0, new Margins()));
		add(cntName, new BorderLayoutData(LayoutRegion.NORTH, 100.0f));
	}

	public static void fillForm(String suiteXml) {
		xmlSuite = suiteXml;
		
		try {
			Document suiteDom = XMLParser.parse(suiteXml);

			Node suiteNode = suiteDom.getElementsByTagName("suite").item(0);

			Node str = suiteNode.getAttributes().getNamedItem("name");
			txtSuiteName.setValue(str.getNodeValue());

			str = suiteNode.getAttributes().getNamedItem("verbose");
			txtVerbose.setRawValue(str.getNodeValue());
			
			Node parameter = suiteDom.getElementsByTagName("parameter").item(0);
			str = parameter.getAttributes().getNamedItem("name");
			txtDataFileName.setValue(str.getNodeValue());
			
			str = parameter.getAttributes().getNamedItem("value");
			txtDataFilePath.setValue(str.getNodeValue());
			
			NodeList tests = suiteDom.getElementsByTagName("test");
			
			List <SuiteTestsList> testsList = Utilities.getSuiteTestsList(tests);
			gridTestsList.stopEditing();
			storeTestsList.removeAll();
			storeTestsList.add(testsList);
			gridTestsList.startEditing(0, 0);
			
			storeTestParameter.removeAll();
			storeTestMethods.removeAll();
		
		} catch (DOMException e) {
			Window.alert("Could not parse XML document.");
		}
	}
}
