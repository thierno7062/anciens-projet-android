package com.raj.sqlitetest;
/* Author						: 	Rajesh P
 * Project Name					:	Test 2-SQLite sample app
 * Version						:	1.0
 * Date							:	15th August 2013
 * 
 * Setter and getter for Customer Object 
 */
public class Customer {

	//private variables
	int _id;
	String userName;
	String eMail;
	String phoneNumber;

	public Customer(){

	}
	// constructor
	public Customer(int id, String userName,String eMail, String phoneNumber){
		this._id = id;
		this.userName = userName;
		this.eMail = eMail;
		this.phoneNumber = phoneNumber;
	}

	// constructor
	public Customer(String name, String phoneNumber){
		this.userName = name;
		this.phoneNumber = phoneNumber;
	}
	// constructor
	public Customer(String userName,String eMail, String phoneNumber){
		this.userName = userName;
		this.phoneNumber = phoneNumber;
		this.eMail = eMail;

	}
	// getting ID
	public int getID(){
		return this._id;
	}

	// setting id
	public void setID(int id){
		this._id = id;
	}

	// getting name
	public String getName(){
		return this.userName;
	}

	// setting name
	public void setName(String name){
		this.userName = name;
	}
	// getting email
	public String getEmail(){
		return this.eMail;
	}

	// setting email
	public void setEmail(String email){
		this.eMail = email;
	}
	// getting phonenumber
	public String getPhoneNumber(){
		return this.phoneNumber;
	}

	// setting phone number
	public void setPhoneNumber(String phone_number){
		this.phoneNumber = phone_number;
	}
}
