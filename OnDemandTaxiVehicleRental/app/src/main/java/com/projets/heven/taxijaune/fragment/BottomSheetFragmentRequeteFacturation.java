package com.projets.heven.taxijaune.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
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

public class BottomSheetFragmentRequeteFacturation extends BottomSheetDialogFragment {
    private static Context context;
    private Activity activity;
    private TextView cout_requete,distance_requete, commander, reserver,duree_requete;
    private String cout, distance, distance_init, duration;
    private Location loc1, loc2;
    private Progressdialog progressdialog;
    private String[] tabDistance = {};
    private ImageView cancel;

    public static RecyclerView recycler_view_conducteur_dispo;
    public static List<ConducteurDispoPojo> albumList_conducteur_dispo;
    public static ConducteurDispoAdapter adapter_conducteur_dispo;

    public BottomSheetFragmentRequeteFacturation() {
        // Required empty public constructor
    }

    public BottomSheetFragmentRequeteFacturation(Activity activity, Location loc1, Location loc2, String distance, String duration) {
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
        View rootView = inflater.inflate(R.layout.fragment_bottom_sheet_requete_facturation, container, false);

        context = getContext();
        progressdialog = new Progressdialog(context);
        progressdialog.init();

        cout_requete = (TextView) rootView.findViewById(R.id.cout_requete);
        distance_requete = (TextView) rootView.findViewById(R.id.distance_requete);
        reserver = (TextView) rootView.findViewById(R.id.reserver);
        commander = (TextView) rootView.findViewById(R.id.commander);
        duree_requete = (TextView) rootView.findViewById(R.id.duree_requete);
        cancel = (ImageView) rootView.findViewById(R.id.cancel);

        albumList_conducteur_dispo = new ArrayList<>();
        adapter_conducteur_dispo = new ConducteurDispoAdapter(context, albumList_conducteur_dispo, getActivity());
        recycler_view_conducteur_dispo = (RecyclerView) rootView.findViewById(R.id.recycler_view_conducteur);
        @SuppressLint("WrongConstant") LinearLayoutManager horizontalLayoutManagerGarde = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recycler_view_conducteur_dispo.setLayoutManager(horizontalLayoutManagerGarde);
        recycler_view_conducteur_dispo.setItemAnimator(new DefaultItemAnimator());
        recycler_view_conducteur_dispo.setAdapter(adapter_conducteur_dispo);

//        distance = getDistance(loc1,loc2);
//        if(M.getRouteDistance(context).trim().length() != 0)
//            distance = M.getRouteDistance(context);
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
        duree_requete.setText(duration);
        commander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressdialog.show();
                new setRequete().execute();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        reserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentAccueil.showReservationFacturationSheet(distance, duration);
            }
        });

        new getConducteurDispo().execute();

        setCancelable(false);

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

    /** Enregistrement d'une requête**/
    private class setRequete extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"set_requete.php";
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
