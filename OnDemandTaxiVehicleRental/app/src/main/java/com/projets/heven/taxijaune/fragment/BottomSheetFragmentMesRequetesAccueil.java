package com.projets.heven.taxijaune.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.projets.heven.taxijaune.R;
import com.projets.heven.taxijaune.activity.TchatActivity;
import com.projets.heven.taxijaune.adapter.RequeteAdapter;
import com.projets.heven.taxijaune.controller.AppController;
import com.projets.heven.taxijaune.directionhelpers.FetchURLConducteur;
import com.projets.heven.taxijaune.listener.SwipeToDeleteCallback;
import com.projets.heven.taxijaune.model.M;
import com.projets.heven.taxijaune.model.RequetePojo;
import com.projets.heven.taxijaune.settings.AppConst;
import com.projets.heven.taxijaune.settings.ConnectionDetector;
import com.projets.heven.taxijaune.settings.Progressdialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Woumtana on 01/01/2019.
 */

public class BottomSheetFragmentMesRequetesAccueil extends BottomSheetDialogFragment{

    ViewPager pager;
    TabLayout tabs;
    View view;
    public static Context context;
    public static Activity activity;
    public static ConnectionDetector connectionDetector;
    String TAG="FragmentAccueil";
    ArrayList<String> tabNames = new ArrayList<String>();
    int currpos=0;

    public static RecyclerView recycler_view_mes_requetes;
    public static List<RequetePojo> albumList_mes_requetes;
    public static RequeteAdapter adapter_mes_requetes;
    public static Progressdialog progressdialog;
    public static SwipeRefreshLayout swipe_refresh;

    public static ProgressBar progressBar_failed;
    public static LinearLayout layout_not_found,layout_failed;
    public static RelativeLayout layout_liste;
    private static RequetePojo requetePojo = null;

    public BottomSheetFragmentMesRequetesAccueil() {
        // Required empty public constructor
    }

    public BottomSheetFragmentMesRequetesAccueil(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bottom_sheet_mes_requetes_accueil, container, false);

        context = getActivity();
//        activity = getActivity();
        connectionDetector=new ConnectionDetector(context);
        progressdialog = new Progressdialog(context);
        progressdialog.init();

        albumList_mes_requetes = new ArrayList<>();
        adapter_mes_requetes = new RequeteAdapter(context, albumList_mes_requetes, getActivity(), "MesRequeteAccueil");

        progressBar_failed = (ProgressBar) rootView.findViewById(R.id.progressBar_failed);
        layout_liste = (RelativeLayout) rootView.findViewById(R.id.layout_liste);
        layout_not_found = (LinearLayout) rootView.findViewById(R.id.layout_not_found);
        layout_failed = (LinearLayout) rootView.findViewById(R.id.layout_failed);
        swipe_refresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        recycler_view_mes_requetes = (RecyclerView) rootView.findViewById(R.id.recycler_view_mes_requetes);
        @SuppressLint("WrongConstant") LinearLayoutManager horizontalLayoutManagerGarde = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recycler_view_mes_requetes.setLayoutManager(horizontalLayoutManagerGarde);
        recycler_view_mes_requetes.setItemAnimator(new DefaultItemAnimator());
        recycler_view_mes_requetes.setAdapter(adapter_mes_requetes);

//        recycler_view_historic.addOnItemTouchListener(new RecyclerTouchListener(context, recycler_view_historic, new ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                RequetePojo requetePojo = albumList_historic.get(position);
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//                RequetePojo requetePojo = albumList_historic.get(position);
//            }
//        }));
//        getHistoric();
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FragmentMesRequetes.getMesRequete().execute();
            }
        });

        layout_failed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar_failed.setVisibility(View.VISIBLE);
                new FragmentMesRequetes.getMesRequete().execute();
            }
        });

        swipe_refresh.setRefreshing(true);
        new getMesRequete().execute();

        return rootView;
    }

    private static void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(context,"mes_requetes") {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                requetePojo = adapter_mes_requetes.getData().get(position);

                if(requetePojo.getStatut().equals("accepter")) {
                    Intent intent = new Intent(context, TchatActivity.class);
                    if (M.getUserCategorie(context).equals("user_app")) {
                        intent.putExtra("id_receveur", requetePojo.getConducteur_id());
                        intent.putExtra("id_envoyeur", M.getID(context));
                        intent.putExtra("nom_receveur", requetePojo.getConducteur_name());
                    } else {
                        intent.putExtra("id_receveur", requetePojo.getUser_id());
                        intent.putExtra("id_envoyeur", M.getID(context));
                        intent.putExtra("nom_receveur", requetePojo.getUser_name());
                    }
                    intent.putExtra("id_requete", requetePojo.getId());
                    context.startActivity(intent);
                }else
                    Toast.makeText(context, context.getResources().getString(R.string.your_request_is_being_processed), Toast.LENGTH_SHORT).show();
                adapter_mes_requetes.notifyDataSetChanged();
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recycler_view_mes_requetes);
    }

    /** Récupération des requêtes d'un utilisateur **/
    public static class getMesRequete extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"get_current_requete_user_app.php";
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                swipe_refresh.setRefreshing(false);
                                albumList_mes_requetes.clear();
                                adapter_mes_requetes.notifyDataSetChanged();
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    layout_liste.setVisibility(View.VISIBLE);
                                    layout_not_found.setVisibility(View.GONE);
                                    layout_failed.setVisibility(View.GONE);
                                    progressBar_failed.setVisibility(View.GONE);

                                    for(int i=0; i<(msg.length()-1); i++) {
                                        JSONObject taxi = msg.getJSONObject(String.valueOf(i));
//                                        Toast.makeText(context, ""+taxi.getString("moyenne"), Toast.LENGTH_SHORT).show();
                                        albumList_mes_requetes.add(new RequetePojo(taxi.getInt("id"),taxi.getInt("id_user_app"),taxi.getInt("id_conducteur_accepter"), taxi.getString("nom")+" "+taxi.getString("prenom"), taxi.getString("nomConducteur")+" "+taxi.getString("prenomConducteur"), taxi.getString("distance"),taxi.getString("creer"),taxi.getString("statut"),"",taxi.getString("latitude_depart")
                                                ,taxi.getString("longitude_depart"),taxi.getString("latitude_arrivee"),taxi.getString("longitude_arrivee"),taxi.getString("statut_course")
                                                ,taxi.getString("niveau"),taxi.getString("moyenne"),taxi.getString("nb_avis"),taxi.getString("montant"),taxi.getString("duree")));

//                                        Toast.makeText(context, "Yes", Toast.LENGTH_SHORT).show();
                                        adapter_mes_requetes.notifyDataSetChanged();
                                    }

                                    enableSwipeToDeleteAndUndo();
                                }else{
                                    layout_liste.setVisibility(View.GONE);
                                    layout_not_found.setVisibility(View.VISIBLE);
                                    layout_failed.setVisibility(View.GONE);
                                    progressBar_failed.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    layout_liste.setVisibility(View.GONE);
                    layout_not_found.setVisibility(View.GONE);
                    layout_failed.setVisibility(View.VISIBLE);
                    progressBar_failed.setVisibility(View.GONE);
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_user_app", M.getID(context));
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

    public static void showDialog(){
        progressdialog.show();
    }

    public static void dismissDialog(){
        progressdialog.dismiss();
    }
}
