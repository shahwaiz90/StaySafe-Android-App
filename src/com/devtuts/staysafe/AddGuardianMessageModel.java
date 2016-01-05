package com.devtuts.staysafe;
 
public class AddGuardianMessageModel {

	private String msg = ""; 
	private int id = 0;

	public AddGuardianMessageModel() {

	} 

	public int getId() {
		return this.id;
	}

	public String getMessage() {
		return this.msg;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setMessage(String msg) {
		this.msg = msg;
	}
}
