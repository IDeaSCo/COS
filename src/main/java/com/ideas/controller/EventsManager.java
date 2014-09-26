package com.ideas.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.dhtmlx.planner.DHXEv;
import com.dhtmlx.planner.DHXEvent;
import com.dhtmlx.planner.DHXEventsManager;

public class EventsManager extends DHXEventsManager {

	public EventsManager(HttpServletRequest request) {
		super(request);
	}

	@Override
	public Iterable getEvents() {
		ArrayList events = new ArrayList();
		
		
		for (int i = 1; i <= 30; i++) {

			DHXEvent ev1 = new DHXEvent();
			ev1.setId(1);
			ev1.setStart_date("09/23/2014 09:30");
			ev1.setEnd_date("09/23/2014 09:35");
			ev1.setText("In-Time");

			DHXEvent ev2 = new DHXEvent();
			ev2.setId(2);
			ev2.setStart_date("09/23/2014 18:30");
			ev2.setEnd_date("09/23/2014 18:35");
			ev2.setText("Out-Time");

			DHXEvent ev3 = new DHXEvent();
			ev3.setId(3);
			ev3.setStart_date("09/24/2014 09:30");
			ev3.setEnd_date("09/24/2014 09:35");
			ev3.setText("In-Time");

			DHXEvent ev4 = new DHXEvent();
			ev4.setId(4);
			ev4.setStart_date("09/24/2014 18:30");
			ev4.setEnd_date("09/24/2014 18:35");
			ev4.setText("Out-Time");
			events.add(ev1);
			events.add(ev2);
			events.add(ev3);
			events.add(ev4);
		}
		return events;

	}
}