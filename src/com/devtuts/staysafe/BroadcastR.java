package com.devtuts.staysafe; 

import com.devtuts.helpme.R;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

public class BroadcastR extends BroadcastReceiver {

	TelephonyManager telephonyManager;

	double lat, lon;
	String sendData = "";

	@Override
	public void onReceive(Context c, Intent in) {

		Bundle bundle = in.getExtras();
		SmsMessage[] msgs = null;
		String msg = "";
		String contact = "";

		if (bundle != null) {
			// ---retrieve the SMS message received---
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];

			for (int i = 0; i < msgs.length; i++) {

				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				contact = msgs[i].getOriginatingAddress();
				msg += msgs[i].getMessageBody().toString();
			}
		}
		if (msg.equalsIgnoreCase("Location")) {
			
			DatabaseHandler db = new DatabaseHandler(c);
						
			boolean isNumber = db.IsNumberPresent(contact);

			if (!isNumber) {

				SmsManager sms = SmsManager.getDefault();
				sms.sendTextMessage(
						contact,
						null,
						"You dont have permission to see their location. Ask them for the Permission First or give correct format!",
						null, null);
				abortBroadcast();

			} else {

				//c.startService(new Intent(c, ServiceS.class));
				LocationManager locationManager = null;
				LocationListener mlocListener = null;
				locationManager = (LocationManager) c
						.getSystemService(Context.LOCATION_SERVICE);

				Location location;

				if (locationManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
					mlocListener = new MyLocationListener();
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER, 1000, 1,
							mlocListener);

					location = locationManager
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

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
					sendData += "Wireless Network is Off ";
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
				// Set Notification Title
				String title = "Stay Safe!";
				String detail = contact
						+ " just saw your location! Remove them from Guardian if you dont want them to see you! ";
				// Open NotificationView Class on Notification Click
				Intent intent = new Intent(c, MainPage.class);
				// Send data to NotificationView Class
				intent.putExtra("title", "Stay Safe!");
				intent.putExtra("text", sendData);
				// Open NotificationView.java Activity
				PendingIntent pIntent = PendingIntent.getActivity(c, 0, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);

				// Create Notification using NotificationCompat.Builder
				NotificationCompat.Builder builder = new NotificationCompat.Builder(
						c)
				// Set Icon
						.setSmallIcon(R.drawable.ic_launcher)
						// Set Ticker Message
						.setTicker(detail)
						// Set Title
						.setContentTitle(title)
						// Set Text
						.setContentText(detail)
						// Add an Action Button below Notification
						.addAction(R.drawable.ic_launcher, "Action Button", pIntent)
						// Set PendingIntent into Notification
						.setContentIntent(pIntent)
						// Dismiss Notification
						.setAutoCancel(true);

				// Create Notification Manager
				NotificationManager notificationmanager = (NotificationManager) c
						.getSystemService(Context.NOTIFICATION_SERVICE);
				// Build Notification with Notification Manager
				notificationmanager.notify(0, builder.build());

				SmsManager sms = SmsManager.getDefault();
				sms.sendTextMessage(contact, null, sendData, null, null);
				abortBroadcast();
			}
		}
	}
}
