package com.ideas.sso;

import com4j.COM4J;
import com4j.Variant;
import com4j.typelibs.activeDirectory.IADs;
import com4j.typelibs.ado20.ClassFactory;
import com4j.typelibs.ado20.Fields;
import com4j.typelibs.ado20._Command;
import com4j.typelibs.ado20._Connection;
import com4j.typelibs.ado20._Recordset;
import com4j.ComException;

public class ActiveDirectoryUserInfo {
	private Fields userData;
	static String defaultNamingContext = null;
	
	private ActiveDirectoryUserInfo(String username, String requestedFields) throws AuthenticationError{
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
			userData = rs.fields();
			if(userData == null)
				throw new AuthenticationError("User information not found");
			rs.close();
			connection.close();
		}
		else
			throw new AuthenticationError("Username cannot be found");
	}
	
	void initNamingContext(){
		if(defaultNamingContext == null){
			IADs rootDSE = COM4J.getObject(IADs.class, "LDAP://RootDSE", null);
			defaultNamingContext = (String) rootDSE.get("defaultNamingContext");
		}
	}
	
	public static UserDTO getUserInfo(String username, String requestedFields) throws AuthenticationError {
		ActiveDirectoryUserInfo userInfo = new ActiveDirectoryUserInfo(username, requestedFields);
		UserDTO userDTO = extractUserInfo(userInfo);
		return userDTO;
	}
	
	private static UserDTO extractUserInfo(ActiveDirectoryUserInfo userInfo){
		String employeeID;
		String firstName;
		String lastName;
		String email;
		Object object;
		
		try{
			object = userInfo.userData.item("employeeID").value();
			employeeID = object.toString();
		}catch(ComException e){
			employeeID = "";
		}
		try{
			object = userInfo.userData.item("givenName").value();
			firstName = object.toString();
			object = userInfo.userData.item("sn").value();
			lastName = object.toString();
		}catch(ComException e){
			firstName = "";
			lastName = "";
		}
		try{
			object = userInfo.userData.item("mail").value();
			email = object.toString();
		}catch(ComException e){
			email = "";
		}
		UserDTO userDTO = new UserDTO(employeeID, firstName + lastName, email);
		return userDTO;
	}
}
