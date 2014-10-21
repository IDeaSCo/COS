package com.ideas.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.ideas.domain.EmployeeRepository;

public class AdminActionController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private EmployeeRepository repository; 
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		repository = (EmployeeRepository) config.getServletContext().getAttribute("repository");
	}
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		TreeMap<Date, String> companyHolidays = repository.getCompanyHolidays();
		ArrayList<JSONObject> holidayList = new COSServiceLayer().convertToJSON(companyHolidays);
		request.setAttribute("holidays", holidayList);
		RequestDispatcher dispatcher = request.getRequestDispatcher("AdminDashboard.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String reason = request.getParameter("title");
		long timeInMillis = Long.valueOf(request.getParameter("start"));
		Date holiday = new Date(timeInMillis);
		boolean isHolidayMarked = repository.addCompanyHoliday(holiday, reason);
	}

}
