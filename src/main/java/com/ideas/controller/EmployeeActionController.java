package com.ideas.controller;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.ideas.domain.Address;
import com.ideas.domain.Employee;
import com.ideas.domain.EmployeeRepository;

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
		Employee employeeDetails = new Employee(username, mobile, employeeAddress);
		boolean isAdded = repository.addEmployee(employeeDetails);
		try {
			repository.populateDefaultTimings(username);
		} catch (SQLException e) {
		}
		response.sendRedirect("dashboard?username=" + username);
	}
}
