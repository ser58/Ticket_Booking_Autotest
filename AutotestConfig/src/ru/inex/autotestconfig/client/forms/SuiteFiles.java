package ru.inex.autotestconfig.client.forms;

import java.util.ArrayList;
import java.util.List;

import ru.inex.autotestconfig.client.AutotestConfig;
import ru.inex.autotestconfig.client.ConfigService;
import ru.inex.autotestconfig.client.ConfigServiceAsync;
import ru.inex.autotestconfig.modeldata.testng.TestngSuiteFiles;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SuiteFiles extends LayoutContainer {
	private final ConfigServiceAsync configService = GWT.create(ConfigService.class);

	public SuiteFiles() {

	}

	private static EditorGrid<TestngSuiteFiles> grid;
	private static ListStore<TestngSuiteFiles> store;

	private ContentPanel cp;

	protected TestngSuiteFiles createSuite() {
		TestngSuiteFiles suiteFiles = new TestngSuiteFiles();
		suiteFiles.setPath("src/suites/NewSuiteFile.xml");
		suiteFiles.setEnabled(true);
		return suiteFiles;
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);

		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig column = new ColumnConfig();
		column.setAlignment(HorizontalAlignment.LEFT);
		column.setId("select");
		// column.setHeader("*");
		column.setWidth(20);
		configs.add(column);

		column = new ColumnConfig();
		column.setAlignment(HorizontalAlignment.LEFT);
		column.setId("path");
		column.setHeader("Suite File");
		column.setWidth(400);

		TextField<String> text = new TextField<String>();
		text.setAllowBlank(false);
		column.setEditor(new CellEditor(text));
		configs.add(column);

		CheckColumnConfig checkColumn = new CheckColumnConfig("enabled", "Enabled?", 55);
		checkColumn.setAlignment(HorizontalAlignment.CENTER);
		CellEditor checkBoxEditor = new CellEditor(new CheckBox());
		checkColumn.setEditor(checkBoxEditor);
		configs.add(checkColumn);

		store = new ListStore<TestngSuiteFiles>();

		ColumnModel cm = new ColumnModel(configs);

		GridSelectionModel<TestngSuiteFiles> sm = new GridSelectionModel<TestngSuiteFiles>();
		sm.setSelectionMode(SelectionMode.SINGLE);

		cp = new ContentPanel();
		cp.setScrollMode(Scroll.AUTO);
		cp.setButtonAlign(HorizontalAlignment.CENTER);
		cp.setHeading("Suite Files");
		cp.setWidth(455);
		cp.setLayout(new FitLayout());

		grid = new EditorGrid<TestngSuiteFiles>(store, cm);
		grid.addListener(Events.RowClick, new Listener<GridEvent<TestngSuiteFiles>>() {
					public void handleEvent(GridEvent<TestngSuiteFiles> e) {
						String projectRoot = AutotestConfig.txtProjectRoot.getValue();
						String suiteXML = projectRoot + "/"	+ e.getModel().get("path");

						getSuiteData(suiteXML);
					}
				});
		grid.setSelectionModel(sm);
		grid.setAutoExpandColumn("path");
		setLayout(new FitLayout());
		grid.addPlugin(checkColumn);

		cp.add(grid);
		grid.setSize("461px", "178px");

		add(cp);
	}

	void getSuiteData(String projectRoot) {
		configService.getSuiteXML(projectRoot, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				Window.alert(caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(String result) {
				cntSuiteFileEdit.fillForm(result);
				AutotestConfig.tabPanel.setSelection(AutotestConfig.tabSuiteFileEdit);
			}
		});
	}

	public static void SetGridData(List<TestngSuiteFiles> dataFiles) {
		grid.stopEditing();
		store.removeAll();
		store.add(dataFiles);
		grid.startEditing(0, 0);
	}
}