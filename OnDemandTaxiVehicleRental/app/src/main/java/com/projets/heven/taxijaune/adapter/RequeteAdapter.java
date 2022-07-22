package com.projets.heven.taxijaune.adapter;

/**
 * Created by Woumtana on 01/12/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.projets.heven.taxijaune.R;
import com.projets.heven.taxijaune.activity.MainActivity;
import com.projets.heven.taxijaune.activity.TchatActivity;
import com.projets.heven.taxijaune.controller.AppController;
import com.projets.heven.taxijaune.fragment.BottomSheetFragmentHistoric;
import com.projets.heven.taxijaune.fragment.BottomSheetFragmentMesRequete;
import com.projets.heven.taxijaune.fragment.BottomSheetFragmentRequete;
import com.projets.heven.taxijaune.fragment.FragmentAccueil;
import com.projets.heven.taxijaune.fragment.FragmentMesRequetes;
import com.projets.heven.taxijaune.fragment.FragmentRequete;
import com.projets.heven.taxijaune.model.M;
import com.projets.heven.taxijaune.model.RequetePojo;
import com.projets.heven.taxijaune.settings.AppConst;
import com.projets.heven.taxijaune.settings.Progressdialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequeteAdapter extends RecyclerView.Adapter<RequeteAdapter.MyViewHolder> {

    private Context mContext;
    private List<RequetePojo> albumList;
    Activity activity;
    private String currentActivity;
    private String distance = "";
    final private String[][] tab = {{}};
    final private String[][] tab1 = { {} };

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        private TextView user_name_requete,moreOptions,date_requete,distance_requete,distance_client_requete,statut_requete;
        private LinearLayout layout_distance_requete;
        private RelativeLayout relative_layout;


        public MyViewHolder(View view) {
            super(view);
            if(currentActivity.equals("Requete")){
                user_name_requete = (TextView) view.findViewById(R.id.user_name_requete);
                distance_client_requete = (TextView) view.findViewById(R.id.distance_client_requete);
                moreOptions = (TextView) view.findViewById(R.id.moreOptions);
            }else{
                moreOptions = (TextView) view.findViewById(R.id.moreOptions);
            }
            statut_requete = (TextView) view.findViewById(R.id.statut_requete);
            date_requete = (TextView) view.findViewById(R.id.date_requete);
            distance_requete = (TextView) view.findViewById(R.id.distance_requete);
            layout_distance_requete = (LinearLayout) view.findViewById(R.id.layout_distance_requete);
            relative_layout = (RelativeLayout) view.findViewById(R.id.relative_layout);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            RequetePojo requetePojo = albumList.get(getAdapterPosition());
            if(currentActivity.equals("MesRequete")){
                BottomSheetFragmentMesRequete bottomSheetFragmentMesRequete = new BottomSheetFragmentMesRequete(activity, requetePojo.getStatut_course(), requetePojo.getDistance(),
                        requetePojo.getDistance_client(), requetePojo.getLatitude_client(), requetePojo.getLongitude_client(),
                        requetePojo.getLatitude_destination(), requetePojo.getLongitude_destination(), String.valueOf(requetePojo.getId()), String.valueOf(getAdapterPosition())
                        , requetePojo.getConducteur_name(), requetePojo.getStatut(), String.valueOf(requetePojo.getConducteur_id()), requetePojo.getNote(), requetePojo.getMoyenne(), requetePojo.getNb_avis(),requetePojo.getDuree());
                bottomSheetFragmentMesRequete.show(((FragmentActivity) mContext).getSupportFragmentManager(), bottomSheetFragmentMesRequete.getTag());
            }else if(currentActivity == "Requete"){
//                showMessageAccepter(requetePojo.getId(),getAdapterPosition());
                BottomSheetFragmentRequete bottomSheetFragmentRequete = new BottomSheetFragmentRequete(activity, requetePojo.getStatut(), requetePojo.getDistance(),
                        requetePojo.getDistance_client(), requetePojo.getLatitude_client(), requetePojo.getLongitude_client(),
                        requetePojo.getLatitude_destination(), requetePojo.getLongitude_destination(), String.valueOf(requetePojo.getId()), String.valueOf(getAdapterPosition()),requetePojo.getDuree());
                bottomSheetFragmentRequete.show(((FragmentActivity) mContext).getSupportFragmentManager(), bottomSheetFragmentRequete.getTag());
            }else if(currentActivity == "Historic"){
                BottomSheetFragmentHistoric bottomSheetFragmentHistoric = new BottomSheetFragmentHistoric(activity, requetePojo.getStatut_course(), requetePojo.getDistance(),
                        requetePojo.getDistance_client(), requetePojo.getLatitude_client(), requetePojo.getLongitude_client(),
                        requetePojo.getLatitude_destination(), requetePojo.getLongitude_destination(), String.valueOf(requetePojo.getId()), String.valueOf(getAdapterPosition())
                        , requetePojo.getConducteur_name(), requetePojo.getStatut(), String.valueOf(requetePojo.getConducteur_id()), requetePojo.getNote(), requetePojo.getMoyenne(), requetePojo.getNb_avis(),requetePojo.getDuree());
                bottomSheetFragmentHistoric.show(((FragmentActivity) mContext).getSupportFragmentManager(), bottomSheetFragmentHistoric.getTag());
            }else {
                FragmentAccueil.showDirection(requetePojo.getLatitude_client(),requetePojo.getLongitude_client(),requetePojo.getLatitude_destination(),requetePojo.getLongitude_destination());
            }
        }
    }

    public RequeteAdapter(Context mContext, List<RequetePojo> albumList, Activity activity, String currentActivity) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.activity = activity;
        this.currentActivity = currentActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if(currentActivity.equals("Requete"))
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_card_requete, parent, false);
        else
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_card_mes_requete, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final RequetePojo requetePojo = albumList.get(position);
        distance = requetePojo.getDistance();
        if(distance.length() > 3) {
            String virgule = distance.substring(distance.length() - 3,distance.length() - 2);
            distance = distance.substring(0, distance.length() - 3);
            distance = distance+"."+virgule+" km";
        }else
            distance = distance+" m";
        if(currentActivity.equals("Requete")) {
            holder.user_name_requete.setText(requetePojo.getUser_name());
            if(!requetePojo.getDistance_client().equals("0 m")) {
                holder.distance_client_requete.setText("" + requetePojo.getDistance_client() + " from here");
                holder.distance_client_requete.setVisibility(View.VISIBLE);
            }else{
                holder.distance_client_requete.setVisibility(View.GONE);
            }
        }
//        holder.statut_requete.setTextColor(mContext.getResources().getColor(R.color.colorLogoBlack));
        holder.date_requete.setText(requetePojo.getDate());
        holder.distance_requete.setText(distance);

        if(!requetePojo.getStatut_course().equals("clôturer")) {
            holder.statut_requete.setText(requetePojo.getStatut());
            if (requetePojo.getStatut().equals("en cours")) {
                holder.distance_requete.setVisibility(View.VISIBLE);
                holder.statut_requete.setBackground(mContext.getResources().getDrawable(R.drawable.custom_bg_statut_en_cours));
                holder.statut_requete.setTextColor(mContext.getResources().getColor(R.color.colorLogoBlack));
                if(!currentActivity.equals("Requete"))
                    holder.moreOptions.setVisibility(View.VISIBLE);
            } else if (requetePojo.getStatut().equals("accepter")) {
                holder.distance_requete.setVisibility(View.VISIBLE);
                holder.statut_requete.setBackground(mContext.getResources().getDrawable(R.drawable.custom_bg_statut_valide));
                holder.statut_requete.setTextColor(Color.WHITE);
                if(!currentActivity.equals("Requete"))
                    holder.moreOptions.setVisibility(View.VISIBLE);
            } else if (requetePojo.getStatut().equals("annuler")) {
                holder.distance_requete.setVisibility(View.VISIBLE);
                holder.statut_requete.setBackground(mContext.getResources().getDrawable(R.drawable.custom_bg_statut_annuler));
                holder.statut_requete.setTextColor(Color.WHITE);
                if(!currentActivity.equals("Requete"))
                    holder.moreOptions.setVisibility(View.VISIBLE);
            } else {
                holder.distance_requete.setVisibility(View.VISIBLE);
                holder.statut_requete.setBackground(mContext.getResources().getDrawable(R.drawable.custom_bg_statut_execute));
                holder.statut_requete.setTextColor(Color.WHITE);
                if(!currentActivity.equals("Requete"))
                    holder.moreOptions.setVisibility(View.VISIBLE);
            }
        }else{
            holder.statut_requete.setText(requetePojo.getStatut_course());
            holder.distance_requete.setVisibility(View.VISIBLE);
            holder.statut_requete.setBackground(mContext.getResources().getDrawable(R.drawable.custom_bg_statut_execute));
            holder.statut_requete.setTextColor(Color.WHITE);
            if(!currentActivity.equals("Requete"))
                holder.moreOptions.setVisibility(View.VISIBLE);
            else
                holder.moreOptions.setVisibility(View.VISIBLE);
        }

        /*if(currentActivity.equals("Requete")) {
//            holder.relative_layout.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    BottomSheetFragmentRequete bottomSheetFragmentRequete = new BottomSheetFragmentRequete(activity, requetePojo.getStatut(), requetePojo.getDistance(),
//                            requetePojo.getDistance_client(), requetePojo.getLatitude_client(), requetePojo.getLongitude_client(),
//                            requetePojo.getLatitude_destination(), requetePojo.getLongitude_destination(), String.valueOf(requetePojo.getId()), String.valueOf(position),requetePojo.getDuree());
//                    bottomSheetFragmentRequete.show(((FragmentActivity) mContext).getSupportFragmentManager(), bottomSheetFragmentRequete.getTag());
//                    return false;
//                }
//            });
        }else{
            holder.relative_layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    BottomSheetFragmentMesRequete bottomSheetFragmentMesRequete = new BottomSheetFragmentMesRequete(activity, requetePojo.getStatut_course(), requetePojo.getDistance(),
                            requetePojo.getDistance_client(), requetePojo.getLatitude_client(), requetePojo.getLongitude_client(),
                            requetePojo.getLatitude_destination(), requetePojo.getLongitude_destination(), String.valueOf(requetePojo.getId()), String.valueOf(position)
                            , requetePojo.getConducteur_name(), requetePojo.getStatut(), String.valueOf(requetePojo.getConducteur_id()), requetePojo.getNote(), requetePojo.getMoyenne(), requetePojo.getNb_avis(),requetePojo.getDuree());
                    bottomSheetFragmentMesRequete.show(((FragmentActivity) mContext).getSupportFragmentManager(), bottomSheetFragmentMesRequete.getTag());
                    return false;
                }
            });
        }*/

        holder.moreOptions.setVisibility(View.GONE);
        if(!currentActivity.equals("Requete")) {
            holder.moreOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(mContext, holder.moreOptions);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.menu_action_mes_requete);
                    if(requetePojo.getStatut().equals("en cours"))
                        popup.getMenu().findItem(R.id.action_annuler).setVisible(true);
                    else
                        popup.getMenu().findItem(R.id.action_annuler).setVisible(false);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_voir_trajet:
                                    BottomSheetFragmentMesRequete bottomSheetFragmentMesRequete = new BottomSheetFragmentMesRequete(activity, requetePojo.getStatut_course(), requetePojo.getDistance(),
                                            requetePojo.getDistance_client(), requetePojo.getLatitude_client(), requetePojo.getLongitude_client(),
                                            requetePojo.getLatitude_destination(), requetePojo.getLongitude_destination(), String.valueOf(requetePojo.getId()), String.valueOf(position)
                                            , requetePojo.getConducteur_name(), requetePojo.getStatut(), String.valueOf(requetePojo.getConducteur_id()), requetePojo.getNote(), requetePojo.getMoyenne(), requetePojo.getNb_avis(),requetePojo.getDuree());
                                    bottomSheetFragmentMesRequete.show(((FragmentActivity) mContext).getSupportFragmentManager(), bottomSheetFragmentMesRequete.getTag());
                                    return true;
                                case R.id.action_annuler:
                                    showMessageAnnuler(requetePojo.getId(),position);
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
        }else{
            holder.moreOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(mContext, holder.moreOptions);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.menu_action_requete);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
//                                case R.id.action_modifier:
//
//                                    return true;
                                case R.id.action_voir_trajet:
                                    BottomSheetFragmentRequete bottomSheetFragmentRequete = new BottomSheetFragmentRequete(activity, requetePojo.getStatut(), requetePojo.getDistance(),
                                            requetePojo.getDistance_client(), requetePojo.getLatitude_client(), requetePojo.getLongitude_client(),
                                            requetePojo.getLatitude_destination(), requetePojo.getLongitude_destination(), String.valueOf(requetePojo.getId()), String.valueOf(position),requetePojo.getDuree());
                                    bottomSheetFragmentRequete.show(((FragmentActivity) mContext).getSupportFragmentManager(), bottomSheetFragmentRequete.getTag());
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
    }

    public void showMessageAccepter(final int idRequete, final int position){
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(mContext.getResources().getString(R.string.voulez_vous_accepter_cette_course))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FragmentRequete.showDialog();
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

    public void showMessageAnnuler(final int idRequete, final int position){
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(mContext.getResources().getString(R.string.voulez_vous_annuler_votre_demande))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FragmentMesRequetes.showDialog();
                        new deleteRequete().execute(String.valueOf(idRequete), String.valueOf(position));
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

    /** Supprimer une requête **/
    private class deleteRequete extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"delete_requete.php";
            final String requete = params[0];
            final String position = params[1];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                FragmentMesRequetes.dismissDialog();
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    delete(Integer.parseInt(position));
                                    notifyDataSetChanged();
                                    Toast.makeText(mContext, "Votre requête a été annulée avec succès", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(mContext, "Erreur de suppression", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    FragmentMesRequetes.dismissDialog();
//                    Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_user_app", M.getID(mContext));
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

    public void CalculDistance(){
        Collections.sort(albumList, new Comparator<RequetePojo>() {
            @Override
            public int compare(RequetePojo lhs, RequetePojo rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                tab[0] = String.valueOf(lhs.getDistance_client()).split(" ");
                tab1[0] = String.valueOf(rhs.getDistance_client()).split(" ");
                if(tab[0][1].equals("m"))
                    tab[0][0] = String.valueOf(Float.parseFloat(tab[0][0])/1000);
                if(tab1[0][1].equals("m"))
                    tab1[0][0] = String.valueOf(Float.parseFloat(tab1[0][0])/1000);
                return Float.parseFloat(tab[0][0]) < Float.parseFloat(tab1[0][0])  ? -1 : (Float.parseFloat(tab[0][0]) > Float.parseFloat(tab1[0][0])) ? 1 : 0;
            }
        });
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

    public RequetePojo getRequete(int id){
        RequetePojo requetePojo = null;
        for (int i=0; i< albumList.size(); i++){
            if(albumList.get(i).getId() == id){
                requetePojo = albumList.get(i);
                break;
            }
        }
        return requetePojo;
    }

    public void restoreItem(RequetePojo requetePojo, int position) {
        albumList.add(position, requetePojo);
        notifyItemInserted(position);
    }

    public List<RequetePojo> getData() {
        return albumList;
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
                                FragmentRequete.dismissDialog();
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    delete(Integer.parseInt(position));
                                    notifyDataSetChanged();
                                    Toast.makeText(mContext, mContext.getResources().getString(R.string.la_requete_a_ete_valide_avec_succes), Toast.LENGTH_SHORT).show();
                                    if(albumList.size() == 0)
                                        FragmentRequete.showNotFound();
                                }else if(etat.equals("2")){
                                    Toast.makeText(mContext, mContext.getResources().getString(R.string.echec_de_validation), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(mContext, mContext.getResources().getString(R.string.un_de_vos_collegue_a_deja_valide_cette_course), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    FragmentRequete.dismissDialog();
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
}