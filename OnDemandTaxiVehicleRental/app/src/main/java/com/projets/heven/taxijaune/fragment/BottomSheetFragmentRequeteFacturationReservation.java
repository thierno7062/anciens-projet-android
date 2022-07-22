package com.projets.heven.taxijaune.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.projets.heven.taxijaune.R;
import com.projets.heven.taxijaune.adapter.ConducteurDispoAdapter;
import com.projets.heven.taxijaune.controller.AppController;
import com.projets.heven.taxijaune.model.ConducteurDispoPojo;
import com.projets.heven.taxijaune.model.M;
import com.projets.heven.taxijaune.settings.AppConst;
import com.projets.heven.taxijaune.settings.Progressdialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Woumtana on 01/01/2019.
 */

public class BottomSheetFragmentRequeteFacturationReservation extends BottomSheetDialogFragment implements TimePickerDialog.OnTimeSetListener{
    private static Context context;
    private Activity activity;
    private TextView cout_requete,distance_requete,envoyer,date_depart,heure_depart,duration_requete;
    private String cout, distance, distance_init,duration;
    private Location loc1, loc2;
    private Progressdialog progressdialog;
    private LinearLayout layout_date_depart,layout_heure_depart;
    private EditText input_phone;
    private TextInputLayout input_layout_phone;
    private int mYear_depart, mMonth_depart, mDay_depart;
    private int mYear_fin, mMonth_fin, mDay_fin;
    public static final String[] MONTHS = {"Jan", "Fev", "Mar", "Avr", "Mai", "Jui", "Jul", "Aou", "Sep", "Oct", "Nov", "Dec"};
    String val_date_fin = "", val_date_depart = "";
    private String mMonth_1;
    private String[] tabDistance = {};
    private ImageView annuler;

    public static RecyclerView recycler_view_conducteur_dispo;
    public static List<ConducteurDispoPojo> albumList_conducteur_dispo;
    public static ConducteurDispoAdapter adapter_conducteur_dispo;

    public BottomSheetFragmentRequeteFacturationReservation() {
        // Required empty public constructor
    }

    public BottomSheetFragmentRequeteFacturationReservation(Activity activity, Location loc1, Location loc2, String distance, String duration) {
        this.activity = activity;
        this.loc1 = loc1;
        this.loc2 = loc2;
        this.distance = distance;
        this.duration = duration;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bottom_sheet_requete_facturation_reservation, container, false);

        context = getContext();
        progressdialog = new Progressdialog(context);
        progressdialog.init();

        albumList_conducteur_dispo = new ArrayList<>();
        adapter_conducteur_dispo = new ConducteurDispoAdapter(context, albumList_conducteur_dispo, getActivity());
        recycler_view_conducteur_dispo = (RecyclerView) rootView.findViewById(R.id.recycler_view_conducteur);
        @SuppressLint("WrongConstant") LinearLayoutManager horizontalLayoutManagerGarde = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recycler_view_conducteur_dispo.setLayoutManager(horizontalLayoutManagerGarde);
        recycler_view_conducteur_dispo.setItemAnimator(new DefaultItemAnimator());
        recycler_view_conducteur_dispo.setAdapter(adapter_conducteur_dispo);

        cout_requete = (TextView) rootView.findViewById(R.id.cout_requete);
        distance_requete = (TextView) rootView.findViewById(R.id.distance_requete);
        duration_requete = (TextView) rootView.findViewById(R.id.duree_requete);
        annuler = (ImageView) rootView.findViewById(R.id.annuler);
        envoyer = (TextView) rootView.findViewById(R.id.envoyer);

        date_depart = (TextView) rootView.findViewById(R.id.date_depart);
        heure_depart = (TextView) rootView.findViewById(R.id.heure_depart);
        layout_date_depart = (LinearLayout) rootView.findViewById(R.id.layout_date_depart);
        layout_heure_depart = (LinearLayout) rootView.findViewById(R.id.layout_heure_depart);
        input_phone = (EditText)rootView.findViewById(R.id.input_phone);
        input_layout_phone = (TextInputLayout)rootView.findViewById(R.id.input_layout_phone);

//        distance = getDistance(loc1,loc2);
        tabDistance = distance.split(" ");

        if(tabDistance[1].equals("m"))
            distance_init = tabDistance[0];
        else
            distance_init = String.valueOf(Float.parseFloat(tabDistance[0])*1000);

        cout = String.valueOf((Float.parseFloat(M.getCoutByKm(context))/1000) * Float.parseFloat(distance_init));
//        if(distance.length() > 3) {
//            String virgule = distance.substring(distance.length() - 3,distance.length() - 2);
//            distance = distance.substring(0, distance.length() - 3);
//            distance = distance+"."+virgule+" km";
//        }else
//            distance = distance+" m";

        cout_requete.setText(cout+" $");
        distance_requete.setText(distance);
        duration_requete.setText(duration);
        envoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input_phone.getText().toString().trim().length() != 0) {
                    progressdialog.show();
                    new setReservation().execute(mYear_depart +"-"+mMonth_1+"-"+ mDay_depart);
                }else
                    Toast.makeText(activity, context.getResources().getString(R.string.entrez_correctement_vos_informations), Toast.LENGTH_SHORT).show();
            }
        });
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Calendar c = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            c = Calendar.getInstance();
            mYear_depart = c.get(Calendar.YEAR);
            mMonth_depart = c.get(Calendar.MONTH);
            mDay_depart = c.get(Calendar.DAY_OF_MONTH);
            mYear_fin = c.get(Calendar.YEAR);
            mMonth_fin = c.get(Calendar.MONTH);
            mDay_fin = c.get(Calendar.DAY_OF_MONTH)+1;
        }
        date_depart.setText(mDay_depart + " " + MONTHS[mMonth_depart] + ". " + mYear_depart);
        mMonth_1 = String.valueOf(mMonth_depart +1);
        if(mMonth_1.trim().length() == 1){
            mMonth_1 = '0'+mMonth_1;
        }

        val_date_fin = mDay_fin +"-"+mMonth_1+"-"+ mYear_fin;
        val_date_depart = mDay_depart +"-"+mMonth_1+"-"+ mYear_depart;

        /** TIME PICKER **/
        EditText chooseTime;
        ;
        Calendar calendar;
        final int currentHour;
        final int currentMinute;
        final String[] amPm = new String[1];

        calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);
        heure_depart.setText(currentHour+":"+currentMinute);
        layout_heure_depart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm[0] = "PM";
                        } else {
                            amPm[0] = "AM";
                        }
                        heure_depart.setText(String.format("%02d:%02d", hourOfDay, minutes));
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });

        layout_date_depart.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String newdayOfMonth = String.valueOf(dayOfMonth);
                                String newmonthOfYear = String.valueOf(monthOfYear+1);
                                if(String.valueOf(dayOfMonth).trim().length() == 1){
                                    newdayOfMonth = '0'+newdayOfMonth;
                                }
                                if(newmonthOfYear.trim().length() == 1){
                                    newmonthOfYear = '0'+newmonthOfYear;
                                }
                                date_depart.setText(newdayOfMonth + " " + MONTHS[monthOfYear] + ". " + year);
                                val_date_depart = newdayOfMonth+"-"+newmonthOfYear+"-"+year;

                                mYear_depart = year;
                                mMonth_depart = monthOfYear;
                                mDay_depart = Integer.parseInt(newdayOfMonth);
                            }
                        }, mYear_depart, mMonth_depart, mDay_depart);
                datePickerDialog.show();
            }
        });

        setCancelable(false);
        new getConducteurDispo().execute();

        return rootView;
    }

    /** Récupération des conducteurs disponibles**/
    public class getConducteurDispo extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"get_conducteur_dispo.php";
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                albumList_conducteur_dispo.clear();
                                adapter_conducteur_dispo.notifyDataSetChanged();
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    for(int i=0; i<(msg.length()-1); i++) {
                                        JSONObject taxi = msg.getJSONObject(String.valueOf(i));
                                        albumList_conducteur_dispo.add(new ConducteurDispoPojo(taxi.getInt("id"),taxi.getInt("idConducteur"),taxi.getString("nom")+" "+taxi.getString("prenom"),
                                                taxi.getString("immatriculation"), taxi.getString("numero")));
                                        adapter_conducteur_dispo.notifyDataSetChanged();
                                    }
                                }else{

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
                    params.put("lat1", String.valueOf(loc1.getLatitude()));
                    params.put("lng1", String.valueOf(loc1.getLongitude()));
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

    private String getDistance(Location loc1, Location loc2){
        final String[][] tab = {{}};

        float distanceInMeters1 = loc1.distanceTo(loc2);
        tab[0] = String.valueOf(distanceInMeters1).split("\\.");
        String distance = tab[0][0];
        return distance;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

//        if (mapFragment != null)
//            getFragmentManager().beginTransaction().remove(mapFragment).commit();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

    }

    /** Enregistrement d'une requête**/
    private class setReservation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"set_reservation.php";
            final String date_depart = params[0];
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
                                    FragmentAccueil.dismissCommandeFacturationSheet();
                                    Toast.makeText(activity, context.getResources().getString(R.string.votre_requete_a_ete_envoye_avec_succes), Toast.LENGTH_LONG).show();
                                    dismiss();
                                }else{
                                    Toast.makeText(activity, context.getResources().getString(R.string.une_erreur_est_survenue_lors_de_lenvoi_de_votre_requete), Toast.LENGTH_LONG).show();
                                }
                                progressdialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressdialog.dismiss();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", M.getID(context));
                    params.put("lat1", String.valueOf(loc1.getLatitude()));
                    params.put("lng1", String.valueOf(loc1.getLongitude()));
                    params.put("lat2", String.valueOf(loc2.getLatitude()));
                    params.put("lng2", String.valueOf(loc2.getLongitude()));
                    params.put("cout", cout);
                    params.put("distance", distance_init);
                    params.put("date_depart", date_depart);
                    params.put("heure_depart", heure_depart.getText().toString());
                    params.put("contact", input_phone.getText().toString());
                    params.put("duree", duration);
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
