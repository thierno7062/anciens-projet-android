package sn.fallou_syll.covid_project;

import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Contact extends menu {
    private static final String LOG_TAG ="AndroidExample";
    private static final int MY_PERMISSION_REQUEST_CODE_SEND_SMS = 1;
    private EditText editTextObjet;
    private EditText editTextMessage;
    private Button buttonSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);


        ActivityCompat.requestPermissions(Contact.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

        editTextObjet = findViewById(R.id.txt_objet_sms);
        editTextMessage = findViewById(R.id.txt_message_sms);


        this.buttonSend = (Button) this.findViewById(R.id.btn_envoyer);

        this.buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS_by_Intent_ACTION_SEND();
            }
        });
    }

    private void sendSMS_by_Intent_ACTION_SEND()  {
        String phoneNumber = "777959477";
        String objet = "Objet :"+this.editTextObjet.getText().toString();
        String message =  this.editTextMessage.getText().toString();
        //objet = "Objet: "+ objet;
        message = objet+"\n" + message;

        // Add the phone number in the data
        Uri uri = Uri.parse("smsto:" + phoneNumber);

        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, uri);
        // Add the message at the sms_body extra field
        smsIntent.putExtra("sms_body", message);
        try {
            startActivity(smsIntent);
        } catch (Exception ex) {
            Log.e(LOG_TAG, "Your sms has failed... " + ex.getMessage(), ex);
            Toast.makeText(Contact.this, "Your sms has failed... " + ex.getMessage(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
        SmsManager manager = SmsManager.getDefault();
        manager .sendTextMessage(""+phoneNumber, null, ""+message, null, null);
    }
}