package com.ideas.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;

import org.json.JSONObject;

import com.ideas.domain.Repository;
import com.ideas.domain.EmployeeSchedule;

public class DashboardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Repository repository;
    
	@Override
	public void init(ServletConfig config) throws ServletException {
		repository = (Repository) config.getServletContext().getAttribute("repository");
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = (String) request.getSession().getAttribute("username");
		EmployeeSchedule schedule = repository.getEmployeeSchedule(username);
		ArrayList<JSONObject> jsonObjArray= new COSServiceLayer().convertEmpScheduleToJson(schedule);
		request.setAttribute("eventScheduleArray", jsonObjArray);
		List<Time> shiftTimings = repository.getShiftTimings();
		request.setAttribute("shiftTimings", shiftTimings);
		RequestDispatcher dispatcher = request.getRequestDispatcher("Dashboard.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = (String) request.getSession().getAttribute("username");
		String events = (request.getParameter("events"));
		EmployeeSchedule schedule = null;
		try {
			schedule = new COSServiceLayer().jsonToEmployeeSchedule(events, username);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Boolean isDone = repository.updateSchedule(schedule);
		if (isDone)
			response.setContentType("application/json");
		response.getWriter().append(isDone.toString());
		response.flushBuffer();
	}

}
