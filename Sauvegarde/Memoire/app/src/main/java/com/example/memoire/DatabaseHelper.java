package com.example.memoire;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Users.db";
    private static final String TABLE_NAME = "register";
    private static final String COL_ID = "ID";
    private static final String COL_NAME = "username";
    private static final String COL_PASSWORD = "password";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE register (ID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        long result = db.insert("register", null, values);
        db.close();
        return result;
    }

    public boolean checkUser(String username, String password) {
        String[] columns = {COL_ID};
        SQLiteDatabase db = getReadableDatabase();
        String selection = COL_NAME + "=?" + "and " + COL_PASSWORD + "=?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if (count > 0)
            return true;
        else return false;

    }
}
