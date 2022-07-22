package sn.fallou_syll.covid_project;


import android.Manifest;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class personal_info extends menu {

    private static final int MODE_CREATE = 1;
    private static final int MODE_EDIT = 2;

    private EditText txt_prenom;
    private EditText txt_nom;
    private EditText txt_email;
    private EditText txt_telephone;
    private TextView txt_adresse;
    private TextView location;
    private Button buttonSave;

    private Note note;
    private boolean needRefresh;
    private int mode;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        this.txt_prenom = (EditText) findViewById(R.id.txt_prenom);
        this.txt_nom = (EditText) findViewById(R.id.txt_nom);
        this.location = (TextView) findViewById(R.id.btn_location);
        this.txt_email = (EditText) findViewById(R.id.txt_email);
        this.txt_telephone = (EditText) findViewById(R.id.txt_telephone);
        this.txt_adresse = (TextView) findViewById(R.id.ed_adresse);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        this.buttonSave = (Button)findViewById(R.id.btn_save);

        this.buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)  {
                buttonSaveClicked();
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(personal_info.this
                        , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(personal_info.this
                            , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });


        Intent intent = this.getIntent();
        this.note = (Note) intent.getSerializableExtra("note");
        if(note== null)  {
            this.mode = MODE_CREATE;
        } else  {
            this.mode = MODE_EDIT;
            this.txt_prenom.setText(note.getPrenom());
            this.txt_nom.setText(note.getNom());
            this.txt_adresse.setText(note.getAdresse());
            this.txt_email.setText(note.getEmail());
            this.txt_telephone.setText(note.getTelephone());
        }
    }

    // Utilisateur Cliquez sur le bouton Enregistrer.
    public void buttonSaveClicked()  {
        MyDatabaseHelper db = new MyDatabaseHelper(this);

        Intent i = new Intent(getApplicationContext(),Questionnaire.class);


        String prenom = this.txt_prenom.getText().toString();
        String nom = this.txt_nom.getText().toString();
        String adresse = this.txt_adresse.getText().toString();

        String email = this.txt_email.getText().toString();
        String telephone = this.txt_telephone.getText().toString();



        if(mode == MODE_CREATE ) {
            this.note= new Note(prenom,nom,adresse,email,telephone);
            db.addNote(note);
        }
        startActivity(i);


       this.needRefresh = true;

        // Retour à MainActivity.
        this.onBackPressed();
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null){
                    try {
                        Geocoder geocoder = new Geocoder(personal_info.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        txt_adresse.setText(Html.fromHtml(addresses.get(0).getAddressLine(0)));


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}