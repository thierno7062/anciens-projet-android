package com.projets.heven.taxijaune.fragment;

import android.Manifest;
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
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
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
import com.projets.heven.taxijaune.R;
import com.projets.heven.taxijaune.controller.AppController;
import com.projets.heven.taxijaune.directionhelpers.FetchURLConducteur;
import com.projets.heven.taxijaune.directionhelpers.FetchURLCourse;
import com.projets.heven.taxijaune.model.CoursePojo;
import com.projets.heven.taxijaune.model.M;
import com.projets.heven.taxijaune.settings.AppConst;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Woumtana on 01/01/2019.
 */

public class BottomSheetFragmentCourse extends BottomSheetDialogFragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,
        GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMyLocationButtonClickListener {
    private static Context context;
    private Activity activity;
    private SupportMapFragment mapFragment;
    private String statut = "", distance = "", distance_client = "", id_course = "", position = "", user_name = "", duration = "",
            latitude_client,longitude_client,latitude_distination,longitude_distination;
    public static TextView distance_requete,terminer,statut_requete,fermer,montant_course,user_name_input,duree_requete;
//    private Marker clientMarker = null, destinationMarker = null;

    /** MAP **/
    public static GoogleMap mMap;
    public static Location currentLocation = new Location("dummyprovider1"), clientLocation = new Location("dummyprovider2"), destinationLocation = new Location("dummyprovider3");
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
    public static Marker markerDepart, markerDestination;
    private static String[] tabDistance = {};
    private static String distance_init,cout;
    public static Polyline currentPolyline;
    private ImageView btn_notifier_payer;
    private RelativeLayout layout_notifier_payer;

    public static ArrayList<LatLng> points;
    private static String img_data,img_name;

    public BottomSheetFragmentCourse() {
        // Required empty public constructor
    }

    public BottomSheetFragmentCourse(Activity activity,String statut,String distance
            ,String latitude_client,String longitude_client,String latitude_distination,String longitude_distination
            ,String id_course,String position,String user_name,String duration) {
        this.activity = activity;
        this.statut = statut;
        this.distance = distance;
        this.distance_client = distance_client;
        this.latitude_client = latitude_client;
        this.longitude_client = longitude_client;
        this.latitude_distination = latitude_distination;
        this.longitude_distination = longitude_distination;
        this.id_course = id_course;
        this.position = position;
        this.user_name = user_name;
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
        View rootView = inflater.inflate(R.layout.fragment_bottom_sheet_course, container, false);

        context = getContext();
        points = new ArrayList<>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);

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
//            clientLocation = locationManager.getLastKnownLocation(provider);
//            destinationLocation = locationManager.getLastKnownLocation(provider);
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


        terminer = (TextView)rootView.findViewById(R.id.terminer);
        distance_requete = (TextView)rootView.findViewById(R.id.distance_course);
        statut_requete = (TextView)rootView.findViewById(R.id.statut_course);
        fermer = (TextView)rootView.findViewById(R.id.fermer);
        montant_course = (TextView)rootView.findViewById(R.id.montant_course);
        user_name_input = (TextView)rootView.findViewById(R.id.user_name_input);
        btn_notifier_payer = (ImageView) rootView.findViewById(R.id.btn_notifier_payer);
        layout_notifier_payer = (RelativeLayout) rootView.findViewById(R.id.layout_notifier_payer);
        duree_requete = (TextView) rootView.findViewById(R.id.duree_requete);

//        if(distance.length() > 3) {
//            String virgule = distance.substring(distance.length() - 3,distance.length() - 2);
//            distance = distance.substring(0, distance.length() - 3);
//            distance = distance+"."+virgule+" km";
//        }else
//            distance = distance+" m";
//        distance_requete.setText(distance);
        duree_requete.setText(duration);
        terminer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentMesCourses.showDialog();
                getStaticMap();
            }
        });
        btn_notifier_payer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentMesCourses.showDialog();
                new sendPaymentMsg().execute(id_course);
            }
        });
        fermer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        setCancelable(false);

        user_name_input.setText(user_name);
        statut_requete.setText(statut);
        if(statut.equals("en cours")){
            statut_requete.setBackground(context.getResources().getDrawable(R.drawable.custom_bg_statut_en_cours));
            statut_requete.setTextColor(context.getResources().getColor(R.color.colorLogoBlack));
        }else if(statut.equals("accepter")){
            statut_requete.setBackground(context.getResources().getDrawable(R.drawable.custom_bg_statut_valide));
            statut_requete.setTextColor(Color.WHITE);
        }else if(statut.equals("annuler")){
            statut_requete.setBackground(context.getResources().getDrawable(R.drawable.custom_bg_statut_annuler));
            statut_requete.setTextColor(Color.WHITE);
        }else{
            terminer.setVisibility(View.GONE);
            layout_notifier_payer.setVisibility(View.GONE);
            statut_requete.setBackground(context.getResources().getDrawable(R.drawable.custom_bg_statut_execute));
            statut_requete.setTextColor(Color.WHITE);
        }

        return rootView;
    }

    /** Clôturer une course **/
    private class terminerCourse extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"terminer_course.php";
            final String requete = params[0];
            final String position = params[1];
            final String image = params[2];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                FragmentMesCourses.dismissDialog();
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    CoursePojo coursePojo = FragmentMesCourses.albumList_course.get(Integer.parseInt(position));
                                    coursePojo.setStatut("clôturer");
                                    FragmentMesCourses.adapter_course.notifyDataSetChanged();
                                    Toast.makeText(context, context.getResources().getString(R.string.you_have_finished_the_race), Toast.LENGTH_SHORT).show();
                                    terminer.setVisibility(View.GONE);
                                    statut_requete.setBackground(context.getResources().getDrawable(R.drawable.custom_bg_statut_execute));
                                    statut_requete.setTextColor(Color.WHITE);
                                    statut_requete.setText(coursePojo.getStatut());
                                    layout_notifier_payer.setVisibility(View.GONE);
                                }else {

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    FragmentMesCourses.dismissDialog();
//                    Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_conducteur", M.getID(context));
                    params.put("id_requete", requete);
                    params.put("image", image);
                    params.put("image_name", "recu_trajet_image_"+id_course+".jpg");
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

    private void getStaticMap(){
        if(points.size() != 0){
            String path = "";
            for(int i=0; i<points.size(); i++){
                LatLng latLng = points.get(i);
                if(i==0)
                    path = latLng.latitude+","+latLng.longitude;
                else
                    path = path+"|"+latLng.latitude+","+latLng.longitude;
            }
            final String STATIC_MAP_API_ENDPOINT = "http://maps.googleapis.com/maps/api/staticmap?size=1000x300&markers="+latitude_client+","+longitude_client+"|"+latitude_distination+","+longitude_distination+"&path=color:0x000000|"+path+"&sensor=false&key=AIzaSyB9pYFPSnzYV-IkWHTe0PXciACAsVwJU0E";

            // loading model cover using Glide library
            Glide.with(getActivity())
                    .asBitmap()
                    .load(STATIC_MAP_API_ENDPOINT)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
//                        Toast.makeText(activity, ""+bitmap, Toast.LENGTH_SHORT).show();
                            img_data = getStringImage(bitmap);
                            new terminerCourse().execute(id_course,String.valueOf(position),img_data);
                        }
                    });
        }else{
            FragmentMesCourses.dismissDialog();
            Toast.makeText(context, context.getResources().getString(R.string.veuillez_attendre_que_les_donnees_se_chargent), Toast.LENGTH_SHORT).show();
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    /** Clôturer une course **/
    private class sendPaymentMsg extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"send_payment_msg.php";
            final String requete = params[0];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                FragmentMesCourses.dismissDialog();
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    Toast.makeText(context, context.getResources().getString(R.string.message_envoye_avec_succes), Toast.LENGTH_SHORT).show();
                                }else {

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    FragmentMesCourses.dismissDialog();
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
    public void onDestroyView() {
        super.onDestroyView();

        if (mapFragment != null)
            getFragmentManager().beginTransaction().remove(mapFragment).commit();
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    public static void parseRouteDistance(JSONObject jObject) {

        JSONArray jRoutes;
        JSONArray jLegs;
        try {
            jRoutes = jObject.getJSONArray("routes");
            /** Traversing all routes */
            for (int i = 0; i < jRoutes.length(); i++) {
                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                /** Traversing all legs */
                for (int j = 0; j < jLegs.length(); j++) {
                    String distance = ((JSONObject) jLegs.get(j)).getJSONObject("distance").getString("text");
                    String start_address = ((JSONObject) jLegs.get(j)).getString("start_address");
                    String end_address = ((JSONObject) jLegs.get(j)).getString("end_address");
//                    String duration = ((JSONObject) jLegs.get(j)).getJSONObject("duration").getString("text");

                    tabDistance = distance.split(" ");

                    if(tabDistance[1].equals("m"))
                        distance_init = tabDistance[0];
                    else
                        distance_init = String.valueOf(Float.parseFloat(tabDistance[0])*1000);

                    cout = String.valueOf((Float.parseFloat(M.getCoutByKm(context))/1000) * Float.parseFloat(distance_init));

                    montant_course.setText(cout+" $");
                    distance_requete.setText(distance);

                    markerDepart.setSnippet(start_address);
                    markerDestination.setSnippet(end_address);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
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

        addMarkerDepart(new LatLng(Double.parseDouble(latitude_client),Double.parseDouble(longitude_client)),context.getResources().getString(R.string.depart),context.getResources().getString(R.string.position_du_client));
        addMarkerDestination(new LatLng(Double.parseDouble(latitude_distination),Double.parseDouble(longitude_distination)),context.getResources().getString(R.string.destination),context.getResources().getString(R.string.destination_du_client));

        M.setCurrentFragment("course",context);
        new FetchURLCourse(getActivity()).execute(getUrl(markerDepart.getPosition(), markerDestination.getPosition(), "driving"), "driving");

        mMap.setMyLocationEnabled(true);
        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.setMargins(0, 20, 20, 400);
        }

//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Initialize the location fields
        if (currentLocation != null) {
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)             // Sets the center of the map to location user
                    .zoom(13)                   // Sets the zoom
//                    .bearing(90)                // Sets the orientation of the camera to east
//                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
//                Toast.makeText(context, ""+latLng.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addMarkerDepart(LatLng latLng, String title, String sniper){
        // Add Marker to Map
        MarkerOptions option = new MarkerOptions();
        option.title(title);
        option.snippet(sniper);
        option.position(latLng);
        option.icon(generateBitmapDescriptorFromRes(context, R.drawable.ic_pin_2));
        markerDepart = mMap.addMarker(option);
        markerDepart.setTag(title);
    }

    private void addMarkerDestination(LatLng latLng, String title, String sniper){
        // Add Marker to Map
        MarkerOptions option = new MarkerOptions();
        option.title(title);
        option.snippet(sniper);
        option.position(latLng);
        option.icon(generateBitmapDescriptorFromRes(context, R.drawable.ic_arrival_point_2));
        markerDestination = mMap.addMarker(option);
        markerDestination.setTag(title);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if(!isLocationEnabled(context))
                    showMessageEnabledGPS();
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
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

    @Override
    public void onLocationChanged(Location location) {
        this.currentLocation = location;
//        Toast.makeText(context, "Ok", Toast.LENGTH_SHORT).show();
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onMyLocationButtonClick() {
//        Toast.makeText(mContext, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        if(!isLocationEnabled(context))
            showMessageEnabledGPS();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
//        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    public void showMessageEnabledGPS(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getResources().getString(R.string.ce_service_necessite_l_activation_du_service_gps))
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

    private void addMarker(LatLng latLng,String title,String sniper){
        // Add Marker to Map
        MarkerOptions option = new MarkerOptions();
        option.title(title);
        option.snippet(sniper);
        option.position(latLng);
        option.icon(generateBitmapDescriptorFromRes(context, R.drawable.ic_location_pin_1));
        Marker marker = mMap.addMarker(option);
        marker.setTag(title);
    }

    public static BitmapDescriptor generateBitmapDescriptorFromRes(
            Context context, int resId) {
        Drawable drawable = ContextCompat.getDrawable(context, resId);
        drawable.setBounds(
                0,
                0,
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
