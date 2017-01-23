package ru.inex.autotestconfig.client.forms.datafileeditforms;

import ru.inex.autotestconfig.resources.Resources;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.BoxLayout.BoxLayoutPack;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.google.gwt.user.client.Window;

public class TicketDataFile extends LayoutContainer {

	public static Passenger tbitmPassenger = new Passenger();
	public static TabPanel tabPanel = new TabPanel();
	
	public TicketDataFile() {
		setLayout(new FitLayout());
		
		ContentPanel cntTicketData = new ContentPanel();
		cntTicketData.setHeading("Ticket Data File Edit");
		cntTicketData.setLayout(new BorderLayout());
		
		LayoutContainer layCommonParameters = new LayoutContainer();
		layCommonParameters.setLayout(new FitLayout());
		
		LayoutContainer layoutContainer = new LayoutContainer();
		layoutContainer.setLayout(new RowLayout(Orientation.VERTICAL));
		
		LayoutContainer layoutContainer_1 = new LayoutContainer();
		layoutContainer_1.setLayout(new HBoxLayout());
		
		LabelField lblId = new LabelField("Train ID");
		layoutContainer_1.add(lblId, new HBoxLayoutData(0, 0, 0, 10));
		
		NumberField nmbID = new NumberField();
		layoutContainer_1.add(nmbID, new HBoxLayoutData(0, 0, 0, 5));
		nmbID.setWidth("50");
		
		LabelField lblDescription = new LabelField("Description");
		layoutContainer_1.add(lblDescription, new HBoxLayoutData(0, 0, 0, 5));
		
		TextField<String> txtDescription = new TextField<String>();
		layoutContainer_1.add(txtDescription, new HBoxLayoutData(0, 0, 0, 5));
		txtDescription.setWidth("700");
		layoutContainer.add(layoutContainer_1, new RowData(Style.DEFAULT, Style.DEFAULT, new Margins(10, 0, 0, 0)));
		layoutContainer_1.setHeight("24");
		
		LayoutContainer layoutContainer_2 = new LayoutContainer();
		layoutContainer_2.setLayout(new HBoxLayout());
		
		LabelField lblFrom = new LabelField("From");
		layoutContainer_2.add(lblFrom, new HBoxLayoutData(0, 0, 0, 10));
		lblFrom.setWidth("46");
		
		TextField<String> txtFrom = new TextField<String>();
		layoutContainer_2.add(txtFrom, new HBoxLayoutData(0, 0, 0, 5));
		txtFrom.setWidth("300");
		txtFrom.setFieldLabel("From");
		
		LabelField lblTo = new LabelField("To");
		layoutContainer_2.add(lblTo, new HBoxLayoutData(0, 0, 0, 5));
		
		TextField<String> txtTo = new TextField<String>();
		layoutContainer_2.add(txtTo, new HBoxLayoutData(0, 0, 0, 5));
		txtTo.setWidth("300");
		layoutContainer.add(layoutContainer_2, new RowData(Style.DEFAULT, Style.DEFAULT, new Margins(5, 0, 0, 0)));
		layoutContainer_2.setHeight("24");
		
		LayoutContainer layoutContainer_3 = new LayoutContainer();
		layoutContainer_3.setLayout(new HBoxLayout());
		
		LabelField lblDayOfWeek = new LabelField("Day Of Week");
		layoutContainer_3.add(lblDayOfWeek, new HBoxLayoutData(0, 0, 0, 10));
		
		RadioGroup grpDayofweek = new RadioGroup();
		grpDayofweek.setBorders(true);
		
		Radio rdMonday = new Radio();
		grpDayofweek.add(rdMonday);
		rdMonday.setSize("99px", "18px");
		rdMonday.setBoxLabel("Понедельник");
		rdMonday.setHideLabel(true);
		
		Radio rdTuesday = new Radio();
		grpDayofweek.add(rdTuesday);
		rdTuesday.setSize("71px", "17px");
		rdTuesday.setBoxLabel("Вторник");
		rdTuesday.setHideLabel(true);
		
		Radio rdWednesday = new Radio();
		grpDayofweek.add(rdWednesday);
		rdWednesday.setSize("56px", "17px");
		rdWednesday.setBoxLabel("Среда");
		rdWednesday.setHideLabel(true);
		
		Radio rdThursday = new Radio();
		grpDayofweek.add(rdThursday);
		rdThursday.setSize("69px", "18px");
		rdThursday.setBoxLabel("Четверг");
		rdThursday.setHideLabel(true);
		
		Radio rdFriday = new Radio();
		grpDayofweek.add(rdFriday);
		rdFriday.setHeight("18px");
		rdFriday.setBoxLabel("Пятница");
		rdFriday.setHideLabel(true);
		
		Radio rdSaturday = new Radio();
		grpDayofweek.add(rdSaturday);
		rdSaturday.setSize("67px", "18px");
		rdSaturday.setBoxLabel("Суббота");
		rdSaturday.setHideLabel(true);
		
		Radio rdSunday = new Radio();
		grpDayofweek.add(rdSunday);
		rdSunday.setSize("94px", "18px");
		rdSunday.setBoxLabel("Воскресенье");
		rdSunday.setHideLabel(true);
		layoutContainer_3.add(grpDayofweek, new HBoxLayoutData(0, 0, 0, 5));
		grpDayofweek.setFieldLabel("DayOfWeek");
		
		LabelField lblWeek = new LabelField("+ Week");
		layoutContainer_3.add(lblWeek, new HBoxLayoutData(0, 0, 0, 10));
		
		NumberField nmbWeek = new NumberField();
		nmbWeek.setMaxLength(2);
		layoutContainer_3.add(nmbWeek, new HBoxLayoutData(0, 0, 0, 5));
		nmbWeek.setWidth("40");
		nmbWeek.setFieldLabel("Week");
		nmbWeek.setMaxValue(10);
		
		layoutContainer.add(layoutContainer_3, new RowData(Style.DEFAULT, Style.DEFAULT, new Margins(5, 0, 0, 0)));
		layoutContainer_3.setHeight("24");
		
		LayoutContainer layoutContainer_4 = new LayoutContainer();
		layoutContainer_4.setLayout(new HBoxLayout());
		
		FieldSet fldstNewFieldset = new FieldSet();
		fldstNewFieldset.setLayout(new HBoxLayout());
		
		LabelField lblTimeFrom = new LabelField("From");
		fldstNewFieldset.add(lblTimeFrom);
		
		NumberField nmbTimeFrom = new NumberField();
		nmbTimeFrom.setMaxLength(2);
		fldstNewFieldset.add(nmbTimeFrom, new HBoxLayoutData(0, 0, 0, 5));
		nmbTimeFrom.setWidth("40");
		nmbTimeFrom.setMaxValue(24);
		
		LabelField lblTimeTo = new LabelField("To");
		fldstNewFieldset.add(lblTimeTo, new HBoxLayoutData(0, 0, 0, 5));
		
		NumberField nmbTimeTo = new NumberField();
		nmbTimeTo.setMaxLength(2);
		fldstNewFieldset.add(nmbTimeTo, new HBoxLayoutData(0, 0, 0, 5));
		nmbTimeTo.setWidth("40");
		nmbTimeTo.setMaxValue(24);
		
		layoutContainer_4.add(fldstNewFieldset);
		fldstNewFieldset.setWidth("170");
		fldstNewFieldset.setHeading("Departure Time");
		
		CheckBox chkOnlyWithTickets = new CheckBox();
		layoutContainer_4.add(chkOnlyWithTickets, new HBoxLayoutData(20, 0, 0, 10));
		chkOnlyWithTickets.setBoxLabel("Only With Tickets");
		chkOnlyWithTickets.setHideLabel(true);
		
		CheckBox chkLocalTrain = new CheckBox();
		layoutContainer_4.add(chkLocalTrain, new HBoxLayoutData(20, 0, 0, 5));
		chkLocalTrain.setBoxLabel("Local Train");
		chkLocalTrain.setHideLabel(true);
		
		CheckBox chkReturn = new CheckBox();
		layoutContainer_4.add(chkReturn, new HBoxLayoutData(20, 0, 0, 5));
		chkReturn.setBoxLabel("Return");
		chkReturn.setHideLabel(true);
		layoutContainer.add(layoutContainer_4, new RowData(Style.DEFAULT, 65.0, new Margins(5, 0, 0, 0)));
		layoutContainer_4.setHeight("25");
		layCommonParameters.add(layoutContainer);
		layoutContainer.setHeight("171px");
		layoutContainer.setBorders(true);
		cntTicketData.add(layCommonParameters, new BorderLayoutData(LayoutRegion.NORTH, 160.0f));
		
		TabPanel tabPanelTrains = new TabPanel();
		tabPanelTrains.setPlain(true);
		
		TabItem tabTrainTo = new TabItem("Train To");
		tabTrainTo.setLayout(new AccordionLayout());
		
		ContentPanel cntDirectTrain = new ContentPanel();
		cntDirectTrain.setHeading("Direct Train");
		cntDirectTrain.setCollapsible(true);
		tabTrainTo.add(cntDirectTrain);
		
		ContentPanel cntTransferTrain = new ContentPanel();
		cntTransferTrain.setHeading("Transfer Train");
		cntTransferTrain.setCollapsible(true);
		tabTrainTo.add(cntTransferTrain);
		tabPanelTrains.add(tabTrainTo);
		tabTrainTo.setWidth("589px");
		
		TabItem tabTrainBack = new TabItem("Train Back");
		tabTrainBack.setLayout(new AccordionLayout());
		
		ContentPanel cntDirectTrainBack = new ContentPanel();
		cntDirectTrainBack.setHeading("Direct Train");
		cntDirectTrainBack.setCollapsible(true);
		tabTrainBack.add(cntDirectTrainBack);
		
		ContentPanel cntTransferTrainFrom = new ContentPanel();
		cntTransferTrainFrom.setHeading("Transfer Train");
		cntTransferTrainFrom.setCollapsible(true);
		tabTrainBack.add(cntTransferTrainFrom);
		tabPanelTrains.add(tabTrainBack);
		cntTicketData.add(tabPanelTrains, new BorderLayoutData(LayoutRegion.WEST, 575.0f));
		
		LayoutContainer layoutContainer_5 = new LayoutContainer();
		layoutContainer_5.setLayout(new BorderLayout());
		
		tabPanel.setPlain(true);
		
		tbitmPassenger.setText("Passenger 1");
		tabPanel.add(tbitmPassenger);
		layoutContainer_5.add(tabPanel, new BorderLayoutData(LayoutRegion.CENTER));
		
		LayoutContainer layoutContainer_6 = new LayoutContainer();
		HBoxLayout hbl_layoutContainer_6 = new HBoxLayout();
		hbl_layoutContainer_6.setPack(BoxLayoutPack.CENTER);
		hbl_layoutContainer_6.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		layoutContainer_6.setLayout(hbl_layoutContainer_6);
		
		Button btnAdd = new Button("Add");
		btnAdd.addListener(Events.Select, new Listener<ButtonEvent>() {
			public void handleEvent(ButtonEvent e) {
				Passenger passenger = new Passenger();
				passenger.setTitle("Passenger");
				int numPass = tabPanel.getItemCount();
				
				if (!(numPass >= 4)) {
					passenger.setText("Passenger " + (numPass + 1));
					tabPanel.add(passenger);
				}
			}
		});
		
		btnAdd.setIconAlign(IconAlign.RIGHT);
		btnAdd.setIcon(Resources.ICONS.Plus());
		layoutContainer_6.add(btnAdd);
		
		Button btnDelete = new Button("Delete");
		btnDelete.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				int numPass = tabPanel.getItemCount();
				
				if (numPass > 1) {
					TabItem item = tabPanel.getItem(numPass - 1);
					tabPanel.remove(item);
				}
			}
		});
		btnDelete.setIcon(Resources.ICONS.Minus());
		btnDelete.setIconAlign(IconAlign.RIGHT);
		
		layoutContainer_6.add(btnDelete, new HBoxLayoutData(0, 0, 0, 5));
		layoutContainer_5.add(layoutContainer_6, new BorderLayoutData(LayoutRegion.SOUTH, 40.0f));
		cntTicketData.add(layoutContainer_5, new BorderLayoutData(LayoutRegion.CENTER));
		layoutContainer_5.setBorders(true);
		add(cntTicketData);
	}
}
