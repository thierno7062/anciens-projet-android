package com.projets.heven.taxijaune.fragment;

/**
 * Created by Woumtana Pingdiwindé Youssouf 03/2019
 * Tel: +226 63 86 22 46 - 73 35 41 41
 * Email: issoufwoumtana@gmail.com
 **/

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.material.tabs.TabLayout;
import com.projets.heven.taxijaune.R;
import com.projets.heven.taxijaune.activity.TchatActivity;
import com.projets.heven.taxijaune.adapter.CourseAdapter;
import com.projets.heven.taxijaune.controller.AppController;
import com.projets.heven.taxijaune.listener.SwipeToDeleteCallback;
import com.projets.heven.taxijaune.model.CoursePojo;
import com.projets.heven.taxijaune.model.M;
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

public class FragmentMesCourses extends Fragment  implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,
        GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMyLocationButtonClickListener{

    ViewPager pager;
    TabLayout tabs;
    View view;
    private static Context context;
    ConnectionDetector connectionDetector;
    String TAG="FragmentAccueil";
    ArrayList<String> tabNames = new ArrayList<String>();
    int currpos=0;

    public static RecyclerView recycler_view_course;
    public static List<CoursePojo> albumList_course;
    public static CourseAdapter adapter_course;

    /** MAP **/
    private GoogleMap mMap;
    private Location currentLocation, clientLocation, destinationLocation;
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
    private static CoursePojo coursePojo = null;

    public ProgressBar progressBar_failed;
    private LinearLayout layout_not_found,layout_failed;
    private RelativeLayout layout_liste;

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
        view= inflater.inflate(R.layout.fragment_mes_courses, container, false);

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

        albumList_course = new ArrayList<>();
        adapter_course = new CourseAdapter(context, albumList_course, getActivity());

        progressBar_failed = (ProgressBar) view.findViewById(R.id.progressBar_failed);
        layout_liste = (RelativeLayout) view.findViewById(R.id.layout_liste);
        layout_not_found = (LinearLayout) view.findViewById(R.id.layout_not_found);
        layout_failed = (LinearLayout) view.findViewById(R.id.layout_failed);
        swipe_refresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        recycler_view_course = (RecyclerView) view.findViewById(R.id.recycler_view_course);
        @SuppressLint("WrongConstant") LinearLayoutManager horizontalLayoutManagerGarde = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recycler_view_course.setLayoutManager(horizontalLayoutManagerGarde);
        recycler_view_course.setItemAnimator(new DefaultItemAnimator());
        recycler_view_course.setAdapter(adapter_course);

//        recycler_view_course.addOnItemTouchListener(new RecyclerTouchListener(context, recycler_view_course, new ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                CoursePojo coursePojo = albumList_course.get(position);
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//                CoursePojo coursePojo = albumList_course.get(position);
//            }
//        }));
//        getCourses();

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getCourses().execute();
            }
        });

        layout_failed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar_failed.setVisibility(View.VISIBLE);
                new getCourses().execute();
            }
        });

        swipe_refresh.setRefreshing(true);
        new getCourses().execute();

        return view;
    }

//    private void getCourses(){
//        CoursePojo coursePojo;
//        coursePojo = new CoursePojo(1,1,"Woumtana youssouf","5 km","24 Sep. à 10h.","oui","30.000 $");
//        albumList_course.add(coursePojo);
//        albumList_course.add(coursePojo);
//        albumList_course.add(coursePojo);
//        albumList_course.add(coursePojo);
//        albumList_course.add(coursePojo);
//        albumList_course.add(coursePojo);
//        albumList_course.add(coursePojo);
//        albumList_course.add(coursePojo);
//        albumList_course.add(coursePojo);
//        albumList_course.add(coursePojo);
//        albumList_course.add(coursePojo);
//        adapter_course.notifyDataSetChanged();
//    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        menu.clear();
//        inflater.inflate(R.menu.menu_course,menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId()==R.id.action_effectuer){
////            Intent it=new Intent(context,SearchActivity.class);
////            startActivity(it);
//        }
//        if(item.getItemId()==R.id.action_en_cours){
////            Intent it=new Intent(context,SearchActivity.class);
////            startActivity(it);
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private static void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(context,"mes_courses") {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                coursePojo = adapter_course.getData().get(position);

                Intent intent = new Intent(context, TchatActivity.class);
                intent.putExtra("id_receveur",coursePojo.getUser_id());
                intent.putExtra("id_envoyeur",M.getID(context));
                intent.putExtra("id_requete",coursePojo.getId());
                intent.putExtra("nom_receveur",coursePojo.getUser_name());
                context.startActivity(intent);
                adapter_course.notifyDataSetChanged();
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recycler_view_course);
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

    private String getDistance(Location loc1, Location loc2){
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
            CoursePojo coursePojo;
            float distanceInMeters1 = 0;
            String distance = "",virgule = "";

            for(int i=0; i<adapter_course.getItemCount(); i++){
                coursePojo = albumList_course.get(i);
                loc1.setLatitude(Double.parseDouble(coursePojo.getLatitude_client()));
                loc1.setLongitude(Double.parseDouble(coursePojo.getLongitude_client()));

                distanceInMeters1 = loc1.distanceTo(loc2);
                tab[0] = String.valueOf(distanceInMeters1).split("\\.");
                distance = tab[0][0];
//                Log.i("distance", String.valueOf(distanceInMeters1));
                if(distance.length() > 3) {
                    virgule = distance.substring(distance.length() - 3,distance.length() - 2);
                    distance = distance.substring(0, distance.length() - 3);
                    coursePojo.setDistance_destination(distance+"."+virgule+" km");
                }else
                    coursePojo.setDistance_destination(distance+" m");
            }
//            adapter_course.CalculDistance();
            adapter_course.notifyDataSetChanged();
        }
    }

    /** Récupération des requêtes d'un conducteur**/
    private class getCourses extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"get_course.php";
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                swipe_refresh.setRefreshing(false);
                                albumList_course.clear();
                                adapter_course.notifyDataSetChanged();
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    layout_liste.setVisibility(View.VISIBLE);
                                    layout_not_found.setVisibility(View.GONE);
                                    layout_failed.setVisibility(View.GONE);
                                    progressBar_failed.setVisibility(View.GONE);

                                    String distance_requete = "", distance_destination = "", montant = "";
                                    for(int i=0; i<(msg.length()-1); i++) {
                                        JSONObject taxi = msg.getJSONObject(String.valueOf(i));
//                                        destinationLocation.setLatitude(Float.parseFloat(taxi.getString("latitude_arrivee")));
//                                        destinationLocation.setLongitude(Float.parseFloat(taxi.getString("longitude_arrivee")));
                                        if(currentLocation != null && clientLocation != null){
                                            clientLocation.setLatitude(Float.parseFloat(taxi.getString("latitude_depart")));
                                            clientLocation.setLongitude(Float.parseFloat(taxi.getString("longitude_depart")));
                                            distance_destination = getDistance(currentLocation, clientLocation);
                                            if(distance_destination.length() > 3) {
                                                String virgule = distance_destination.substring(distance_destination.length() - 3,distance_destination.length() - 2);
                                                distance_destination = distance_destination.substring(0, distance_destination.length() - 3);
                                                distance_destination = distance_destination+"."+virgule+" km";
                                            }else
                                                distance_destination = distance_destination+" m";
                                        }else{
                                            distance_destination = "0 m";
                                        }
                                        distance_requete = taxi.getString("distance");

                                        montant = String.valueOf((Float.parseFloat(M.getCoutByKm(context))/1000) * Float.parseFloat(taxi.getString("distance")));

                                        albumList_course.add(new CoursePojo(taxi.getInt("id"),taxi.getInt("id_user_app"),taxi.getInt("id_conducteur_accepter"),taxi.getString("nom")+" "+taxi.getString("prenom"), taxi.getString("nomConducteur")+" "+taxi.getString("prenomConducteur"), distance_requete,taxi.getString("creer"),taxi.getString("statut_course"),montant,taxi.getString("latitude_depart")
                                                ,taxi.getString("longitude_depart"),taxi.getString("latitude_arrivee"),taxi.getString("longitude_arrivee"),distance_destination,String.valueOf(i+1),taxi.getString("duree")));
                                        adapter_course.notifyDataSetChanged();
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

    public static void showDialog(){
        progressdialog.show();
    }

    public static void dismissDialog(){
        progressdialog.dismiss();
    }

}
