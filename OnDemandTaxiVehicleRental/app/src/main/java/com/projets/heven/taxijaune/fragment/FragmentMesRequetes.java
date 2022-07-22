package com.projets.heven.taxijaune.fragment;

/**
 * Created by Woumtana Pingdiwindé Youssouf 03/2019
 * Tel: +226 63 86 22 46 - 73 35 41 41
 * Email: issoufwoumtana@gmail.com
 **/

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.android.material.tabs.TabLayout;
import com.projets.heven.taxijaune.R;
import com.projets.heven.taxijaune.activity.TchatActivity;
import com.projets.heven.taxijaune.adapter.RequeteAdapter;
import com.projets.heven.taxijaune.controller.AppController;
import com.projets.heven.taxijaune.listener.SwipeToDeleteCallback;
import com.projets.heven.taxijaune.model.M;
import com.projets.heven.taxijaune.model.RequetePojo;
import com.projets.heven.taxijaune.settings.AppConst;
import com.projets.heven.taxijaune.settings.ConnectionDetector;
import com.projets.heven.taxijaune.settings.Progressdialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentMesRequetes extends Fragment {

    ViewPager pager;
    TabLayout tabs;
    View view;
    public static Context context;
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
        view= inflater.inflate(R.layout.fragment_mes_requetes, container, false);

        context=getActivity();
        connectionDetector=new ConnectionDetector(context);
        progressdialog = new Progressdialog(context);
        progressdialog.init();

        albumList_mes_requetes = new ArrayList<>();
        adapter_mes_requetes = new RequeteAdapter(context, albumList_mes_requetes, getActivity(), "MesRequete");

        progressBar_failed = (ProgressBar) view.findViewById(R.id.progressBar_failed);
        layout_liste = (RelativeLayout) view.findViewById(R.id.layout_liste);
        layout_not_found = (LinearLayout) view.findViewById(R.id.layout_not_found);
        layout_failed = (LinearLayout) view.findViewById(R.id.layout_failed);
        swipe_refresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        recycler_view_mes_requetes = (RecyclerView) view.findViewById(R.id.recycler_view_mes_requetes);
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
                new getMesRequete().execute();
            }
        });

        layout_failed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar_failed.setVisibility(View.VISIBLE);
                new getMesRequete().execute();
            }
        });

        swipe_refresh.setRefreshing(true);
        new getMesRequete().execute();

        return view;
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

    /** Récupération des requêtes d'un utilisateur**/
    public static class getMesRequete extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"get_requete_user_app.php";
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
