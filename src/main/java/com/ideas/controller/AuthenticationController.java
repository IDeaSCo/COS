package com.ideas.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import waffle.windows.auth.IWindowsAccount;
import waffle.windows.auth.impl.WindowsAuthProviderImpl;

import com.ideas.sso.ActiveDirectoryUserInfo;
import com.ideas.sso.AuthenticationError;
import com.ideas.sso.UserDTO;

public class AuthenticationController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String remoteUsername = request.getParameter("username").substring(4);
		WindowsAuthProviderImpl provider = new WindowsAuthProviderImpl();
		IWindowsAccount account = provider.lookupAccount(remoteUsername);
		String requestedFields = "employeeID,sn,givenName,mail";
		ActiveDirectoryUserInfo userInfo = null;
		UserDTO userDetails = null;
		try {
			userInfo = new ActiveDirectoryUserInfo(account.getFqn(), requestedFields);
			userDetails = userInfo.getUserDetails();
		} catch (AuthenticationError e) {
			//todo : if request has no username or it is empty
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("/captureEmployeeDetails.jsp");
		request.setAttribute("userdetails", userDetails);
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

}
