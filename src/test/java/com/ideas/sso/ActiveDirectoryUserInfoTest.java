package com.ideas.sso;

import static org.junit.Assert.*;
import org.junit.Test;
import com.ideas.domain.Employee;
import com.ideas.sso.ActiveDirectoryUserInfo;
import com.ideas.sso.AuthenticationError;

public class ActiveDirectoryUserInfoTest {
	private String REQUESTEDFILEDS = "employeeID,sn,givenName,mail";
	
	@Test(expected = AuthenticationError.class)
	public void SearchForNonExistingUserThrowsException() throws AuthenticationError{
		Employee retrievedUserInfo = new ActiveDirectoryUserInfo("someUsername", REQUESTEDFILEDS).getUserDetails();
		fail("Test was supposed to throw exception");
	}
	
	@Test
	public void SearchForExistingUserReturnsCorrectDetails(){
		String requestedFields = "employeeID,sn,givenName,mail";
		Employee userInfo = new Employee("32560", "Sonam Rasal", "Sonam.Rasal@ideas.com");
		Employee retrievedUserInfo = null;
		try {
			retrievedUserInfo = new ActiveDirectoryUserInfo("ROW\\idnsor", REQUESTEDFILEDS).getUserDetails();
			assertTrue(userInfo.equals(retrievedUserInfo));
		} catch (AuthenticationError e) {
			fail("Unexpected exception occurred");
		}
	}
}
