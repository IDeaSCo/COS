package com.ideas.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ideas.domain.Repository;
import com.ideas.routeOptimization.DataPoint;
import com.ideas.routeOptimization.RouteOptimizer;

public class RouteOptimizationController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private RouteOptimizer routeOptimizer;

	@Override
	public void init(ServletConfig config) throws ServletException {
		routeOptimizer = (RouteOptimizer) config.getServletContext()
				.getAttribute("routeOptimizer");
	}

	public RouteOptimizationController() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ArrayList<TreeMap<Time, ArrayList<ArrayList<DataPoint>>>> timeMapList = null;
		try {
			timeMapList = routeOptimizer.optimizeRoute();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("timeMapList", timeMapList);
		RequestDispatcher dispatcher = request
				.getRequestDispatcher("/vendorMapSolutions.jsp");
		dispatcher.forward(request, response);

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

}
