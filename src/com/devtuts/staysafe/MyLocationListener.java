package com.devtuts.staysafe;
 
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class MyLocationListener implements LocationListener {
	public static double latitude;
	public static double longitude;

	@Override
	public void onLocationChanged(Location location) {  
		latitude = location.getLatitude();
		longitude = location.getLongitude(); 
	}

	@Override
	public void onProviderDisabled(String s) {
	}

	@Override
	public void onProviderEnabled(String s) {
	}

	@Override
	public void onStatusChanged(String s, int i, Bundle b) {
	}

}