package com.ideas.sso;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com4j.COM4J;
import com4j.Variant;
import com4j.typelibs.activeDirectory.IADs;
import com4j.typelibs.ado20.Fields;
import com4j.typelibs.ado20._Command;
import com4j.typelibs.ado20._Connection;
import com4j.typelibs.ado20.ClassFactory;
import com4j.typelibs.ado20._Recordset;
import waffle.windows.auth.IWindowsAccount;
import waffle.windows.auth.impl.WindowsAuthProviderImpl;

public class ActiveDirectoryUserInformation {
	public static void main(String[] args){
/*		WindowsAuthProviderImpl provider = new WindowsAuthProviderImpl();
		IWindowsIdentity user = provider.logonUser("myusername", "mypassword");
		if(user.isGuest())
			System.out.println("Guest");
		else
			System.out.println("Owner");
	
		WindowsAuthProviderImpl provider = new WindowsAuthProviderImpl();
		IWindowsAccount account = provider.lookupAccount("idnsor");
		System.out.println(account.getSidString() + " | " + account.getDomain() + " | " + account.getName() + " | " + account.getFqn());
*/
		WindowsAuthProviderImpl provider = new WindowsAuthProviderImpl();
		IWindowsAccount account = provider.lookupAccount("idnpam");
		String requestedFields = "distinguishedName,userPrincipalName,telephoneNumber,mail,sAMAccountName,sn,givenName,employeeID,department,title";
		ActiveDirectoryUserInfo1 userInfo = ActiveDirectoryUserInfo1.getInstance(account.getFqn(), requestedFields);
	}
}

