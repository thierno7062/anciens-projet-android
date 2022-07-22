package com.projets.heven.taxijaune.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.projets.heven.taxijaune.R;
import com.projets.heven.taxijaune.controller.AppController;
import com.projets.heven.taxijaune.model.M;
import com.projets.heven.taxijaune.settings.AppConst;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Woumtana on 01/01/2019.
 */

public class BottomSheetFragmentRecu extends BottomSheetDialogFragment {
    private static Context context;
    private Activity activity;
    private TextView fermer,distance_requete, duree_requete,cout_requete,date_heure_requete;
    private String cout, distance, distance_init, duration, id_user_app, id_requete;
    private ImageView trajet_requete,cancel;
    private String[] tabDistance = {};
    private ProgressBar progressBar;
    public static final String[] MONTHS = {"Jan", "Fev", "Mar", "Avr", "Mai", "Jui", "Jul", "Aou", "Sep", "Oct", "Nov", "Dec"};

    public BottomSheetFragmentRecu() {
        // Required empty public constructor
    }

    public BottomSheetFragmentRecu(Activity activity, String id_requete, String id_user_app) {
        this.activity = activity;
        this.id_requete = id_requete;
        this.id_user_app = id_user_app;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bottom_sheet_recu, container, false);

        context = getContext();

        fermer = (TextView) rootView.findViewById(R.id.fermer);
        cancel = (ImageView) rootView.findViewById(R.id.cancel);
        trajet_requete = (ImageView) rootView.findViewById(R.id.trajet_requete);
        cout_requete = (TextView) rootView.findViewById(R.id.cout_requete);
        date_heure_requete = (TextView) rootView.findViewById(R.id.date_heure_requete);
        distance_requete = (TextView) rootView.findViewById(R.id.distance_requete);
        duree_requete = (TextView) rootView.findViewById(R.id.duree_requete);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        fermer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        new getRecu().execute(id_requete, id_user_app);

        setCancelable(false);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

//        if (mapFragment != null)
//            getFragmentManager().beginTransaction().remove(mapFragment).commit();
    }

    /** Récupération du reçu**/
    private class getRecu extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"get_recu.php";
            final String id_requete = params[0];
            final String id_user_app = params[1];
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
                                    JSONObject recu = msg.getJSONObject(String.valueOf(0));

                                    distance = recu.getString("distance");
                                    duration = recu.getString("duree");
                                    cout = recu.getString("montant");
                                    String image = recu.getString("image");
                                    String date_heure = recu.getString("creer");

                                    if(distance.length() > 3) {
                                        String virgule = distance.substring(distance.length() - 3,distance.length() - 2);
                                        distance = distance.substring(0, distance.length() - 3);
                                        distance = distance+"."+virgule+" km";
                                    }else
                                    distance = distance+" m";

                                    tabDistance = distance.split(" ");

                                    if(tabDistance[1].equals("m"))
                                        distance_init = tabDistance[0];
                                    else
                                        distance_init = String.valueOf(Float.parseFloat(tabDistance[0])*1000);

                                    cout = String.valueOf((Float.parseFloat(M.getCoutByKm(context))/1000) * Float.parseFloat(distance_init));

                                    cout_requete.setText(cout+" $");
                                    distance_requete.setText(distance);
                                    duree_requete.setText(duration);

                                    // loading model cover using Glide library
                                    Glide.with(context).load(AppConst.Server_url+"/images/recu_trajet_course/"+image)
                                            .skipMemoryCache(false)
                                            .listener(new RequestListener<Drawable>() {
                                                @Override
                                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                    progressBar.setVisibility(View.GONE);
                                                    return false;
                                                }

                                                @Override
                                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                    progressBar.setVisibility(View.GONE);
                                                    return false;
                                                }
                                            })
                                            .into(trajet_requete);

                                    String tabDateHeure[] = date_heure.split(" ");
                                    String tabDate[] = tabDateHeure[0].split("-");
                                    date_heure_requete.setText(tabDate[2] + " " + MONTHS[Integer.parseInt(tabDate[1])-1] + ". à "+tabDateHeure[1]);

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
                    params.put("id_requete", id_requete);
                    params.put("id_user_app", id_user_app);
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
