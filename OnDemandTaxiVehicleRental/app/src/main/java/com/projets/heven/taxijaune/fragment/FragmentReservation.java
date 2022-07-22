package com.projets.heven.taxijaune.fragment;

/**
 * Created by Woumtana Pingdiwindé Youssouf 03/2019
 * Tel: +226 63 86 22 46 - 73 35 41 41
 * Email: issoufwoumtana@gmail.com
 **/

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.tabs.TabLayout;
import com.projets.heven.taxijaune.R;
import com.projets.heven.taxijaune.adapter.RequeteAdapter;
import com.projets.heven.taxijaune.adapter.ReservationAdapter;
import com.projets.heven.taxijaune.controller.AppController;
import com.projets.heven.taxijaune.model.M;
import com.projets.heven.taxijaune.model.RequetePojo;
import com.projets.heven.taxijaune.model.ReservationPojo;
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

public class FragmentReservation extends Fragment {

    ViewPager pager;
    TabLayout tabs;
    View view;
    Context context;
    ConnectionDetector connectionDetector;
    String TAG="FragmentAccueil";
    ArrayList<String> tabNames = new ArrayList<String>();
    int currpos=0;

    public static RecyclerView recycler_view_reservation;
    public static List<ReservationPojo> albumList_reservation;
    public static ReservationAdapter adapter_reservation;

    public static Progressdialog progressdialog;
    public static SwipeRefreshLayout swipe_refresh;

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
        view= inflater.inflate(R.layout.fragment_reservation, container, false);

        context=getActivity();
        connectionDetector=new ConnectionDetector(context);
        progressdialog = new Progressdialog(context);
        progressdialog.init();

        albumList_reservation = new ArrayList<>();
        adapter_reservation = new ReservationAdapter(context, albumList_reservation, getActivity());

        progressBar_failed = (ProgressBar) view.findViewById(R.id.progressBar_failed);
        layout_liste = (RelativeLayout) view.findViewById(R.id.layout_liste);
        layout_not_found = (LinearLayout) view.findViewById(R.id.layout_not_found);
        layout_failed = (LinearLayout) view.findViewById(R.id.layout_failed);
        swipe_refresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        recycler_view_reservation = (RecyclerView) view.findViewById(R.id.recycler_view_mes_requetes);
        @SuppressLint("WrongConstant") LinearLayoutManager horizontalLayoutManagerGarde = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recycler_view_reservation.setLayoutManager(horizontalLayoutManagerGarde);
        recycler_view_reservation.setItemAnimator(new DefaultItemAnimator());
        recycler_view_reservation.setAdapter(adapter_reservation);

        recycler_view_reservation.addOnItemTouchListener(new RecyclerTouchListener(context, recycler_view_reservation, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ReservationPojo reservationPojo = albumList_reservation.get(position);
            }

            @Override
            public void onLongClick(View view, int position) {
                ReservationPojo reservationPojo = albumList_reservation.get(position);
            }
        }));

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getMesReservation().execute();
            }
        });

        layout_failed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar_failed.setVisibility(View.VISIBLE);
                new getMesReservation().execute();
            }
        });

        swipe_refresh.setRefreshing(true);
        new getMesReservation().execute();
        return view;
    }

    /*private void getRequete(){
        ReservationPojo reservationPojo;
        reservationPojo = new ReservationPojo(1,1,"5 km","25 Sep. à 10h. 00min.","oui");
        albumList_reservation.add(reservationPojo);
        albumList_reservation.add(reservationPojo);
        albumList_reservation.add(reservationPojo);
        albumList_reservation.add(reservationPojo);
        albumList_reservation.add(reservationPojo);
        albumList_reservation.add(reservationPojo);
        albumList_reservation.add(reservationPojo);
        albumList_reservation.add(reservationPojo);
        albumList_reservation.add(reservationPojo);
        albumList_reservation.add(reservationPojo);
        albumList_reservation.add(reservationPojo);
        adapter_reservation.notifyDataSetChanged();
    }*/

    /** Récupération des réservations d'un client**/
    private class getMesReservation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"get_reservation_user_app.php";
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                swipe_refresh.setRefreshing(false);
                                albumList_reservation.clear();
                                adapter_reservation.notifyDataSetChanged();
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
                                        albumList_reservation.add(new ReservationPojo(taxi.getInt("id"),taxi.getInt("id_user_app"),taxi.getString("distance"),taxi.getString("date_depart")
                                                ,taxi.getString("heure_depart"),taxi.getString("statut"),taxi.getString("cout")));
                                        adapter_reservation.notifyDataSetChanged();
                                    }

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

    public static void showDialog(){
        progressdialog.show();
    }

    public static void dismissDialog(){
        progressdialog.dismiss();
    }

}
