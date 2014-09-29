package com.ideas.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.ideas.domain.EmployeeRepository;

public class COSServletContextListener implements ServletContextListener {
    private EmployeeRepository repository;

	public void contextInitialized(ServletContextEvent sce)  {
		System.out.println("IMSServletContextListener.contextInitialized()");
    	ServletContext servletContext = sce.getServletContext();
		try {
			Class.forName(servletContext.getInitParameter("driver"));
			Connection connection = DriverManager.getConnection(
					servletContext.getInitParameter("url"),
					servletContext.getInitParameter("username"),
					servletContext.getInitParameter("password"));
			repository = new EmployeeRepository(connection);
			servletContext.setAttribute("repository", repository);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    public void contextDestroyed(ServletContextEvent sce)  { 
    }
	
}
