package ru.inex.autotestconfig.modeldata.datafiles;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class DataFile extends BaseModelData {
	private static final long serialVersionUID = -7011165103987802872L;

	public DataFile() {

	}

	public DataFile(String name, String value) {
		setName(name);
		setValue(value);
	}

	public String getName() {
		return get("name");
	}

	public void setName(String name) {
		set("name", name);
	}
	
	public String getValue() {
		return get("value");
	}

	public void setValue(String value) {
		set("value", value);
	}
}