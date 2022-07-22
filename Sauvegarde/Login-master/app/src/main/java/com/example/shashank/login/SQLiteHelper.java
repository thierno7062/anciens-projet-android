package com.example.shashank.login;

/**
 * Created by Shashank on 14-Feb-18.
 */

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteHelper extends SQLiteOpenHelper {

    static String DATABASE_NAME="UserDataBase.db";

    public static final String TABLE_NAME="UserTable";

    public static final String Table_Column_ID="id";

    public static final String Table_Column_1_Name="name";

    public static final String Table_Column_2_Email="email";

    public static final String Table_Column_3_Password="password";

    public static final String Table_Column_4_phone="telephone";

    public static final String Table_Column_5_adresse="adresse";

    public SQLiteHelper(Context context) {

        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        String CREATE_TABLE="CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ("+Table_Column_ID+" INTEGER PRIMARY KEY, "+Table_Column_1_Name+" TEXT, "+Table_Column_2_Email+" TEXT, "+Table_Column_3_Password+" TEXT,"+Table_Column_4_phone+" TEXT,"+Table_Column_5_adresse+" TEXT)";
        database.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);

    }

}