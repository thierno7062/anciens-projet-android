package com.example.memoire;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button btn_commencer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_commencer=(Button)findViewById(R.id.btn_start);
        btn_commencer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();

            }
        });
    }
    public void start(){
        Intent accueil=new Intent(this, ConnexionActivity.class);
        startActivity(accueil);
    }
    public void start_login(){
        Intent log=new Intent(this,ConnexionActivity.class);
        startActivity(log);
    }
    public void start_register(){
        Intent reg=new Intent(this,InscriptionActivity.class);
        startActivity(reg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu_accueil,menu);
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