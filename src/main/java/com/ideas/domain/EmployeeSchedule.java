package com.ideas.domain;

import java.sql.Time;
import java.sql.Date;
import java.util.HashMap;

public class EmployeeSchedule {
	private String username;
	private HashMap<Date, HashMap<String, Time>> eventsDateMap;

	public EmployeeSchedule(String username, HashMap<Date, HashMap<String, Time>> eventsDateMap) {
		this.username = username;
		this.eventsDateMap = eventsDateMap;
	}

	public String getUsername() {
		return username;
	}

	public HashMap<Date, HashMap<String, Time>> getEventsDateMap() {
		return eventsDateMap;
	}

}
