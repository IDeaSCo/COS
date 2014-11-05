package com.ideas.routeOptimization;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.TreeMap;

public class RouteOptimizerHandler {

	private static Connection conn;

	public RouteOptimizerHandler(Connection conn) {
		this.conn = conn;
	}

	public ArrayList<TreeMap<Time, ArrayList<DataPoint>>> getData(Time timeOfDay) {
		TreeMap<Time, ArrayList<DataPoint>> inTimeMap = new TreeMap<Time, ArrayList<DataPoint>>();
		TreeMap<Time, ArrayList<DataPoint>> outTimeMap = new TreeMap<Time, ArrayList<DataPoint>>();
		try {
			ResultSet resultSet;
			if (timeOfDay.getHours() <= 11)
				resultSet = getTodaysData();
			else
				resultSet = getTomorrowsData();
			while (resultSet.next()) {
				String username = resultSet.getString("username");
				double latitude = resultSet.getDouble("latitude");
				double longitude = resultSet.getDouble("longitude");
				String eventType = resultSet.getString("event");
				Time time = resultSet.getTime("time");
				if (eventType.equalsIgnoreCase("In-Time"))
					addElementsToTimeMap(inTimeMap, username, latitude, longitude, eventType, time);
				else
					addElementsToTimeMap(outTimeMap, username, latitude, longitude, eventType, time);
			}

			ArrayList<TreeMap<Time, ArrayList<DataPoint>>> timesMapList = new ArrayList<TreeMap<Time, ArrayList<DataPoint>>>();
			timesMapList.add(inTimeMap);
			timesMapList.add(outTimeMap);
			return timesMapList;
		} catch (SQLException e) {
			throw new RuntimeException(e.toString());
		}
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://didnsaeina6/cabservice", "admin", "admin");
		RouteOptimizerHandler routeOptimizer = new RouteOptimizerHandler(conn);
		routeOptimizer.getData(new Time(15, 00, 00));
	}

	private void addElementsToTimeMap(TreeMap<Time, ArrayList<DataPoint>> TimeMap, String username, double latitude,
			double longitude, String eventType, Time time) {

		if (TimeMap.containsKey(time)) {
			TimeMap.get(time).add(new DataPoint(latitude, longitude, username));
		} else {
			TimeMap.put(time, new ArrayList<DataPoint>());
			TimeMap.get(time).add(new DataPoint(latitude, longitude, username));
		}

	}

	private ResultSet getTomorrowsData() throws SQLException {
		ResultSet resultSet = conn.createStatement().executeQuery(
				" select dashboard.username, latitude, longitude, event, time from employee_info info "
						+ "inner join employee_dashboard dashboard  " + "on (info.username=dashboard.username)   "
						+ "where travel_date = DATE_ADD(curdate(),INTERVAL 0 day)   ");
		return resultSet;
	}

	private ResultSet getTodaysData() throws SQLException {
		ResultSet resultSet = conn.createStatement().executeQuery(
				" select dashboard.username, latitude, longitude, event, time from employee_info info "
						+ "inner join employee_dashboard dashboard  " + "on (info.username=dashboard.username)   "
						+ "where travel_date = curdate()   " + "and time > '16:00:00'    ");
		return resultSet;
	}
}
