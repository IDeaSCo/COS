package com.ideas.sso;

public class UserDTO {
	private final String employeeID;
	private final String name;
	private final String email;
	
	public UserDTO(String employeeID, String name, String email){
		this.employeeID = employeeID;
		this.name = name;
		this.email = email;
	}
	
	public String getEmployeeID(){
		return employeeID;
	}
	
	public String getName(){
		return name;
	}
	
	public String getEmail(){
		return email;
	}
}
