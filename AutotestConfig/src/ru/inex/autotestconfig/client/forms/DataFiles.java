package ru.inex.autotestconfig.client.forms;

import java.util.ArrayList;
import java.util.List;

import ru.inex.autotestconfig.modeldata.datafiles.DataFile;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;

public class DataFiles extends LayoutContainer {
	public DataFiles() {
		
	}

	private static EditorGrid<DataFile> grid;
	private static ContentPanel cp;
	private static ListStore<DataFile> store;
	private ColumnConfig column_1;

	protected DataFile createNew() {
		DataFile dataFile = new DataFile();
		dataFile.setName("new_DataFile");
		dataFile.setValue("/src/test/ru/oooinex/data/new_DatFile.json");
		return dataFile;
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);

		setLayout(new FitLayout());

		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig column = new ColumnConfig();
		column.setRowHeader(true);
		column.setMenuDisabled(true);
		column.setAlignment(HorizontalAlignment.CENTER);
		column.setId("select");
		//column.setHeader("");
		column.setWidth(20);
		configs.add(column);
		
		column = new ColumnConfig();
		column.setAlignment(HorizontalAlignment.LEFT);
		column.setId("name");
		column.setHeader("Valiable Name");
		column.setWidth(160);

		TextField<String> text = new TextField<String>();
		text.setAllowBlank(false);
		column.setEditor(new CellEditor(text));
		configs.add(column);

		column_1 = new ColumnConfig();
		column_1.setAlignment(HorizontalAlignment.LEFT);
		column_1.setId("value");
		column_1.setHeader("Data File Name");
		column_1.setWidth(400);

		text = new TextField<String>();
		text.setAllowBlank(false);
		column_1.setEditor(new CellEditor(text));
		configs.add(column_1);

		store = new ListStore<DataFile>();

		ColumnModel cm = new ColumnModel(configs);
		
		GridSelectionModel<DataFile> sm = new GridSelectionModel<DataFile>();
		sm.setSelectionMode(SelectionMode.SINGLE);
		
		cp = new ContentPanel();
		cp.setScrollMode(Scroll.AUTO);
		cp.setButtonAlign(HorizontalAlignment.CENTER);
		cp.setHeading("Common Data Files");
		cp.setWidth(630);
		cp.setLayout(new FitLayout());

		grid = new EditorGrid<DataFile>(store, cm);
		grid.setSelectionModel(sm);
		grid.setStripeRows(true);
		grid.setAutoExpandColumn("name");
		grid.setBorders(false);
		grid.setWidth("607px");
		cp.add(grid);

		cp.addButton(new Button("Add", new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				grid.stopEditing();
				store.insert(createNew(), 0);
				grid.startEditing(0, 0);
			}
		}));
		
		cp.addButton(new Button("Reset", new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				store.rejectChanges();
			}
		}));

		cp.addButton(new Button("Save", new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				store.commitChanges();
			}
		}));

		add(cp);
		cp.setWidth("585px");
	}
	
	public static void SetGridData(List <DataFile> dataFiles) {
		grid.stopEditing();
		store.removeAll();
		store.add(dataFiles);
		grid.startEditing(0, 0);
	}
}