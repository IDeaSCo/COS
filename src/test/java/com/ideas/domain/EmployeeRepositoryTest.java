package com.ideas.domain;

import static org.junit.Assert.*;
import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import com.ideas.sso.ActiveDirectoryUserInfo;
import com.ideas.sso.AuthenticationError;
import waffle.windows.auth.IWindowsAccount;
import waffle.windows.auth.impl.WindowsAuthProviderImpl;

public class EmployeeRepositoryTest {
	private static EmployeeRepository repository;
	private static EmployeeRepositorySupport repositorySupport;
	
	@BeforeClass
	public static void setUpConnectionAndTables() throws Exception {
		repositorySupport = new EmployeeRepositorySupport();
		repository = repositorySupport.createTableAndRepository();
	}

	@AfterClass
	public static void destroyTables() throws SQLException{
		repositorySupport.dropTable();
	}
	
	@After
	public void cleanTables() throws SQLException{
		repositorySupport.cleanTable();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void RepositoryCannotWorkWithoutAConnection() {
		new EmployeeRepository(null);
		fail("No valid connection provided. Repository should not be able to operate");
	}
	
	@Test
	public void RepositoryNotContainingEmployeeReturnsFalse(){
		boolean containsEmployee = repository.getEmployeeDetails("someEmployeeID");
		Assert.assertFalse(containsEmployee);
	}

	@Test
	public void RepositoryStoresEmployeeDetails(){
		Address address = new Address(0.0, 0.0, "Some location");
		Employee employee = new Employee("idnsor", "2066002133", address);
		boolean isEmployeeAdded = repository.addEmployee(employee);
		Assert.assertTrue(isEmployeeAdded);
	}
	
	@Test
	public void checkForFirstTimeUser() throws Exception {
		boolean employeeAlreadyExists = repository.getEmployeeDetails("idnais");
		assertFalse(employeeAlreadyExists);
		
	}
	@Test
	public void checkForExistingUser() throws Exception {
		Address address = new Address(0.0, 0.0, "Pune");
		Employee employee = new Employee("idnadg", "2066002133", address);
		boolean isEmployeeAdded = repository.addEmployee(employee);
		boolean employeeAlreadyExists = repository.getEmployeeDetails(employee.getUsername());
		assertTrue(employeeAlreadyExists);
	}
	
	
}
