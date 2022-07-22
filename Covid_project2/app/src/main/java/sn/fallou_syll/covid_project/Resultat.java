package sn.fallou_syll.covid_project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Resultat extends Questionnaire {

    private static final int MY_PERMISSION_REQUEST_CODE_CALL_PHONE = 555;

    private static final String LOG_TAG = "AndroidExample";

    private Vibrator Levibreur;
    private TextView finalScore;
    private TextView message;
    private FloatingActionButton call;



    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat);

        call = (FloatingActionButton) findViewById(R.id.btn_call);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermissionAndCall();
            }
        });


        Levibreur = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        finalScore = (TextView) findViewById(R.id.score);
        int scor = getIntent().getExtras().getInt("scor");
        message = (TextView) findViewById(R.id.txt_message);

        if (scor >=5){
            message.setText("votre cas est critique, veillez vous rendre dans le centre de santé le plus proche :(");
            message.setTextColor(R.color.red);

            Levibreur.vibrate(30000);


        }
        else if (scor>3 && scor<5){
            message.setText("votre cas est sensible, veillez surveiller votre etat !");
        }
        else {
            message.setText("votre etat est corect :)");
        }




        finalScore.setText(String.format("Score: %d", scor));
        Toast.makeText(getApplicationContext(), "le score est: " + scor, Toast.LENGTH_LONG).show();


    }
    private void askPermissionAndCall() {


        // Demande de la permission à l'utiisateur
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) { // 23

            // Verification de l'autorisation
            int sendSmsPermisson = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE);

            if (sendSmsPermisson != PackageManager.PERMISSION_GRANTED) {
                // Si vous n'avez pas la permission, on demande à l'utilisateur.
                this.requestPermissions(
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSION_REQUEST_CODE_CALL_PHONE
                );
                return;
            }
        }
        this.callNow();
    }

    @SuppressLint("MissingPermission")
    private void callNow() {


        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + 777959477));
        try {
            this.startActivity(callIntent);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),"Votre appel a échoué... " + ex.getMessage(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }


    //Lorsque vous avez les résultats de la demande de permission
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE_CALL_PHONE: {

                // Remarque: Si la demande est annulée, les tableaux de résultats sont vides.
                // Autorisations accordées (CALL_PHONE).
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.i( LOG_TAG,"Permission accordée!");
                    Toast.makeText(this, "Permission accordée!", Toast.LENGTH_LONG).show();

                    this.callNow();
                }
                // Cancelled or denied.
                else {
                    Log.i( LOG_TAG,"Permission refusée!");
                    Toast.makeText(this, "Permission refusée!", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    // When results returned
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_PERMISSION_REQUEST_CODE_CALL_PHONE) {
            if (resultCode == RESULT_OK) {
                // Do something with data (Result returned).
                Toast.makeText(this, "Action OK", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action annulée", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action échoué", Toast.LENGTH_LONG).show();
            }
        }
    }
}