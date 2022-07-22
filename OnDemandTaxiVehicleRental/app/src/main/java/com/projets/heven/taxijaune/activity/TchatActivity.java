package com.projets.heven.taxijaune.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.tabs.TabLayout;
import com.projets.heven.taxijaune.R;
import com.projets.heven.taxijaune.adapter.MessageAdapter;
import com.projets.heven.taxijaune.controller.AppController;
import com.projets.heven.taxijaune.fragment.FragmentAccueil;
import com.projets.heven.taxijaune.model.M;
import com.projets.heven.taxijaune.model.MessagePojo;
import com.projets.heven.taxijaune.onclick.ClickListener;
import com.projets.heven.taxijaune.onclick.RecyclerTouchListener;
import com.projets.heven.taxijaune.settings.AppConst;
import com.projets.heven.taxijaune.settings.ConnectionDetector;
import com.projets.heven.taxijaune.settings.Progressdialog;
import com.simplymadeapps.quickperiodicjobscheduler.PeriodicJob;
import com.simplymadeapps.quickperiodicjobscheduler.QuickJobFinishedCallback;
import com.simplymadeapps.quickperiodicjobscheduler.QuickPeriodicJob;
import com.simplymadeapps.quickperiodicjobscheduler.QuickPeriodicJobCollection;
import com.simplymadeapps.quickperiodicjobscheduler.QuickPeriodicJobScheduler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TchatActivity extends AppCompatActivity {

    ViewPager pager;
    TabLayout tabs;
    View view;
    Context context;
    ConnectionDetector connectionDetector;
    String TAG="FragmentAccueil";
    ArrayList<String> tabNames = new ArrayList<String>();
    int currpos=0,id_envoyeur=0,id_receveur=0,id_requete=0;

    public static RecyclerView recycler_view_message;
    public static List<MessagePojo> albumList_message;
    public static MessageAdapter adapter_message;
    public static SwipeRefreshLayout swipe_refresh;
    private EditText field;
    private ImageView send;
    private Progressdialog progressdialog;
    QuickPeriodicJobScheduler jobScheduler;
    QuickPeriodicJob job = null;
    Boolean verif_scroll = true;
    String nom_receveur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tchat);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context=this;
        connectionDetector=new ConnectionDetector(context);
        progressdialog = new Progressdialog(context);
        progressdialog.init();

        Bundle objetbundle = this.getIntent().getExtras();
        id_envoyeur = Integer.parseInt(objetbundle.getString("id_envoyeur"));
        id_receveur = objetbundle.getInt("id_receveur");
        id_requete = objetbundle.getInt("id_requete");
        nom_receveur = objetbundle.getString("nom_receveur");
        toolbar.setTitle(nom_receveur);
        objetbundle.clear();

//        Toast.makeText(context, ""+objetbundle.getInt("id_receveur"), Toast.LENGTH_SHORT).show();

        albumList_message = new ArrayList<>();
        adapter_message = new MessageAdapter(context, albumList_message);

        field = (EditText) findViewById(R.id.field);
        send = (ImageView) findViewById(R.id.send);
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        recycler_view_message = (RecyclerView) findViewById(R.id.recycler_view_message);
        @SuppressLint("WrongConstant") LinearLayoutManager horizontalLayoutManagerGarde = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recycler_view_message.setLayoutManager(horizontalLayoutManagerGarde);
        recycler_view_message.setItemAnimator(new DefaultItemAnimator());
        recycler_view_message.setAdapter(adapter_message);

        recycler_view_message.addOnItemTouchListener(new RecyclerTouchListener(context, recycler_view_message, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                MessagePojo messagePojo = albumList_message.get(position);
//                Toast.makeText(TchatActivity.this, ""+id_requete, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                MessagePojo messagePojo = albumList_message.get(position);
            }
        }));
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getMessage().execute();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(field.getText().toString().trim().length() != 0){
                    progressdialog.show();
//                    Toast.makeText(context, ""+id_envoyeur+""+id_receveur+""+id_requete, Toast.LENGTH_SHORT).show();
                    new setMessage().execute(field.getText().toString());
                }else{
                    Toast.makeText(TchatActivity.this, getResources().getString(R.string.ecrivez_quelque_chose), Toast.LENGTH_SHORT).show();
                }
            }
        });

        swipe_refresh.setRefreshing(true);
        new getMessage().execute();
        initJobs();
    }

    public void initJobs() {
        int jobId = id_requete;
        job = new QuickPeriodicJob(jobId, new PeriodicJob() {
            @Override
            public void execute(QuickJobFinishedCallback callback) {
//                Toast.makeText(context, "ok"+id_requete, Toast.LENGTH_SHORT).show();
                if(connectionDetector.isConnectingToInternet())
                    new getMessage().execute();
                // When you have done all your work in the job, call jobFinished to release the resources
                callback.jobFinished();
            }
        });

        QuickPeriodicJobCollection.addJob(job);

        jobScheduler = new QuickPeriodicJobScheduler(context);
        jobScheduler.start(id_requete, 5000l); // Run job with jobId=1 every 5 seconds
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(job != null)
            jobScheduler.stop(job.getJobId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(job != null)
            jobScheduler.start(job.getJobId(), 5000l); // Run job with jobId=1 every 5 seconds
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(job != null)
            jobScheduler.stop(job.getJobId());
    }

    /*private void getMessage(){
        swipe_refresh.setRefreshing(false);
        MessagePojo messagePojo;
        messagePojo = new MessagePojo(1,1,"Ouedraogo Moussa","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019 à 09:30","oui",false);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Woumtana Youssouf","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019 à 09:30","oui",true);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Ouedraogo Moussa","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019 à 09:30","oui",false);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Woumtana Youssouf","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019 à 09:30","oui",true);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Ouedraogo Moussa","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019 à 09:30","oui",false);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Ouedraogo Moussa","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019 à 09:30","oui",false);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Woumtana Youssouf","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019 à 09:30","oui",true);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Ouedraogo Moussa","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019 à 09:30","oui",false);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Woumtana Youssouf","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019 à 09:30","oui",true);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Woumtana Youssouf","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019 à 09:30","oui",true);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Woumtana Youssouf","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019 à 09:30","oui",true);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Ouedraogo Moussa","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019 à 09:30","oui",false);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Woumtana Youssouf","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019 à 09:30","oui",true);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Ouedraogo Moussa","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019 à 09:30","oui",false);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Woumtana Youssouf","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019 à 09:30","oui",true);
        albumList_message.add(messagePojo);
        messagePojo = new MessagePojo(1,1,"Woumtana Youssouf","Un nouveau message d'un conducteur Un nouveau message d'un conducteur Un nouveau message d'un conducteur","24 Sep. 2019 à 09:30","oui",true);
        albumList_message.add(messagePojo);
        adapter_message.notifyDataSetChanged();
    }*/

    /** Envoyer un message**/
    private class setMessage extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"set_message.php";
            final String message = params[0];
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
                                if(etat.equals("1")){
                                    field.setText("");
                                    albumList_message.add(new MessagePojo(msg.getInt("id"),msg.getInt("id_user_app"),msg.getInt("id_conducteur")
                                            ,msg.getString("nom")+" "+msg.getString("nom"),msg.getString("nomConducteur")+" "+msg.getString("prenomConducteur")
                                            ,msg.getString("message"),msg.getString("creer"),msg.getString("user_cat")));
                                    adapter_message.notifyDataSetChanged();
                                    scrollToLastBottom();
                                    Toast.makeText(context, getResources().getString(R.string.votre_message_a_ete_envoye), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, getResources().getString(R.string.echec_denvoi), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressdialog.dismiss();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("message", message);
                    params.put("id_envoyeur", String.valueOf(id_envoyeur));
                    params.put("id_receveur", String.valueOf(id_receveur));
                    params.put("user_cat", String.valueOf(M.getUserCategorie(context)));
                    params.put("id_requete", String.valueOf(id_requete));
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

    /** Récuperer un message**/
    private class getMessage extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"get_message.php";
//            Toast.makeText(context, ""+id_requete, Toast.LENGTH_SHORT).show();
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                swipe_refresh.setRefreshing(false);
                                albumList_message.clear();
                                adapter_message.notifyDataSetChanged();
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    for(int i=0; i<(msg.length()-1); i++) {
                                        JSONObject message = msg.getJSONObject(String.valueOf(i));
                                        albumList_message.add(new MessagePojo(message.getInt("id"),message.getInt("id_user_app"),message.getInt("id_conducteur")
                                                ,message.getString("nom")+" "+message.getString("nom"),message.getString("nomConducteur")+" "+message.getString("prenomConducteur")
                                        ,message.getString("message"),message.getString("creer"),message.getString("user_cat")));
                                        adapter_message.notifyDataSetChanged();
                                    }
                                    if(verif_scroll == true) {
                                        scrollToLastBottom();
                                        verif_scroll = false;
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
//                    params.put("id_user", String.valueOf(id_envoyeur));
//                    params.put("user_cat", String.valueOf(M.getUserCategorie(context)));
                    params.put("id_requete", String.valueOf(id_requete));
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

    public void scrollToLastBottom(){
        recycler_view_message.scrollToPosition(albumList_message.size()-1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //do whatever
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
