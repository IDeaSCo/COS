package com.ideas.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.ideas.domain.Repository;

public class AdminActionController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Repository repository; 
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		repository = (Repository) config.getServletContext().getAttribute("repository");
	}
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		TreeMap<Date, String> companyHolidays = repository.getCompanyHolidays();
		ArrayList<JSONObject> holidayList = new COSServiceLayer().convertToJSON(companyHolidays);
		request.setAttribute("holidays", holidayList);
		Map<String, String> shiftTimings = repository.getShiftTimings();
		List<String> inTime = getIndividualTimings(shiftTimings, "in");
		request.setAttribute("inTime", inTime);
		List<String> outTime = getIndividualTimings(shiftTimings, "out");
		request.setAttribute("outTime", outTime);
		RequestDispatcher dispatcher = request.getRequestDispatcher("AdminDashboard.jsp");
		dispatcher.forward(request, response);
	}

	private List<String> getIndividualTimings(Map<String, String> shiftTimings, String slot) {
		List<String> timings = new ArrayList<String>();
		for (Entry<String, String> entry : shiftTimings.entrySet()) {
			if (entry.getValue().equals(slot))
				timings.add(entry.getKey());
		}
		return timings;
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("action").equals("addShift")) {
			String inTime = request.getParameter("start");
			String outTime = request.getParameter("end");
			if (inTime != "")
				inTime = inTime.substring(0, 5);
			if (outTime != "")
				outTime = outTime.substring(0, 5);
			boolean timeAdded = repository.addNewShifts(inTime, outTime);
		} else {
			String reason = request.getParameter("title");
			long timeInMillis = Long.valueOf(request.getParameter("start"));
			Date holiday = new Date(timeInMillis);
			String action = request.getParameter("action");
			if (action.equals("add")){
				Boolean isAdded = repository.addCompanyHoliday(holiday, reason);
				response.setContentType("application/json");
				response.getWriter().append(isAdded.toString());
				response.flushBuffer();
			}
			else
				repository.removeCompanyHoliday(holiday);
		}
	}
}
