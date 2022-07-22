package sn.fallou_syll.covid_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_de_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itm_apropos:
                Toast.makeText(this, "Vous avez sélectionné la page 'à propos'!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), A_propos.class);
                startActivity(i);
                return true;
            case R.id.itm_contact:
                Toast.makeText(this, "Vous avez sélectionné la page 'Contact'!", Toast.LENGTH_LONG).show();
                Intent in= new Intent(getApplicationContext(), Contact.class);
                startActivity(in);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}