package com.projets.heven.taxijaune.activity;

/**
 * Created by Woumtana Pingdiwindé Youssouf 03/2019
 * Tel: +226 63 86 22 46 - 73 35 41 41
 * Email: issoufwoumtana@gmail.com
 **/

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.projets.heven.taxijaune.R;
import com.projets.heven.taxijaune.controller.AppController;
import com.projets.heven.taxijaune.model.M;
import com.projets.heven.taxijaune.model.User;
import com.projets.heven.taxijaune.settings.AppConst;
import com.projets.heven.taxijaune.settings.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Inscription extends AppCompatActivity {
    private TextView envoyer,jai_un_compte;
    private PrefManager prefManager;
    private String val_prenom_insc, val_phone_insc, val_mdp_insc, val_mdp_conf_insc;
    private static EditText phone_insc, mdp_insc,mdp_conf,prenom_insc;
    private Context mContext;
    private static ProgressBar progressBar_insc;
    private TextInputLayout input_layout_prenom_insc, input_layout_phone_insc,input_layout_mdp_inc,input_layout_mdp_conf;
    private static String global_url = AppConst.Server_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        mContext = Inscription.this;

        envoyer = (TextView)findViewById(R.id.envoyer);
        jai_un_compte = (TextView)findViewById(R.id.jai_un_compte);
        phone_insc = (EditText) findViewById(R.id.phone_insc);
        mdp_insc = (EditText) findViewById(R.id.mdp_insc);
        mdp_conf = (EditText) findViewById(R.id.mdp_conf);
        prenom_insc = (EditText)findViewById(R.id.prenom_insc);
        progressBar_insc = (ProgressBar) findViewById(R.id.progressBar_insc);
        input_layout_prenom_insc = (TextInputLayout)findViewById(R.id.input_layout_prenom_insc);
        input_layout_phone_insc = (TextInputLayout)findViewById(R.id.input_layout_phone_insc);
        input_layout_mdp_inc = (TextInputLayout)findViewById(R.id.input_layout_mdp_insc);
        input_layout_mdp_conf = (TextInputLayout)findViewById(R.id.input_layout_mdp_conf);

        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);

        envoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                val_prenom_insc = prenom_insc.getText().toString();
                val_phone_insc = phone_insc.getText().toString();
                val_mdp_insc = mdp_insc.getText().toString();
                val_mdp_conf_insc = mdp_conf.getText().toString();
                submitFormSubscribe();
            }
        });
        jai_un_compte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch7(false);
        Intent intent = new Intent(Inscription.this, MainActivity.class);
        intent.putExtra("fragment_name", "");
        startActivity(intent);
        finish();
    }

    /**
     * Validating form
     */
    private void submitFormSubscribe() {
        if (!validatePrenom()) {
            return;
        }
        if (!validatePhone()) {
            return;
        }
        if (!validatePhoneValid()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }
        if (!validatePasswordValid()) {
            return;
        }
        if (!validateMdpConf()) {
            return;
        }
        if(val_mdp_conf_insc.equals(val_mdp_insc)){
            progressBar_insc.setVisibility(View.VISIBLE);
            new createUser().execute();
        }else{
            Toast.makeText(mContext, getResources().getString(R.string.mot_de_passe_non_identiques), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validatePrenom() {
        if (prenom_insc.getText().toString().trim().isEmpty()) {
            input_layout_prenom_insc.setError(getResources().getString(R.string.entrez_votre_mot_de_passe));
            requestFocus(prenom_insc);
            return false;
        } else {
            input_layout_prenom_insc.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePhone() {
        if (phone_insc.getText().toString().trim().isEmpty()) {
            input_layout_phone_insc.setError(getResources().getString(R.string.entrez_votre_numero_de_telephone));
            requestFocus(phone_insc);
            return false;
        } else {
            input_layout_phone_insc.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePhoneValid() {
        if (phone_insc.getText().toString().trim().length() < 8) {
            input_layout_phone_insc.setError(getResources().getString(R.string.entrez_votre_bon_numero_de_telephone));
            requestFocus(phone_insc);
            return false;
        } else {
            input_layout_phone_insc.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (mdp_insc.getText().toString().trim().isEmpty()) {
            input_layout_mdp_inc.setError(getResources().getString(R.string.entrez_votre_mot_de_passe));
            requestFocus(mdp_insc);
            return false;
        } else {
            input_layout_mdp_inc.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePasswordValid() {
        if (mdp_insc.getText().toString().trim().length() < 8) {
            input_layout_mdp_inc.setError(getResources().getString(R.string.le_mot_de_passe_necessite_8_caracteres));
            requestFocus(mdp_insc);
            return false;
        } else {
            input_layout_mdp_inc.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateMdpConf() {
        if (mdp_conf.getText().toString().trim().isEmpty()) {
            input_layout_mdp_conf.setError(getResources().getString(R.string.confirmez_votre_mot_de_passe));
            requestFocus(mdp_conf);
            return false;
        } else {
            input_layout_mdp_conf.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /** Enregistrement d'un utilisateur **/
    private class createUser extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = global_url+"user_register.php";
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                progressBar_insc.setVisibility(View.INVISIBLE);
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    JSONObject user = json.getJSONObject("user");
                                    String phone = user.getString("phone");

                                    phone_insc.setText("");
                                    mdp_insc.setText("");
                                    mdp_conf.setText("");
                                    prenom_insc.setText("");
                                    Toast.makeText(mContext, "Effectué avec succès", Toast.LENGTH_SHORT).show();

                                    saveProfile(new User(user.getString("id"),user.getString("nom"),user.getString("prenom"),user.getString("phone")
                                            ,user.getString("email"),user.getString("statut"),user.getString("login_type"),user.getString("tonotify"),user.getString("device_id"),
                                            user.getString("fcm_id"),user.getString("creer"),user.getString("modifier"),user.getString("photo"),user.getString("user_cat"),""));


//                                    phone_insc.setText(phone);
//                                    Connexion.input_phone.setText(user.getString("phone"));
                                    launchHomeScreen();
                                }else if(etat.equals("2")){
                                    Toast.makeText(mContext, "Ce numéro existe déjà", Toast.LENGTH_SHORT).show();
                                    requestFocus(phone_insc);
                                    input_layout_phone_insc.setError("Entrez un autre numéro de téléphone");
                                }else
                                    Toast.makeText(mContext, "Echec d'inscription", Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {

                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar_insc.setVisibility(View.INVISIBLE);
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("prenom", val_prenom_insc);
                    params.put("phone", val_phone_insc);
                    params.put("mdp", val_mdp_insc);
                    params.put("login_type", "phone");
                    params.put("tonotify", "yes");
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(jsonObjReq);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //to add spacing between cards
            if (this != null) {

            }

        }

        @Override
        protected void onPreExecute() {

        }
    }

    private void saveProfile(User user){
        M.setNom(user.getNom(),mContext);
        M.setPrenom(user.getPrenom(),mContext);
        M.setPhone(user.getPhone(),mContext);
        M.setEmail(user.getEmail(),mContext);
        M.setID(user.getId(),mContext);
        M.setlogintype(user.getLogin_type(),mContext);
        M.setUsername(user.getNom(),mContext);
        M.setUserCategorie(user.getUser_cat(),mContext);
        M.setCoutByKm("400",mContext);
        M.setCurrentFragment("",mContext);
        if(user.getTonotify().equals("yes"))
            M.setPushNotification(true, mContext);
        else
            M.setPushNotification(false, mContext);

        updateFCM(M.getID(mContext));
    }

    public void updateFCM(final String user_id) {
        final String[] fcmid = {""};
        final String[] deviceid = { "" };
        if(AppConst.fcm_id==null){
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
//                                Toast.makeText(Demarrage.this, "getInstanceId failed", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Get new Instance ID token
                            String token = task.getResult().getToken();
                            fcmid[0] = token;
                            // Log and toast
                            deviceid[0] = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
//                            Toast.makeText(Demarrage.this, ""+ fcmid[0] +" "+ deviceid[0], Toast.LENGTH_SHORT).show();
                            if(fcmid[0] !=null && fcmid[0].trim().length()>0 && deviceid[0] !=null && deviceid[0].trim().length()>0) {
                                new setUserFCM().execute(user_id, fcmid[0], deviceid[0]);
                            }
                        }
                    });
        }else{
            fcmid[0] = AppConst.fcm_id;
        }
    }

    /** Mettre à jour le token de l'utilisateur**/
    private class setUserFCM extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"update_fcm.php";
            final String user_id = params[0];
            final String fcmid = params[1];
            final String deviceid = params[2];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id",user_id);
                    params.put("fcm_id",fcmid);
                    params.put("device_id",deviceid);
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(jsonObjReq);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //to add spacing between cards
            if (this != null) {

            }

        }

        @Override
        protected void onPreExecute() {

        }
    }
}
