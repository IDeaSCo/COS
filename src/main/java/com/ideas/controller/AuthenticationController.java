package com.ideas.controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import waffle.windows.auth.IWindowsAccount;
import waffle.windows.auth.impl.WindowsAuthProviderImpl;
import com.ideas.domain.Address;
import com.ideas.domain.EmployeeRepository;
import com.ideas.domain.UserDTO;
import com.ideas.sso.ActiveDirectoryUserInfo;
import com.ideas.sso.AuthenticationError;

public class AuthenticationController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private EmployeeRepository repository; 
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		repository = (EmployeeRepository) config.getServletContext().getAttribute("repository");
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username").substring(4);
		boolean isEmployeeRegistered = repository.find(username);
		RequestDispatcher dispatcher;
		if(!isEmployeeRegistered)
			dispatcher = request.getRequestDispatcher("Maps.jsp");
		else
			dispatcher = request.getRequestDispatcher("WaffleDemo.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		UserDTO employeeLocationDetails = getEmployeeLocationDetails(request, username);
		request.setAttribute("locationDetails", employeeLocationDetails);
		UserDTO employeeDetails = getEmployeeDetailsFromActiveDirectory(username);
		request.setAttribute("employeeDetails", employeeDetails);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/captureEmployeeDetails.jsp");
		dispatcher.forward(request, response);
	}

	private UserDTO getEmployeeLocationDetails(HttpServletRequest request, String username){
		String pickUpLocation = request.getParameter("userAddress");
		double latitude = Double.parseDouble(request.getParameter("latitude"));
		double longitude = Double.parseDouble(request.getParameter("longitude"));
		Address address = new Address(latitude, longitude, pickUpLocation);
		UserDTO employeeLocationDetails = new UserDTO(username, null, address);
		return employeeLocationDetails;
	}
	
	private UserDTO getEmployeeDetailsFromActiveDirectory(String username) {
		WindowsAuthProviderImpl provider = new WindowsAuthProviderImpl();
		IWindowsAccount account = provider.lookupAccount(username);
		String requestedFields = "employeeID,sn,givenName,mail";
		ActiveDirectoryUserInfo userInfo = null;
		UserDTO employeeDetails = null;
		try {
			userInfo = new ActiveDirectoryUserInfo(account.getFqn(), requestedFields);
			employeeDetails = userInfo.getUserDetails();
		} catch (AuthenticationError e) {
			employeeDetails = new UserDTO("", "", "");
		}
		return employeeDetails;
	}

}