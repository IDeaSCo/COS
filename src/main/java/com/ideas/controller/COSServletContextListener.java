package com.ideas.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import com.ideas.domain.Repository;

public class COSServletContextListener implements ServletContextListener {
	private Repository repository;
	private DataSource dataSource;

	public void contextInitialized(ServletContextEvent sce) {
		ServletContext servletContext = sce.getServletContext();
		String username = servletContext.getInitParameter("username");
		String password = servletContext.getInitParameter("password");
		String driverClassName = servletContext.getInitParameter("driver");
		String url = servletContext.getInitParameter("url");
		dataSource = setupDataSource(username, password, driverClassName, url);
		try {
			repository = new Repository(dataSource.getConnection());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		servletContext.setAttribute("repository", repository);

//		try {
//			Class.forName(servletContext.getInitParameter("driver"));
//			Connection connection = DriverManager.getConnection(
//					servletContext.getInitParameter("url"),
//					servletContext.getInitParameter("username"),
//					servletContext.getInitParameter("password"));
//			repository = new Repository(connection);
//			servletContext.setAttribute("repository", repository);
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
	}

	private static DataSource setupDataSource(String username, String password,
			String driverClassName, String url) {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driverClassName);
		ds.setUsername(username);
		ds.setPassword(password);
		ds.setUrl(url);
		return ds;
	}

	public void contextDestroyed(ServletContextEvent sce) {
		BasicDataSource bds = (BasicDataSource) dataSource;
		try {
			bds.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
