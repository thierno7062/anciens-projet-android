package com.example.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    EditText name_input, type_input, cni_input;
    Button add_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        name_input = findViewById(R.id.name_input);
        type_input = findViewById(R.id.type_input);
        cni_input = findViewById(R.id.cni_input);
        add_button = findViewById(R.id.add_button);
       add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
                myDB.addannonce(name_input.getText().toString().trim(),
                        type_input.getText().toString().trim(),
                        cni_input.getText().toString().trim());
            }
        });
    }
}