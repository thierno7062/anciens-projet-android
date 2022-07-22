package com.example.essai1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="register.db";
    public static final String TABLE_NAME="utilisateurs";
    public static final String COL_1="ID";
    public static final String COL_2="FirstName";
    public static final String COL_3="LastName";
    public static final String COL_4="Password";
    public static final String COL_5="Email";
    public static final String COL_6="Phone";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,FirstName TEXT,LastName TEXT,Password TEXT,Email TEXT,Phone TEXT)");
        String sql2="CREATE TABLE Compte(id INTEGER PRIMARY KEY AUTOINCREMENT,Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP ,id_user INTEGER," +
                " Foreign key (id_user) References "+TABLE_NAME+" (id) on delete set null)";
        String sql3="CREATE TABLE Annonce(id INTEGER PRIMARY KEY AUTOINCREMENT,titre TEXT,description TEXT,date_publication date," +
                "type TEXT,id_user INTEGER,Foreign key (id_user) References Utilisateur(id) )";
        String sql4="CREATE TABLE fichier(id INTEGER PRIMARY KEY AUTOINCREMENT,url TEXT,type TEXT,taille TEXT," +
                "id_annonce INTEGER,Foreign key (id_annonce) References Annonce(id) )";
        String sql5="CREATE TABLE Categorie(id INTEGER PRIMARY KEY AUTOINCREMENT,nom TEXT,id_annonce INTEGER," +
                " Foreign key (id_annonce) References Annonce(id) )";
        String sql6="CREATE TABLE Sondage(id INTEGER PRIMARY KEY AUTOINCREMENT,titre TEXT,description TEXT,commentaire TEXT,id_annonce INTEGER," +
                "Foreign key (id_annonce) References Annonce(id) )";
        String sql7="CREATE TABLE Reponse(id INTEGER PRIMARY KEY AUTOINCREMENT,titre VARCHAR,description VARCHAR,id_sondage INTEGER," +
                "Foreign key (id_sondage) References Sondage(id) )";
        db.execSQL(sql2);
        db.execSQL(sql3);
        db.execSQL(sql4);
        db.execSQL(sql5);
        db.execSQL(sql6);
        db.execSQL(sql7);

        Log.i( "CC", "Creation de la table" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME); //Drop older table if exists
        db.execSQL("DROP TABLE IF EXISTS " + "Compte");
        db.execSQL("DROP TABLE IF EXISTS " + "Annonce");
        db.execSQL("DROP TABLE IF EXISTS " + "fichier");
        db.execSQL("DROP TABLE IF EXISTS " + "Categorie");
        db.execSQL("DROP TABLE IF EXISTS " + "Sondage");
        db.execSQL("DROP TABLE IF EXISTS " + "Reponse");
        onCreate(db);
    }


    public boolean checkUser(String username, String password) {
        String[] columns = {COL_1};
        SQLiteDatabase db = getReadableDatabase();
        String selection = COL_5 + "=?" + "and " + COL_5 + "=?";
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
