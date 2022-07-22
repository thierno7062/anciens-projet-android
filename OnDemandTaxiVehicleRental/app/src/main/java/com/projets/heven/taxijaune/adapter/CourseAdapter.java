package com.projets.heven.taxijaune.adapter;

/**
 * Created by Woumtana on 01/12/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.projets.heven.taxijaune.R;
import com.projets.heven.taxijaune.activity.TchatActivity;
import com.projets.heven.taxijaune.controller.AppController;
import com.projets.heven.taxijaune.fragment.BottomSheetFragmentCourse;
import com.projets.heven.taxijaune.fragment.FragmentMesCourses;
import com.projets.heven.taxijaune.model.CoursePojo;
import com.projets.heven.taxijaune.model.M;
import com.projets.heven.taxijaune.model.RequetePojo;
import com.projets.heven.taxijaune.settings.AppConst;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyViewHolder> {

    private Context mContext;
    private List<CoursePojo> albumList;
    Activity activity;
    private String distance = "";
    final private String[][] tab = {{}};
    final private String[][] tab1 = { {} };

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        private TextView customer_name_course,moreOptions,date_course,distance_course,montant_course,
                distance_destination,statut_course,compteur_course;
        private LinearLayout layout_distance_requete;
        private RelativeLayout relative_layout;


        public MyViewHolder(View view) {
            super(view);
            customer_name_course = (TextView) view.findViewById(R.id.customer_name_course);
            date_course = (TextView) view.findViewById(R.id.date_course);
            distance_course = (TextView) view.findViewById(R.id.distance_course);
            moreOptions = (TextView) view.findViewById(R.id.moreOptions);
            montant_course = (TextView) view.findViewById(R.id.montant_course);
            distance_destination = (TextView) view.findViewById(R.id.distance_destination);
            statut_course = (TextView) view.findViewById(R.id.statut_course);
            compteur_course = (TextView) view.findViewById(R.id.compteur_course);
            layout_distance_requete = (LinearLayout) view.findViewById(R.id.layout_distance_course);
            relative_layout = (RelativeLayout) view.findViewById(R.id.relative_layout);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            CoursePojo coursePojo = albumList.get(getAdapterPosition());
//            Intent intent = new Intent(mContext, TchatActivity.class);
//            intent.putExtra("id_receveur",coursePojo.getUser_id());
//            intent.putExtra("id_envoyeur",M.getID(mContext));
//            intent.putExtra("id_requete",coursePojo.getId());
//            intent.putExtra("nom_receveur",coursePojo.getUser_name());
//            mContext.startActivity(intent);
            BottomSheetFragmentCourse bottomSheetFragmentCourse = new BottomSheetFragmentCourse(activity,coursePojo.getStatut(),coursePojo.getDistance(),
                    coursePojo.getLatitude_client(),coursePojo.getLongitude_client(),
                    coursePojo.getLatitude_destination(),coursePojo.getLongitude_destination(),String.valueOf(coursePojo.getId()),String.valueOf(getAdapterPosition()),coursePojo.getUser_name(),coursePojo.getDuree());
            bottomSheetFragmentCourse.show(((FragmentActivity)mContext).getSupportFragmentManager(), bottomSheetFragmentCourse.getTag());

        }
    }

    public CourseAdapter(Context mContext, List<CoursePojo> albumList, Activity activity) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card_course, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final CoursePojo coursePojo = albumList.get(position);

        distance = coursePojo.getDistance();
        if(distance.length() > 3) {
            String virgule = distance.substring(distance.length() - 3,distance.length() - 2);
            distance = distance.substring(0, distance.length() - 3);
            distance = distance+"."+virgule+" km";
        }else
            distance = distance+" m";

        holder.compteur_course.setText(coursePojo.getCompteur());
        holder.customer_name_course.setText(coursePojo.getUser_name());
        holder.date_course.setText(coursePojo.getDate());
        holder.distance_course.setText(distance);
        holder.montant_course.setText(coursePojo.getSomme()+" $   ");

        if(!coursePojo.getDistance_destination().equals("0 m")) {
            holder.distance_destination.setText("" + coursePojo.getDistance_destination() + " from here");
            holder.distance_destination.setVisibility(View.VISIBLE);
        }else{
            holder.distance_destination.setVisibility(View.GONE);
        }

        holder.statut_course.setText(coursePojo.getStatut());
        if(coursePojo.getStatut().equals("en cours")){
            holder.distance_destination.setVisibility(View.VISIBLE);
            holder.statut_course.setBackground(mContext.getResources().getDrawable(R.drawable.custom_bg_statut_en_cours));
            holder.statut_course.setTextColor(Color.BLACK);
        }else if(coursePojo.getStatut().equals("clôturer")){
            holder.distance_destination.setVisibility(View.VISIBLE);
            holder.statut_course.setBackground(mContext.getResources().getDrawable(R.drawable.custom_bg_statut_execute));
            holder.statut_course.setTextColor(Color.WHITE);
        }else if(coursePojo.getStatut().equals("annuler")){
            holder.distance_destination.setVisibility(View.VISIBLE);
            holder.statut_course.setBackground(mContext.getResources().getDrawable(R.drawable.custom_bg_statut_annuler));
            holder.statut_course.setTextColor(Color.WHITE);
        }else{
            holder.distance_destination.setVisibility(View.VISIBLE);
            holder.statut_course.setBackground(mContext.getResources().getDrawable(R.drawable.custom_bg_statut_execute));
            holder.statut_course.setTextColor(Color.WHITE);
        }

        if(coursePojo.getStatut().equals("clôturer"))
            holder.moreOptions.setVisibility(View.VISIBLE);
        else
            holder.moreOptions.setVisibility(View.VISIBLE);

//        holder.relative_layout.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                BottomSheetFragmentCourse bottomSheetFragmentCourse = new BottomSheetFragmentCourse(activity,coursePojo.getStatut(),coursePojo.getDistance(),
//                        coursePojo.getLatitude_client(),coursePojo.getLongitude_client(),
//                        coursePojo.getLatitude_destination(),coursePojo.getLongitude_destination(),String.valueOf(coursePojo.getId()),String.valueOf(position),coursePojo.getUser_name(),coursePojo.getDuree());
//                bottomSheetFragmentCourse.show(((FragmentActivity)mContext).getSupportFragmentManager(), bottomSheetFragmentCourse.getTag());
//                return false;
//            }
//        });
        holder.moreOptions.setVisibility(View.GONE);
        holder.moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(mContext, holder.moreOptions);
                //inflating menu from xml resource
                popup.inflate(R.menu.menu_action_course);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
//                            case R.id.action_terminer:
//                                FragmentMesCourses.showDialog();
//                                new terminerCourse().execute(String.valueOf(coursePojo.getId()),String.valueOf(position));
                            case R.id.action_voir_trajet:
                                BottomSheetFragmentCourse bottomSheetFragmentCourse = new BottomSheetFragmentCourse(activity,coursePojo.getStatut(),coursePojo.getDistance(),
                                        coursePojo.getLatitude_client(),coursePojo.getLongitude_client(),
                                        coursePojo.getLatitude_destination(),coursePojo.getLongitude_destination(),String.valueOf(coursePojo.getId()),String.valueOf(position),coursePojo.getUser_name(),coursePojo.getDuree());
                                bottomSheetFragmentCourse.show(((FragmentActivity)mContext).getSupportFragmentManager(), bottomSheetFragmentCourse.getTag());
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();
            }
        });
    }

    public void CalculDistance(){
        Collections.sort(albumList, new Comparator<CoursePojo>() {
            @Override
            public int compare(CoursePojo lhs, CoursePojo rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                tab[0] = String.valueOf(lhs.getDistance_destination()).split(" ");
                tab1[0] = String.valueOf(rhs.getDistance_destination()).split(" ");
                if(tab[0][1].equals("m"))
                    tab[0][0] = String.valueOf(Float.parseFloat(tab[0][0])/1000);
                if(tab1[0][1].equals("m"))
                    tab1[0][0] = String.valueOf(Float.parseFloat(tab1[0][0])/1000);
                return Float.parseFloat(tab[0][0]) < Float.parseFloat(tab1[0][0])  ? -1 : (Float.parseFloat(tab[0][0]) > Float.parseFloat(tab1[0][0])) ? 1 : 0;
            }
        });
    }

    /** Clôturer une course **/
    private class terminerCourse extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"terminer_course.php";
            final String requete = params[0];
            final String position = params[1];
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
                                    CoursePojo coursePojo = albumList.get(Integer.parseInt(position));
                                    coursePojo.setStatut("clôturer");
                                    notifyDataSetChanged();
                                    Toast.makeText(mContext, "Vous avez terminé la course avec succès", Toast.LENGTH_SHORT).show();
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
                    params.put("id_conducteur", M.getID(mContext));
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
    public int getItemCount() {
        return albumList.size();
    }

    public void delete(int position){
        albumList.remove(position);
        notifyItemRemoved(position);
        return;
    }

    public CoursePojo getCourse(int id){
        CoursePojo coursePojo = null;
        for (int i=0; i< albumList.size(); i++){
            if(albumList.get(i).getId() == id){
                coursePojo = albumList.get(i);
                break;
            }
        }
        return coursePojo;
    }

    public void restoreItem(CoursePojo coursePojo, int position) {
        albumList.add(position, coursePojo);
        notifyItemInserted(position);
    }

    public List<CoursePojo> getData() {
        return albumList;
    }
}