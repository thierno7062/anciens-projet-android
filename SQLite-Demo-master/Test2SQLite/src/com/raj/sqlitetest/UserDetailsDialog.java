package com.raj.sqlitetest;
/* Author						: 	Rajesh P
 * Project Name					:	Test 2-SQLite sample app
 * Version						:	1.0
 * Date							:	15th August 2013
 * 
 * Dialog fragment for edit, save and go back to list view.
 */
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class UserDetailsDialog extends DialogFragment{
	
	//defining an interface for communicating with the Fragment activity
	public interface UserInfoChangedListener {
		public void onInfoChanged();
		public void onInfoChanged(Customer newInfo);
	}
	//Variables
	static FragmentActivity activityPtr;
	static UserInfoChangedListener mCallback;
	public EditText etName, etEmail, etPhno;
	public String user, userName, eMail, phNo;
	public Button btSave;
	public final Pattern EMAIL_ADDRESS_PATTERN = Pattern
			.compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
					+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
					+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");
	Builder alerter;
	String validNumber = "";
	Customer userDetails;
	
	public UserDetailsDialog() {
		// Empty constructor required for DialogFragment
	}

	public UserDetailsDialog(Customer userDetails) {
		this.userDetails=userDetails;	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view;
		alerter = new Builder(getActivity());
		activityPtr=getActivity();
		view = inflater.inflate(R.layout.dialog_edit_details, container);
		//setting title as null
		getDialog().setTitle("");
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
		
		etName = (EditText) view.findViewById(R.id.et_name);
		etEmail = (EditText)view. findViewById(R.id.et_email);
		etPhno = (EditText)view. findViewById(R.id.et_phno);
		etName.setText(userDetails.getName());
		etEmail.setText(userDetails.getEmail());
		etPhno.setText(userDetails.getPhoneNumber());
		btSave = (Button)view. findViewById(R.id.bt_save);
		
		//setting listeners

		etName.setOnKeyListener(new OnKeyListener()
		{

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{

				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
				{
					Log.i("event", "**captured");
					etName.clearFocus();
					etEmail.requestFocus();
					return true;
				} else if (event.getAction() == KeyEvent.ACTION_DOWN
						&& event.getKeyCode() == KeyEvent.KEYCODE_BACK)
				{
					Log.i("Back event Trigered", "**Back event");

				}

				return false;
			}
		});
		etEmail.setOnKeyListener(new OnKeyListener()
		{

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{

				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
				{
					Log.i("event", "**captured"); //
					etEmail.clearFocus();
					etPhno.requestFocus();
					return true;
				} else if (event.getAction() == KeyEvent.ACTION_DOWN
						&& event.getKeyCode() == KeyEvent.KEYCODE_BACK)
				{
					Log.i("Back event Trigered", "**Back event");

				}

				return false;
			}
		});


		btSave.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				btSave.setClickable(false);
				userName = etName.getEditableText().toString().trim();
				Log.i("username", "" + userName);
				eMail = etEmail.getEditableText().toString().trim();
				Log.i("email", "" + eMail);
				

				if (userName.length() < 1)
				{
					Toast.makeText(getActivity(), R.string.valid_name,
							Toast.LENGTH_LONG).show();
					btSave.setClickable(true);
					return;
				}
				if (eMail.length() < 1)
				{
					Toast.makeText(getActivity(), R.string.msg_valid,
							Toast.LENGTH_LONG).show();
					btSave.setClickable(true);
					return;
				}

				Log.i("email selected", "" + eMail);
				if (!checkEmail(eMail))
				{
					Toast.makeText(getActivity(), R.string.msg_valid,
							Toast.LENGTH_LONG).show();
					btSave.setClickable(true);
					return;
				}

				validNumber = etPhno.getText().toString();
				Log.i("phone number", "" + validNumber);
				userDetails.setName(etName.getEditableText().toString());
				userDetails.setEmail(etEmail.getEditableText().toString());
				userDetails.setPhoneNumber(etPhno.getEditableText().toString());
				Log.i("","updated "+userDetails.getName());
				
				// updating customer info
				mCallback.onInfoChanged(userDetails);
				getDialog().dismiss();
			}

		});
		return view;
	}
	
	void invokeAlertDialog(String Title, String Message, Builder _instance)
	{
		if (_instance != null)
		{
			_instance.setTitle(Title);
			_instance.setCancelable(false);
			_instance.setMessage(Message);
			_instance.setPositiveButton("OK",
					new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{

					dialog.dismiss();
					return;
				}
			});
			_instance.show();
			btSave.setClickable(true);
			return;
		} else
		{
			btSave.setClickable(true);
			return;
		}
	}

	private boolean checkEmail(String email)
	{
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mCallback = (UserInfoChangedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement listener");
		}

	}
}
