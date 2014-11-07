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
	private ControllerHelper helper;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		routeOptimizer = (RouteOptimizer) config.getServletContext().getAttribute("routeOptimizer");
		helper = (ControllerHelper) config.getServletContext().getAttribute("helper");
	}

	public RouteOptimizationController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<TreeMap<Time, ArrayList<ArrayList<DataPoint>>>> timeMapList = null;
		try {
			timeMapList = routeOptimizer.optimizeRoute();
			request.setAttribute("timeMapList", timeMapList);
			helper.sendRequest(request, response, "vendorMapSolutions.jsp");
/*			RequestDispatcher dispatcher = request.getRequestDispatcher("/vendorMapSolutions.jsp");
			dispatcher.forward(request, response);
*/		} catch (ClassNotFoundException e) {}
		  catch (SQLException e) {}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
