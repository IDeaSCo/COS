package com.ideas.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ideas.domain.Address;
import com.ideas.domain.EmployeeRepository;
import com.ideas.domain.UserDTO;
import com.sun.org.apache.bcel.internal.generic.LALOAD;

public class EmployeeActionController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private EmployeeRepository repository;
    
	@Override
	public void init(ServletConfig config) throws ServletException {
		repository = (EmployeeRepository) config.getServletContext().getAttribute("repository");
	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String address = request.getParameter("userAddress");
		double latitude = Double.parseDouble(request.getParameter("latitude"));
		double longitude = Double.parseDouble(request.getParameter("longitude"));
		String mobile = request.getParameter("mobile");
		Address employeeAddress = new Address(latitude, longitude, address);
		UserDTO employeeDetails = new UserDTO(username, mobile, employeeAddress);
		boolean isAdded = repository.add(employeeDetails);
		RequestDispatcher dispatcher = request.getRequestDispatcher("WaffleDemo.jsp");
		dispatcher.forward(request, response);
	}
}
