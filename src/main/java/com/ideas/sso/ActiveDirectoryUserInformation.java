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
		IWindowsAccount account = provider.lookupAccount("idnsor");
		String requestedFields = "distinguishedName,userPrincipalName,telephoneNumber,mail,sAMAccountName,sn,givenName,employeeID,department,title";
		ActiveDirectoryUserInfo userInfo = ActiveDirectoryUserInfo.getInstance(account.getFqn(), requestedFields);
	}
}

class ActiveDirectoryUserInfo{
	static HashMap<String, ActiveDirectoryUserInfo> knownUsers = new HashMap<String, ActiveDirectoryUserInfo>();
	private final Map<String, String> infoMap = new HashMap<String, String>();
	static String defaultNamingContext = null;
	
	private ActiveDirectoryUserInfo(String username, String requestedFields){
		infoMap.clear();
		initNamingContext();
		_Connection connection = ClassFactory.createConnection();
		connection.provider("ADsDSOObject");
		connection.open("Active Directory Provider", "", "", -1);
		_Command command = ClassFactory.createCommand();
		command.activeConnection(connection);
		String searchField = "userPrincipalName";
		int pSlash = username.indexOf('\\');
		if(pSlash > 0){
			searchField = "sAMAccountName";
			username = username.substring(pSlash + 1);
		}
		command.commandText("<LDAP://" + defaultNamingContext + ">;(" + searchField + "=" + username + ");" + requestedFields + ";subTree");
		_Recordset rs = command.execute(null, Variant.getMissing(), -1);
		if(!rs.eof()){
			Fields userData = rs.fields();
			if(userData != null){
				buildInfoMap(userData, requestedFields);				
			}
			else
				System.out.println("No user information");
			rs.close();
			connection.close();
		}
		else
			System.out.println("Username not found");
	}
	
	void buildInfoMap(Fields userData, String requestedFields){
		StringTokenizer tokenizer = new StringTokenizer(requestedFields, ",");
		String detail;
		
		while(tokenizer.hasMoreTokens()){
			detail = tokenizer.nextToken();
			Object object = userData.item(detail).value();
			if(object != null)
				System.out.println(detail + ": " + object.toString());
		}
	}
	
	void initNamingContext(){
		if(defaultNamingContext == null){
			IADs rootDSE = COM4J.getObject(IADs.class, "LDAP://RootDSE", null);
			defaultNamingContext = (String) rootDSE.get("defaultNamingContext");
		}
	}
	
	public static ActiveDirectoryUserInfo getInstance(String username, String requestedFields){
		ActiveDirectoryUserInfo foundUser = knownUsers.get(username);
		if(foundUser != null)
			return foundUser;
		return getInstanceNoCache(username, requestedFields);
	}
	
	public static ActiveDirectoryUserInfo getInstanceNoCache(String username, String requestedFields){
		ActiveDirectoryUserInfo newUserInfo = new ActiveDirectoryUserInfo(username, requestedFields);
		if(newUserInfo.infoMap.isEmpty())
			return null;
		knownUsers.put(username, newUserInfo);
		return newUserInfo;
	}
}