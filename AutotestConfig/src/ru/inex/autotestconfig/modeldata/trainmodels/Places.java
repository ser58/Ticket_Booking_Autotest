package ru.inex.autotestconfig.modeldata.trainmodels;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class Places extends BaseModelData {
	private static final long serialVersionUID = -7011165103987802872L;
	
	public Places() {

	}

	public Places(String plGender, boolean plKupe, int range0, int range1, boolean plBedding, String plUpDown, String plComp) {
		setGender(plGender);
		setKupe(plKupe);
		setRange0(range0);
		setRange1(range1);
		setBedding(plBedding);
		setUpDown(plUpDown);
		setComp(plComp);
	}
	
	public String getComp() {
		return get("plComp");
	}
	public void setComp(String plComp) {
		set("plComp", plComp);
	}

	public String getUpDown() {
		return get("plUpDown");
	}
	public void setUpDown(String plUpDown) {
		set("plUpDown", plUpDown);
	}

	public boolean getBedding() {
		return get("plBedding");
	}
	public void setBedding(boolean plBedding) {
		set("plBedding", plBedding);
	}

	public int getRange1() {
		return get("range1");
	}
	public void setRange1(int range1) {
		set("range1", range1);
	}

	public int getRange0() {
		return get("range0");
	}
	public void setRange0(int range0) {
		set("range0", range0);
	}

	public boolean getKupe() {
		return get("plKupe");
	}
	public void setKupe(boolean plKupe) {
		set("plKupe", plKupe);
	}

	public String getGender() {
		return get("plGender");
	}
	public void setGender(String plGender) {
		set("plGender", plGender);
	}
}