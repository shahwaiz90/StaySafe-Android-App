package com.devtuts.staysafe;
 
import java.util.Vector;
 
public class  Singleton {
 	 
	public Vector<AdminSettingModel> adminSettingModel;
	
	public Vector<Singleton> quizAttributesObject;
	private static Singleton singleton;
	
	public static Singleton getInstance(){
		if(singleton == null){
			singleton = new Singleton();
		}
		return singleton;
	} 
	
	private  Singleton(){
		
		adminSettingModel 				= 	new Vector<AdminSettingModel>(); 
	}
	
	public void addAdminSettingModel(AdminSettingModel uModel){
		adminSettingModel.add(uModel);
	} 
	public Vector<AdminSettingModel> getAdminSettingModel(){
		return adminSettingModel;
	}
	 
}
