package com.devtuts.staysafe;
 
public class HelpRecordModel {

	private String contactName = "";
	private String email = "";
	private String msg = "";
	private String contactNumber = ""; 
	public HelpRecordModel() {

	} 

	public String getContactName() {
		return this.contactName;
	}
	public String getContactNumber() {
		return this.contactNumber;
	}
	
	public String getEmail() {
		return this.email;
	}
	public String getMessage() {
		return this.msg;
	} 
	
	public void setContactName(String name) {
		this.contactName = name;
	}

	public void setContactNumber(String number) {
		this.contactNumber = number;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public void setMessage(String msg) {
		this.msg = msg;
	}
}
