package com.ideas.domain;

import java.sql.Time;
import java.sql.Date;
import java.util.HashMap;
import java.util.TreeMap;

public class EmployeeSchedule {
	private String username;
	private TreeMap<Date, HashMap<String, Time>> eventsDateMap;

	public EmployeeSchedule(String username, TreeMap<Date, HashMap<String, Time>> eventDateMap) {
		this.username = username;
		this.eventsDateMap = eventDateMap;
	}

	public String getUsername() {
		return username;
	}

	public TreeMap<Date,HashMap<String,Time>> getEventsDateMap() {
		return eventsDateMap;
	}

}
