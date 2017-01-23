package ru.inex.autotestconfig.modeldata.testng;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class TestngSuiteFiles extends BaseModelData {
	private static final long serialVersionUID = -7011165103987802872L;

	public TestngSuiteFiles() {

	}

	public TestngSuiteFiles(String path, boolean enabled) {
		setPath(path);
		setEnabled(enabled);
	}

	public String getPath() {
		return get("path");
	}

	public void setPath(String path) {
		set("path", path);
	}
	
	public boolean getEnabled() {
		return get("enabled");
	}

	public void setEnabled(boolean enabled) {
		set("enabled", enabled);
	}

}