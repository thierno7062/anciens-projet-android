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
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Connexion extends AppCompatActivity {
    private TextView se_connecter,creer_compte,ou_connectez_vous,mdp_oublier;
    private PrefManager prefManager;
    private String val_phone, val_mdp;
    public static EditText input_phone,mdp;
    private TextInputLayout input_layout_mdp,input_layout_phone;
    private static ProgressBar progressBar_sc;
    private static String global_url = AppConst.Server_url;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        mContext = Connexion.this;

        se_connecter = (TextView)findViewById(R.id.se_connecter);
        creer_compte = (TextView)findViewById(R.id.creer_compte);
        ou_connectez_vous = (TextView)findViewById(R.id.ou_connectez_vous);
        mdp_oublier = (TextView)findViewById(R.id.mdp_oublier);
        input_phone = (EditText)findViewById(R.id.input_phone);
        mdp = (EditText)findViewById(R.id.mdp);
        input_layout_phone = (TextInputLayout)findViewById(R.id.input_layout_phone);
        input_layout_mdp = (TextInputLayout)findViewById(R.id.input_layout_mdp);
        progressBar_sc = (ProgressBar) findViewById(R.id.progressBar_sc);

        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch7()) {
            launchHomeScreen();
        }

        creer_compte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Connexion.this, Inscription.class));
            }
        });
        se_connecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                val_phone = input_phone.getText().toString();
                val_mdp = mdp.getText().toString();
                submitFormLogin();
            }
        });
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch7(false);
        Intent intent = new Intent(Connexion.this, MainActivity.class);
        intent.putExtra("fragment_name", "");
        startActivity(intent);
        finish();
    }

    /**
     * Validating form
     */
    private void submitFormLogin() {
        if (!validatePhoneLogin()) {
            return;
        }
        if (!validatePhoneValidLogin()) {
            return;
        }
        if (!validatePasswordLogin()) {
            return;
        }
        if (!validatePasswordValidLogin()) {
            return;
        }
        progressBar_sc.setVisibility(View.VISIBLE);
        new loginUser().execute();
    }

    private boolean validatePhoneLogin() {
        if (input_phone.getText().toString().trim().isEmpty()) {
            input_layout_phone.setError(getResources().getString(R.string.entrez_votre_numero_de_telephone));
            requestFocus(input_phone);
            return false;
        } else {
            input_layout_phone.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePhoneValidLogin() {
        if (input_phone.getText().toString().trim().length() < 8) {
            input_layout_phone.setError(getResources().getString(R.string.entrez_votre_bon_numero_de_telephone));
            requestFocus(input_phone);
            return false;
        } else {
            input_layout_phone.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePasswordLogin() {
        if (mdp.getText().toString().trim().isEmpty()) {
            input_layout_mdp.setError(getResources().getString(R.string.entrez_votre_mot_de_passe));
            requestFocus(mdp);
            return false;
        } else {
            input_layout_mdp.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePasswordValidLogin() {
        if (mdp.getText().toString().trim().length() < 8) {
            input_layout_mdp.setError(getResources().getString(R.string.le_mot_de_passe_necessite_8_caracteres));
            requestFocus(mdp);
            return false;
        } else {
            input_layout_mdp.setErrorEnabled(false);
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch7()) {
            finish();
        }
    }

    /** Connexion d'un utilisateur **/
    private class loginUser extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = global_url+"user_login.php";
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                progressBar_sc.setVisibility(View.INVISIBLE);
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    JSONObject user = json.getJSONObject("user");

                                    saveProfile(new User(user.getString("id"),user.getString("nom"),user.getString("prenom"),user.getString("phone")
                                            ,user.getString("email"),user.getString("statut"),user.getString("login_type"),user.getString("tonotify"),user.getString("device_id"),
                                            user.getString("fcm_id"),user.getString("creer"),user.getString("modifier"),user.getString("photo"),user.getString("user_cat"),user.getString("online")));

                                    input_phone.setText("");
                                    mdp.setText("");
                                    launchHomeScreen();
                                }else if(etat.equals("0")){
                                    Toast.makeText(mContext, "Ce compte n'existe pas", Toast.LENGTH_SHORT).show();
                                    requestFocus(input_phone);
                                    input_layout_phone.setError("Entrez un autre numéro de téléphone");
                                }else if(etat.equals("2")){
                                    Toast.makeText(mContext, "Mot de passe incorrect", Toast.LENGTH_SHORT).show();
                                    requestFocus(mdp);
                                    input_layout_mdp.setError("Entrez un autre mot de passe");
                                }else {
                                    requestFocus(input_phone);
                                    Toast.makeText(mContext, "Ce compte a été bloqué", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {

                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar_sc.setVisibility(View.INVISIBLE);
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("phone", val_phone);
                    params.put("mdp", val_mdp);
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
        M.setStatutConducteur(user.getStatut_online(),mContext);
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
                    params.put("user_cat",M.getUserCategorie(mContext));
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

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
