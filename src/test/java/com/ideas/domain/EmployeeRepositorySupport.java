package com.ideas.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmployeeRepositorySupport {
	private Connection connection;
	
	public EmployeeRepository createTableAndRepository() throws ClassNotFoundException, SQLException{
		Class.forName("org.hsqldb.jdbcDriver");
		connection = DriverManager.getConnection("jdbc:hsqldb:mem:cos", "SA", "");
		createTable(connection);
		return new EmployeeRepository(connection);
	}
	
	public void createTable(Connection connection) throws SQLException{
		StringBuilder createTable = new StringBuilder();
		createTable.append("CREATE TABLE employee_info(")
		.append("username VARCHAR(10) PRIMARY KEY,")
		.append("address VARCHAR(200) NOT NULL,")
		.append("latitude DOUBLE NOT NULL,")
		.append("longitude DOUBLE NOT NULL,")
		.append("mobile VARCHAR(10) NOT NULL)");
		connection.createStatement().executeUpdate(createTable.toString());
	}
	
	public void insert(UserDTO employee) throws SQLException{
		PreparedStatement insertEmployeeDetails = connection.prepareStatement("insert into employee_info values(?, ?, ?, ?, ?)");
		insertEmployeeDetails.setString(1, employee.getEmployeeID());
		insertEmployeeDetails.setString(2, employee.getAddress().getPickUpLocation());
		insertEmployeeDetails.setDouble(3, employee.getAddress().getLatitude());
		insertEmployeeDetails.setDouble(4, employee.getAddress().getLongitude());
		insertEmployeeDetails.setString(4, employee.getMobile());
		insertEmployeeDetails.executeUpdate();
	}
	
	public void cleanTable() throws SQLException{
		connection.createStatement().executeQuery("delete from employee_info");
	}
	
	public void dropTable() throws SQLException{
		connection.createStatement().executeUpdate("drop table employee_info");
	}
}
