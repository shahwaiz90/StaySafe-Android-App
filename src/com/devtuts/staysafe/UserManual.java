package com.devtuts.staysafe;
  

import com.devtuts.helpme.R;

import android.app.Activity; 
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle; 
import android.view.Window; 
import android.widget.TextView;
 
public class UserManual extends Activity { 
	
	String[] 					phoneNumberList;
	
	@Override
	public void onBackPressed() { 
		finish(); 
		Intent main 	= 	new Intent(UserManual.this, MainPage.class);
		Bundle b 		= 	new Bundle(); 
		b.putStringArray("PhoneNumbers", phoneNumberList);
		main.putExtras(b);
		startActivity(main);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.user_manual); 
		phoneNumberList 	 =	MainPage.phoneNumberList; 
		Typeface myTypeface = Typeface.createFromAsset(getAssets(), "demi.ttf");
	    TextView myTextView = (TextView)findViewById(R.id.textView2);
	    myTextView.setTextSize(25);
	    myTextView.setTypeface(myTypeface); 
	    
	}
 
}
