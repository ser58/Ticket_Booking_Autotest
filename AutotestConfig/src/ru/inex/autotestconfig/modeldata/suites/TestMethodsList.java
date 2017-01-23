package ru.inex.autotestconfig.modeldata.suites;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class TestMethodsList extends BaseModelData {
	private static final long serialVersionUID = -7011165103987802872L;

	public TestMethodsList() {

	}

	public TestMethodsList(String name, boolean include) {
		setName(name);
		setIncluded(include);
	}

	public String getName() {
		return get("name");
	}

	public void setName(String name) {
		set("name", name);
	}
	
	public boolean getIncluded() {
		return get("include");
	}

	public void setIncluded(boolean include) {
		set("include", include);
	}
}