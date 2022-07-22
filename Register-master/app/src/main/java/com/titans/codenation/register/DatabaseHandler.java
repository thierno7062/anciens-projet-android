package com.titans.codenation.register;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by joseph on 3/16/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "SQLiteExample.db";
    public static final int DATABASE_VERSION = 1;
    public static final String PERSON_TABLE_NAME = "person";
    public static final String PERSON_COLUMN_ID = "_id";
    public static final String PERSON_COLUMN_NAME = "name";
    public static final String PERSON_COLUMN_GENDER = "gender";
    public static final String PERSON_COLUMN_AGE = "age";



    //constructor
    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }


//declaring the table schema
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + PERSON_TABLE_NAME + "(" +
                        PERSON_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        PERSON_COLUMN_NAME + " TEXT, " +
                        PERSON_COLUMN_GENDER + " TEXT, " +
                        PERSON_COLUMN_AGE + " INTEGER)"
        );
    }



    //this method is called when the db needs to be upgraded(possibly due to version change)
    //here one drops/adds tables or migrate  data into a new tables or basically whatever needs to be done
    //to move from the previous schema to a new schema. Here I basically drop the table
    //PERSON_TABLE_NAME and then call onCreate() to recreate it.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PERSON_TABLE_NAME);
        onCreate(db);

    }
    //basically, the Databasehandler class is supposed to handle all insertion,
    // deletion, updates  and views to the db (basic CRUD ops).
    //to insert a new person, I require an addPerson() method,
    //deletion will have a deletePerson() method, updatePeron() for updating
    //for viewing, I implement two methods: getPerson() and getAllPersons()


    //adding a person

    public boolean addPerson(String name, String gender, String age){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PERSON_COLUMN_NAME, name);
        contentValues.put(PERSON_COLUMN_GENDER, gender);
        contentValues.put(PERSON_COLUMN_AGE, age);

        //calling the DBhandler constructor
        db.insert(PERSON_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updatePerson(Integer id, String name, String gender, int age){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PERSON_COLUMN_NAME, name);
        contentValues.put(PERSON_COLUMN_GENDER, gender);
        contentValues.put(PERSON_COLUMN_AGE, age);

        db.update(PERSON_TABLE_NAME, contentValues, PERSON_COLUMN_ID + " = ?", new String[]{
                Integer.toString(id)});
        return true;
    }

    //for the view, I implement two methods; one to view a single person, another
    // to display a number of people.
    //Since in this case we're not writing to the db, we retreive a readable db with getReadableDatabase()
    //and perform a rawQuery(). Raw query takes a SQL query string, with optional queryArgs
    //and returns a Cursor object. A cursor will provide access to the result set
    //returned. It also provides methods to reteive particular columns, total columns, total rows...

    //for an individual
    public Cursor getPerson(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + PERSON_TABLE_NAME + " WHERE "+
        PERSON_COLUMN_ID + "=?", new String[] {Integer.toString(id)});

        return res;
    }

    // method to get all people in the database.
    public Cursor getAllPersons(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+ PERSON_TABLE_NAME, null);
        return res;
    }


    //Deleting data is also pretty straightforward. the delete()
    //method takes the table name to delete from,and optional whereClause and whereArgs.


    public Integer deletePerson(Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(PERSON_TABLE_NAME,
                PERSON_COLUMN_ID + " = ? ",
                new String[]{Integer.toString(id)}
                );
    }
}
