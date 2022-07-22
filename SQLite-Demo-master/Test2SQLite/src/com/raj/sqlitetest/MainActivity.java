package com.raj.sqlitetest;
/* Author						: 	Rajesh P
 * Project Name					:	Test 2-SQLite sample app
 * Version						:	1.0
 * Date							:	15th August 2013
 * 
 * Main activity
 * 
 */
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.raj.sqlitetest.UserDetailsDialog.UserInfoChangedListener;


public class MainActivity extends FragmentActivity implements UserInfoChangedListener {
	/** Called when the activity is first created. */
	public static ArrayList<Customer> UsersList;
	UserDetailsListAdapter listAdapter;
	ListView usersListview;
	Context context;
	static DBHelper dbHelper;
	static List<Customer> customersList;

	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		dbHelper = new DBHelper(this);
		UsersList=new ArrayList<Customer>();
		context=getApplicationContext();
		usersListview = (ListView) findViewById(R.id.lv_users);

		//setting dummy values to the DB table only once.
		sharedPreferences = getSharedPreferences("appData", MODE_PRIVATE);
		editor = sharedPreferences.edit();
		boolean isFirstTime =sharedPreferences.getBoolean("isFirstTime", true); 
		if(isFirstTime) { 
			//inserting dummy values.
			initDatabaseTable();
			editor.putBoolean("isFirstTime", false);
			editor.commit();
		}



		// Reading all customersList
		Log.d("Reading: ", "Reading all customersList.");
		customersList = dbHelper.getAllContacts();       
		if(customersList.size()==0)
		{
			Log.d("Reading: ", "Reading all customersList size zero retrying");
			initDatabaseTable();
		}
		for (Customer cust : customersList) {
			UsersList.add(cust);
			String log = "Id: "+cust.getID()+" ,Name: " + cust.getName() + " ,Phone: " + cust.getPhoneNumber();
			// Writing Contacts to log
			Log.d("Name: ", log);


		}
		listAdapter=new UserDetailsListAdapter(context, UsersList);
		usersListview.setAdapter(listAdapter);
	}
	//init with dummy data
	public static void initDatabaseTable()
	{
		// Inserting info
		Log.d("Insert: ", "Inserting dummy values ..");
		dbHelper.insertCustomerInfo(new Customer("John","abc@gmail.com", "9453400000"));
		dbHelper.insertCustomerInfo(new Customer("Tim Bernes","tim@gmail.com", "94534543534"));
		dbHelper.insertCustomerInfo(new Customer("Raj","raish@gmail.com", "845565464454"));
		dbHelper.insertCustomerInfo(new Customer("Muhammed","muhammed@gmail.com", "88036567567"));
		dbHelper.insertCustomerInfo(new Customer("Maria","maria001@gmail.com", "9453400000"));
		dbHelper.insertCustomerInfo(new Customer("Elton","elton@gmail.com", "94534543534"));
		dbHelper.insertCustomerInfo(new Customer("Javad","jabes@gmail.com", "845565464454"));
		dbHelper.insertCustomerInfo(new Customer("savani","savvani@gmail.com", "88036567567"));
	}

	/*
	 * ListAdapter 
	 */
	class UserDetailsListAdapter extends BaseAdapter
	{
		ArrayList<Customer> map;
		Context _cxt;
		LayoutInflater inflatorService;
		Bitmap bitmap;


		public UserDetailsListAdapter(Context _cxt,
				ArrayList<Customer> pool)
		{
			map = pool;
			this._cxt = _cxt;
			inflatorService = (LayoutInflater) _cxt
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return map.size();
		}

		@Override
		public Object getItem(int position)
		{
			// TODO Auto-generated method stub
			return map.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			// TODO Auto-generated method stub
			final Holder holder;
			View item = convertView;
			final Customer row = map.get(position);
			if (item == null)
			{
				holder = new Holder();
				item = inflatorService.inflate(R.layout.list_row_item, null);
				holder.userName = (TextView) item
						.findViewById(R.id.tv_name);
				item.setTag(holder);
			} else
				holder = (Holder) item.getTag();


			holder.userName.setText(row.getName());

			item.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					FragmentManager fm = getSupportFragmentManager();
					UserDetailsDialog pm = new UserDetailsDialog(row);
					pm.show(fm, "fragment_edit_name");

				}
			});
			return item;
		}

		class Holder
		{
			TextView userName;
		}
	}



	@Override
	public void onInfoChanged() {

	}

	@Override
	public void onInfoChanged(Customer newInfo) {
		Log.d("updating: ", "updating ..");

		//updating customer info
		dbHelper.updateCustomerInfo(newInfo);
		// Reading all customersList
		Log.d("Reading: ", "Reading all customersList..2");
		customersList = dbHelper.getAllContacts();       
		UsersList.clear();
		for (Customer cn : customersList) {
			UsersList.add(cn);
			String log = "Id: "+cn.getID()+" ,Name: " + cn.getName()+" ,email: " + cn.getEmail() + " ,Phone: " + cn.getPhoneNumber();
			Log.d("DB : ", log);


		}
		//notify the observer that the data has been changed
		listAdapter.notifyDataSetChanged();
	}


}