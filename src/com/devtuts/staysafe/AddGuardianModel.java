package com.devtuts.staysafe;
 
public class AddGuardianModel {

	private String number = ""; 
	private String name = "";
	private int id = 0;

	public AddGuardianModel() {

	} 

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}
	
	public String getNumber() {
		return this.number;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setNumber(String number) {
		this.number = number;
	}
}
