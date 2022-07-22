package com.example.adddatafirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private EditText mName , mSurname , mEmail;
    private Button button ;

    private FirebaseDatabase db= FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mName = findViewById(R.id.name);
        mSurname = findViewById(R.id.surname);
        mEmail = findViewById(R.id.email);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mName.getText().toString();
                String email = mEmail.getText().toString();
                String surname = mSurname.getText().toString();

                HashMap<String , String> userMap = new HashMap<>();

                userMap.put("name" , name);
                userMap.put("surname" , surname);
                userMap.put("email" , email);

                root.push().setValue(userMap);
            }
        });

    }
}