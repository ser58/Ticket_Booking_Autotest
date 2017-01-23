package ru.inex.autotestconfig.client;

import java.util.List;

import ru.inex.autotestconfig.client.forms.CommonParameters;
import ru.inex.autotestconfig.client.forms.DataFiles;
import ru.inex.autotestconfig.client.forms.SuiteFiles;
import ru.inex.autotestconfig.client.forms.cntSuiteFileEdit;
import ru.inex.autotestconfig.client.forms.dlgShowTestng;
import ru.inex.autotestconfig.client.forms.datafileeditforms.TicketDataFile;
import ru.inex.autotestconfig.modeldata.datafiles.DataFile;
import ru.inex.autotestconfig.modeldata.testng.TestngSuiteFiles;
import ru.inex.autotestconfig.utilities.Utilities;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.Direction;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.fx.FxConfig;
import com.extjs.gxt.ui.client.state.StateManager;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class AutotestConfig implements EntryPoint {

	private final ConfigServiceAsync configService = GWT.create(ConfigService.class);
	private static Button btnLoad;
	private static Button btnSave;
	private static Button btnShow;
	public static String testngText = new String();
	public static TextField<String> txtProjectRoot;
	public static TabPanel tabPanel;
	public static TabItem tabTestNG;
	public static TabItem tabSuiteFileEdit;
	public static TabItem tabDataFileEdit;
	
	public void onModuleLoad() {
		RootPanel rootPanel = RootPanel.get();
		
		LayoutContainer layoutContainer = new LayoutContainer();
		layoutContainer.setLayout(new BorderLayout());
		
		LayoutContainer cntHeader = new LayoutContainer();
		cntHeader.setLayout(new FitLayout());
		
		LayoutContainer layoutContainer_2 = new LayoutContainer();
		layoutContainer_2.setLayout(new RowLayout(Orientation.VERTICAL));
		
		LayoutContainer layoutContainer_8 = new LayoutContainer();
		layoutContainer_8.setLayout(new CenterLayout());
		
		LabelField labelField = new LabelField("<p align=\"center\"><b><font face=\"Verdana\" size=5 color=\"gray\">Редактор тестовых данных</strong><font></b></p>");
		layoutContainer_8.add(labelField);
		labelField.setSize("400", "36px");
		layoutContainer_2.add(layoutContainer_8, new RowData(Style.DEFAULT, 55.0, new Margins()));
		
		LayoutContainer layoutContainer_7 = new LayoutContainer();
		layoutContainer_7.setLayout(new HBoxLayout());
		
		LabelField lblProjectRoot = new LabelField("Project Root");
		layoutContainer_7.add(lblProjectRoot, new HBoxLayoutData(0, 0, 0, 10));
		
		txtProjectRoot  = new TextField<String>();
		txtProjectRoot.setValue(StateManager.get().getString("ProjectRoot"));
		txtProjectRoot.addListener(Events.OnChange, new Listener<FieldEvent>() {
			public void handleEvent(FieldEvent e) {
				StateManager.get().set("ProjectRoot", txtProjectRoot.getRawValue());
			}
		});
		txtProjectRoot.setWidth("600");
		layoutContainer_7.add(txtProjectRoot, new HBoxLayoutData(1, 0, 0, 5));
		
		btnLoad = new Button("Load Testng");
		btnLoad.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				String projectRoot = txtProjectRoot.getRawValue();
				getTestngFile(projectRoot);
			}
		});
		layoutContainer_7.add(btnLoad, new HBoxLayoutData(0, 0, 0, 5));
		    
		btnSave = new Button("Save Testng");
		btnSave.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				String path = GWT.getHostPageBaseURL() + "data/data.xml";
				Window.alert(path);
			}
		});
		layoutContainer_7.add(btnSave, new HBoxLayoutData(0, 0, 0, 5));
		
		btnShow = new Button("Show Testng");
		btnShow.addListener(Events.Select, new Listener<ButtonEvent>() {
			public void handleEvent(ButtonEvent e) {
				final dlgShowTestng showTestng = new dlgShowTestng();
				
				if (showTestng.isRendered()) {
					showTestng.el().slideIn(Direction.DOWN, FxConfig.NONE);
				} else {
					showTestng.show();
					showTestng.el().slideIn(Direction.DOWN, FxConfig.NONE);
				}
			}
		});
		layoutContainer_7.add(btnShow, new HBoxLayoutData(0, 0, 0, 5));
		layoutContainer_2.add(layoutContainer_7, new RowData(Style.DEFAULT, Style.DEFAULT, new Margins(2, 0, 0, 0)));
		cntHeader.add(layoutContainer_2);
		layoutContainer_2.setHeight("61px");
		layoutContainer.add(cntHeader, new BorderLayoutData(LayoutRegion.NORTH, 95.0f));
		
		tabPanel = new TabPanel();
		
		tabTestNG = new TabItem("TestNG");
		tabTestNG.setLayout(new FitLayout());
		
		LayoutContainer layoutContainer_3 = new LayoutContainer();
		layoutContainer_3.setLayout(new BorderLayout());
		
		ContentPanel cntntpnlCommonOptions = new ContentPanel();
		cntntpnlCommonOptions.setHeading("Common Parameters");
		cntntpnlCommonOptions.setLayout(new BorderLayout());
		
		CommonParameters commonParameters = new CommonParameters();
		
		cntntpnlCommonOptions.add(commonParameters, new BorderLayoutData(LayoutRegion.NORTH, 365.0f));
		
		DataFiles cntDataFiles = new DataFiles();
		
		cntntpnlCommonOptions.add(cntDataFiles, new BorderLayoutData(LayoutRegion.CENTER));
		layoutContainer_3.add(cntntpnlCommonOptions, new BorderLayoutData(LayoutRegion.WEST, 625.0f));
		
		SuiteFiles cntSuiteFiles = new SuiteFiles();
		cntSuiteFiles.setLayout(new FitLayout());
		layoutContainer_3.add(cntSuiteFiles, new BorderLayoutData(LayoutRegion.CENTER));
		tabTestNG.add(layoutContainer_3);
		layoutContainer_3.setHeight("702px");
		tabPanel.add(tabTestNG);
		tabTestNG.setSize("1103px", "675px");
		
		tabSuiteFileEdit = new TabItem("Suite File Edit");
		tabSuiteFileEdit.setLayout(new FitLayout());
		
		cntSuiteFileEdit suiteFileEdit = new cntSuiteFileEdit();
		tabSuiteFileEdit.add(suiteFileEdit);
		
		tabPanel.add(tabSuiteFileEdit);
		
		tabDataFileEdit = new TabItem("Data File Edit");
		tabDataFileEdit.setLayout(new FitLayout());
		tabPanel.add(tabDataFileEdit);
		
		TicketDataFile ticketDataFile = new TicketDataFile();
		tabDataFileEdit.add(ticketDataFile);
		layoutContainer.add(tabPanel, new BorderLayoutData(LayoutRegion.CENTER));
	
		Viewport viewport = new Viewport();
		viewport.setLayout(new FitLayout());
		
		viewport.add(layoutContainer);
		layoutContainer.setHeight("829px");
		viewport.setSize("469px", "312px");

		rootPanel.add(viewport);
		rootPanel.setWidgetPosition(viewport, 0, 0);
	}
	
	void getTestngFile(String projectRoot) {
		configService.getTestngFile(projectRoot, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				Window.alert(caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(String result) {
				testngText = result;
				
				CommonParameters.SetParameters(Utilities.getTestngParams(result));
				
				List <DataFile> dataFiles = Utilities.getDataFiles(result);
				DataFiles.SetGridData(dataFiles);
				
				List <TestngSuiteFiles> suiteFiles = Utilities.getSuiteFiles(result);
				SuiteFiles.SetGridData(suiteFiles);
			}
		});
	}
}
