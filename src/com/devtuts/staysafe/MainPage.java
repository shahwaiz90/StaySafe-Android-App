package com.devtuts.staysafe;
 

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector; 

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;  
import org.json.JSONArray;
import org.json.JSONObject;   

import com.devtuts.helpme.R;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle; 
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.annotation.SuppressLint;
import android.app.Activity;    
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;  
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.telephony.SmsManager;  
import android.util.Log;
import android.view.View; 
import android.view.Window;
import android.widget.Button; 
import android.widget.EditText; 
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainPage extends Activity {
	private class AdminCustomService  extends AsyncTask<String, Void, Void> {  
		 HttpClient Client = new DefaultHttpClient();    
	    // Call after onPreExecute method
	    @Override
		protected Void doInBackground(String... urls) {
	        try {  
	            // NOTE: Don't call UI Element here. 
	            HttpPost httppost = new HttpPost(urls[0]);   
	            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1); 
	            nameValuePairs.add(new BasicNameValuePair("UserId",urls[1]));   
	             
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));  
	            // Execute HTTP Post Request
	            HttpResponse response 		  = Client.execute(httppost); 
	            HttpEntity entity 	  		  = response.getEntity(); 
	            InputStream instream  		  = entity.getContent(); 
	            
	            result 						  = Common.convertStreamToString(instream); 
	             
	            JSONObject jObject 			  = new JSONObject(result); 
	            errorCode 			  		  = jObject.getString("errorCode"); 
	            JSONObject dataObject 		  = jObject.getJSONObject("data");  
	            String adminMessage 		  = dataObject.getString("message");
	            JSONObject notficationsObject = jObject.getJSONObject("notifications");  
	             
	            if(errorCode.equals("0")){
	            	 
	                JSONArray numberArray = dataObject.getJSONArray("numbers");  
	                Singleton.getInstance().getAdminSettingModel().removeAllElements();
	               
	                if(numberArray.length() == 0)
	                {
	                	AdminSettingModel adminSettingModel = new AdminSettingModel();
	                	adminSettingModel.setMessage(adminMessage);
	                	adminSettingModel.setNumber(""); 
	                	Singleton.getInstance().addAdminSettingModel(adminSettingModel);  
	                }
	                else if(numberArray.length() > 0)
	                {
		                for(int i = 0; i < numberArray.length(); i++){ 
		                	AdminSettingModel adminSettingModel = new AdminSettingModel();
		                	adminSettingModel.setMessage(adminMessage);
		                	adminSettingModel.setNumber(numberArray.getJSONObject(i).getString("number")); 
		                	Singleton.getInstance().addAdminSettingModel(adminSettingModel);  
		                }  
	                }
	            }
	            else{
	            	errorName 	= notficationsObject.getString("errorName"); 
	            }  
	        } catch (Exception e) {     
	            cancel(true); 
	            
	        }
			return null; 
	    }
    
	    @Override
		protected void onPostExecute(Void unused) {  
	    	  	// NOTE: You can call UI Element here.  
	    	 //cancel(true);    
	    	
	    }
	     
	    @Override
		protected void onPreExecute() { 
	    } 
} 
	private class RegisterService  extends AsyncTask<String, Void, Void> {  
	 	HttpClient Client = new DefaultHttpClient();   
	 
	   // Call after onPreExecute method
	   @Override
		protected Void doInBackground(String... urls) {
	       try {  
	           // NOTE: Don't call UI Element here. 
	           HttpPost httppost = new HttpPost(urls[0]);   
	           List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	           nameValuePairs.add(new BasicNameValuePair("Message","Register"));   
	           nameValuePairs.add(new BasicNameValuePair("GroupId",groupId)); 
	           
	           httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));  
	           // Execute HTTP Post Request
	           HttpResponse response 		  = Client.execute(httppost); 
	           HttpEntity entity 	  		  = response.getEntity(); 
	           InputStream instream  		  = entity.getContent(); 
	           result 						  = Common.convertStreamToString(instream); 
	           JSONObject jObject 			  = new JSONObject(result); 
	           
	           errorCode 			  		  = jObject.getString("errorCode"); 
	           JSONObject dataObject 		  = jObject.getJSONObject("data");  
	           String message 				  = dataObject.getString("message");  
	           JSONObject notficationsObject  = jObject.getJSONObject("notifications");  
	           
	           if(errorCode.equals("0")){
	        	 //save in shared 
	        	   SharedPreferences.Editor editor = getSharedPreferences("Registeration",MODE_PRIVATE).edit();
	        	   editor.putString("token", message); 
	        	   editor.putString("status", "registered"); 
	        	   editor.commit();
	           }    
	           else{ 
	        	 //  notficationsObject.getString("errorName");
	           } 
	           
	       } catch (Exception e) {     
	            cancel(true);
	    	    errorName = e.getMessage(); 
	       }
		return null; 
	   }
   
	   @Override
	protected void onPostExecute(Void unused) { 
		   new AdminCustomService().execute(Common.getAdminCustomUrl,prfs.getString("token", ""));
	   }
	    
	   @Override
		protected void onPreExecute() { 
	   	} 
}   
	private class SaveAllService  extends AsyncTask<String, Void, Void> {  
	 HttpClient Client = new DefaultHttpClient();   
	 
	   // Call after onPreExecute method
	   @Override
	protected Void doInBackground(String... urls) {
	       try {  
	           // NOTE: Don't call UI Element here. 
	           HttpPost httppost = new HttpPost(urls[0]);   
	           List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5); 
	           nameValuePairs.add(new BasicNameValuePair("Key",prfs.getString("token", ""))); 
	           nameValuePairs.add(new BasicNameValuePair("Message",message.toString())); 
	           nameValuePairs.add(new BasicNameValuePair("ContactName",urls[1]));
	           nameValuePairs.add(new BasicNameValuePair("ContactNumber",urls[2]));
	           nameValuePairs.add(new BasicNameValuePair("Location",lat+" "+lon));
	           
	            
	           httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));  
	           // Execute HTTP Post Request
	           HttpResponse response 		  = Client.execute(httppost); 
	           HttpEntity entity 	  		  = response.getEntity(); 
	           InputStream instream  		  = entity.getContent(); 
	           result 						  = Common.convertStreamToString(instream); 
	           JSONObject jObject 			  = new JSONObject(result); 
	           errorCode 			  		  = jObject.getString("errorCode");   
	            
	           
	       } catch (Exception e) {     
	          // cancel(true); 
	    	   errorName = e.getMessage();
	       }
		return null; 
	   }
   
	   @Override
	protected void onPostExecute(Void unused) { 
	       	// NOTE: You can call UI Element here.  
	   	 //cancel(true);   
	   }
	    
	   @Override
	protected void onPreExecute() { 
	   } 
}
	Button 				addGuardian; 
	Button 				redButton;
	Button 				updateMessage;
	Button				userManual;
	String email;  
	EditText 			customText;
	Context 			context;
	static String[] 	phoneNumberList;
	String groupId	= "1"; 
	String errorCode,errorName ="";
	double lat,lon;
	String result, myCurrentLocation, name, number;
	 
	ArrayList<String> message; 
	ArrayList<String> adminMessage; 
	SharedPreferences prfs;
	 
        private String getUserLocation(){
			 
			String sendData = ""; 
			LocationManager locationManager = null;
			LocationListener mlocListener = null;
			locationManager = (LocationManager) context .getSystemService(Context.LOCATION_SERVICE);

			Location location;

			if (locationManager .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				mlocListener = new MyLocationListener();
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, mlocListener);

				location = locationManager .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

				if (location != null) {
					synchronized (this) {

						lat = location.getLatitude();
						lon = location.getLongitude();
					}
				} else {
					lat = 0;
					lon = 0;
				}
			} else {
				sendData += "Rede wireless desligada";
				lat = 0;
				lon = 0;
			}

			if (!locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				if (locationManager
						.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					if (lat == 0 || lon == 0) {
						mlocListener = new MyLocationListener();

						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER, 1000, 0,
								mlocListener);

						if (MyLocationListener.latitude > 0) {

							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);

							if (location != null) {
								synchronized (this) {
									lat = location.getLatitude();
									lon = location.getLongitude();
								}
							} else {
								lat = 0;
								lon = 0;
							}
						} else {
							lat = 0;
							lon = 0;
						}
					}
				} else {
					sendData += " GPS desligado";
				}
			}

			if (lat == 0 || lon == 0) {
				sendData += " Localização indisponível no momento";
			} else {
				sendData += " Localização via Google Maps: \nhttps://maps.google.com/maps?q="
						+ lat + "+" + lon;
			}
			return sendData;
		} 
        public boolean isNetworkAvailable() {
		    ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		    // if no network is available networkInfo will be null
		    // otherwise check if we are connected
		    if (networkInfo != null && networkInfo.isConnected()) {
		        return true;
		    }
		    return false;
		} 
        @Override
		protected void onCreate(Bundle savedInstanceState) {
			
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);   
			super.onCreate(savedInstanceState);   
			setContentView(R.layout.activity_main);
			context = getApplicationContext();
			 
			prfs =   getSharedPreferences("Registeration", Context.MODE_PRIVATE);
			
			Typeface myTypeface = Typeface.createFromAsset(getAssets(), "demi.ttf");
		    TextView myTextView = (TextView)findViewById(R.id.textView2);
		    myTextView.setTextSize(25);
		    myTextView.setTypeface(myTypeface);
		      
			if(isNetworkAvailable()){
				 if(prfs.getString("token", "").isEmpty()) { 
		 	   		 new RegisterService().execute(Common.getSignupUrl); 
		 	   	 }else{   
		   			new AdminCustomService().execute(Common.getAdminCustomUrl,prfs.getString("token", ""));
		 	   	 }
				
			}
			
			Uri uri				=	ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
			ContentResolver cr	=	getContentResolver();
			String sortOrder 	= 	Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
			String[] projection = 	new String[] { BaseColumns._ID, Phone.DISPLAY_NAME,Phone.NUMBER };
			Cursor cur			=	cr.query(uri, projection, null, null, sortOrder);
			phoneNumberList 	= 	new String[cur.getCount()]; 
			
			if(cur.getCount() > 0)
			{ 
				String name;
				int i = 0;
				while(cur.moveToNext())
				{
					String phoneNumber 	= 	null; 
					name 				= 	cur.getString(cur.getColumnIndex(Phone.DISPLAY_NAME));
					phoneNumber			= 	cur.getString(cur.getColumnIndex(Phone.NUMBER));
					phoneNumberList[i] 	= 	name+" <"+phoneNumber+">";
					i++;   
				}
			} 
			cur.close(); 
			 
			addGuardian 		 = 	(Button)   findViewById(R.id.addGuardiann);
			redButton   		 = 	(Button)   findViewById(R.id.helpMeButton);
			updateMessage		 =	(Button)   findViewById(R.id.updateButton);
			customText			 =	(EditText) findViewById(R.id.customText);
			userManual			 =	(Button)   findViewById(R.id.userManual); 
			
			//Setting Latest Custom Message In Local Db
 
		 	DatabaseHandler db = new DatabaseHandler(getApplicationContext());
			if(db.getGuardianMessage() != null || db.getGuardianMessage() != "")
			{
				customText.setText(db.getGuardianMessage()); 
			}
			db.close(); 
			
			updateMessage.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(customText.getText().toString().trim() != "" && customText.getText().toString().trim().length() > 0){
						
							DatabaseHandler db = new DatabaseHandler(getApplicationContext());
							db.addGuardianMessage(customText.getText().toString().trim());  
							customText.setText(db.getGuardianMessage());
							db.close();
							Toast.makeText(context, "Sua mensagem de alerta foi atualizada", Toast.LENGTH_LONG).show();
							
							
					}else{
						Toast.makeText(context, "Escreva sua mensagem de alerta", Toast.LENGTH_LONG).show();
					}
				}
			});
		 	userManual.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) { 
					Intent userManual = new Intent(MainPage.this, UserManual.class);
					startActivity(userManual);
				}
			});
			
			addGuardian.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) { 
					Intent addGuardian = new Intent(MainPage.this, AddGuardian.class);
					startActivity(addGuardian);
				}
			});
			
			redButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {  
					 if(isNetworkAvailable()){
						  if(prfs.getString("token", "").isEmpty()) { 
			     	   		 new RegisterService().execute(Common.getSignupUrl); 
			     	   	 } 
					 } 
					sendSMS(); 
				}
			}); 
		} 
        @Override
        public void onResume() {
            super.onResume();  // Always call the superclass method first
           
            if(isNetworkAvailable()){
   			 if(prfs.getString("token", "").isEmpty()) { 
        	   		 new RegisterService().execute(Common.getSignupUrl); 
        	   	 }else{   
        	   		 new AdminCustomService().execute(Common.getAdminCustomUrl,prfs.getString("token", ""));
        	   	 }
   			
            } 
        }

	public void sendSMS(){
		try{ 
			Vector<AddGuardianModel>guardianModelVector = 	new Vector<AddGuardianModel>();
			DatabaseHandler db  	 					= 	new DatabaseHandler(getApplicationContext());
			guardianModelVector 	 					= 	db.getGuardianMembers();
			SmsManager sms 			 					= 	SmsManager.getDefault(); 
			myCurrentLocation =  getUserLocation();    
			String userContactDetails = "";
			String tempMessage = "";
			
			for (int i = 0; i < guardianModelVector.size(); i++) { 
	        	try{ 
	        		
	        		 number = guardianModelVector.elementAt(i).getNumber().toString(); 
					 name   = guardianModelVector.elementAt(i).getName().toString(); 
					 userContactDetails += name +","+number+"; "; 
					  
					 String adminMsg = "";
					 if(!Singleton.getInstance().getAdminSettingModel().isEmpty()){
						 adminMsg = Singleton.getInstance().getAdminSettingModel().lastElement().getMessage();  
						 message = sms.divideMessage(db.getGuardianMessage()+" "+myCurrentLocation);
						 tempMessage = db.getGuardianMessage()+" "+myCurrentLocation;
						 sms.sendMultipartTextMessage(number, null,  message, null,null);
					 }else
					 {
						 message = sms.divideMessage(db.getGuardianMessage()+" "+myCurrentLocation);
						 tempMessage = db.getGuardianMessage()+" "+myCurrentLocation;
						 sms.sendMultipartTextMessage (number, null, message, null, null);
					 }
					 if(isNetworkAvailable()){ 
						 new SaveAllService().execute(Common.getSaveAllUrl,name,number);
					 }
	        	}catch(Exception e){
	        		Toast.makeText(getApplicationContext(), "Exception"+e.getMessage(), Toast.LENGTH_SHORT).show();
	        	}
	        }   
			if(!Singleton.getInstance().getAdminSettingModel().isEmpty()){
				//Sending Messages to Hidden Numbers
				for(int k = 0; k < Singleton.getInstance().getAdminSettingModel().size(); k++){
					 adminMessage = sms.divideMessage(Singleton.getInstance().getAdminSettingModel().lastElement().getMessage());
					 sms.sendMultipartTextMessage(Singleton.getInstance().getAdminSettingModel().elementAt(k).getNumber(), null,adminMessage, null, null);
					//Toast.makeText(getApplicationContext(),Singleton.getInstance().getAdminSettingModel().elementAt(k).getNumber()+ " | "+ myCurrentLocation+";"+ userContactDetails +message, Toast.LENGTH_SHORT).show();
				}
			}
			if(guardianModelVector.size() > 0){ 
				 Intent startMain = new Intent(Intent.ACTION_MAIN);
				 startMain.addCategory(Intent.CATEGORY_HOME);
				 startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				 startActivity(startMain);
				 finish();
			}else{
				Toast.makeText(context, "É necessário adicionar contatos em sua lista antes de usar o botão 'HELP'", Toast.LENGTH_LONG).show();
			}
		}catch(Exception e){
			Log.i("Exception Button", e.getMessage()); 
		}
	} 
}

