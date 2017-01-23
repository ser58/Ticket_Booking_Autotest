package ru.inex.autotestconfig.modeldata.trainmodels;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class Train extends BaseModelData {
	private static final long serialVersionUID = -7011165103987802872L;
	
	public Train() {

	}
	
	public Train(String trainNumber, String carCheckers, String wagonType, String wagonCategory, String servicesAndCapabilities, String carrier) {
		setTrainNumber(trainNumber);
		setCarCheckers(carCheckers);
		setWagonType(wagonType);
		setWagonCategory(wagonCategory);
		setServicesAndCapabilities(servicesAndCapabilities);
		setCarrier(carrier);
	}
	
	public String getTrainNumber() {
		return get("trainNumber");
	}
	public void setTrainNumber(String trainNumber) {
		set("trainNumber", trainNumber);
	}

	public String getCarCheckers() {
		return get("carCheckers");
	}
	public void setCarCheckers(String carCheckers) {
		set("carCheckers", carCheckers);
	}

	public String getWagonType() {
		return get("wagonType");
	}
	public void setWagonType(String wagonType) {
		set("wagonType", wagonType);
	}

	public String getWagonCategory() {
		return get("wagonCategory");
	}
	public void setWagonCategory(String wagonCategory) {
		set("wagonCategory", wagonCategory);
	}

	public String getServicesAndCapabilities() {
		return get("servicesAndCapabilities");
	}
	public void setServicesAndCapabilities(String servicesAndCapabilities) {
		set("servicesAndCapabilities", servicesAndCapabilities);
	}

	public String getCarrier() {
		return get("carrier");
	}
	public void setCarrier(String carrier) {
		set("carrier", carrier);
	}
}