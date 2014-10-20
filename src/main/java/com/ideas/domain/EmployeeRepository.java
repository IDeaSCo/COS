package com.ideas.domain;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class EmployeeRepository {
	private final Connection connection;
	
	public EmployeeRepository(Connection connection) {
		if(connection == null)
			throw new IllegalArgumentException("Empty connection");
		this.connection = connection;
	}

	public Boolean getEmployeeDetails(String username) {
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

	public boolean addEmployee(Employee employee) {
		try {
			PreparedStatement insertEmployeeInfo = connection.prepareStatement("insert into employee_info values(?, ?, ?, ?, ?, ?)");
			insertEmployeeInfo.setString(1, employee.getUsername());
			insertEmployeeInfo.setString(2, employee.getName());
			insertEmployeeInfo.setString(3, employee.getAddress().getPickUpLocation());
			insertEmployeeInfo.setDouble(4, employee.getAddress().getLatitude());
			insertEmployeeInfo.setDouble(5, employee.getAddress().getLongitude());
			insertEmployeeInfo.setString(6, employee.getMobile());
			insertEmployeeInfo.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return true;
	}

	public EmployeeSchedule getEmployeeSchedule(String username) {
		try {
			TreeMap<Date, HashMap<String, Time>> eventsDateMap = new TreeMap<Date, HashMap<String, Time>>();
			ResultSet rs = connection.createStatement().executeQuery("select *  from employee_dashboard where username = '"	+ username + "'");
			while (rs.next()) {
				HashMap<String, Time> eventsTimeMap = new HashMap<String, Time>();

				if (!eventsDateMap.containsKey(rs.getDate(2))) {
					eventsTimeMap.put(rs.getString(3), rs.getTime(4));
					eventsDateMap.put(rs.getDate(2), eventsTimeMap);
				} else {
					eventsTimeMap = eventsDateMap.get(rs.getDate(2));
					eventsTimeMap.put(rs.getString(3), rs.getTime(4));
					eventsDateMap.put(rs.getDate(2), eventsTimeMap);
				}

			}
			return new EmployeeSchedule(username, eventsDateMap);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void populateDefaultTimings(String username) throws SQLException {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
		String startDate = year + "-" + month + "-01";
		CallableStatement procCall = connection.prepareCall("{call fillDefaultTiming(?, ?)}");
		procCall.setString(1, username);
		procCall.setString(2, startDate);
		procCall.execute();
	}
	
	public boolean updateSchedule(EmployeeSchedule schedule) {
		PreparedStatement ps;
		try {
			connection.createStatement().execute("delete from employee_dashboard where username = '" + schedule.getUsername() + "'");
			for (Date dateKey : schedule.getEventsDateMap().keySet()) {
				for (String eventKey : schedule.getEventsDateMap().get(dateKey).keySet()) {
					ps = connection.prepareStatement("insert into employee_dashboard (username, travel_date,event, time) values(?, ?, ?, ?)");
					ps.setString(1, schedule.getUsername());
					ps.setDate(2, dateKey);
					ps.setString(3, eventKey);
					ps.setTime(4, schedule.getEventsDateMap().get(dateKey).get(eventKey));
					ps.executeUpdate();
				}
			}
			return true;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Time> getShiftTimings(){
		ResultSet rs;
		List<Time> shiftTimings = new ArrayList<Time>();
		try {
			rs = connection.createStatement().executeQuery("select * from shift_details");
			while(rs.next())
				shiftTimings.add(rs.getTime(1));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return shiftTimings;
	}

	public boolean isEmployeeAdmin(String username) {
		boolean isRoleAdmin = false;
		try {
			ResultSet rs = connection.createStatement().executeQuery("select * from admin_info where username = '" + username + "'");
			if(rs.next())
				isRoleAdmin = true;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return isRoleAdmin;
	}
}