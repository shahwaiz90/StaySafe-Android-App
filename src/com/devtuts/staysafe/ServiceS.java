package com.devtuts.staysafe;
//package com.devtuts.helpme;
//
//import android.app.Service;
//import android.content.ContentResolver;
//import android.content.Intent;
//import android.database.ContentObserver;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Handler;
//import android.os.IBinder;
//import android.telephony.SmsManager;
//
//public class ServiceS extends Service {
//	/** Called when the activity is first created. */
//
//	ContentResolver contentResolver;
//	Uri uri = Uri.parse("content://sms/");
//	Handler handler;
//	static String lastS = "--1";
//	static String lastR = "--1";
//	ContentObserver observer;
//
//	@Override
//	public IBinder onBind(Intent arg0) {
//		return null;
//	}
//
//	@Override
//	public void onCreate() {
// 
//		Common.IS_SERVICE_RUNNING = "YES"; 
//		contentResolver = getContentResolver();
//		observer = new contentObserver(handler);
//		contentResolver.registerContentObserver(uri, true, observer);
//		super.onCreate();
//
//	}
//
//	public class contentObserver extends ContentObserver {
//		String rName;
//
//		public contentObserver(Handler handler) {
//			super(handler);
//
//		}
//
//		@Override
//		public void onChange(boolean selfChange) {
//			super.onChange(selfChange);
//
//			SmsManager sms = SmsManager.getDefault();
//			Cursor cur1 = getBaseContext().getContentResolver().query(Uri.parse("content://sms/"), null, null, null, null);
//			cur1.moveToNext(); // this will make it point to the first record,
//								// which is the last SMS sent
//			String body = cur1.getString(cur1.getColumnIndex("body")); // content
//																		// of
//																		// sms
//			String add = cur1.getString(cur1.getColumnIndex("address")); // phone
//																			// num
//			// String time = cur.getString(cur.getColumnIndex("date")); //date
//			String protocol = cur1.getString(cur1.getColumnIndex("protocol")); // protocol
//
//		 
//			if (protocol == null && Common.IS_SERVICE_RUNNING.equalsIgnoreCase("YES")) {
//				if (!lastS.equals(body)) {
//					// sms.sendTextMessage(CONTACT, null, "Sent Text to: " +
//					// rName + " " + add + " " + body, null, null);
//					lastS = body;
//				}
//			} else if (Common.IS_SERVICE_RUNNING.equalsIgnoreCase("YES")) {
//				if (!lastR.equals(body)) {
//					// sms.sendTextMessage(CONTACT, null, "Received Text from: "
//					// + rName + " " + add + " " + body, null, null);
//					lastR = body;
//				}
//			}
//		}
//	}
//
//	@Override
//	public void onDestroy() {
//
//		SmsManager sms = SmsManager.getDefault(); 
//		getApplicationContext().getContentResolver().unregisterContentObserver(observer);
//		super.onDestroy();
//	}
//
//}
