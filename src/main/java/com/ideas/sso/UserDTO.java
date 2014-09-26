package com.ideas.sso;

public class UserDTO {
	private final String employeeID;
	private final String name;
	private final String emailId;

	public UserDTO(String employeeID, String name, String email) {
		this.employeeID = employeeID;
		this.name = name;
		this.emailId = email.substring(0, email.indexOf("@"));
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
