package com.example.memoire;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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


    }
}