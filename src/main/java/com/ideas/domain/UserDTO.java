package com.ideas.domain;

public class UserDTO {
	private String employeeID;
	private String name;
	private String emailId;
	private String mobile = null;
	private Address address = null;
	
	public UserDTO(String employeeID, String name, String email) {
		this.employeeID = employeeID;
		this.name = name;
		this.emailId = email.substring(0, email.indexOf("@"));
	}
	
	public UserDTO(String employeeID, String mobile, Address address){
		this.employeeID = employeeID;
		this.mobile = mobile;
		this.address = address;
	}

	public String getEmployeeID() {
		return employeeID;
	}

	public String getName() {
		return name;
	}

	public String getEmailId() {
		return emailId;
	}
	
	public String getMobile(){
		return mobile;
	}
	
	public Address getAddress(){
		return address;
	}
	
	@Override
	public boolean equals(Object other){
		if(this == null || other == null)
			return false;
		UserDTO that = (UserDTO) other;
		if(this.getClass() == that.getClass())
			return true;
		return this.employeeID.equals(that.employeeID)
				&& this.name.equals(that.name)
				&& this.emailId.equals(that.emailId);
	}
}
