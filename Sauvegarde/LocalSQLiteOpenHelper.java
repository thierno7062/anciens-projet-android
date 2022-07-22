package com.example.memoire.controler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class LocalSQLiteOpenHelper extends SQLiteOpenHelper {
    static final String DB_NAME= "GestUtilisateurs.db";
    static final int DB_VERSION=1;


    public LocalSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="CREATE TABLE Utilisateurs(id INTEGER PRIMARY KEY AUTOINCREMENT,email TEXT,password TEXT)";
       /* String sql="CREATE TABLE Utilisateurs(id INTEGER PRIMARY KEY AUTOINCREMENT,nom TEXT,prenom TEXT,email TEXT," +
                "password TEXT,photo TEXT,adresse TEXT ,telephone INTEGER)";
        String sql2="CREATE TABLE Compte(id INTEGER PRIMARY KEY AUTOINCREMENT, Date_de_creation INTEGER ,id_user INTEGER," +
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

        Log.i( "CC", "Creation de la table" );*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Utilisateurs");
        onCreate(db);
    }

    //inserting in database

    public boolean insert(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        long ins = db.insert("Utilisateurs", null, contentValues);
        if(ins==-1) return false;
        else return true;
    }

    //checking if email exists

    public Boolean chkemail(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Utilisateurs where email=?",new String[]{email});
        if(cursor.getCount()>0) return false;
        else return true;
    }

    //checking the email and password
    public Boolean connexion(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Utilisateurs where email=? and password=?",new String[]{email,password});
        if(cursor.getCount()>0) return true;
        else return false;
    }



}
