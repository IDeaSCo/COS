package com.ideas.domain;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.HashMap;

public class EmployeeRepository {
	private final Connection connection;
	
	public EmployeeRepository(Connection connection) {
		if(connection == null)
			throw new IllegalArgumentException("Empty connection");
		this.connection = connection;
	}

	public Boolean findEmployee(String username) {
		ResultSet rs;
		try {
			rs = connection.createStatement().executeQuery("select *  from employee_info where username = '" + username + "'");
			if(rs.next())
				return true;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
		return false;
	}

	public boolean add(Employee employee) {
		try {
			PreparedStatement insertEmployeeInfo = connection.prepareStatement("insert into employee_info values(?, ?, ?, ?, ?)");
			insertEmployeeInfo.setString(1, employee.getUsername());
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

	public EmployeeSchedule getEmployeeSchedule(String username) {
		try {
			HashMap<String, Time> eventsTimeMap = new HashMap<String, Time>();
			HashMap<Date, HashMap<String, Time>> eventsDateMap = new HashMap<Date, HashMap<String, Time>>();
			ResultSet rs = connection.createStatement().executeQuery("select *  from employee_dashboard where username = '" + username + "'");
			while (rs.next()) {
				eventsTimeMap.put(rs.getString(3), rs.getTime(4));
				eventsDateMap.put(rs.getDate(2), eventsTimeMap);
			}
			return new EmployeeSchedule(username, eventsDateMap);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}