package ru.inex.autotestconfig.client.forms;

import ru.inex.autotestconfig.client.AutotestConfig;

import com.extjs.gxt.ui.client.Style.Direction;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.fx.FxConfig;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class dlgShowTestng extends Window {
	static TextArea testng = new TextArea();
	
	public dlgShowTestng() {
		setClosable(false);
		setHeading("Show Testng File");
		setSize("907px", "623px");
		setButtonAlign(HorizontalAlignment.CENTER);
		setAnimCollapse(true);
		setLayout(new BorderLayout());
		
		LayoutContainer layoutContainer = new LayoutContainer();
		
		Button btnNewButton = new Button("Close");
		btnNewButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				el().slideOut(Direction.UP, FxConfig.NONE);
			}
		});
		layoutContainer.add(btnNewButton);
		layoutContainer.setLayout(new CenterLayout());
		add(layoutContainer, new BorderLayoutData(LayoutRegion.SOUTH, 50.0f));
		
		LayoutContainer layoutContainer_1 = new LayoutContainer();
		layoutContainer_1.setLayout(new FitLayout());
		
		layoutContainer_1.add(testng);
		add(layoutContainer_1, new BorderLayoutData(LayoutRegion.CENTER));
		
		addListener(Events.Show, new Listener<ComponentEvent>() {
			public void handleEvent(ComponentEvent e) {
				testng.setValue(AutotestConfig.testngText);
			}
		});
	}
}
