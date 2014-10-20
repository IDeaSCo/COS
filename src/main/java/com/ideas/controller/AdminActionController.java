package com.ideas.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ideas.domain.EmployeeRepository;

public class AdminActionController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private EmployeeRepository repository; 
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		repository = (EmployeeRepository) config.getServletContext().getAttribute("repository");
	}
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("AdminDashboard.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		@SuppressWarnings("deprecation")
		Date holiday = new Date(request.getParameter("start"));
	}

}
