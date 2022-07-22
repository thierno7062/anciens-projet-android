package com.projets.heven.taxijaune.fragment;

/**
 * Created by Woumtana Pingdiwindé Youssouf 03/2019
 * Tel: +226 63 86 22 46 - 73 35 41 41
 * Email: issoufwoumtana@gmail.com
 **/

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.tabs.TabLayout;
import com.projets.heven.taxijaune.R;
import com.projets.heven.taxijaune.controller.AppController;
import com.projets.heven.taxijaune.directionhelpers.FetchURL;
import com.projets.heven.taxijaune.model.M;
import com.projets.heven.taxijaune.model.TaxiPojo;
import com.projets.heven.taxijaune.settings.AppConst;
import com.projets.heven.taxijaune.settings.ConnectionDetector;
import com.projets.heven.taxijaune.settings.Progressdialog;
import com.simplymadeapps.quickperiodicjobscheduler.PeriodicJob;
import com.simplymadeapps.quickperiodicjobscheduler.QuickJobFinishedCallback;
import com.simplymadeapps.quickperiodicjobscheduler.QuickPeriodicJob;
import com.simplymadeapps.quickperiodicjobscheduler.QuickPeriodicJobCollection;
import com.simplymadeapps.quickperiodicjobscheduler.QuickPeriodicJobScheduler;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.Constants;
import com.sucho.placepicker.MapType;
import com.sucho.placepicker.PlacePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;

public class FragmentAccueil extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,
        GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMyLocationButtonClickListener,
        RoutingListener{

    ViewPager pager;
    TabLayout tabs;
    View view;
    private static Context context;
    public static Activity activity;
    ConnectionDetector connectionDetector;
    String TAG = "FragmentAccueil";
    ArrayList<String> tabNames = new ArrayList<String>();
    int currpos = 0;
    QuickPeriodicJobScheduler jobScheduler;
    QuickPeriodicJob job = null;

    /** MAP **/
    public static GoogleMap mMap;
    public static Location currentLocation
            ,destinationLocation = new Location("dummyprovider1")
            ,departLocationReservation = new Location("dummyprovider2")
            ,destinationLocationReservation = new Location("dummyprovider3")
            ,departLocationMesRequetes = new Location("dummyprovider2")
            ,destinationLocationMesRequetes = new Location("dummyprovider3");
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

//    private FloatingActionButton fab;
    public static Marker currentMarker = null, destinationMarker = null
        , departMarkerReservation = null, destinationMarkerReservation = null
        , departMarkerMesRequetes = null, destinationMarkerMesRequetes = null;

    ArrayList<Marker> listMarker = new ArrayList<Marker>();
//    private TextView commander,reserver;
    int PLACE_PICKER_REQUEST_RESERVATION_DEPART = 101;
    public static ArrayList<Location> tabLocation = new ArrayList<Location>();

    private Boolean verif = false;
    public static Polyline currentPolyline;
    PlacesClient placesClient;
    private ImageView my_location,choose_my_location;
    EditText input_text_depart;
    EditText input_text_arrivee;

    private static Progressdialog progressdialog;
    private static File file;
    private static RelativeLayout layout_main;
    public static BottomSheetFragmentRequeteFacturation bottomSheetFragmentRequeteFacturation;
    public static BottomSheetFragmentMesRequetesAccueil bottomSheetFragmentMesRequetesAccueil;
    private Bitmap bitmap;
    ImageView btn_my_request;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null)
            currpos = getArguments().getInt("tab_pos", 0);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_accueil, container, false);

        context = getActivity();
        activity = (Activity)view.getContext();
        connectionDetector = new ConnectionDetector(context);
        progressdialog = new Progressdialog(context);
        progressdialog.init();

//        fab = (FloatingActionButton)view.findViewById(R.id.fab);
//        commander = (TextView)view.findViewById(R.id.commander);
//        reserver = (TextView) view.findViewById(R.id.reserver);
        my_location = (ImageView) view.findViewById(R.id.my_location);
        choose_my_location = (ImageView) view.findViewById(R.id.choose_my_location);
        layout_main = (RelativeLayout) view.findViewById(R.id.layout_main);
        btn_my_request =(ImageView) view.findViewById(R.id.btn_my_request);

        btn_my_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetFragmentMesRequetesAccueil = new BottomSheetFragmentMesRequetesAccueil(getActivity());
                bottomSheetFragmentMesRequetesAccueil.show(((FragmentActivity) context).getSupportFragmentManager(), bottomSheetFragmentMesRequetesAccueil.getTag());
            }
        });

        destinationLocation.setLatitude(12.36858);
        destinationLocation.setLongitude(-1.52709);
        departLocationReservation.setLatitude(12.36858);
        departLocationReservation.setLongitude(-1.52709);
        destinationLocationReservation.setLatitude(12.36858);
        destinationLocationReservation.setLongitude(-1.52709);

        //Place Autocomplete
        String apikey = getResources().getString(R.string.google_maps_key);
        if(!Places.isInitialized()){
            Places.initialize(getActivity().getApplicationContext(),apikey);
        }

        placesClient = Places.createClient(context);

        // Auto complete départ
        final AutocompleteSupportFragment autocompleteSupportFragment_depart =
                ((AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_depart));

        autocompleteSupportFragment_depart.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        ImageView searchIcon_depart = (ImageView)((LinearLayout)autocompleteSupportFragment_depart.getView()).getChildAt(0);
        // Set the desired icon
        searchIcon_depart.setImageDrawable(getResources().getDrawable(R.drawable.ic_pin));
        input_text_depart = ((EditText)autocompleteSupportFragment_depart.getView().findViewById(R.id.places_autocomplete_search_input));
        input_text_depart.setTextSize(16.0f);
        input_text_depart.setHint(context.getResources().getString(R.string.depart));
//        autocompleteSupportFragment_depart.setCountry("BF");

        autocompleteSupportFragment_depart.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                if(departMarkerMesRequetes != null && destinationMarkerMesRequetes != null) {
                    departMarkerMesRequetes.remove();
                    destinationMarkerMesRequetes.remove();
                    currentPolyline.remove();
                }

                final LatLng latLng = place.getLatLng();
                String name = place.getName();
//                Toast.makeText(context, ""+latLng.latitude+" "+name, Toast.LENGTH_SHORT).show();
                if(place.getName().trim().length() != 0)
                    input_text_depart.setText(place.getName());

                if(destinationMarker != null)
                    destinationMarker.remove();
                if((departLocationReservation != null && destinationLocationReservation != null) && tabLocation.size() > 1) {
//                    departMarkerReservation.remove();
//                    destinationMarkerReservation.remove();
                    tabLocation.clear();
                    departMarkerReservation.remove();
                    destinationMarkerReservation.remove();
                    currentPolyline.remove();
                }
                if(departLocationReservation != null && destinationLocationReservation != null){
                    departLocationReservation.setLatitude(latLng.latitude);
                    departLocationReservation.setLongitude(latLng.longitude);
                    tabLocation.add(departLocationReservation);
                    if(departMarkerReservation != null)
                        departMarkerReservation.remove();
                    addMarkerDepart(new LatLng(latLng.latitude,latLng.longitude));

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLng)             // Sets the center of the map to location user
                            .zoom(15)                   // Sets the zoom
//                    .bearing(90)                // Sets the orientation of the camera to east
//                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                            .build();                   // Creates a CameraPosition from the builder
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    if(departMarkerReservation != null && destinationMarkerReservation != null && tabLocation.size() > 1) {
                        showProgressDialog();
                        M.setCurrentFragment("accueil",context);
                        new FetchURL(getActivity(),"accueil").execute(getUrl(departMarkerReservation.getPosition(), destinationMarkerReservation.getPosition(), "driving"), "driving");
//                        BottomSheetFragmentRequeteFacturation bottomSheetFragmentRequeteFacturation = new BottomSheetFragmentRequeteFacturation(getActivity(), departLocationReservation, destinationLocationReservation);
//                        bottomSheetFragmentRequeteFacturation.show(((FragmentActivity) context).getSupportFragmentManager(), bottomSheetFragmentRequeteFacturation.getTag());
                    }
                }
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });


        // Auto complete arrivée
        final AutocompleteSupportFragment autocompleteSupportFragment_arrivee =
                ((AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_arrivee));

        autocompleteSupportFragment_arrivee.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        ImageView searchIcon_arrivee = (ImageView)((LinearLayout)autocompleteSupportFragment_arrivee.getView()).getChildAt(0);
        // Set the desired icon
        searchIcon_arrivee.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrival_point));
//        searchIcon.getLayoutParams().height = 30;
//        searchIcon.getLayoutParams().width = 30;
//        searchIcon.setPadding(10,10,10,10);
        input_text_arrivee = ((EditText)autocompleteSupportFragment_arrivee.getView().findViewById(R.id.places_autocomplete_search_input));
        input_text_arrivee.setTextSize(16.0f);
        input_text_arrivee.setHint(context.getResources().getString(R.string.ou_allez_vous));
//        autocompleteSupportFragment_arrivee.setCountry("BF");

        autocompleteSupportFragment_arrivee.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                if(departMarkerMesRequetes != null && destinationMarkerMesRequetes != null) {
                    departMarkerMesRequetes.remove();
                    destinationMarkerMesRequetes.remove();
                    currentPolyline.remove();
                }

                final LatLng latLng = place.getLatLng();
                String name = place.getName();
//                Toast.makeText(context, ""+latLng.latitude+" "+name, Toast.LENGTH_SHORT).show();
                if(place.getName().trim().length() != 0)
                    input_text_arrivee.setText(place.getName());

                if(destinationMarker != null)
                    destinationMarker.remove();

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)             // Sets the center of the map to location user
                        .zoom(15)                   // Sets the zoom
//                    .bearing(90)                // Sets the orientation of the camera to east
//                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                if((departLocationReservation != null && destinationLocationReservation != null) && tabLocation.size() > 1) {
//                    departMarkerReservation.remove();
//                    destinationMarkerReservation.remove();
                    tabLocation.clear();
                    departMarkerReservation.remove();
                    destinationMarkerReservation.remove();
                    currentPolyline.remove();
                }
                if(departLocationReservation != null && destinationLocationReservation != null){
                    destinationLocationReservation.setLatitude(latLng.latitude);
                    destinationLocationReservation.setLongitude(latLng.longitude);
                    tabLocation.add(destinationLocationReservation);
                    if(destinationMarkerReservation != null)
                        destinationMarkerReservation.remove();
                    addMarkerDestination(new LatLng(latLng.latitude,latLng.longitude));

//                    Toast.makeText(context, ""+tabLocation.size(), Toast.LENGTH_SHORT).show();
                    if(departMarkerReservation != null && destinationMarkerReservation != null && tabLocation.size() > 1) {
                        showProgressDialog();
                        M.setCurrentFragment("accueil",context);
                        new FetchURL(getActivity(),"accueil").execute(getUrl(departMarkerReservation.getPosition(), destinationMarkerReservation.getPosition(), "driving"), "driving");
                    }
                }
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });

        my_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isLocationEnabled(context))
                    showMessageEnabledGPS();
                else{
                    if(currentLocation != null){
                        if(departMarkerMesRequetes != null && destinationMarkerMesRequetes != null) {
                            departMarkerMesRequetes.remove();
                            destinationMarkerMesRequetes.remove();
                            currentPolyline.remove();
                        }

                        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(latLng)             // Sets the center of the map to location user
                                .zoom(15)                   // Sets the zoom
//                    .bearing(90)                // Sets the orientation of the camera to east
//                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                                .build();                   // Creates a CameraPosition from the builder
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        input_text_depart.setText("My position");

                        if(destinationMarker != null)
                            destinationMarker.remove();
                        if((departLocationReservation != null && destinationLocationReservation != null) && tabLocation.size() > 1) {
//                            departMarkerReservation.remove();
//                            destinationMarkerReservation.remove();
                            tabLocation.clear();
                            departMarkerReservation.remove();
                            destinationMarkerReservation.remove();
                            currentPolyline.remove();
                        }
                        if(departLocationReservation != null && destinationLocationReservation != null){
                            departLocationReservation.setLatitude(latLng.latitude);
                            departLocationReservation.setLongitude(latLng.longitude);
                            tabLocation.add(departLocationReservation);
                            if(departMarkerReservation != null)
                                departMarkerReservation.remove();
                            addMarkerDepart(new LatLng(latLng.latitude,latLng.longitude));

                            if(departMarkerReservation != null && destinationMarkerReservation != null && tabLocation.size() > 1) {
                                showProgressDialog();
                                M.setCurrentFragment("accueil",context);
                                new FetchURL(getActivity(),"accueil").execute(getUrl(departMarkerReservation.getPosition(), destinationMarkerReservation.getPosition(), "driving"), "driving");
//                                BottomSheetFragmentRequeteFacturation bottomSheetFragmentRequeteFacturation = new BottomSheetFragmentRequeteFacturation(getActivity(), departLocationReservation, destinationLocationReservation);
//                                bottomSheetFragmentRequeteFacturation.show(((FragmentActivity) context).getSupportFragmentManager(), bottomSheetFragmentRequeteFacturation.getTag());
                            }
                        }
                    }
                }
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map));
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
//            return;
        }
//        fetchLastLocation();

        // Get the location manager
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (provider != null) {
            currentLocation = locationManager.getLastKnownLocation(provider);
//            destinationLocation = locationManager.getLastKnownLocation(provider);
//            departLocationReservation = locationManager.getLastKnownLocation(provider);
//            destinationLocationReservation = locationManager.getLastKnownLocation(provider);
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

        choose_my_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (departLocationReservation != null) {
                    Intent intent = new PlacePicker.IntentBuilder()
                            .setLatLong(departLocationReservation.getLatitude(), departLocationReservation.getLongitude())  // Initial Latitude and Longitude the Map will load into
                            .showLatLong(true)  // Show Coordinates in the Activity
                            .setMapZoom(15.0f)  // Map Zoom Level. Default: 14.0
                            .setAddressRequired(true) // Set If return only Coordinates if cannot fetch Address for the coordinates. Default: True
                            .hideMarkerShadow(true) // Hides the shadow under the map markerDepart. Default: False
                            .setMarkerDrawable(R.drawable.ic_pin) // Change the default Marker Image
                            .setMarkerImageImageColor(R.color.grisGooglePlay)
                            .setFabColor(R.color.colorYelloDark)
                            .setPrimaryTextColor(R.color.colorLogoBlack) // Change text color of Shortened Address
                            .setSecondaryTextColor(R.color.colorLogoBlack) // Change text color of full Address
//                        .setMapRawResourceStyle(R.raw.map_style)  //Set Map Style
                            .setMapType(MapType.NORMAL)
                            .disableBootomSheetAnimation(true)
                            .onlyCoordinates(true)  //Get only Coordinates from Place Picker
                            .build(getActivity());
                    startActivityForResult(intent, PLACE_PICKER_REQUEST_RESERVATION_DEPART);
                } else {
                    Intent intent = new PlacePicker.IntentBuilder()
                            .setLatLong(12.36858, -1.52709)  // Initial Latitude and Longitude the Map will load into
                            .showLatLong(true)  // Show Coordinates in the Activity
                            .setMapZoom(15.0f)  // Map Zoom Level. Default: 14.0
                            .setAddressRequired(true) // Set If return only Coordinates if cannot fetch Address for the coordinates. Default: True
                            .hideMarkerShadow(true) // Hides the shadow under the map markerDepart. Default: False
                            .setMarkerDrawable(R.drawable.ic_pin) // Change the default Marker Image
                            .setMarkerImageImageColor(R.color.grisGooglePlay)
                            .setFabColor(R.color.colorYelloDark)
                            .setPrimaryTextColor(R.color.colorLogoBlack) // Change text color of Shortened Address
                            .setSecondaryTextColor(R.color.colorLogoBlack) // Change text color of full Address
//                        .setMapRawResourceStyle(R.raw.map_style)  //Set Map Style
                            .setMapType(MapType.NORMAL)
                            .disableBootomSheetAnimation(true)
                            .onlyCoordinates(true)  //Get only Coordinates from Place Picker
                            .build(getActivity());
                    startActivityForResult(intent, PLACE_PICKER_REQUEST_RESERVATION_DEPART);
                }
            }
        });

        /*commander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentLocation != null && destinationLocation == null) {
                    Intent intent = new PlacePicker.IntentBuilder()
                            .setLatLong(currentLocation.getLatitude(), currentLocation.getLongitude())  // Initial Latitude and Longitude the Map will load into
                            .showLatLong(true)  // Show Coordinates in the Activity
                            .setMapZoom(15.0f)  // Map Zoom Level. Default: 14.0
                            .setAddressRequired(true) // Set If return only Coordinates if cannot fetch Address for the coordinates. Default: True
                            .hideMarkerShadow(true) // Hides the shadow under the map markerDepart. Default: False
                            .setMarkerDrawable(R.drawable.ic_map_marker) // Change the default Marker Image
                            .setMarkerImageImageColor(R.color.colorYelloDark)
                            .setFabColor(R.color.colorYelloDark)
                            .setPrimaryTextColor(R.color.colorLogoBlack) // Change text color of Shortened Address
                            .setSecondaryTextColor(R.color.colorLogoBlack) // Change text color of full Address
//                        .setMapRawResourceStyle(R.raw.map_style)  //Set Map Style
                            .setMapType(MapType.NORMAL)
                            .disableBootomSheetAnimation(true)
                            .onlyCoordinates(true)  //Get only Coordinates from Place Picker
                            .build(getActivity());
                    startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST);
                }else if(destinationLocation != null) {
                    Intent intent = new PlacePicker.IntentBuilder()
                            .setLatLong(destinationLocation.getLatitude(), destinationLocation.getLongitude())  // Initial Latitude and Longitude the Map will load into
                            .showLatLong(true)  // Show Coordinates in the Activity
                            .setMapZoom(15.0f)  // Map Zoom Level. Default: 14.0
                            .setAddressRequired(true) // Set If return only Coordinates if cannot fetch Address for the coordinates. Default: True
                            .hideMarkerShadow(true) // Hides the shadow under the map markerDepart. Default: False
                            .setMarkerDrawable(R.drawable.ic_map_marker) // Change the default Marker Image
                            .setMarkerImageImageColor(R.color.colorYelloDark)
                            .setFabColor(R.color.colorYelloDark)
                            .setPrimaryTextColor(R.color.colorLogoBlack) // Change text color of Shortened Address
                            .setSecondaryTextColor(R.color.colorLogoBlack) // Change text color of full Address
//                        .setMapRawResourceStyle(R.raw.map_style)  //Set Map Style
                            .setMapType(MapType.NORMAL)
                            .disableBootomSheetAnimation(true)
                            .onlyCoordinates(true)  //Get only Coordinates from Place Picker
                            .build(getActivity());
                    startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST);
                }else {
                    Intent intent = new PlacePicker.IntentBuilder()
                            .setLatLong(12.36858, -1.52709)  // Initial Latitude and Longitude the Map will load into
                            .showLatLong(true)  // Show Coordinates in the Activity
                            .setMapZoom(15.0f)  // Map Zoom Level. Default: 14.0
                            .setAddressRequired(true) // Set If return only Coordinates if cannot fetch Address for the coordinates. Default: True
                            .hideMarkerShadow(true) // Hides the shadow under the map markerDepart. Default: False
                            .setMarkerDrawable(R.drawable.ic_map_marker) // Change the default Marker Image
                            .setMarkerImageImageColor(R.color.colorYelloDark)
                            .setFabColor(R.color.colorYelloDark)
                            .setPrimaryTextColor(R.color.colorLogoBlack) // Change text color of Shortened Address
                            .setSecondaryTextColor(R.color.colorLogoBlack) // Change text color of full Address
//                        .setMapRawResourceStyle(R.raw.map_style)  //Set Map Style
                            .setMapType(MapType.NORMAL)
                            .disableBootomSheetAnimation(true)
                            .onlyCoordinates(true)  //Get only Coordinates from Place Picker
                            .build(getActivity());
                    startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST);
                }

//                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//                try {
//                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
//                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
//                    e.printStackTrace();
//                }
            }
        });*/

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

        mMap.setMyLocationEnabled(true);
        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.setMargins(0, 20, 20, 550);
            View zoomButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("1"));
            RelativeLayout.LayoutParams layoutParamsZoom = (RelativeLayout.LayoutParams) zoomButton.getLayoutParams();
            layoutParamsZoom.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParamsZoom.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParamsZoom.setMargins(0, 20, 20, 200);
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
                    .zoom(15)                   // Sets the zoom
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

//        LatLng start = new LatLng(18.015365, -77.499382);
//        LatLng waypoint= new LatLng(18.01455, -77.499333);
//        LatLng end = new LatLng(18.012590, -77.500659);
//
//        Routing routing = new Routing.Builder()
//                .travelMode(Routing.TravelMode.WALKING)
//                .withListener(this)
//                .waypoints(start, waypoint, end)
//                .build();
//        routing.execute();

        new getTaxi().execute();
        initJobs();
    }

    public static void showDirection(String latitude_client, String longitude_client, String latitude_destination, String longitude_destination){
        if(departMarkerMesRequetes != null && destinationMarkerMesRequetes != null) {
            departMarkerMesRequetes.remove();
            destinationMarkerMesRequetes.remove();
        }
        addMarkerDepartMesRequetes(new LatLng(Double.parseDouble(latitude_client),Double.parseDouble(longitude_client)));
        addMarkerDestinationMesRequetes(new LatLng(Double.parseDouble(latitude_destination),Double.parseDouble(longitude_destination)));

        if(destinationMarker != null)
            destinationMarker.remove();
        if((departLocationMesRequetes != null && destinationLocationMesRequetes != null) && tabLocation.size() > 1) {
            tabLocation.clear();
        }
        if(departLocationMesRequetes != null && destinationLocationMesRequetes != null){
            departLocationMesRequetes.setLatitude(Double.parseDouble(latitude_client));
            departLocationMesRequetes.setLongitude(Double.parseDouble(longitude_client));
            destinationLocationMesRequetes.setLatitude(Double.parseDouble(latitude_destination));
            destinationLocationMesRequetes.setLongitude(Double.parseDouble(longitude_destination));
            tabLocation.add(departLocationMesRequetes);
            tabLocation.add(destinationLocationMesRequetes);
        }

        if(departMarkerMesRequetes != null && destinationMarkerMesRequetes != null) {
            showProgressDialog();
            M.setCurrentFragment("mes_requetes_accueil",context);
            new FetchURL(context,"mes_requetes_accueil").execute(getUrl(departMarkerMesRequetes.getPosition(), destinationMarkerMesRequetes.getPosition(), "driving"), "driving");
        }
    }

    public static void dismissBottomSheet() {
        dismissProgressDialog();
        bottomSheetFragmentMesRequetesAccueil.dismiss();
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
//                    snapShot();
                    dismissProgressDialog();
//                    Log.i("distance_between", ((JSONObject) jLegs.get(j)).getJSONObject("distance").getString("text"));
//                    Log.i("duration_between", ((JSONObject) jLegs.get(j)).getJSONObject("duration").getString("text"));
                    String distance = ((JSONObject) jLegs.get(j)).getJSONObject("distance").getString("text");
                    String duration = ((JSONObject) jLegs.get(j)).getJSONObject("duration").getString("text");
                    bottomSheetFragmentRequeteFacturation = new BottomSheetFragmentRequeteFacturation(activity, departLocationReservation, destinationLocationReservation, distance, duration);
                    bottomSheetFragmentRequeteFacturation.show(((FragmentActivity) activity).getSupportFragmentManager(), bottomSheetFragmentRequeteFacturation.getTag());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
    }

    public static void dismissCommandeFacturationSheet(){
        bottomSheetFragmentRequeteFacturation.dismiss();
    }

    public static void showReservationFacturationSheet(String distance, String duration){
        BottomSheetFragmentRequeteFacturationReservation bottomSheetFragmentRequeteFacturationReservation = new BottomSheetFragmentRequeteFacturationReservation(activity, departLocationReservation, destinationLocationReservation, distance, duration);
        bottomSheetFragmentRequeteFacturationReservation.show(((FragmentActivity) activity).getSupportFragmentManager(), bottomSheetFragmentRequeteFacturationReservation.getTag());
    }

    private static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas (returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        }   else{
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    private static void showProgressDialog(){
        progressdialog.show();
    }

    public static void dismissProgressDialog(){
        progressdialog.dismiss();
    }

    private static String getUrl(LatLng origin, LatLng dest, String directionMode) {
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
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + context.getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        /*if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                if((departLocationReservation != null && destinationLocationReservation != null) && tabLocation.size() > 1) {
                    departMarkerReservation.remove();
                    destinationMarkerReservation.remove();
                    tabLocation.clear();
//                    Toast.makeText(context, "clear", Toast.LENGTH_SHORT).show();
                }
                AddressData addressData = data.getParcelableExtra(Constants.ADDRESS_INTENT);
                if(destinationMarker != null)
                    destinationMarker.remove();
                if(currentLocation != null && destinationLocation != null){
//                    Toast.makeText(context, "Yes", Toast.LENGTH_SHORT).show();
                    destinationLocation.setLatitude(addressData.getLatitude());
                    destinationLocation.setLongitude(addressData.getLongitude());
                    addMarker(new LatLng(addressData.getLatitude(),addressData.getLongitude()));
                    BottomSheetFragmentRequeteFacturation bottomSheetFragmentRequeteFacturation = new BottomSheetFragmentRequeteFacturation(getActivity(),currentLocation,destinationLocation);
                    bottomSheetFragmentRequeteFacturation.show(((FragmentActivity) context).getSupportFragmentManager(), bottomSheetFragmentRequeteFacturation.getTag());
                }
            }
        }*/

        if(requestCode == PLACE_PICKER_REQUEST_RESERVATION_DEPART) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                if(departMarkerMesRequetes != null)
                    departMarkerMesRequetes.remove();
                if(destinationMarkerMesRequetes != null)
                    destinationMarkerMesRequetes.remove();

                AddressData addressData = data.getParcelableExtra(Constants.ADDRESS_INTENT);
                if(destinationMarker != null)
                    destinationMarker.remove();

                LatLng latLng = new LatLng(addressData.getLatitude(),addressData.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)             // Sets the center of the map to location user
                        .zoom(15)                   // Sets the zoom
//                    .bearing(90)                // Sets the orientation of the camera to east
//                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                if((departLocationReservation != null && destinationLocationReservation != null) && tabLocation.size() > 1) {
//                    departMarkerReservation.remove();
//                    destinationMarkerReservation.remove();
                    tabLocation.clear();
                    departMarkerReservation.remove();
                    destinationMarkerReservation.remove();
                    currentPolyline.remove();
                }
                if(departLocationReservation != null && destinationLocationReservation != null){
                    departLocationReservation.setLatitude(addressData.getLatitude());
                    departLocationReservation.setLongitude(addressData.getLongitude());
                    tabLocation.add(departLocationReservation);
                    if(departMarkerReservation != null)
                        departMarkerReservation.remove();
                    addMarkerDepart(new LatLng(addressData.getLatitude(),addressData.getLongitude()));

                    if(departMarkerReservation != null && destinationMarkerReservation != null && tabLocation.size() > 1) {
                        showProgressDialog();
                        M.setCurrentFragment("accueil",context);
                        new FetchURL(getActivity(),"accueil").execute(getUrl(departMarkerReservation.getPosition(), destinationMarkerReservation.getPosition(), "driving"), "driving");
//                        BottomSheetFragmentRequeteFacturation bottomSheetFragmentRequeteFacturation = new BottomSheetFragmentRequeteFacturation(getActivity(), departLocationReservation, destinationLocationReservation);
//                        bottomSheetFragmentRequeteFacturation.show(((FragmentActivity) context).getSupportFragmentManager(), bottomSheetFragmentRequeteFacturation.getTag());
                    }
                }
                input_text_depart.setText(context.getResources().getString(R.string.point_de_depart));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
//        MainActivity mainActivity = new MainActivity();
//        mainActivity.selectItem(0);
    }

    public void initJobs() {
        int jobId = 1;
        job = new QuickPeriodicJob(jobId, new PeriodicJob() {
            @Override
            public void execute(QuickJobFinishedCallback callback) {
                if(connectionDetector.isConnectingToInternet())
                    new getTaxi().execute();
                // When you have done all your work in the job, call jobFinished to release the resources
                callback.jobFinished();
            }
        });

        QuickPeriodicJobCollection.addJob(job);

        jobScheduler = new QuickPeriodicJobScheduler(context);
        jobScheduler.start(1, 1000l); // Run job with jobId=1 every 5 seconds
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        menu.clear();
//        inflater.inflate(R.menu.menu_accueil,menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId()==R.id.item_message){
////            Intent it=new Intent(context,SearchActivity.class);
////            startActivity(it);
//        }
//        return super.onOptionsItemSelected(item);
//    }

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
        if(verif == false) {
//            destinationLocation = location;
//            departLocationReservation = location;
//            destinationLocationReservation = location;

            // Initialize the location fields
            if (currentLocation != null) {
                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)             // Sets the center of the map to location user
                        .zoom(15)                   // Sets the zoom
//                    .bearing(90)                // Sets the orientation of the camera to east
//                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
            verif = true;
        }

        if (location != null) {
            if(currentMarker != null)
                currentMarker.remove();
            new setCurrentLocation().execute(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));
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
        if(job != null)
            jobScheduler.stop(job.getJobId());

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

    @Override
    public void onResume() {
        super.onResume();
        if(job != null)
            jobScheduler.start(job.getJobId(), 5000l); // Run job with jobId=1 every 5 seconds
//        else
//            jobScheduler.start(1, 5000l); // Run job with jobId=1 every 5 seconds
    }

    @Override
    public void onPause() {
        super.onPause();
        if(job != null)
            jobScheduler.stop(job.getJobId());
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

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {

    }

    @Override
    public void onRoutingCancelled() {

    }

    /** Récupération des taxi**/
    private class getTaxi extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"get_taxi.php";
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                for(int i=0; i<listMarker.size(); i++) {
                                    listMarker.get(i).remove();
                                }
                                if(etat.equals("1")){
//                                    Toast.makeText(context, "Ok", Toast.LENGTH_SHORT).show();
                                    for(int i=0; i<(msg.length()-1); i++) {
                                        JSONObject taxi = msg.getJSONObject(String.valueOf(i));
                                        TaxiPojo taxiPojo = new TaxiPojo(taxi.getInt("id"),taxi.getString("numero"),taxi.getString("immatriculation"),taxi.getString("statut"),taxi.getString("latitude"),taxi.getString("longitude"),taxi.getString("creer"),taxi.getString("modifier"),taxi.getString("libTypeVehicule"));
                                        addTaxiToMap(taxiPojo);
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
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(jsonObjReq);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    7000,
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

    private void addMarker(LatLng latLng){
        // Add Marker to Map
        MarkerOptions option = new MarkerOptions();
        option.title(context.getResources().getString(R.string.destination));
        option.snippet(context.getResources().getString(R.string.vous_voulez_vous_rendre_ici));
        option.position(latLng);
        option.icon(generateBitmapDescriptorFromRes(context, R.drawable.ic_location_pin_1));
        destinationMarker = mMap.addMarker(option);
        destinationMarker.setTag(context.getResources().getString(R.string.destination));
    }

    private static void addMarkerDepart(LatLng latLng){
        // Add Marker to Map
        MarkerOptions option = new MarkerOptions();
        option.title(context.getResources().getString(R.string.depart));
        option.snippet(context.getResources().getString(R.string.votre_point_de_depart));
        option.position(latLng);
        option.icon(generateBitmapDescriptorFromRes(context, R.drawable.ic_pin_2));
        departMarkerReservation = mMap.addMarker(option);
        departMarkerReservation.setTag(context.getResources().getString(R.string.depart));
    }

    private static void addMarkerDestination(LatLng latLng){
        // Add Marker to Map
        MarkerOptions option = new MarkerOptions();
        option.title(context.getResources().getString(R.string.destination));
        option.snippet(context.getResources().getString(R.string.vous_voulez_vous_rendre_ici));
        option.position(latLng);
        option.icon(generateBitmapDescriptorFromRes(context, R.drawable.ic_arrival_point_2));
        destinationMarkerReservation = mMap.addMarker(option);
        destinationMarkerReservation.setTag(context.getResources().getString(R.string.destination));
    }

    private static void addMarkerDepartMesRequetes(LatLng latLng){
        // Add Marker to Map
        MarkerOptions option = new MarkerOptions();
        option.title(context.getResources().getString(R.string.depart));
        option.snippet(context.getResources().getString(R.string.votre_point_de_depart));
        option.position(latLng);
        option.icon(generateBitmapDescriptorFromRes(context, R.drawable.ic_pin_2));
        departMarkerMesRequetes = mMap.addMarker(option);
        departMarkerMesRequetes.setTag(context.getResources().getString(R.string.depart));
    }

    private static void addMarkerDestinationMesRequetes(LatLng latLng){
        // Add Marker to Map
        MarkerOptions option = new MarkerOptions();
        option.title(context.getResources().getString(R.string.destination));
        option.snippet(context.getResources().getString(R.string.vous_voulez_vous_rendre_ici));
        option.position(latLng);
        option.icon(generateBitmapDescriptorFromRes(context, R.drawable.ic_arrival_point_2));
        destinationMarkerMesRequetes = mMap.addMarker(option);
        destinationMarkerMesRequetes.setTag(context.getResources().getString(R.string.destination));
    }

    private void addTaxiToMap(TaxiPojo taxiPojo){
        if(taxiPojo.getLatitude().trim().length() != 0) {
            // Add Marker to Map
            LatLng latLng = new LatLng(Double.parseDouble(taxiPojo.getLatitude()), Double.parseDouble(taxiPojo.getLongitude()));
            MarkerOptions option = new MarkerOptions();
            option.title(taxiPojo.getNumero());
            option.snippet(taxiPojo.getType_vehicule());
            option.position(latLng);
//            option.icon(generateBitmapDescriptorFromRes(context, R.drawable.ic_car_top_view));
            option.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car_marker));
            Marker marker = mMap.addMarker(option);
            listMarker.add(marker);
            marker.setTag(taxiPojo.getId());
        }
    }

//    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
//        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
//        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
//        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        vectorDrawable.draw(canvas);
//        return BitmapDescriptorFactory.fromBitmap(bitmap);
//    }

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

//    private void fetchLastLocation() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//        }
//        Task<Location> task = fusedLocationProviderClient.getLastLocation();
//        task.addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if (location != null) {
//                    currentLocation = location;
//                    SupportMapFragment supportMapFragment = ((SupportMapFragment) getChildFragmentManager()
//                            .findFragmentById(R.id.map));
//                    supportMapFragment.getMapAsync(getActivity());
//                } else {
////                    Toast.makeText(MapsActivity.this, "No Location recorded", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
}
