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

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView empty_imageview;
    TextView no_data;
    MyDatabaseHelper myDB;
    ArrayList<String> id, nom, type, cni;
    CustomAdapter customAdapter;
    Button ajouter_pdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        ajouter_pdt=findViewById(R.id.btn_add);
        empty_imageview = findViewById(R.id.empty_imageview);
        no_data = findViewById(R.id.no_data);


        myDB = new MyDatabaseHelper(MainActivity.this);
        id = new ArrayList<>();
        nom = new ArrayList<>();
        type = new ArrayList<>();
        cni = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new CustomAdapter(MainActivity.this,this, id,nom, type,
                cni);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        ajouter_pdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ConnexionActivity.class));
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

                        return true;
                    case R.id.nav_search:
                        startActivity(new Intent(getApplicationContext()
                                ,Search_Activity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_add_publishing:
                        startActivity(new Intent(getApplicationContext()
                                ,InscriptionActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_favorite:
                        startActivity(new Intent(getApplicationContext()
                                ,Favoris_Activity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_add_account:
                        start_login();
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });*/
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



    public void start_login(){
        Intent log=new Intent(this,ConnexionActivity.class);
        startActivity(log);
    }
    public void start_register(){
        Intent reg=new Intent(this,InscriptionActivity.class);
        startActivity(reg);
    }
    public void start_login2(){
        Intent add=new Intent(this,ConnexionActivity.class);
        startActivity(add);
    }

       @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu_accueil,menu);
        inflater.inflate(R.menu.bouton_navigation,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_login:
                start_login();
                return true;
            case R.id.item_register:
                start_register();
                return true;
                default:
                return super.onOptionsItemSelected(item);
        }
    }



}