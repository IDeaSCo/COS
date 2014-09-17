package com.ideas.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.ideas.sso.ActiveDirectoryUserInfo;
import com.ideas.sso.AuthenticationError;
import com.ideas.sso.UserDTO;
import waffle.windows.auth.IWindowsAccount;
import waffle.windows.auth.impl.WindowsAccountImpl;

public class AuthenticationController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String currentUsername = WindowsAccountImpl.getCurrentUsername();
		IWindowsAccount userAccount = new WindowsAccountImpl(currentUsername);
		String requestedFields = "employeeID,sn,givenName,mail";
		try {
			UserDTO userInfo = ActiveDirectoryUserInfo.getUserInfo(userAccount.getFqn(), requestedFields);
			System.out.println(userInfo.getEmployeeID() + " " + userInfo.getName() + " " + userInfo.getName());
		} catch (AuthenticationError e) {
		}  
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
