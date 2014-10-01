package com.ideas.controller;

import junit.framework.Assert;
import org.eclipse.jetty.testing.HttpTester;
import org.eclipse.jetty.testing.ServletTester;
import org.junit.Before;
import org.junit.Test;

public class AuthenticationControllerTest {
	private static ServletTester servletTester;
	
	@Before
	public void setUp() throws Exception {
		ServletTester servletTester = new ServletTester();
		servletTester.setContextPath("/");
		servletTester.addServlet(AuthenticationController.class, "/authenticate");
		servletTester.start();
	}

	@Test
	public void getEmployeeDetailsFromActiveDirectoryIsSuccessfull() throws Exception {
/*		HttpTester httpRequestTester = new HttpTester();
		httpRequestTester.setMethod("GET");
		httpRequestTester.setURI("/authenticate?username=ROWidnsor");
		httpRequestTester.setVersion("HTTP/1.0");
		HttpTester httpResponseTester = new HttpTester();
		httpResponseTester.parse(servletTester.getResponses(httpRequestTester.generate()));
		Assert.assertEquals(200, httpResponseTester.getStatus());	
*/	}
}
