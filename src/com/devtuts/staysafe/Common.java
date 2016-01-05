package com.devtuts.staysafe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Common {

	public static String IS_SERVICE_RUNNING = "NO";
	public static String SEND_DATA = "NO";
	public static String themeName = "";  
	public static String getAdminCustomUrl		 = "http://www.entrevizinhos.com.br/app/adminCustom.php";
	public static String getSignupUrl			 = "http://www.entrevizinhos.com.br/app/signupService.php";
	public static String getSaveAllUrl			 = "http://www.entrevizinhos.com.br/app/saveall.php";
	
	public static String convertStreamToString(InputStream is) {

	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}
}
