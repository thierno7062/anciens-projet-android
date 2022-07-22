package com.example.projet;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {

    EditText nom_input, type_input, cni_input;
    Button update_button, delete_button;

    String id, nom, type, cni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
/*
        nom_input = findViewById(R.id.name_input2);
        type_input = findViewById(R.id.type_input2);
        cni_input = findViewById(R.id.cni_input2);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);

        //First we call this
        getAndSetIntentData();

        //Set actionbar title after getAndSetIntentData method
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(nom);
        }

       update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //And only then we call this
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                nom = nom_input.getText().toString().trim();
                type = type_input.getText().toString().trim();
                cni = cni_input.getText().toString().trim();
                myDB.updateData(id, nom, type, cni);
            }
        });
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });*/
    }
/*
    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("title") &&
                getIntent().hasExtra("author") && getIntent().hasExtra("pages")){
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            nom = getIntent().getStringExtra("title");
            type = getIntent().getStringExtra("author");
            cni = getIntent().getStringExtra("pages");

            //Setting Intent Data
            nom_input.setText(nom);
            type_input.setText(type);
            cni_input.setText(cni);
            Log.d("stev", nom+" "+type+" "+cni);
        }else{
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + nom + " ?");
        builder.setMessage("Are you sure you want to delete " + nom + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                myDB.deleteOneRow(id);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }*/
}