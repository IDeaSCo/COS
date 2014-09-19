package com.ideas.sso;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com4j.COM4J;
import com4j.ComException;
import com4j.Variant;
import com4j.typelibs.activeDirectory.IADs;
import com4j.typelibs.ado20.ClassFactory;
import com4j.typelibs.ado20.Fields;
import com4j.typelibs.ado20._Command;
import com4j.typelibs.ado20._Connection;
import com4j.typelibs.ado20._Recordset;

public class ActiveDirectoryUserInfo1 {
	static HashMap<String, ActiveDirectoryUserInfo1> knownUsers = new HashMap<String, ActiveDirectoryUserInfo1>();
	private final Map<String, String> infoMap = new HashMap<String, String>();
	static String defaultNamingContext = null;
	private Fields userData;
	private UserDTO userDetails;

	public ActiveDirectoryUserInfo1(String username, String requestedFields) {
		infoMap.clear();
		initNamingContext();
		_Connection connection = ClassFactory.createConnection();
		connection.provider("ADsDSOObject");
		connection.open("Active Directory Provider", "", "", -1);
		_Command command = ClassFactory.createCommand();
		command.activeConnection(connection);
		String searchField = "userPrincipalName";
		int pSlash = username.indexOf('\\');
		if (pSlash > 0) {
			searchField = "sAMAccountName";
			username = username.substring(pSlash + 1);
		}
		command.commandText("<LDAP://" + defaultNamingContext + ">;("
				+ searchField + "=" + username + ");" + requestedFields
				+ ";subTree");
		_Recordset rs = command.execute(null, Variant.getMissing(), -1);
		if (!rs.eof()) {
			this.userData = rs.fields();
			if (this.userData != null) {
				buildInfoMap(requestedFields);
			} else
				System.out.println("No user information");
			rs.close();
			connection.close();
		} else
			System.out.println("Username not found");
	}

	public void buildInfoMap(String requestedFields) {
		StringTokenizer tokenizer = new StringTokenizer(requestedFields, ",");
		String detail;

		// while(tokenizer.hasMoreTokens()){
		// detail = tokenizer.nextToken();
		// Object object = this.userData.item(detail).value();
		// if(object != null)
		// System.out.println(detail + ": " + object.toString());
		// }
		userDetails = this.extractUserInfo();
	}

	void initNamingContext() {
		if (defaultNamingContext == null) {
			IADs rootDSE = COM4J.getObject(IADs.class, "LDAP://RootDSE", null);
			defaultNamingContext = (String) rootDSE.get("defaultNamingContext");
		}
	}

	public static ActiveDirectoryUserInfo1 getInstance(String username,
			String requestedFields) {
		ActiveDirectoryUserInfo1 foundUser = knownUsers.get(username);
		if (foundUser != null)
			return foundUser;
		return getInstanceNoCache(username, requestedFields);
	}

	public static ActiveDirectoryUserInfo1 getInstanceNoCache(String username,
			String requestedFields) {
		ActiveDirectoryUserInfo1 newUserInfo = new ActiveDirectoryUserInfo1(
				username, requestedFields);
		if (newUserInfo.infoMap.isEmpty())
			return null;
		knownUsers.put(username, newUserInfo);
		return newUserInfo;
	}

	private UserDTO extractUserInfo() {
		String employeeID;
		String firstName;
		String lastName;
		String email;
		Object object;

		try {
			object = this.userData.item("employeeID").value();
			employeeID = object.toString();
		} catch (ComException e) {
			employeeID = "";
		}
		try {
			object = this.userData.item("givenName").value();
			firstName = object.toString();
			object = this.userData.item("sn").value();
			lastName = object.toString();
		} catch (ComException e) {
			firstName = "";
			lastName = "";
		}
		try {
			object = this.userData.item("mail").value();
			email = object.toString();
		} catch (ComException e) {
			email = "";
		}
		UserDTO userDTO = new UserDTO(employeeID, firstName + " " + lastName,
				email);
		return userDTO;
	}

	public UserDTO getUserDetails() {
		return this.userDetails;
	}
}