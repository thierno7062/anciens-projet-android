package com.projets.heven.taxijaune.fragment;

/**
 * Created by Woumtana Pingdiwindé Youssouf 03/2019
 * Tel: +226 63 86 22 46 - 73 35 41 41
 * Email: issoufwoumtana@gmail.com
 **/

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.projets.heven.taxijaune.R;
import com.projets.heven.taxijaune.adapter.RequeteAdapter;
import com.projets.heven.taxijaune.controller.AppController;
import com.projets.heven.taxijaune.listener.SwipeToDeleteCallback;
import com.projets.heven.taxijaune.model.M;
import com.projets.heven.taxijaune.model.RequetePojo;
import com.projets.heven.taxijaune.onclick.ClickListener;
import com.projets.heven.taxijaune.onclick.RecyclerTouchListener;
import com.projets.heven.taxijaune.settings.AppConst;
import com.projets.heven.taxijaune.settings.ConnectionDetector;
import com.projets.heven.taxijaune.settings.Progressdialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentRequete extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,
        GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMyLocationButtonClickListener{

    ViewPager pager;
    TabLayout tabs;
    View view;
    public static Context context;
    public static ConnectionDetector connectionDetector;
    String TAG="FragmentAccueil";
    ArrayList<String> tabNames = new ArrayList<String>();
    int currpos=0;

    public static RecyclerView recycler_view_requetes;
    public static List<RequetePojo> albumList_requetes;
    public static RequeteAdapter adapter_requetes;

    /** MAP **/
    private GoogleMap mMap;
    public static Location currentLocation, clientLocation, destinationLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_REQUEST_CODE = 101;

    /** GOOGLE API CLIENT **/
    private GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 5000, FASTEST_INTERVAL = 5000; // = 5 seconds

    View mapView;
    private LocationManager locationManager;
    private final int REQUEST_FINE_LOCATION = 1234;
    private String provider;
    private int PLACE_PICKER_REQUEST = 1;

    public static Progressdialog progressdialog;
    public static SwipeRefreshLayout swipe_refresh;

    public static ProgressBar progressBar_failed;
    public static LinearLayout layout_not_found,layout_failed;
    public static RelativeLayout layout_liste;
    private static LinearLayout linear_layout;
    private static RequetePojo requetePojo = null;

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
        view= inflater.inflate(R.layout.fragment_requete, container, false);

        context=getActivity();
        connectionDetector=new ConnectionDetector(context);
        progressdialog = new Progressdialog(context);
        progressdialog.init();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
//            return;
        }

        // Get the location manager
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (provider != null) {
            currentLocation = locationManager.getLastKnownLocation(provider);
            clientLocation = locationManager.getLastKnownLocation(provider);
            destinationLocation = locationManager.getLastKnownLocation(provider);
        }

        // we build google api client
        googleApiClient = new GoogleApiClient.Builder(context)
//                .enableAutoManage(getActivity(),0,this)
                .addApi(LocationServices.API)
//                .addApi(Places.GEO_DATA_API)
//                .addApi(Places.PLACE_DETECTION_API)
//                .enableAutoManage(getActivity(), this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        albumList_requetes = new ArrayList<>();
        adapter_requetes = new RequeteAdapter(context, albumList_requetes, getActivity(), "Requete");

        linear_layout = (LinearLayout) view.findViewById(R.id.linear_layout);
        progressBar_failed = (ProgressBar) view.findViewById(R.id.progressBar_failed);
        layout_liste = (RelativeLayout) view.findViewById(R.id.layout_liste);
        layout_not_found = (LinearLayout) view.findViewById(R.id.layout_not_found);
        layout_failed = (LinearLayout) view.findViewById(R.id.layout_failed);
        swipe_refresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        recycler_view_requetes = (RecyclerView) view.findViewById(R.id.recycler_view_requetes);
        @SuppressLint("WrongConstant") LinearLayoutManager horizontalLayoutManagerGarde = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recycler_view_requetes.setLayoutManager(horizontalLayoutManagerGarde);
        recycler_view_requetes.setItemAnimator(new DefaultItemAnimator());
        recycler_view_requetes.setAdapter(adapter_requetes);

//        recycler_view_requetes.addOnItemTouchListener(new RecyclerTouchListener(context, recycler_view_requetes, new ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                RequetePojo requetePojo = albumList_requetes.get(position);
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//                RequetePojo requetePojo = albumList_requetes.get(position);
//            }
//        }));

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getRequete().execute();
            }
        });

        layout_failed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar_failed.setVisibility(View.VISIBLE);
                new getRequete().execute();
            }
        });

        swipe_refresh.setRefreshing(true);
        new getRequete().execute();

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
    }

    private static void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(context,"requete") {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                requetePojo = adapter_requetes.getData().get(position);

                showMessageAccepter(requetePojo.getId(),position);
                adapter_requetes.notifyDataSetChanged();
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recycler_view_requetes);
    }

    private static void showSnackbar(final int position){
        Snackbar snackbar = Snackbar
                .make(linear_layout, "You accepted the order successfully.", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adapter_requetes.restoreItem(requetePojo, position);
                recycler_view_requetes.scrollToPosition(position);
            }
        });

        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    }

    public static void showMessageAccepter(final int idRequete, final int position){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getResources().getString(R.string.voulez_vous_accepter_cette_course))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showDialog();
                        new accepterRequete().execute(String.valueOf(idRequete), String.valueOf(position));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    /** Accepter une requête**/
    private static class accepterRequete extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"acceper_requete.php";
            final String requete = params[0];
            final String position = params[1];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                dismissDialog();
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    adapter_requetes.delete(Integer.parseInt(position));
                                    adapter_requetes.notifyDataSetChanged();
                                    Toast.makeText(context, context.getResources().getString(R.string.la_requete_a_ete_valide_avec_succes), Toast.LENGTH_SHORT).show();
                                    if(albumList_requetes.size() == 0)
                                        showNotFound();
//                                    showSnackbar(Integer.parseInt(position));
                                }else if(etat.equals("2")){
                                    Toast.makeText(context, context.getResources().getString(R.string.echec_de_validation), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, context.getResources().getString(R.string.un_de_vos_collegue_a_deja_valide_cette_course), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    dismissDialog();
//                    Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_conducteur", M.getID(context));
                    params.put("id_requete", requete);
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

    @Override
    public void onLocationChanged(Location location) {
        this.currentLocation = location;
//        Toast.makeText(context, "Ok", Toast.LENGTH_SHORT).show();
        if (location != null) {
            getDistance(location);
        }
    }

    /** Start COOGLE API Client **/
    @Override
    public void onStart() {
        super.onStart();

        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Permissions ok, we get last location
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "You need to enable permissions to display location !", Toast.LENGTH_SHORT).show();
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
//        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

//    private void getRequete(){
//        RequetePojo requetePojo;
//        requetePojo = new RequetePojo(1,1,"Woumtana Youssouf","5 km","24 Sep. à 10h.","oui","à 1 km d'ici");
//        albumList_requetes.add(requetePojo);
//        albumList_requetes.add(requetePojo);
//        albumList_requetes.add(requetePojo);
//        albumList_requetes.add(requetePojo);
//        albumList_requetes.add(requetePojo);
//        albumList_requetes.add(requetePojo);
//        albumList_requetes.add(requetePojo);
//        albumList_requetes.add(requetePojo);
//        albumList_requetes.add(requetePojo);
//        albumList_requetes.add(requetePojo);
//        albumList_requetes.add(requetePojo);
//        adapter_requetes.notifyDataSetChanged();
//    }

    private static String getDistance(Location loc1, Location loc2){
        final String[][] tab = {{}};

        float distanceInMeters1 = loc1.distanceTo(loc2);
        tab[0] = String.valueOf(distanceInMeters1).split("\\.");
        String distance = tab[0][0];
        return distance;
    }

    private void getDistance(Location location){
        if(location != null){
            Location loc1 = new Location("");
            Location loc2 = new Location("");
            loc2.setLatitude(location.getLatitude());
            loc2.setLongitude(location.getLongitude());
            final String[][] tab = {{}};
            RequetePojo requetePojo;
            float distanceInMeters1 = 0;
            String distance = "",virgule = "";

            for(int i=0; i<adapter_requetes.getItemCount(); i++){
                requetePojo = albumList_requetes.get(i);
                loc1.setLatitude(Double.parseDouble(requetePojo.getLatitude_client()));
                loc1.setLongitude(Double.parseDouble(requetePojo.getLongitude_client()));

                distanceInMeters1 = loc1.distanceTo(loc2);
                tab[0] = String.valueOf(distanceInMeters1).split("\\.");
                distance = tab[0][0];
//                Log.i("distance", String.valueOf(distanceInMeters1));
                if(distance.length() > 3) {
                    virgule = distance.substring(distance.length() - 3,distance.length() - 2);
                    distance = distance.substring(0, distance.length() - 3);
                    requetePojo.setDistance_client(distance+"."+virgule+" km");
                }else
                    requetePojo.setDistance_client(distance+" m");
            }
//            adapter_requetes.CalculDistance();
            adapter_requetes.notifyDataSetChanged();
        }
    }

    /** Récupération des requêtes d'un conducteur**/
    public static class getRequete extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"get_requete.php";
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                swipe_refresh.setRefreshing(false);
                                albumList_requetes.clear();
                                adapter_requetes.notifyDataSetChanged();
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    layout_liste.setVisibility(View.VISIBLE);
                                    layout_not_found.setVisibility(View.GONE);
                                    layout_failed.setVisibility(View.GONE);
                                    progressBar_failed.setVisibility(View.GONE);

                                    String distance_client = "";
                                    for(int i=0; i<(msg.length()-1); i++) {
                                        JSONObject taxi = msg.getJSONObject(String.valueOf(i));
//                                        destinationLocation.setLatitude(Float.parseFloat(taxi.getString("latitude_arrivee")));
//                                        destinationLocation.setLongitude(Float.parseFloat(taxi.getString("longitude_arrivee")));
                                        if(currentLocation != null && clientLocation != null){
                                            clientLocation.setLatitude(Float.parseFloat(taxi.getString("latitude_depart")));
                                            clientLocation.setLongitude(Float.parseFloat(taxi.getString("longitude_depart")));
                                            distance_client = getDistance(currentLocation, clientLocation);
                                            if(distance_client.length() > 3) {
                                                String virgule = distance_client.substring(distance_client.length() - 3,distance_client.length() - 2);
                                                distance_client = distance_client.substring(0, distance_client.length() - 3);
                                                distance_client = distance_client+"."+virgule+" km";
                                            }else
                                                distance_client = distance_client+" m";
                                        }else{
                                            distance_client = "0 m";
                                        }
                                        albumList_requetes.add(new RequetePojo(taxi.getInt("id"),taxi.getInt("id_user_app"),0, taxi.getString("nom")+" "+taxi.getString("prenom"), "", taxi.getString("distance"),taxi.getString("creer"),taxi.getString("statut"),distance_client,taxi.getString("latitude_depart")
                                                ,taxi.getString("longitude_depart"),taxi.getString("latitude_arrivee"),taxi.getString("longitude_arrivee"),taxi.getString("statut_course")
                                                ,"","","",taxi.getString("montant"),taxi.getString("duree")));
                                        adapter_requetes.notifyDataSetChanged();
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
                    params.put("id_conducteur", M.getID(context));
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

    public static void showNotFound(){
        layout_liste.setVisibility(View.GONE);
        layout_not_found.setVisibility(View.VISIBLE);
        layout_failed.setVisibility(View.GONE);
        progressBar_failed.setVisibility(View.GONE);
    }

    public static void showDialog(){
        progressdialog.show();
    }

    public static void dismissDialog(){
        progressdialog.dismiss();
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        menu.clear();
//        inflater.inflate(R.menu.menu_home,menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId()==R.id.mi_search){
//            Intent it=new Intent(context,SearchActivity.class);
//            startActivity(it);
//        }
//        return super.onOptionsItemSelected(item);
//    }

}
