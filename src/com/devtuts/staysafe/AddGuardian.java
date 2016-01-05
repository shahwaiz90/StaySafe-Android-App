package com.devtuts.staysafe;


import java.util.ArrayList;
import java.util.Vector;  

import com.devtuts.helpme.R;
 

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent; 
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button; 
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView; 
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class AddGuardian extends ListActivity {

	ArrayList<String> 			listItemsNames; 
	ArrayAdapter<String> 		adapter;
	Vector <AddGuardianModel> 	guardianModelVector;
	Context 					context;
	ListView 					grantedMembersList;
	CharSequence[] 				removePermission;
	Button 						addGuardian;
	MultiAutoCompleteTextView 	guardianNumber;
	String[] 					phoneNumberList;
	Button						userManual; 
	@Override
	public void onBackPressed() { 
		finish(); 
		Intent main 	= 	new Intent(AddGuardian.this, MainPage.class);
		Bundle b 		= 	new Bundle(); 
		b.putStringArray("PhoneNumbers", phoneNumberList);
		main.putExtras(b);
		startActivity(main);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.add_guardian); 
		 
		Typeface myTypeface = Typeface.createFromAsset(getAssets(), "demi.ttf");
	    TextView myTextView = (TextView)findViewById(R.id.textView2);
	    myTextView.setTextSize(25);
	    myTextView.setTypeface(myTypeface);
	    
		phoneNumberList 	 =	MainPage.phoneNumberList; 
		grantedMembersList	 = 	(ListView) findViewById(android.R.id.list); 
		addGuardian			 =  (Button) findViewById(R.id.addGuardian);
		guardianNumber 		 = 	(MultiAutoCompleteTextView) findViewById(R.id.guardianNumber);
		listItemsNames  	 =  new ArrayList <String> ();
		userManual			 = (Button) findViewById(R.id.userManual);
		adapter 			 =	new ArrayAdapter <String> (this, android.R.layout.simple_list_item_1, listItemsNames){
			
			 @Override
		        public View getView(int position, View convertView,
		                ViewGroup parent) {
		            View view =super.getView(position, convertView, parent);

		            TextView textView=(TextView) view.findViewById(android.R.id.text1);

		            /*YOUR CHOICE OF COLOR*/
		            textView.setTextColor(Color.WHITE); 
		            textView.setTextSize(20);
		            return view;
		        }
		};
		
		context 			 = 	this;  
		
		 ArrayAdapter<String> numberAdapter = new ArrayAdapter <String> (this, android.R.layout.simple_dropdown_item_1line ,phoneNumberList);
	     guardianNumber.setThreshold(1);
	     numberAdapter .setNotifyOnChange(true);
	     guardianNumber.setAdapter(numberAdapter);
	     guardianNumber.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer()); 
		 setListAdapter(adapter);
		 populateListItems();  
		
		 addGuardian.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) { 
				DatabaseHandler db = new DatabaseHandler(getApplicationContext());
				boolean isNumberPresent = db.IsNumberPresent(guardianNumber.getText().toString().trim());
				db.close();
				if(isNumberPresent){
					Toast.makeText(context, "Número duplicado não é permitido", Toast.LENGTH_SHORT).show();
				}
				else if(!guardianNumber.getText().toString().equals("") && !guardianNumber.getText().toString().trim().isEmpty()){
					DatabaseHandler db2 = new DatabaseHandler(getApplicationContext());
					db2.addGuardianMember(guardianNumber.getText().toString().trim(),"Pending");
					db2.close();
					listItemsNames.removeAll(listItemsNames);
					populateListItems();
				}else{
					Toast.makeText(context, "É necessário entrar com os dados do contato no campo acima.", Toast.LENGTH_SHORT).show();
				} 
			}
		}); 
		 userManual.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) { 
					Intent userManual = new Intent(AddGuardian.this, UserManual.class);
					startActivity(userManual);
				}
			});
		grantedMembersList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View v, final int position, long arg3) {
				removePermission = new CharSequence[] { "Clique para apagar" };
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("REMOÇÃO DE CONTATOS");

				builder.setItems(removePermission, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

							String number = guardianModelVector .get(position).getNumber();
							DatabaseHandler db = new DatabaseHandler(getApplicationContext());
							db.removeGuardianMember(number);
							db.close();
							listItemsNames.remove(position);
							adapter.notifyDataSetChanged();
							Toast.makeText(getApplicationContext(), "Afastado", Toast.LENGTH_LONG).show();
						}
				});
				builder.show(); 
				return true;
			}
		});
	}
	 
  
	public void populateListItems() {

		guardianModelVector = new Vector<AddGuardianModel>();
		DatabaseHandler db  = new DatabaseHandler(getApplicationContext());
		guardianModelVector = db.getGuardianMembers();

		for (int i = 0; i < guardianModelVector.size(); i++) {
 
			String number = guardianModelVector.elementAt(i).getNumber().toString();
			String name   = guardianModelVector.elementAt(i).getName().toString();

			listItemsNames.add(name+" "+number); 
			adapter.notifyDataSetChanged();

		}
	}
}
