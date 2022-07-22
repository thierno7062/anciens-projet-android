package com.raj.sqlitetest;
/* Author						: 	Rajesh P
 * Project Name					:	Test 2-SQLite sample app
 * Version						:	1.0
 * Date							:	15th August 2013
 * 
 * Helper class for database and table creation and version management.
 * 
 */
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	// Name of the Database
	private static final String DB_NAME = "unicom123";
	// Customer table name
	private static final String CUSTOMER_TABLE = "customer";
	//Database version
	private static int DB_VERSION = 5;
	// Customer Table Columns
	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String EMAIL = "email";
	private static final String PH_NO = "phone_number";
	private static final String TAG="DB Handler class";
	Context context;
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context=context;
		Log.i(TAG, " in DBHelper()");

	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(TAG, "in onCreateDB");
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + CUSTOMER_TABLE + "("
				+ ID + " INTEGER PRIMARY KEY," + NAME + " TEXT,"
				+ EMAIL + " TEXT,"
				+ PH_NO + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, "in onUpgrade");
		db.execSQL("DROP TABLE IF EXISTS " + CUSTOMER_TABLE);
		onCreate(db);
	}

	// Adding new Customer info
	void insertCustomerInfo(Customer customer) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(NAME, customer.getName()); // Customer Name
		values.put(EMAIL, customer.getEmail()); // Customer email
		values.put(PH_NO, customer.getPhoneNumber()); // Customer Phone
		// Inserting row of values
		db.insert(CUSTOMER_TABLE, null, values);
		db.close(); // Closing database connection
	}


	// Getting All Infos
	public List<Customer> getAllContacts() {
		List<Customer> contactList = new ArrayList<Customer>();
		String selectQuery = "SELECT  * FROM " + CUSTOMER_TABLE;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				Customer customer = new Customer();
				customer.setID(Integer.parseInt(cursor.getString(0)));
				customer.setName(cursor.getString(1));
				customer.setEmail(cursor.getString(2));
				customer.setPhoneNumber(cursor.getString(3));
				// Adding contact to list
				contactList.add(customer);
			} while (cursor.moveToNext());
		}//closing cursor properly.
		cursor.close();
		return contactList;
	}

	// Updating  Customer Info
	public int updateCustomerInfo(Customer customer) {
		//opening database as writable
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(NAME, customer.getName());
		values.put(EMAIL, customer.getEmail());
		values.put(PH_NO, customer.getPhoneNumber());

		// updating
		return db.update(CUSTOMER_TABLE, values, ID + " = ?",
				new String[] { String.valueOf(customer.getID()) });
	}

	// Deleting single Customer
	public void deleteCustomer(Customer customer) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(CUSTOMER_TABLE, ID + " = ?",
				new String[] { String.valueOf(customer.getID()) });
		db.close();
	}


	// Getting customersList Count
	public int getTotalCustomerCount() {
		String countQuery = "SELECT  * FROM " + CUSTOMER_TABLE;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count=cursor.getCount();
		cursor.close();
		return count;
	}

}
