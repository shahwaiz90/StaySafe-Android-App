package com.devtuts.staysafe;
 

public class AdminSettingModel {
	String message = "";
	String  number;
	
	AdminSettingModel(){ 
	}
	
	String getMessage(){
		return this.message;
	}
	String  getNumber(){
		return this.number;
	}
	
	void setMessage(String msg){
		this.message = msg;
	}
	void setNumber(String numb){
		this.number= numb;
	}
}
