package com.example.projet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InscriptionActivity extends AppCompatActivity {

    EditText editUsername, editPassword, editConfirmPassword;
    Button registerBtn;
    TextView viewLogin;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);


        databaseHelper = new DatabaseHelper(this);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        registerBtn = findViewById(R.id.registerBtn);
        viewLogin = findViewById(R.id.viewLogin);


        viewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InscriptionActivity.this, ConnexionActivity.class);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editUsername.getText().toString().trim();
                String password = editPassword.getText().toString().trim();
                String confirmPassword = editConfirmPassword.getText().toString().trim();

                //   String telephone = editPassword..getText().toString().trim();

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(InscriptionActivity.this, "Enter username", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(InscriptionActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(InscriptionActivity.this, "Enter confirm password", Toast.LENGTH_SHORT).show();
                }
                else if (password.equals(confirmPassword)) {
                    long value = databaseHelper.addUser(username,password);
                    if (value > 0) {
                        Toast.makeText(InscriptionActivity.this, "You are registered", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(InscriptionActivity.this, ConnexionActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(InscriptionActivity.this, "Registration error", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(InscriptionActivity.this, "Password not match", Toast.LENGTH_SHORT).show();
                }

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
                                ,ConnexionActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_favorite:
                        startActivity(new Intent(getApplicationContext()
                                ,Favoris_Activity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_add_account:
                        start_login2();
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
*/
    }

    private void start_login2() {
        Intent add=new Intent(this,ConnexionActivity.class);
        startActivity(add);
    }
/*
    public void insertdata(String fname, String lname, String pass, String email, String phone){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_1, fname);
        contentValues.put(DatabaseHelper.COL_2, lname);
        contentValues.put(DatabaseHelper.COL_PASSWORD, pass);
        contentValues.put(DatabaseHelper.COL_NAME, email);
        contentValues.put(DatabaseHelper.COL_6, phone);
        long id = databaseHelper.insertdata(DatabaseHelper.TABLE_NAME, null, contentValues);
    }*/
}