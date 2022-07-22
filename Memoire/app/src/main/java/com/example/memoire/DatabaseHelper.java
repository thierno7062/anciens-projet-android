package com.example.memoire;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Users.db";
    private static final String TABLE_NAME = "Utilisateurs";
    private static final String COL_ID = "ID";
    private static final String COL_NAME = "username";
    private static final String COL_PASSWORD = "password";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       // db.execSQL("CREATE TABLE register (ID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT)");

        String sql="CREATE TABLE Utilisateurs(id INTEGER PRIMARY KEY AUTOINCREMENT,nom TEXT,prenom TEXT,username TEXT," +
                "password TEXT,photo TEXT,adresse TEXT ,telephone INTEGER)";
        String sql2="CREATE TABLE Compte(id INTEGER PRIMARY KEY AUTOINCREMENT,  TIMESTAMP Date_de_creation DEFAULT CURRENT_TIMESTAMP NOT NULL ,id_user INTEGER," +
                " Foreign key (id_user) References Utilisateur(id))";
        String sql3="CREATE TABLE Annonce(id INTEGER PRIMARY KEY AUTOINCREMENT, titre TEXT, description TEXT, date_publication date," +
                "type TEXT,id_user INTEGER, Foreign key (id_user) References Utilisateur(id) )";
        String sql4="CREATE TABLE fichier(id INTEGER PRIMARY KEY AUTOINCREMENT, url TEXT, type TEXT, taille TEXT," +
                "id_annonce INTEGER, Foreign key (id_annonce) References Annonce(id) )";
        String sql5="CREATE TABLE Categorie(id INTEGER PRIMARY KEY AUTOINCREMENT, nom TEXT,id_annonce INTEGER," +
                " Foreign key (id_annonce) References Annonce(id) )";
        String sql6="CREATE TABLE Sondage(id INTEGER PRIMARY KEY AUTOINCREMENT, titre TEXT,description TEXT,commentaire TEXT,id_annonce INTEGER," +
                "Foreign key (id_annonce) References Annonce(id) )";
        String sql7="CREATE TABLE Reponse(id INTEGER PRIMARY KEY AUTOINCREMENT, titre VARCHAR,description VARCHAR,id_sondage INTEGER," +
                "Foreign key (id_sondage) References Sondage(id) )";


        db.execSQL(sql);
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
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        long result = db.insert("Utilisateurs", null, values);
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
