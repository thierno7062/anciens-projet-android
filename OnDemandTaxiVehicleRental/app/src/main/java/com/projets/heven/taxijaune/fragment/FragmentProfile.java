package com.projets.heven.taxijaune.fragment;

/**
 * Created by Woumtana Pingdiwindé Youssouf 03/2019
 * Tel: +226 63 86 22 46 - 73 35 41 41
 * Email: issoufwoumtana@gmail.com
 **/

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.projets.heven.taxijaune.R;
import com.projets.heven.taxijaune.controller.AppController;
import com.projets.heven.taxijaune.model.M;
import com.projets.heven.taxijaune.settings.AppConst;
import com.projets.heven.taxijaune.settings.ConnectionDetector;
import com.projets.heven.taxijaune.settings.Progressdialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentProfile extends Fragment {

    ViewPager pager;
    TabLayout tabs;
    View view;
    Context context;
    ConnectionDetector connectionDetector;
    String TAG="FragmentAccueil";
    ArrayList<String> tabNames = new ArrayList<String>();
    int currpos=0;
    private TextView input_nom,input_prenom,input_phone,input_mail,input_mdp;
    private ImageView edit_nom,edit_prenom,edit_phone,edit_mail,edit_mdp;
    public static AlertDialog alertDialog;
    public static Progressdialog progressdialog;
    private EditText input_edit_nom,input_edit_prenom,input_edit_phone,input_edit_mail;
    private TextInputLayout intput_layout_nom,intput_layout_prenom,intput_layout_phone,intput_layout_mail;
    private TextView cancel_nom,save_nom,cancel_prenom,save_prenom,cancel_phone,save_phone,cancel_mail,save_mail;
    private TextView cancel_mdp,save_mdp,titre_dialog_mdp;
    private EditText input_edit_anc_mdp,input_edit_new_mdp,input_edit_conf_mdp;
    private TextInputLayout intput_layout_anc_mdp,intput_layout_new_mdp,intput_layout_conf_mdp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null)
            currpos = getArguments().getInt("tab_pos",0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_profile, container, false);

        context=getActivity();
        connectionDetector=new ConnectionDetector(context);
        progressdialog = new Progressdialog(context);
        progressdialog.init();
        edit_nom = (ImageView) view.findViewById(R.id.edit_nom);
        edit_prenom = (ImageView) view.findViewById(R.id.edit_prenom);
        edit_phone = (ImageView) view.findViewById(R.id.edit_phone);
        edit_mail = (ImageView) view.findViewById(R.id.edit_mail);
        edit_mdp = (ImageView) view.findViewById(R.id.edit_mdp);
        input_nom = (TextView) view.findViewById(R.id.input_nom);
        input_prenom = (TextView) view.findViewById(R.id.input_prenom);
        input_phone = (TextView) view.findViewById(R.id.input_phone);
        input_mail = (TextView) view.findViewById(R.id.input_mail);
        input_mdp = (TextView) view.findViewById(R.id.input_mdp);

        input_nom.setText(M.getNom(context));
        input_prenom.setText(M.getPrenom(context));
        input_phone.setText(M.getPhone(context));
        input_mail.setText(M.getEmail(context));

        edit_nom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogEditNom(input_nom.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        edit_prenom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogEditPrenom(input_prenom.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        edit_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogEditPhone(input_phone.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        edit_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogEditEmail(input_mail.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        edit_mdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogEditMdp();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    //This method would confirm the otp
    private void dialogEditNom(String nom) throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_edit_nom, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        save_nom = (TextView) confirmDialog.findViewById(R.id.save_nom);
        cancel_nom = (TextView) confirmDialog.findViewById(R.id.cancel_nom);
        input_edit_nom = (EditText) confirmDialog.findViewById(R.id.input_edit_nom);
        intput_layout_nom = (TextInputLayout) confirmDialog.findViewById(R.id.intput_layout_nom);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        input_edit_nom.setText(nom);
        save_nom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connectionDetector.isConnectingToInternet()){
                    submitFormChangeNom();
                }else{
                    Toast.makeText(context, context.getResources().getString(R.string.pas_de_connexion_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel_nom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
    }

    //This method would confirm the otp
    private void dialogEditPrenom(String prenom) throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_edit_prenom, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        save_prenom = (TextView) confirmDialog.findViewById(R.id.save_prenom);
        cancel_prenom = (TextView) confirmDialog.findViewById(R.id.cancel_prenom);
        input_edit_prenom = (EditText) confirmDialog.findViewById(R.id.input_edit_prenom);
        intput_layout_prenom = (TextInputLayout) confirmDialog.findViewById(R.id.intput_layout_prenom);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        input_edit_prenom.setText(prenom);
        save_prenom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connectionDetector.isConnectingToInternet()){
                    submitFormChangePrenom();
                }else{
                    Toast.makeText(context, context.getResources().getString(R.string.pas_de_connexion_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel_prenom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
    }

    //This method would confirm the otp
    private void dialogEditPhone(String phone) throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_edit_phone, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        save_phone = (TextView) confirmDialog.findViewById(R.id.save_phone);
        cancel_phone = (TextView) confirmDialog.findViewById(R.id.cancel_phone);
        input_edit_phone = (EditText) confirmDialog.findViewById(R.id.input_edit_phone);
        intput_layout_phone = (TextInputLayout) confirmDialog.findViewById(R.id.intput_layout_phone);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        input_edit_phone.setText(phone);
        save_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connectionDetector.isConnectingToInternet()){
                    submitFormChangePhone();
                }else{
                    Toast.makeText(context, context.getResources().getString(R.string.pas_de_connexion_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
    }

    //This method would confirm the otp
    private void dialogEditEmail(String email) throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_edit_mail, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        save_mail = (TextView) confirmDialog.findViewById(R.id.save_mail);
        cancel_mail = (TextView) confirmDialog.findViewById(R.id.cancel_mail);
        input_edit_mail = (EditText) confirmDialog.findViewById(R.id.input_edit_mail);
        intput_layout_mail = (TextInputLayout) confirmDialog.findViewById(R.id.intput_layout_mail);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        input_edit_mail.setText(email);
        save_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connectionDetector.isConnectingToInternet()){
                    submitFormChangeEmail();
                }else{
                    Toast.makeText(context, context.getResources().getString(R.string.pas_de_connexion_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
    }

    //This method would confirm the otp
    private void dialogEditMdp() throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_edit_mdp, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        save_mdp = (TextView) confirmDialog.findViewById(R.id.save_mdp);
        cancel_mdp = (TextView) confirmDialog.findViewById(R.id.cancel_mdp);
        titre_dialog_mdp = (TextView) confirmDialog.findViewById(R.id.titre_dialog_mdp);
        input_edit_anc_mdp = (EditText) confirmDialog.findViewById(R.id.input_edit_anc_mdp);
        input_edit_new_mdp = (EditText) confirmDialog.findViewById(R.id.input_edit_new_mdp);
        input_edit_conf_mdp = (EditText) confirmDialog.findViewById(R.id.input_edit_conf_mdp);
        intput_layout_anc_mdp = (TextInputLayout) confirmDialog.findViewById(R.id.intput_layout_anc_mdp);
        intput_layout_new_mdp = (TextInputLayout) confirmDialog.findViewById(R.id.intput_layout_new_mdp);
        intput_layout_conf_mdp = (TextInputLayout) confirmDialog.findViewById(R.id.intput_layout_conf_mdp);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();

        save_mdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connectionDetector.isConnectingToInternet()){
                    submitFormChangeMdp();
                }else{
                    Toast.makeText(context, context.getResources().getString(R.string.pas_de_connexion_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel_mdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
    }

    /**
     * Validating form
     */
    private void submitFormChangeNom() {
        if (!validateEditNom()) {
            return;
        }
        alertDialog.hide();
        progressdialog.show();
        new setUserNom().execute(input_edit_nom.getText().toString());
    }

    private boolean validateEditNom() {
        if (input_edit_nom.getText().toString().trim().isEmpty()) {
            intput_layout_nom.setError(context.getResources().getString(R.string.entrez_votre_nom));
            requestFocus(input_edit_nom);
            return false;
        } else {
            intput_layout_nom.setErrorEnabled(false);
        }

        return true;
    }

    private void submitFormChangePrenom() {
        if (!validateEditPrenom()) {
            return;
        }
        alertDialog.hide();
        progressdialog.show();
        new setUserPrenom().execute(input_edit_prenom.getText().toString());
    }

    private boolean validateEditPrenom() {
        if (input_edit_prenom.getText().toString().trim().isEmpty()) {
            intput_layout_prenom.setError(context.getResources().getString(R.string.entrez_votre_prenom));
            requestFocus(input_edit_prenom);
            return false;
        } else {
            intput_layout_prenom.setErrorEnabled(false);
        }

        return true;
    }

    private void submitFormChangePhone() {
        if (!validateEditPhone()) {
            return;
        }
        alertDialog.hide();
        progressdialog.show();
        new setUserPhone().execute(input_edit_phone.getText().toString());
    }

    private boolean validateEditPhone() {
        if (input_edit_phone.getText().toString().trim().isEmpty()) {
            intput_layout_phone.setError(context.getResources().getString(R.string.entrez_votre_numero_de_telephone));
            requestFocus(input_edit_phone);
            return false;
        } else {
            intput_layout_phone.setErrorEnabled(false);
        }

        return true;
    }

    private void submitFormChangeEmail() {
        if (!validateEditEmail()) {
            return;
        }
        alertDialog.hide();
        progressdialog.show();
        new setUserEmail().execute(input_edit_mail.getText().toString());
    }

    private boolean validateEditEmail() {
        if (input_edit_mail.getText().toString().trim().isEmpty()) {
            intput_layout_mail.setError(context.getResources().getString(R.string.entrez_votre_email));
            requestFocus(input_edit_mail);
            return false;
        } else {
            intput_layout_mail.setErrorEnabled(false);
        }

        return true;
    }

    private void submitFormChangeMdp() {
        if (!validateEditAncMdp()) {
            return;
        }
        if (!validateEditAncMdpValid()) {
            return;
        }
        if (!validateEditNewMdp()) {
            return;
        }
        if (!validateEditNewMdpValid()) {
            return;
        }
        if (!validateEditConfMdp()) {
            return;
        }
        if(input_edit_new_mdp.getText().toString().equals(input_edit_conf_mdp.getText().toString())){
            alertDialog.hide();
            progressdialog.show();
            new setUserMdp().execute(input_edit_anc_mdp.getText().toString(),input_edit_new_mdp.getText().toString());
        }else{
            Toast.makeText(context, context.getResources().getString(R.string.mot_de_passe_non_identiques), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateEditAncMdp() {
        if (input_edit_anc_mdp.getText().toString().trim().isEmpty()) {
            intput_layout_anc_mdp.setError(context.getResources().getString(R.string.entrez_votre_mot_de_passe_actuel));
            requestFocus(input_edit_anc_mdp);
            return false;
        } else {
            intput_layout_anc_mdp.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEditAncMdpValid() {
        if (input_edit_anc_mdp.getText().toString().trim().length() < 8) {
            intput_layout_anc_mdp.setError(context.getResources().getString(R.string.le_mot_de_passe_necessite_8_caracteres));
            requestFocus(input_edit_anc_mdp);
            return false;
        } else {
            intput_layout_anc_mdp.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEditNewMdp() {
        if (input_edit_new_mdp.getText().toString().trim().isEmpty()) {
            intput_layout_new_mdp.setError(context.getResources().getString(R.string.entrez_votre_nouveau_mot_de_passe));
            requestFocus(input_edit_new_mdp);
            return false;
        } else {
            intput_layout_new_mdp.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEditNewMdpValid() {
        if (input_edit_new_mdp.getText().toString().trim().length() < 8) {
            intput_layout_new_mdp.setError(context.getResources().getString(R.string.le_mot_de_passe_necessite_8_caracteres));
            requestFocus(input_edit_new_mdp);
            return false;
        } else {
            intput_layout_new_mdp.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEditConfMdp() {
        if (input_edit_conf_mdp.getText().toString().trim().isEmpty()) {
            intput_layout_conf_mdp.setError(context.getResources().getString(R.string.confirmez_le_mot_de_passe));
            requestFocus(input_edit_conf_mdp);
            return false;
        } else {
            intput_layout_conf_mdp.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /** Modification du nom d'un utilisateur **/
    private class setUserNom extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"set_user_nom.php";
            final String nom = params[0];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    M.setNom(msg.getString("nom"),context);
                                    input_nom.setText(msg.getString("nom"));
                                    input_edit_nom.setText("");
                                    alertDialog.cancel();
                                    progressdialog.dismiss();
                                    Toast.makeText(context, context.getResources().getString(R.string.modifie), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, context.getResources().getString(R.string.Echec_de_modification), Toast.LENGTH_SHORT).show();
                                    alertDialog.show();
                                    progressdialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, context.getResources().getString(R.string.Echec_de_modification), Toast.LENGTH_SHORT).show();
                    alertDialog.show();
                    progressdialog.dismiss();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_user",M.getID(context));
                    params.put("nom",nom);
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

    /** Modification du prenom d'un utilisateur **/
    private class setUserPrenom extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"set_user_prenom.php";
            final String prenom = params[0];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    M.setPrenom(msg.getString("prenom"),context);
                                    input_prenom.setText(msg.getString("prenom"));
                                    input_edit_prenom.setText("");
                                    alertDialog.cancel();
                                    progressdialog.dismiss();
                                    Toast.makeText(context, context.getResources().getString(R.string.modifie), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, context.getResources().getString(R.string.Echec_de_modification), Toast.LENGTH_SHORT).show();
                                    alertDialog.show();
                                    progressdialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, context.getResources().getString(R.string.Echec_de_modification), Toast.LENGTH_SHORT).show();
                    alertDialog.show();
                    progressdialog.dismiss();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_user",M.getID(context));
                    params.put("prenom",prenom);
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

    /** Modification du téléphone d'un utilisateur **/
    private class setUserPhone extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"set_user_phone.php";
            final String phone = params[0];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    M.setPhone(msg.getString("phone"),context);
                                    input_phone.setText(msg.getString("phone"));
                                    input_edit_phone.setText("");
                                    alertDialog.cancel();
                                    progressdialog.dismiss();
                                    Toast.makeText(context, context.getResources().getString(R.string.modifie), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, context.getResources().getString(R.string.Echec_de_modification), Toast.LENGTH_SHORT).show();
                                    alertDialog.show();
                                    progressdialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, context.getResources().getString(R.string.Echec_de_modification), Toast.LENGTH_SHORT).show();
                    alertDialog.show();
                    progressdialog.dismiss();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_user",M.getID(context));
                    params.put("phone",phone);
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

    /** Modification du email d'un utilisateur **/
    private class setUserEmail extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"set_user_email.php";
            final String email = params[0];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    M.setEmail(msg.getString("email"),context);
                                    input_mail.setText(msg.getString("email"));
                                    input_edit_mail.setText("");
                                    alertDialog.cancel();
                                    progressdialog.dismiss();
                                    Toast.makeText(context, context.getResources().getString(R.string.modifie), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, context.getResources().getString(R.string.Echec_de_modification), Toast.LENGTH_SHORT).show();
                                    alertDialog.show();
                                    progressdialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, context.getResources().getString(R.string.Echec_de_modification), Toast.LENGTH_SHORT).show();
                    alertDialog.show();
                    progressdialog.dismiss();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_user",M.getID(context));
                    params.put("email",email);
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

    /** Modification du mot de passe d'un utilisateur **/
    private class setUserMdp extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"set_user_mdp.php";
            final String anc_mdp = params[0];
            final String new_mdp = params[1];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    Toast.makeText(context, context.getResources().getString(R.string.modifie), Toast.LENGTH_SHORT).show();
                                    alertDialog.cancel();
                                    progressdialog.dismiss();
                                }else if(etat.equals("2")){
                                    Toast.makeText(context, context.getResources().getString(R.string.Echec_de_modification), Toast.LENGTH_SHORT).show();
                                    alertDialog.show();
                                    progressdialog.dismiss();
                                }else{
                                    Toast.makeText(context, context.getResources().getString(R.string.mot_de_passe_actuel_incorrect), Toast.LENGTH_SHORT).show();
                                    alertDialog.show();
                                    progressdialog.dismiss();
                                    intput_layout_anc_mdp.setError(context.getResources().getString(R.string.entrez_votre_mot_de_passe_actuel));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_user",M.getID(context));
                    params.put("user_cat",M.getUserCategorie(context));
                    params.put("anc_mdp",anc_mdp);
                    params.put("new_mdp",new_mdp);
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
