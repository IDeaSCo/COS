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
		int index = 3;
		if(request.getParameter("username").contains("\\"))
			index++;
		String remoteUsername = request.getParameter("username").substring(index);
		WindowsAuthProviderImpl provider = new WindowsAuthProviderImpl();
		IWindowsAccount account = provider.lookupAccount(remoteUsername);
		String requestedFields = "employeeID,sn,givenName,mail";
		ActiveDirectoryUserInfo userInfo = null;
		UserDTO userDetails = null;
		try {
			userInfo = new ActiveDirectoryUserInfo(account.getFqn(), requestedFields);
			userDetails = userInfo.getUserDetails();
		} catch (AuthenticationError e) {
			userDetails = new UserDTO("", "", "");
		}
		boolean isPresent = repository.find(userDetails.getEmployeeID());
		RequestDispatcher dispatcher;
		if(isPresent)
			dispatcher = request.getRequestDispatcher("/WaffleDemo.jsp");
		else{
			dispatcher = request.getRequestDispatcher("/captureEmployeeDetails.jsp");
			request.setAttribute("userdetails", userDetails);
		}
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		double latitude = Double.parseDouble(request.getParameter("latitude"));
		double longitude = Double.parseDouble(request.getParameter("longitude"));
		UserDTO employee = new UserDTO(request.getParameter("EmployeeId"), request.getParameter("mobile"),new Address(latitude, longitude, request.getParameter("userAddress")));
		Boolean isAdded = repository.add(employee);
//		RequestDispatcher dispatcher = request.getRequestDispatcher("/DisplayCalendar.jsp");
//		dispatcher.forward(request, response);
	}

}
