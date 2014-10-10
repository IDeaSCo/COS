package com.ideas.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.ideas.domain.EmployeeSchedule;

public class COSServiceLayer {

	public ArrayList<JSONObject> convertEmpScheduleToJson(EmployeeSchedule schedule) {
		ArrayList<JSONObject> jsonObjArray = new ArrayList<JSONObject>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("allDay", false);
		for (Date dateKey : schedule.getEventsDateMap().keySet()) {
			for (String eventKey : schedule.getEventsDateMap().get(dateKey).keySet()) {
				map.put("title", eventKey);
				map.put("start", dateKey+" "+schedule.getEventsDateMap().get(dateKey).get(eventKey) );
				jsonObjArray.add(new JSONObject(map));
			}
		}
		return  jsonObjArray;

	}
}
