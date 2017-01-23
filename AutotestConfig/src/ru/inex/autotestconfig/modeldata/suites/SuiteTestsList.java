package ru.inex.autotestconfig.modeldata.suites;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class SuiteTestsList extends BaseModelData {
	private static final long serialVersionUID = -7011165103987802872L;

	public SuiteTestsList() {

	}

	public SuiteTestsList(String name, boolean preserveOrder, boolean enabled) {
		setName(name);
		setPreserveOrder(preserveOrder);
		setEnabled(enabled);
	}

	public String getName() {
		return get("name");
	}

	public void setName(String name) {
		set("name", name);
	}
	
	public String getPreserveOrder() {
		return get("preserveOrder");
	}

	public void setPreserveOrder(boolean preserveOrder) {
		set("preserveOrder", preserveOrder);
	}
	
	public boolean getEnabled() {
		return get("enabled");
	}

	public void setEnabled(boolean enabled) {
		set("enabled", enabled);
	}
}