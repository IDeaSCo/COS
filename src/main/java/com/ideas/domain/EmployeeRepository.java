package com.ideas.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeRepository {
	private final Connection connection;
	
	public EmployeeRepository(Connection connection) {
		if(connection == null)
			throw new IllegalArgumentException("Empty connection");
		this.connection = connection;
	}

	public Boolean find(String employeeId) {
		ResultSet rs;
		try {
			rs = connection.createStatement().executeQuery("select *  from employee_info where emp_id = '" + employeeId + "'");
			if(rs.next())
				return true;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
		return false;
	}

	public boolean add(UserDTO employee) {
		try {
			PreparedStatement insertEmployeeInfo = connection.prepareStatement("insert into employee_info values(?, ?, ?, ?, ?)");
			insertEmployeeInfo.setString(1, employee.getEmployeeID());
			insertEmployeeInfo.setString(2, employee.getAddress().getPickUpLocation());
			insertEmployeeInfo.setDouble(3, employee.getAddress().getLatitude());
			insertEmployeeInfo.setDouble(4, employee.getAddress().getLongitude());
			insertEmployeeInfo.setString(5, employee.getMobile());
			insertEmployeeInfo.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return true;
	}

}