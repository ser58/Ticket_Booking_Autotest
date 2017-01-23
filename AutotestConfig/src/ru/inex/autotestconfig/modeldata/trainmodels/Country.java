package ru.inex.autotestconfig.modeldata.trainmodels;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class Country extends BaseModelData {

	private static final long serialVersionUID = 1L;

	public Country() {

	}

	public Country(String name) {
		setName(name);
	}

	public String getName() {
		return get("name");
	}

	public void setName(String name) {
		set("name", name);
	}
}
