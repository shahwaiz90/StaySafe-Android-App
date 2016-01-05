package com.devtuts.staysafe;
 

import java.util.Vector;

import android.app.Service; 
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder; 
import android.telephony.SmsManager;
import android.widget.Toast;

public class WidgetService extends Service { 
	 
	
	private String getUserLocation(){
		double lat;
		String sendData = "";
		double lon;
		LocationManager locationManager = null;
		LocationListener mlocListener = null;
		locationManager = (LocationManager) getApplicationContext() .getSystemService(Context.LOCATION_SERVICE);

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
			sendData += "Wireless Network Off ";
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
				sendData += "GPS Off ";
			}
		}

		if (lat == 0 || lon == 0) {
			sendData += "Location temporarily unavailable. Please try later";
		} else {
			sendData += "\nLink To Google Maps: \nhttps://maps.google.com/maps?q="
					+ lat + "+" + lon;
		}
		return sendData;
	}
 
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStart(intent, startId);
		 
        updateMood(intent);
			
		stopSelf(startId);
		
		return START_STICKY;
	} 
	public void sendSMS(){
		try{
			Vector<AddGuardianModel>    guardianModelVector = new Vector<AddGuardianModel>();
			DatabaseHandler db  	 = 	new DatabaseHandler(getApplicationContext());
			guardianModelVector 	 = 	db.getGuardianMembers();
			String message 			 =	db.getGuardianMessage();
			SmsManager sms 			 = 	SmsManager.getDefault(); 
			String myCurrentLocation =  getUserLocation();
			
			for (int i = 0; i < guardianModelVector.size(); i++) { 
				
				String number = guardianModelVector.elementAt(i).getNumber().toString(); 
				sms.sendTextMessage(number, null, message + myCurrentLocation+" - Stay Safe", null, null);
			}
			if(guardianModelVector.size() > 0){
				Toast.makeText(getApplicationContext(), "Your Custom Message with Your Location Has Been Sent, So Don't Worry Someone Will Contact You Soon! :-)", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(getApplicationContext(), "No Guardian List Set Yet! Hurry Up Make Your List!:-)", Toast.LENGTH_LONG).show();
			}
		}catch(Exception e){
			//Log.e("Exception", e.getMessage()); 
		}
	}
	private void updateMood(Intent intent) { 
		
        if (intent != null){
    		String requestedAction = intent.getAction(); 
    		
    		if (requestedAction != null && requestedAction.equals("ONCLICK")){ 
    		 	sendSMS(); 
    		}
        }
	}

}
