package com.example.loginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText editUsername, editPassword, editConfirmPassword;
    Button registerBtn;
    TextView viewLogin;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseHelper = new DatabaseHelper(this);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        registerBtn = findViewById(R.id.registerBtn);
        viewLogin = findViewById(R.id.viewLogin);

        viewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editUsername.getText().toString().trim();
                String password = editPassword.getText().toString().trim();
                String confirmPassword = editConfirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(RegisterActivity.this, "Enter username", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Enter confirm password", Toast.LENGTH_SHORT).show();
                }
                else if (password.equals(confirmPassword)) {
                    long value = databaseHelper.addUser(username, password);
                    if (value > 0) {
                        Toast.makeText(RegisterActivity.this, "You are registered", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration error", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Password not match", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
