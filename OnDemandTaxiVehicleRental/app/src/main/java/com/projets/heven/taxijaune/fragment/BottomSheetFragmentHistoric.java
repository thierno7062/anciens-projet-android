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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

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
import com.projets.heven.taxijaune.R;
import com.projets.heven.taxijaune.controller.AppController;
import com.projets.heven.taxijaune.directionhelpers.FetchURLHistoric;
import com.projets.heven.taxijaune.model.M;
import com.projets.heven.taxijaune.model.RequetePojo;
import com.projets.heven.taxijaune.settings.AppConst;
import com.projets.heven.taxijaune.settings.ConnectionDetector;
import com.projets.heven.taxijaune.settings.Progressdialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Woumtana on 01/01/2019.
 */

public class BottomSheetFragmentHistoric extends BottomSheetDialogFragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,
        GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMyLocationButtonClickListener {
    private static Context context;
    private Activity activity;
    private SupportMapFragment mapFragment;
    public static String statut_course = "", distance = "", distance_client = "", duration = "", conducteur_name = "", statut = "",
    latitude_client = "",longitude_client = "",latitude_distination = "",longitude_distination = "",position = "",id_requete = "",id_conducteur = "",
    note = "",moyenne = "",nb_avis = "";
    public static TextView distance_requete,distance_client_requete,accepter,statut_requete,fermer,montant_requete,mon_recu
            ,conducteur_name_input,noter,nb_rate_conducteur,duree_requete;
//    private Marker clientMarker = null, destinationMarker = null;
    private RelativeLayout layout_conducteur;

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
    public static Polyline currentPolyline;

    public static Progressdialog progressdialog;
    public static Marker markerDepart, markerDestination;
    private static String[] tabDistance = {};
    private static String distance_init,cout;
    public static AlertDialog alertDialog;
    private TextView cancel_note,save_note;
    ConnectionDetector connectionDetector;
    private RatingBar rate_conducteur,rate_conducteur_princ;
//    private float note_value = 3f;

    public BottomSheetFragmentHistoric() {
        // Required empty public constructor
    }

    public BottomSheetFragmentHistoric(Activity activity, String statut_course, String distance, String distance_client
            , String latitude_client, String longitude_client, String latitude_distination, String longitude_distination
    , String id_requete, String position, String conducteur_name, String statut, String id_conducteur
            , String note, String moyenne, String nb_avis, String duration) {
        this.activity = activity;
        this.statut_course = statut_course;
        this.distance = distance;
        this.distance_client = distance_client;
        this.latitude_client = latitude_client;
        this.longitude_client = longitude_client;
        this.latitude_distination = latitude_distination;
        this.longitude_distination = longitude_distination;
        this.id_requete = id_requete;
        this.position = position;
        this.conducteur_name = conducteur_name;
        this.statut = statut;
        this.id_conducteur = id_conducteur;
        this.note = note;
        this.moyenne = moyenne;
        this.nb_avis = nb_avis;
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
        View rootView = inflater.inflate(R.layout.fragment_bottom_sheet_mes_requete, container, false);

        context = getContext();
        connectionDetector=new ConnectionDetector(context);
        progressdialog = new Progressdialog(context);
        progressdialog.init();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
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


        accepter = (TextView)rootView.findViewById(R.id.accepter);
        distance_requete = (TextView)rootView.findViewById(R.id.distance_requete);
        distance_client_requete = (TextView)rootView.findViewById(R.id.distance_client_requete);
        statut_requete = (TextView)rootView.findViewById(R.id.statut_requete);
        fermer = (TextView)rootView.findViewById(R.id.fermer);
        montant_requete = (TextView)rootView.findViewById(R.id.montant_requete);
        mon_recu = (TextView)rootView.findViewById(R.id.mon_recu);
        conducteur_name_input = (TextView)rootView.findViewById(R.id.conducteur_name_input);
        noter = (TextView)rootView.findViewById(R.id.noter);
        nb_rate_conducteur = (TextView)rootView.findViewById(R.id.nb_rate_conducteur);
        layout_conducteur = (RelativeLayout) rootView.findViewById(R.id.layout_conducteur);
        rate_conducteur_princ = (RatingBar) rootView.findViewById(R.id.rate_conducteur_princ);
        duree_requete = (TextView) rootView.findViewById(R.id.duree_requete);

        accepter.setVisibility(View.GONE);
        if(statut_course.equals("clôturer")) {
            mon_recu.setVisibility(View.VISIBLE);
            noter.setVisibility(View.VISIBLE);
        }else {
            mon_recu.setVisibility(View.GONE);
            noter.setVisibility(View.GONE);
        }

        duree_requete.setText(duration);
        accepter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
                new accepterRequete().execute(id_requete, position);
            }
        });
        fermer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mon_recu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetFragmentRecu bottomSheetFragmentRecu = new BottomSheetFragmentRecu(activity, id_requete, M.getID(context));
                bottomSheetFragmentRecu.show(((FragmentActivity) context).getSupportFragmentManager(), bottomSheetFragmentRecu.getTag());

            }
        });

        setCancelable(false);

        if(moyenne.trim().length() != 0) {
            rate_conducteur_princ.setRating(Float.parseFloat(moyenne));
            nb_rate_conducteur.setText(nb_avis+" avis");
        }
        conducteur_name_input.setText(conducteur_name);
        statut_requete.setText(statut_course);
        if(statut_course.equals("en cours")){
            statut_requete.setBackground(context.getResources().getDrawable(R.drawable.custom_bg_statut_en_cours));
            statut_requete.setTextColor(context.getResources().getColor(R.color.colorLogoBlack));
            layout_conducteur.setVisibility(View.GONE);
        }else if(statut_course.equals("accepter")){
            statut_requete.setBackground(context.getResources().getDrawable(R.drawable.custom_bg_statut_valide));
            statut_requete.setTextColor(Color.WHITE);
            layout_conducteur.setVisibility(View.VISIBLE);
        }else if(statut_course.equals("annuler")){
            statut_requete.setBackground(context.getResources().getDrawable(R.drawable.custom_bg_statut_annuler));
            statut_requete.setTextColor(Color.WHITE);
            layout_conducteur.setVisibility(View.VISIBLE);
        }else{
            statut_requete.setBackground(context.getResources().getDrawable(R.drawable.custom_bg_statut_execute));
            statut_requete.setTextColor(Color.WHITE);
            layout_conducteur.setVisibility(View.VISIBLE);
        }

        if(statut.equals("en cours") && statut_course.equals("")) {
            statut_requete.setText(statut);
            layout_conducteur.setVisibility(View.GONE);
            statut_requete.setBackground(context.getResources().getDrawable(R.drawable.custom_bg_statut_en_cours));
            statut_requete.setTextColor(context.getResources().getColor(R.color.colorLogoBlack));
        }else {
            layout_conducteur.setVisibility(View.VISIBLE);
        }

        noter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dialogNote();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }

    //This method would confirm the otp
    private void dialogNote() throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_noter, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        save_note = (TextView) confirmDialog.findViewById(R.id.save_note);
        cancel_note = (TextView) confirmDialog.findViewById(R.id.cancel_note);
        rate_conducteur = (RatingBar) confirmDialog.findViewById(R.id.rate_conducteur);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

//        Toast.makeText(context, ""+note, Toast.LENGTH_SHORT).show();
        if(note.trim().length() > 0)
            rate_conducteur.setRating(Float.parseFloat(note));
        else
            rate_conducteur.setRating(1f);
        //Creating an alert dialog
        alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        save_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connectionDetector.isConnectingToInternet()){
                    progressdialog.show();
                    new setNote().execute(String.valueOf(note),id_conducteur);
                }else{
                    Toast.makeText(context, context.getResources().getString(R.string.pas_de_connexion_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        rate_conducteur.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                note = String.valueOf(ratingBar.getRating());
            }
        });
    }

    /** Enregistrer la note d'un conducteur **/
    private class setNote extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"set_note.php";
            final String note_value = params[0];
            final String id_conducteur = params[1];
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
                                    alertDialog.cancel();
                                    progressdialog.dismiss();
                                    JSONObject note = msg.getJSONObject("note");
                                    String nb_avis = note.getString("nb_avis");
                                    String niveau = note.getString("niveau");
                                    String moyenne = note.getString("moyenne");

                                    RequetePojo requetePojo = FragmentHistoric.albumList_historic.get(Integer.parseInt(position));
                                    requetePojo.setNb_avis(nb_avis);
                                    requetePojo.setNote(niveau);
                                    requetePojo.setMoyenne(moyenne);
                                    FragmentHistoric.adapter_historic.notifyDataSetChanged();

                                    rate_conducteur_princ.setRating(Float.parseFloat(moyenne));
                                    nb_rate_conducteur.setText(nb_avis+" avis");
                                    rate_conducteur.setRating(Float.parseFloat(niveau));

                                    Toast.makeText(context, context.getResources().getString(R.string.note_attribuer_avec_succes), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, context.getResources().getString(R.string.echec_d_attribution), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(context, context.getResources().getString(R.string.echec_de_modification), Toast.LENGTH_SHORT).show();
                    alertDialog.show();
                    progressdialog.dismiss();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_user_app",M.getID(context));
                    params.put("id_conducteur",id_conducteur);
                    params.put("note_value",note_value);
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

                    montant_requete.setText(cout+" $");
                    distance_requete.setText(distance);
                    distance_client_requete.setText(""+distance_client+" from here");

                    markerDepart.setSnippet(start_address);
                    markerDestination.setSnippet(end_address);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
    }

    /** Accepter une requête**/
    private class accepterRequete extends AsyncTask<String, Void, String> {
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
                                    FragmentRequete.adapter_requetes.delete(Integer.parseInt(position));
                                    FragmentRequete.adapter_requetes.notifyDataSetChanged();
                                    Toast.makeText(context, context.getResources().getString(R.string.la_requete_a_ete_valide_avec_succes), Toast.LENGTH_SHORT).show();
                                    dismiss();
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
    public void onDestroyView() {
        super.onDestroyView();

        if (mapFragment != null)
            getFragmentManager().beginTransaction().remove(mapFragment).commit();
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

        clientLocation.setLatitude(Double.parseDouble(latitude_client));
        clientLocation.setLongitude(Double.parseDouble(longitude_client));
        destinationLocation.setLatitude(Double.parseDouble(latitude_distination));
        destinationLocation.setLatitude(Double.parseDouble(longitude_distination));

        addMarkerDepart(new LatLng(Double.parseDouble(latitude_client),Double.parseDouble(longitude_client)),context.getResources().getString(R.string.depart),context.getResources().getString(R.string.position_du_client));
        addMarkerDestination(new LatLng(Double.parseDouble(latitude_distination),Double.parseDouble(longitude_distination)),"Destination",context.getResources().getString(R.string.destination_du_client));

        M.setCurrentFragment("historic",context);
        new FetchURLHistoric(getActivity()).execute(getUrl(markerDepart.getPosition(), markerDestination.getPosition(), "driving"), "driving");

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

    public static void showDialog(){
        progressdialog.show();
    }

    public static void dismissDialog(){
        progressdialog.dismiss();
    }
}
