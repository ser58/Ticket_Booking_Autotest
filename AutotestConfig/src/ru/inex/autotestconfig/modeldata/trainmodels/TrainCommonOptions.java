package ru.inex.autotestconfig.modeldata.trainmodels;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class TrainCommonOptions extends BaseModelData {
	private static final long serialVersionUID = -7011165103987802872L;
	
	public TrainCommonOptions() {

	}

	public TrainCommonOptions(String id, String description, String from, String to, int[] dayOfWeek, int[] departureTime, boolean onlyWithTickets, boolean localTrain, boolean back) {
		setId(id);
		setDescription(description);
		setFrom(from);
		setTo(to);
		setDayOfWeek(dayOfWeek);
		setDepartureTime(departureTime);
		setOnlyWithTickets(onlyWithTickets);
		setLocalTrain(localTrain);
		setBack(back);
	}

	public String getId() {
		return get("id");
	}

	public void setId(String id) {
		set("id", id);
	}

	public String getDescription() {
		return get("description");
	}

	public void setDescription(String description) {
		set("description", description);
	}

	public String getFrom() {
		return get("from");
	}

	public void setFrom(String from) {
		set("from", from);
	}

	public String getTo() {
		return get("to");
	}

	public void setTo(String to) {
		set("to", to);
	}

	public int[] getDayOfWeek() {
		return get("dayOfWeek");
	}

	public void setDayOfWeek(int[] dayOfWeek) {
		set("dayOfWeek", dayOfWeek);
	}

	public int[] getDepartureTime() {
		return get("departureTime");
	}

	public void setDepartureTime(int[] departureTime) {
		set("departureTime", departureTime);
	}

	public boolean isOnlyWithTickets() {
		return get("onlyWithTickets");
	}

	public void setOnlyWithTickets(boolean onlyWithTickets) {
		set("onlyWithTickets", onlyWithTickets);
	}

	public boolean isLocalTrain() {
		return get("localTrain");
	}

	public void setLocalTrain(boolean localTrain) {
		set("localTrain", localTrain);
	}

	public boolean isBack() {
		return get("back");
	}

	public void setBack(boolean back) {
		set("back", back);
	}
}