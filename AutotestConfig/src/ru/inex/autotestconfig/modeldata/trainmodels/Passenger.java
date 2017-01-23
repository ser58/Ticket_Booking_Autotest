package ru.inex.autotestconfig.modeldata.trainmodels;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class Passenger extends BaseModelData {
	private static final long serialVersionUID = -7011165103987802872L;
	
	public Passenger() {

	}
	
	public Passenger(int id, 
					 String lastName, 
					 String firstName, 
					 String middleName, 
					 String documentType, 
					 String documentNumber, 
					 String tariff, 
					 String citizenship, 
					 String gender, 
					 String dateOfBirth,
					 String insurance,
					 String phoneNumber) {
		setId(id);
		setLastName(lastName);
		setFirstName(firstName);
		setMiddleName(middleName);
		setDocumentType(documentType);
		setDocumentNumber(documentNumber);
		setTariff(tariff);
		setCitizenship(citizenship);
		setGender(gender);
		setDateOfBirth(dateOfBirth);
		setInsurance(insurance);
		setPhoneNumber(phoneNumber);
	}
	
	public int getId() {
		return get("id");
	}
	public void setId(int id) {
		set("id", id);
	}
	
	public String getLastName() {
		return get("lastName");
	}
	public void setLastName(String lastName) {
		set("lastName", lastName);
	}

	public String getFirstName() {
		return get("firstName");
	}
	public void setFirstName(String firstName) {
		set("firstName", firstName);
	}

	public String getMiddleName() {
		return get("middleName");
	}
	public void setMiddleName(String middleName) {
		set("middleName", middleName);
	}

	public String getDocumentType() {
		return get("documentType");
	}
	public void setDocumentType(String documentType) {
		set("documentType", documentType);
	}

	public String getDocumentNumber() {
		return get("documentNumber");
	}
	public void setDocumentNumber(String documentNumber) {
		set("documentNumber", documentNumber);
	}

	public String getTariff() {
		return get("tariff");
	}
	public void setTariff(String tariff) {
		set("tariff", tariff);
	}

	public String getCitizenship() {
		return get("citizenship");
	}
	public void setCitizenship(String citizenship) {
		set("citizenship", citizenship);
	}

	public String getGender() {
		return get("gender");
	}
	public void setGender(String gender) {
		set("gender", gender);
	}

	public String getDateOfBirth() {
		return get("dateOfBirth");
	}
	public void setDateOfBirth(String dateOfBirth) {
		set("dateOfBirth", dateOfBirth);
	}

	public String getInsurance() {
		return get("insurance");
	}
	public void setInsurance(String insurance) {
		set("insurance", insurance);
	}

	public String getPhoneNumber() {
		return get("phoneNumber");
	}
	public void setPhoneNumber(String phoneNumber) {
		set("phoneNumber", phoneNumber);
	}
}