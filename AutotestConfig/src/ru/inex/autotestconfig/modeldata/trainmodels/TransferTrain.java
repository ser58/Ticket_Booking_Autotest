package ru.inex.autotestconfig.modeldata.trainmodels;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class TransferTrain extends BaseModelData {
	private static final long serialVersionUID = -7011165103987802872L;
	
	public TransferTrain() {

	}
	
	public TransferTrain(String transferTrainNumber, 
						 String transferWagonType, 
						 String transferWagonCategory, 
						 String transferServicesAndCapabilities, 
						 String carrier, 
						 String transferStation,
						 String transferTimeFrom,
						 String transferTimeTo,
						 boolean showAll) {
		setTransferTrainNumber(transferTrainNumber);
		setTransferWagonType(transferWagonType);
		setTransferWagonCategory(transferWagonCategory);
		setTransferServicesAndCapabilities(transferServicesAndCapabilities);
		setCarrier(carrier);
		setTransferStation(transferStation);
		setTransferTimeFrom(transferTimeFrom);
		setTransferTimeTo(transferTimeTo);
		setShowAll(showAll);
	}
	
	public String getTransferTrainNumber() {
		return get("transferTrainNumber");
	}
	public void setTransferTrainNumber(String transferTrainNumber) {
		set("transferTrainNumber", transferTrainNumber);
	}

	public String getTransferWagonType() {
		return get("transferWagonType");
	}
	public void setTransferWagonType(String transferWagonType) {
		set("transferWagonType", transferWagonType);
	}

	public String getTransferWagonCategory() {
		return get("transferWagonCategory");
	}
	public void setTransferWagonCategory(String transferWagonCategory) {
		set("transferWagonCategory", transferWagonCategory);
	}

	public String getTransferServicesAndCapabilities() {
		return get("transferServicesAndCapabilities");
	}
	public void setTransferServicesAndCapabilities(String transferServicesAndCapabilities) {
		set("transferServicesAndCapabilities", transferServicesAndCapabilities);
	}

	public String getCarrier() {
		return get("carrier");
	}
	public void setCarrier(String carrier) {
		set("carrier", carrier);
	}
	
	public String getTransferStation() {
		return get("transferStation");
	}
	public void setTransferStation(String transferStation) {
		set("transferStation", transferStation);
	}

	public String getTransferTimeFrom() {
		return get("transferTimeFrom");
	}
	public void setTransferTimeFrom(String transferTimeFrom) {
		set("transferTimeFrom", transferTimeFrom);
	}

	public String getTransferTimeTo() {
		return get("transferTimeTo");
	}
	public void setTransferTimeTo(String transferTimeTo) {
		set("transferTimeTo", transferTimeTo);
	}

	public boolean getShowAll() {
		return get("showAll");
	}
	public void setShowAll(boolean showAll) {
		set("showAll", showAll);
	}

}