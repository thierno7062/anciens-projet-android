package com.projets.heven.taxijaune.activity;

/**
 * Created by Woumtana Pingdiwindé Youssouf 03/2019
 * Tel: +226 63 86 22 46 - 73 35 41 41
 * Email: issoufwoumtana@gmail.com
 **/

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.projets.heven.taxijaune.R;
import com.projets.heven.taxijaune.controller.AppController;
import com.projets.heven.taxijaune.directionhelpers.TaskLoadedCallback;
import com.projets.heven.taxijaune.fragment.BottomSheetFragmentCourse;
import com.projets.heven.taxijaune.fragment.BottomSheetFragmentHistoric;
import com.projets.heven.taxijaune.fragment.BottomSheetFragmentMesRequete;
import com.projets.heven.taxijaune.fragment.BottomSheetFragmentRequete;
import com.projets.heven.taxijaune.fragment.FragmentAccueil;
import com.projets.heven.taxijaune.fragment.FragmentHistoric;
import com.projets.heven.taxijaune.fragment.FragmentLocationVehicule;
import com.projets.heven.taxijaune.fragment.FragmentMesCourses;
import com.projets.heven.taxijaune.fragment.FragmentMesRequetes;
import com.projets.heven.taxijaune.fragment.FragmentMessage;
import com.projets.heven.taxijaune.fragment.FragmentMyLocationVehicule;
import com.projets.heven.taxijaune.fragment.FragmentRequete;
import com.projets.heven.taxijaune.fragment.FragmentProfile;
import com.projets.heven.taxijaune.fragment.FragmentReservation;
import com.projets.heven.taxijaune.model.DrawerPojo;
import com.projets.heven.taxijaune.model.M;
import com.projets.heven.taxijaune.settings.AppConst;
import com.projets.heven.taxijaune.settings.ConnectionDetector;
import com.projets.heven.taxijaune.settings.PrefManager;
import com.projets.heven.taxijaune.settings.Progressdialog;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements TaskLoadedCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private RecyclerView mDrawerList;
    public static DrawerLayout mDrawerLayout;
    public static Context context;
    ConnectionDetector connectionDetector;
    String TAG="MainActivity";
    DrawerAdapter drawerAdapter;
    public static ArrayList<DrawerPojo> list=new ArrayList<>();
    ImageView floatingActionButton;
    public static PrefManager prefManager;
    private FrameLayout drawer_conducteur;
    private LinearLayout drawer_user;
    private TextView user_name, user_phone,statut_conducteur;
    private SwitchCompat switch_statut;
    private static final int LOCATION_REQUEST_CODE = 101;
    public static Progressdialog progressdialog;
    public static FragmentManager fragmentManager;
    public static FragmentTransaction fragmentTransaction;
    public static Activity activity;
    private Boolean notification = false;

    /** MAP **/
    public static GoogleMap mMap;
    public static Location currentLocation;

    /** GOOGLE API CLIENT **/
    private GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 5000, FASTEST_INTERVAL = 5000; // = 5 seconds
    private LocationManager locationManager;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;
        activity = this;
        connectionDetector=new ConnectionDetector(context);
        prefManager = new PrefManager(this);
        progressdialog = new Progressdialog(context);
        progressdialog.init();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle objetbundle = this.getIntent().getExtras();
        String fragment_name = objetbundle.getString("fragment_name");

        if(!fragment_name.equals("")){
            notification = true;
            if(M.getUserCategorie(context).equals("user_app")){
                if(fragment_name.equals("mes_requetes"))
                    selectItem(1);
//                    fragmentManager.setBackStackEntryCount();
            }else{
                if(fragment_name.equals("requete"))
                    selectItem(0);
            }
        }

        switch_statut = (SwitchCompat) findViewById(R.id.switch_statut);
        user_name = (TextView) findViewById(R.id.user_name);
        user_phone = (TextView) findViewById(R.id.user_phone);
        statut_conducteur = (TextView) findViewById(R.id.statut_conducteur);
        drawer_conducteur = (FrameLayout) findViewById(R.id.drawer_conducteur);
        drawer_user = (LinearLayout) findViewById(R.id.drawer_user);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mDrawerLayout.setScrimColor(Color.GRAY);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationViewLeft = (NavigationView) findViewById(R.id.nav_view);
//        int width = R.dimen.drawer_width;//getResources().getDisplayMetrics().widthPixels;
//        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navigationViewLeft.getLayoutParams();
//        params.width = width;
//        navigationViewLeft.setLayoutParams(params);
        mDrawerList = (RecyclerView) findViewById(R.id.rvdrawer);
        mDrawerList.setLayoutManager(new LinearLayoutManager(context));
        mDrawerList.setHasFixedSize(true);

        setDrawer();

        if (savedInstanceState == null) {
            if(fragment_name.equals(""))
                selectItem(0);
        }

        if(!M.getUserCategorie(context).equals("user_app")) {
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
//            return;
            }

            if(!isLocationEnabled(context))
                showMessageEnabledGPS();
        }

        if(!M.getUserCategorie(context).equals("user_app")) {
            if (M.getStatutConducteur(context).equals("yes")) {
                switch_statut.setChecked(true);
                statut_conducteur.setText("enabled");
            } else {
                switch_statut.setChecked(false);
                statut_conducteur.setText("disabled");
            }
        }

        switch_statut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switch_statut.isChecked()) {
                    progressdialog.show();
                    new changerStatut().execute("yes");
                }else {
                    progressdialog.show();
                    new changerStatut().execute("no");
                }
            }
        });

        // Get the location manager
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (provider != null) {
            currentLocation = locationManager.getLastKnownLocation(provider);
        }

        // we build google api client
        googleApiClient = new GoogleApiClient.Builder(context)
//                .enableAutoManage(getActivity(),0,this)
                .addApi(LocationServices.API)
//                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
//                .addApi(Places.PLACE_DETECTION_API)
//                .enableAutoManage(getActivity(), this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        updateFCM(M.getID(context));
    }

    @Override
    public void onLocationChanged(Location location) {
        this.currentLocation = location;
//        Toast.makeText(context, "Ok", Toast.LENGTH_SHORT).show();

        if (location != null) {
            new setCurrentLocation().execute(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));
        }
    }

    /** MAJ de la position d'un conducteur **/
    private class setCurrentLocation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"set_position.php";
            final String latitude = params[0];
            final String longitude = params[1];
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
                    params.put("id_user", M.getID(context));
                    params.put("user_cat", M.getUserCategorie(context));
                    params.put("latitude", latitude);
                    params.put("longitude", longitude);
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

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == PLACE_PICKER_REQUEST) {
//            if (resultCode == RESULT_OK) {
//                Place place = PlacePicker.getPlace(data, getActivity());
//                StringBuilder stBuilder = new StringBuilder();
//                String placename = String.format("%s", place.getName());
//                String latitude = String.valueOf(place.getLatLng().latitude);
//                String longitude = String.valueOf(place.getLatLng().longitude);
//                String address = String.format("%s", place.getAddress());
//                stBuilder.append("Name: ");
//                stBuilder.append(placename);
//                stBuilder.append("\n");
//                stBuilder.append("Latitude: ");
//                stBuilder.append(latitude);
//                stBuilder.append("\n");
//                stBuilder.append("Logitude: ");
//                stBuilder.append(longitude);
//                stBuilder.append("\n");
//                stBuilder.append("Address: ");
//                stBuilder.append(address);
//                Toast.makeText(context, ""+stBuilder.toString(), Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

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

    /** Change driver status **/
    private class changerStatut extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"change_statut.php";
            final String online = params[0];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                progressdialog.dismiss();
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                String online = msg.getString("online");
                                if(etat.equals("1")){
                                    if(online.equals("yes")) {
                                        switch_statut.setChecked(true);
                                        statut_conducteur.setText("enabled");
                                        M.setStatutConducteur(online,context);
                                    }else {
                                        switch_statut.setChecked(false);
                                        statut_conducteur.setText("disabled");
                                        M.setStatutConducteur(online,context);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressdialog.dismiss();
                    if(switch_statut.isChecked())
                        switch_statut.setChecked(false);
                    else
                        switch_statut.setChecked(true);
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_conducteur", M.getID(context));
                    params.put("online", online);
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
    public void onTaskDone(Object... values) {
        if(M.getUserCategorie(context).equals("user_app")) {
            if (M.getCurrentFragment(context).equals("accueil")) {
                if (FragmentAccueil.currentPolyline != null)
                    FragmentAccueil.currentPolyline.remove();
                FragmentAccueil.currentPolyline = FragmentAccueil.mMap.addPolyline((PolylineOptions) values[0]);
                FragmentAccueil.currentPolyline.setColor(Color.DKGRAY);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                LatLng latLng1 = new LatLng(FragmentAccueil.departLocationReservation.getLatitude(), FragmentAccueil.departLocationReservation.getLongitude());
                LatLng latLng2 = new LatLng(FragmentAccueil.destinationLocationReservation.getLatitude(), FragmentAccueil.destinationLocationReservation.getLongitude());
                builder.include(latLng1);
                builder.include(latLng2);
                FragmentAccueil.mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 17));
            } else if (M.getCurrentFragment(context).equals("mes_requetes_accueil")){
                if (FragmentAccueil.currentPolyline != null)
                    FragmentAccueil.currentPolyline.remove();
                FragmentAccueil.currentPolyline = FragmentAccueil.mMap.addPolyline((PolylineOptions) values[0]);
                FragmentAccueil.currentPolyline.setColor(Color.DKGRAY);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                LatLng latLng1 = new LatLng(FragmentAccueil.departLocationMesRequetes.getLatitude(), FragmentAccueil.departLocationMesRequetes.getLongitude());
                LatLng latLng2 = new LatLng(FragmentAccueil.destinationLocationMesRequetes.getLatitude(), FragmentAccueil.destinationLocationMesRequetes.getLongitude());
                builder.include(latLng1);
                builder.include(latLng2);
                FragmentAccueil.mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 17));
            } else if (M.getCurrentFragment(context).equals("historic")){
                if (BottomSheetFragmentHistoric.currentPolyline != null)
                    BottomSheetFragmentHistoric.currentPolyline.remove();
                BottomSheetFragmentHistoric.currentPolyline = BottomSheetFragmentHistoric.mMap.addPolyline((PolylineOptions) values[0]);
                BottomSheetFragmentHistoric.currentPolyline.setColor(Color.DKGRAY);

                LatLngBounds.Builder builder2 = new LatLngBounds.Builder();
                LatLng latLng3 = new LatLng(BottomSheetFragmentHistoric.clientLocation.getLatitude(),BottomSheetFragmentHistoric.clientLocation.getLongitude());
                LatLng latLng4 = new LatLng(BottomSheetFragmentHistoric.destinationLocation.getLatitude(),BottomSheetFragmentHistoric.destinationLocation.getLongitude());
                builder2.include(latLng3);
                builder2.include(latLng4);
//            BottomSheetFragmentRequete.mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder2.build(), 100));
            }else{
                if (BottomSheetFragmentMesRequete.currentPolyline != null)
                    BottomSheetFragmentMesRequete.currentPolyline.remove();
                BottomSheetFragmentMesRequete.currentPolyline = BottomSheetFragmentMesRequete.mMap.addPolyline((PolylineOptions) values[0]);
                BottomSheetFragmentMesRequete.currentPolyline.setColor(Color.DKGRAY);

                LatLngBounds.Builder builder2 = new LatLngBounds.Builder();
                LatLng latLng3 = new LatLng(BottomSheetFragmentMesRequete.clientLocation.getLatitude(),BottomSheetFragmentMesRequete.clientLocation.getLongitude());
                LatLng latLng4 = new LatLng(BottomSheetFragmentMesRequete.destinationLocation.getLatitude(),BottomSheetFragmentMesRequete.destinationLocation.getLongitude());
                builder2.include(latLng3);
                builder2.include(latLng4);
//            BottomSheetFragmentRequete.mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder2.build(), 100));
            }
        }else{
            if(M.getCurrentFragment(context).equals("requete")){
                if (BottomSheetFragmentRequete.currentPolyline != null)
                    BottomSheetFragmentRequete.currentPolyline.remove();
                BottomSheetFragmentRequete.currentPolyline = BottomSheetFragmentRequete.mMap.addPolyline((PolylineOptions) values[0]);
                BottomSheetFragmentRequete.currentPolyline.setColor(Color.DKGRAY);

                LatLngBounds.Builder builder2 = new LatLngBounds.Builder();
                LatLng latLng3 = new LatLng(BottomSheetFragmentRequete.clientLocation.getLatitude(),BottomSheetFragmentRequete.clientLocation.getLongitude());
                LatLng latLng4 = new LatLng(BottomSheetFragmentRequete.destinationLocation.getLatitude(),BottomSheetFragmentRequete.destinationLocation.getLongitude());
                builder2.include(latLng3);
                builder2.include(latLng4);
//                BottomSheetFragmentRequete.mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder2.build(), 17));
            }else{
                if (BottomSheetFragmentCourse.currentPolyline != null)
                    BottomSheetFragmentCourse.currentPolyline.remove();
                BottomSheetFragmentCourse.currentPolyline = BottomSheetFragmentCourse.mMap.addPolyline((PolylineOptions) values[0]);
                BottomSheetFragmentCourse.currentPolyline.setColor(Color.DKGRAY);

                LatLngBounds.Builder builder2 = new LatLngBounds.Builder();
                LatLng latLng3 = new LatLng(BottomSheetFragmentCourse.clientLocation.getLatitude(),BottomSheetFragmentCourse.clientLocation.getLongitude());
                LatLng latLng4 = new LatLng(BottomSheetFragmentCourse.destinationLocation.getLatitude(),BottomSheetFragmentCourse.destinationLocation.getLongitude());
                builder2.include(latLng3);
                builder2.include(latLng4);
//                BottomSheetFragmentCourse.mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder2.build(), 17));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if(!M.getUserCategorie(context).equals("user_app")) {
                    if(!isLocationEnabled(context))
                        showMessageEnabledGPS();
                }else{
                    if(!isLocationEnabled(context))
                    showMessageEnabledGPSClient();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void showMessageEnabledGPSClient(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Ce service nécessite l'activation du service GPS. Voulez-vous activer le GPS ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
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

    public void showMessageEnabledGPS(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Activez le service GPS pour partager votre position avec les clients. Activez le GPS maintenant ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
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

    private boolean isLocationEnabled(Context context){
//        String locationProviders;
        boolean enabled = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            enabled = (mode != Settings.Secure.LOCATION_MODE_OFF);
        }else{
            LocationManager service = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            enabled =  service.isProviderEnabled(LocationManager.GPS_PROVIDER)||service.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        return enabled;
    }

    public void setDrawer() {
        mDrawerLayout.setFocusable(false);
        list.clear();
        if(M.getUserCategorie(context).equals("user_app")){
            drawer_conducteur.setVisibility(View.GONE);
            switch_statut.setVisibility(View.GONE);
            drawer_user.setVisibility(View.VISIBLE);
            user_name.setText(M.getPrenom(context)+" "+M.getNom(context));
            user_phone.setText(M.getPhone(context));

            list.add(new DrawerPojo(1, "", getString(R.string.item_home), R.drawable.ic_home_outline));
            list.add(new DrawerPojo(2, "", getString(R.string.item_mes_requete), R.drawable.ic_mes_requetes));
            list.add(new DrawerPojo(10, "", getString(R.string.historic), R.drawable.ic_list_outline));
            list.add(new DrawerPojo(3, "", getString(R.string.item_reservation), R.drawable.ic_list_outline));
            list.add(new DrawerPojo(4, "", getString(R.string.item_louer_vehicule), R.drawable.ic_rent_outline));
            list.add(new DrawerPojo(5, "", getString(R.string.item_vehicule_loue), R.drawable.ic_rent_outline));

//            list.add(new DrawerPojo(6, "", getString(R.string.item_message), R.drawable.ic_message_outline));
            list.add(new DrawerPojo(7, "", getString(R.string.item_profile), R.drawable.ic_profile_outline));
            list.add(new DrawerPojo(0, "", getString(R.string.item_logout), R.drawable.ic_logout_outline));
            list.add(new DrawerPojo(8, "", "divider", 0));
            list.add(new DrawerPojo(9, "", getString(R.string.item_help), 0));
        }else{
            drawer_conducteur.setVisibility(View.VISIBLE);
            switch_statut.setVisibility(View.VISIBLE);
            drawer_user.setVisibility(View.GONE);
            drawer_user.setVisibility(View.GONE);
            user_name.setText("");
            user_phone.setText("");

            list.add(new DrawerPojo(1, "", getString(R.string.item_requete), R.drawable.ic_requete_outline));
            list.add(new DrawerPojo(2, "", getString(R.string.item_mes_courses), R.drawable.ic_mes_courses_outline));

//            list.add(new DrawerPojo(3, "", getString(R.string.item_message), R.drawable.ic_message_outline));
            list.add(new DrawerPojo(4, "", getString(R.string.item_profile), R.drawable.ic_profile_outline));
            list.add(new DrawerPojo(0, "", getString(R.string.item_logout), R.drawable.ic_logout_outline));
            list.add(new DrawerPojo(5, "", "divider", 0));
            list.add(new DrawerPojo(6, "", getString(R.string.item_help), 0));
        }
//        list.add(new DrawerPojo(10, "", getString(R.string.item_message), R.drawable.ic_message_outline));
//        list.add(new DrawerPojo(2, "", getString(R.string.item_profile), R.drawable.ic_profile_outline));
//        list.add(new DrawerPojo(0, "", getString(R.string.item_logout), R.drawable.ic_logout_outline));
//        list.add(new DrawerPojo(7, "", "divider", 0));
//        list.add(new DrawerPojo(6, "", getString(R.string.item_help), 0));

        drawerAdapter=new DrawerAdapter(list,context);
        mDrawerList.setAdapter(drawerAdapter);
    }

    public class DrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<DrawerPojo> mDrawerItems;
        Context context;

        public DrawerAdapter(List<DrawerPojo> list, Context mcontext) {
            context = mcontext;
            mDrawerItems = list;
        }

        @Override
        public int getItemViewType(int position) {
            return 1;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_row, parent, false);
            return new ViewHolderPosts(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder1, final int position) {
            final ViewHolderPosts holder = (ViewHolderPosts) holder1;
            DrawerPojo item = mDrawerItems.get(position);

            if(item.getmText().equals("divider")) {
                holder.line_divider.setVisibility(View.VISIBLE);
                holder.layout_item.setVisibility(View.GONE);
            }else {
                holder.line_divider.setVisibility(View.GONE);
                holder.layout_item.setVisibility(View.VISIBLE);
                holder.title.setText(item.getmText());
                holder.llrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectItem(position);
                    }
                });
            }

            if(item.getmIconRes() == 0) {
                holder.img.setVisibility(View.GONE);
            }else {
                holder.img.setVisibility(View.VISIBLE);
                holder.img.setImageDrawable(getResources().getDrawable(item.getmIconRes()));
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return mDrawerItems.size();
        }

        public class ViewHolderPosts extends RecyclerView.ViewHolder {
            TextView title;
            LinearLayout llrow;
            ImageView img;
            View line_divider;
            LinearLayout layout_item;

            public ViewHolderPosts(View convertView) {
                super(convertView);
                llrow=(LinearLayout)convertView.findViewById(R.id.llrow);
                title = (TextView) convertView.findViewById(R.id.tvtitle);
                img = (ImageView) convertView.findViewById(R.id.img);
                line_divider = (View) convertView.findViewById(R.id.line_divider);
                layout_item = (LinearLayout) convertView.findViewById(R.id.layout_item);
            }
        }
    }

    public static void selectItem(int pos1){
        Fragment fragment = null;
        long pos=list.get(pos1).getmId();
        String item=list.get(pos1).getmText();
        String tag = "accueil";

        if(M.getUserCategorie(context).equals("user_app")){
            if(pos==1) {
                fragment = new FragmentAccueil();
                tag = "accueil";
            }else if(pos==2){
                fragment=new FragmentMesRequetes();
                tag = "mes_requetes";
            }else if(pos==3){
                fragment=new FragmentReservation();
                tag = "reservation";
            }else if(pos==4){
                fragment=new FragmentLocationVehicule();
                tag = "location_vehicule";
            }else if(pos==5){
                fragment=new FragmentMyLocationVehicule();
                tag = "mes_locations_vehicule";
//            else if(pos==6)
//                fragment=new FragmentMessage();
            }else if(pos==7){
                fragment=new FragmentProfile();
                tag = "profile";
            }else if(pos==10){
                fragment=new FragmentHistoric();
                tag = "historic";
//            else if(pos==9)
//                Toast.makeText(context, "Aide", Toast.LENGTH_SHORT).show();
//        else if(pos==6)
//            openBrowser();
            }else if(pos==0){
                M.logOut(context);
                prefManager.setFirstTimeLaunch7(true);
                Intent mIntent = new Intent(context, Connexion.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.finish();
                context.startActivity(mIntent);
            }
        }else{
            if(pos==1){
                fragment=new FragmentRequete();
                tag = "requete";
            }else if(pos==2){
                fragment=new FragmentMesCourses();
                tag = "mes_courses";
//            else if(pos==3)
//                fragment=new FragmentMessage();
            }else if(pos==4){
                fragment=new FragmentProfile();
                tag = "profile";
//        else if(pos==6)
//            openBrowser();
            }else if(pos==0){
                M.logOut(context);
                prefManager.setFirstTimeLaunch7(true);
                Intent mIntent = new Intent(context, Connexion.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.finish();
                context.startActivity(mIntent);
            }
        }

        if(fragment!=null) {
            fragmentManager = ((MainActivity)context).getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment, tag);
            if(pos!=1)
                fragmentTransaction.addToBackStack(item);
            else{
                fragmentTransaction.addToBackStack(null);
            }
            fragmentTransaction.commit();
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawer(Gravity.START);
        }else {
            if(notification == true){
                selectItem(0);
                notification = false;
            }else {
                FragmentManager fragmentManager = getSupportFragmentManager();
                String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
                if(fragmentTag==null)
                    finish();
                else {
//                    Toast.makeText(context, ""+fragmentManager.getBackStackEntryCount(), Toast.LENGTH_SHORT).show();
                    if(fragmentManager.getBackStackEntryCount()>=2){
                        String fragmentTag1 = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 2).getName();
//                        Toast.makeText(context, ""+fragmentTag1, Toast.LENGTH_SHORT).show();
                        if(fragmentTag1==null)
                            selectItem(0);
                        else
                            super.onBackPressed();
                    }else
                        super.onBackPressed();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==401 && resultCode==RESULT_OK) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            fragment.onActivityResult(requestCode, resultCode, data);
        }
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
                            deviceid[0] = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
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
                    params.put("user_cat",M.getUserCategorie(context));
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

    public static Fragment getCurrentFragment(){
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.content_frame);
        return currentFragment;
    }
}
