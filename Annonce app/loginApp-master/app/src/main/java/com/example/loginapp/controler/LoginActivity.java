package com.example.loginapp.controler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginapp.R;
import com.example.loginapp.model.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    EditText editUsername, editPassword;
    Button loginBtn;
    TextView viewRegister;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseHelper = new DatabaseHelper(this);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        loginBtn = findViewById(R.id.loginBtn);
        viewRegister = findViewById(R.id.viewRegister);

        viewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editUsername.getText().toString().trim();
                String password = editPassword.getText().toString().trim();
                Boolean result = databaseHelper.checkUser(username, password);

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(LoginActivity.this, "Enter username", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                }
                else if (result == true) {
                    //Toast.makeText(LoginActivity.this, "Successfully login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, ViewActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(LoginActivity.this, "Not Registered", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
