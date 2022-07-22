package com.example.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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