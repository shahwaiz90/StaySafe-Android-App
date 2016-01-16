package com.devtuts.staysafe;
 
import java.util.Vector; 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION 			=   1; 
	// Database Name
	private static final String DATABASE_NAME 			= 	"StaySafD";

	// Table names 
	private static final String TABLE_GUARDIAN_MEMBERS  		= 	"TableGuardianContacts"; 
	private static final String TABLE_GUARDIAN_MESSAGE  		= 	"TableGuardianMessage"; 
	private static final String TABLE_HELP  			= 	"TableHelp";   

	// TABLE_HELP Table Columns names
	private static final String USER_ID 				= 	"id";  
	private static final String USER_EMAIL	 	 		= 	"email";
	private static final String CONTACT_NAME	  		= 	"contactname";
	private static final String CONTACT_NUMBER	  		= 	"contactnumber";
	private static final String USER_MESSAGE		  	= 	"message";
	
	// TABLE_GUARDIAN_MEMBERS Table Columns names
	private static final String GUARDIAN_ID 			= 	"id";  
	private static final String GUARDIAN_MEMBER_NUMBER  		= 	"number";
	private static final String GUARDIAN_MEMBER_NAME    		= 	"username"; 
	
	
	// TABLE_GUARDIAN_MESSAGE Table Columns names
	private static final String GUARDIAN_MSG_ID 			= 	"id";  
	private static final String GUARDIAN_MEMBER_MSG  		= 	"message"; 
	
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	//	GET GUARDIAN NUMBER
	void addGuardianMember(String number,String status) {
		String[] parsedNumber = null;
		String[] finalParsedNumber;
		boolean flag = false;
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values =  new ContentValues();
		for(int i = 0; i < number.length(); i++){
			if(number.charAt(i) == '<' || number.charAt(i) == '>' ){
				flag = true;	
			}
		} 
		if(flag){
			parsedNumber 		 = 	number.split("<");
			finalParsedNumber	 =	parsedNumber[1].split(">"); 
			values.put(GUARDIAN_MEMBER_NUMBER, finalParsedNumber[0]);
			values.put(GUARDIAN_MEMBER_NAME, parsedNumber[0].toString());

		}else{
			values.put(GUARDIAN_MEMBER_NUMBER, number);
			values.put(GUARDIAN_MEMBER_NAME, "Not Saved");
		}
		  
		//Inserting Row
		db.insert(TABLE_GUARDIAN_MEMBERS, null, values);
		db.close(); // Closing database connection
	}

	// GET GUARDIAN MESSAGE
	void addGuardianMessage(String msg) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(GUARDIAN_MEMBER_MSG, msg); 
		// Inserting Row
		db.insert(TABLE_GUARDIAN_MESSAGE, null, values);
		db.close(); // Closing database connection
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////
	//Add User Detail
	void addHelpRecord(String number,String name, String email,String message) { 
		 
		ContentValues values =  new ContentValues();
		SQLiteDatabase db = this.getWritableDatabase();  

		values.put(USER_EMAIL, email);
		values.put(CONTACT_NAME, name);
		values.put(CONTACT_NUMBER, number);  
		values.put(USER_MESSAGE, message);  
		
		// Inserting Row
		db.insert(TABLE_HELP, null, values);
		// Closing database connection
		db.close(); 
		
	}
	Vector<AddGuardianModel> getGuardianMembers() {

		Vector<AddGuardianModel> vectorObject = new Vector <AddGuardianModel>();

		String selectQuery = "SELECT  * FROM " + TABLE_GUARDIAN_MEMBERS;
		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);
		cursor.moveToFirst();

		int count = cursor.getCount();

		for (int i = 0; i < count; i++) {
			AddGuardianModel addGuardianModel = new AddGuardianModel();

			addGuardianModel.setId(Integer.parseInt(cursor.getString(0))); 
			addGuardianModel.setNumber(cursor.getString(1));
			addGuardianModel.setName(cursor.getString(2));
			vectorObject.add(addGuardianModel);
			cursor.moveToNext();
		} 
		db.close(); // Closing database connection
		return vectorObject;
	}  
String  getGuardianMessage() {
		String latestMessage = "";
			try{
				String selectQuery = "SELECT  * FROM " + TABLE_GUARDIAN_MESSAGE+" ORDER BY "+GUARDIAN_MSG_ID+" DESC";
				SQLiteDatabase db  = this.getWritableDatabase(); 
				Cursor cursor 	   = db.rawQuery(selectQuery, null); 
				int count 		   = cursor.getCount();
				if(count > 0){
					 cursor.moveToFirst(); 
					 latestMessage = cursor.getString(1);
				}else{
					latestMessage = "Preciso de ajuda! ";
				}
				db.close(); // Closing database connection
			}catch(Exception e){
				
			}
		return latestMessage;
	}
	Vector<HelpRecordModel> getHelpRecord() {

		Vector<HelpRecordModel> vectorObject = new Vector <HelpRecordModel>();

		String selectQuery = "SELECT  * FROM " + TABLE_HELP;
		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);
		cursor.moveToFirst();

		int count = cursor.getCount();

		for (int i = 0; i < count; i++) {
			HelpRecordModel helpRecordModel = new HelpRecordModel();
 
			helpRecordModel.setEmail(cursor.getString(1));
			helpRecordModel.setContactName(cursor.getString(2));
			helpRecordModel.setContactNumber(cursor.getString(3));
			helpRecordModel.setMessage(cursor.getString(4));
			vectorObject.add(helpRecordModel);
			cursor.moveToNext();
		} 
		db.close(); // Closing database connection
		return vectorObject;
	}  
	boolean IsHelpInRecordDb(){
		String selectQuery = "SELECT  * FROM " + TABLE_HELP;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.getCount() == 0) {
			db.close();
			return false;
		} else {
			db.close();
			return true;
		}
	} 
	public boolean IsNumberPresent(String number) {
		String filterNumber = ""; 
		String[] parsedNumber;
		String[] finalParsedNumber;
		boolean flag = false; 
 
		for(int i = 0; i < number.length(); i++){
			if(number.charAt(i) == '<' || number.charAt(i) == '>' ){
				flag = true;	
			}
		} 
		if(flag){
			parsedNumber 		 = 	number.split("<");
			finalParsedNumber	 =	parsedNumber[1].split(">"); 
			filterNumber = finalParsedNumber[0];

		}else{
			filterNumber = number;

		}
		String selectQuery = "SELECT  * FROM " + TABLE_GUARDIAN_MEMBERS + " WHERE "
				+ GUARDIAN_MEMBER_NUMBER + " = '" + filterNumber+"'";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.getCount() == 0) {
			db.close();
			return false;
		} else {
			db.close();
			return true;
		}
	}
	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
 
		String CREATE_GUARDIAN_TABLE = "CREATE TABLE "
				+ TABLE_GUARDIAN_MEMBERS + "(" + GUARDIAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"   
				+ GUARDIAN_MEMBER_NUMBER   + " TEXT," 
				+ GUARDIAN_MEMBER_NAME   + " TEXT"  + ")";
 
		String CREATE_GUARDIAN_MESSAGE = "CREATE TABLE "
				+ TABLE_GUARDIAN_MESSAGE + "(" + GUARDIAN_MSG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"   
				+ GUARDIAN_MEMBER_MSG   + " TEXT )";
		
		String CREATE_USER_TABLE= "CREATE TABLE "
				+ TABLE_HELP + "(" + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"   
				+ USER_EMAIL   + " TEXT," 
				+ CONTACT_NAME   + " TEXT," 
				+ CONTACT_NUMBER   + " TEXT,"
				+ USER_MESSAGE   + " TEXT" 
				+ ")";
		 
		try{
			Log.i("Creating Table", "creating table");
			db.execSQL(CREATE_GUARDIAN_TABLE); 
			db.execSQL(CREATE_GUARDIAN_MESSAGE);
			db.execSQL(CREATE_USER_TABLE); 
		}catch(Exception e){
			Log.i("Creating Table Error", e.getMessage());
		}
	} 
	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GUARDIAN_MEMBERS);  
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GUARDIAN_MESSAGE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HELP);
		// Create tables again
		onCreate(db);
	}  
	void removeGuardianMember(String Number) {

		SQLiteDatabase db = this.getWritableDatabase(); 
		db.delete(DatabaseHandler.TABLE_GUARDIAN_MEMBERS,  GUARDIAN_MEMBER_NUMBER + " =?", new String[]{
				 Number });

		db.close(); // Closing database connection
	}
	
	void removeHelpRecord() {

		SQLiteDatabase db = this.getWritableDatabase(); 
		db.execSQL("Delete from  " + TABLE_HELP);  
		db.close();  
	}
}
