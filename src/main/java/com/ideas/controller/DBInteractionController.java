package com.ideas.controller;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import org.json.JSONObject;

import com.ideas.domain.EmployeeRepository;
import com.ideas.domain.EmployeeSchedule;

public class DBInteractionController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private EmployeeRepository repository;
    
	@Override
	public void init(ServletConfig config) throws ServletException {
		repository = (EmployeeRepository) config.getServletContext().getAttribute("repository");
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		if(username.contains("\\"))
			username = username.substring(username.indexOf("\\") + 1);
		EmployeeSchedule schedule = repository.getEmployeeSchedule(username);
		ArrayList<JSONObject> jsonObjArray= new COSServiceLayer().convertEmpScheduleToJson(schedule);
		request.setAttribute("eventScheduleArray", jsonObjArray);
		RequestDispatcher dispatcher = request.getRequestDispatcher("Dashboard.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		if(username.contains("\\"))
			username = username.substring(username.indexOf("\\") + 1);
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
