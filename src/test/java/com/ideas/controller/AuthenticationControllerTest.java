package com.ideas.controller;

import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.eclipse.jetty.testing.HttpTester;
import org.eclipse.jetty.testing.ServletTester;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import waffle.windows.auth.IWindowsAccount;
import waffle.windows.auth.impl.WindowsAuthProviderImpl;
import com.ideas.domain.EmployeeRepository;

public class AuthenticationControllerTest {
	private static ServletTester servletTester;
	private static EmployeeRepository repository = mock(EmployeeRepository.class);
			
	@BeforeClass
	public static void setUp() throws Exception {
		servletTester = startServer();
	}

	private static ServletTester startServer(){
		ServletTester servletTester = new ServletTester();
		servletTester.setContextPath("/");
		servletTester.addServlet(AuthenticationController.class, "/authenticate");
		servletTester.setAttribute("repository", repository);
		return servletTester;
	}
	
	@Test
	public void getRequestForEmployeeIsSuccessfull() throws IOException, Exception {
		WindowsAuthProviderImpl provider = new WindowsAuthProviderImpl();
		IWindowsAccount account = provider.lookupAccount("idnsor");
		HttpTester requestTester = new HttpTester();
		requestTester.setMethod("GET");
		requestTester.setURI("/authenticate?username=" + account.getFqn());
		requestTester.setVersion("HTTP/1.0");
		HttpTester responseTester = new HttpTester();
		requestTester.parse(servletTester.getResponses(requestTester.generate()));
		Assert.assertEquals(200, responseTester.getStatus());
	}

}
