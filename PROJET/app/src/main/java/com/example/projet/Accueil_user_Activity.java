package com.example.projet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Accueil_user_Activity extends AppCompatActivity {

    RecyclerView recyclerView;

    ImageView empty_imageview;
    TextView no_data;
    Button ajouter_pdt2;
    MyDatabaseHelper myDB;
    ArrayList<String> id, nom, type, cni;
    CustomAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil_user_);

        recyclerView = findViewById(R.id.recyclerView);
        ajouter_pdt2=findViewById(R.id.btn_add2);
        empty_imageview = findViewById(R.id.empty_imageview);
        no_data = findViewById(R.id.no_data);


        myDB = new MyDatabaseHelper(Accueil_user_Activity.this);
        id = new ArrayList<>();
        nom = new ArrayList<>();
        type = new ArrayList<>();
        cni = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new CustomAdapter(Accueil_user_Activity.this,this, id, nom, type,
                cni);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Accueil_user_Activity.this));
        ajouter_pdt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Accueil_user_Activity.this,AddActivity.class));
            }
        });
/*
        //BOUTON DE NAVIGATION

        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.btn_menu_bottom);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext()
                                ,MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_search:
                        startActivity(new Intent(getApplicationContext()
                                ,Search_Activity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_add_publishing:
                        startActivity(new Intent(getApplicationContext()
                                ,AddActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_favorite:
                        startActivity(new Intent(getApplicationContext()
                                ,Favoris_Activity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_add_account:
                        startActivity(new Intent(getApplicationContext()
                                ,Profil_Activity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    void storeDataInArrays(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        }else{
            while (cursor.moveToNext()){
                id.add(cursor.getString(0));
                nom.add(cursor.getString(1));
                type.add(cursor.getString(2));
                cni.add(cursor.getString(3));
            }
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All?");
        builder.setMessage("Are you sure you want to delete all Data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(Accueil_user_Activity.this);
                myDB.deleteAllData();
                //Refresh Activity
                Intent intent = new Intent(Accueil_user_Activity.this, Accueil_user_Activity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu_user,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all:
               // confirmDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}