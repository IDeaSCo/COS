package com.ideas.controller;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TreeMap;

import org.json.JSONObject;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import com.ideas.domain.EmployeeSchedule;

public class COSServiceLayerTest {

	
	@Test
	public void shouldConvertEmpScheduleToJson() throws Exception {
		Calendar calendar =  Calendar.getInstance();
		calendar.set(2014, 10, 22, 18, 45, 22);
		HashMap<String, Time> eventTimeTestMap = new HashMap<String, Time>();
		TreeMap<Date, HashMap<String, Time>> eventDateTestMap = new TreeMap<Date, HashMap<String,Time>>();
		ArrayList<JSONObject> jsonObjArray = new ArrayList<JSONObject>();
		Time sqlTime = new Time(calendar.getTimeInMillis());
		eventTimeTestMap.put("In-Time",sqlTime);
		Date sqlDate = new Date(calendar.getTimeInMillis());
		eventDateTestMap.put(sqlDate , eventTimeTestMap);
		EmployeeSchedule empSchedule = new EmployeeSchedule("idnpam", eventDateTestMap);
		JSONObject obj = new JSONObject();

	      obj.put("title","In-Time");
	      obj.put("allDay", false);
	      obj.put("start","2014-11-22 18:45:22");
		jsonObjArray = new COSServiceLayer().convertEmpScheduleToJson(empSchedule);
		JSONAssert.assertEquals(obj, jsonObjArray.get(0), true);
	}
	
	@Test
	public void shouldConvertJsonToEmployeeSchedule() throws Exception {
		
	}
	
	
}
