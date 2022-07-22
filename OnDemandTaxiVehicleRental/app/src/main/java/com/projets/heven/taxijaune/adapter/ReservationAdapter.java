package com.projets.heven.taxijaune.adapter;

/**
 * Created by Woumtana on 01/12/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.projets.heven.taxijaune.controller.AppController;
import com.projets.heven.taxijaune.fragment.BottomSheetFragmentRequete;
import com.projets.heven.taxijaune.fragment.FragmentReservation;
import com.projets.heven.taxijaune.model.M;
import com.projets.heven.taxijaune.model.ReservationPojo;
import com.projets.heven.taxijaune.settings.AppConst;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.MyViewHolder> {

    private Context mContext;
    private List<ReservationPojo> albumList;
    Activity activity;
    private String distance;
    public static final String[] MONTHS = {"Jan", "Fev", "Mar", "Avr", "Mai", "Jui", "Jul", "Aou", "Sep", "Oct", "Nov", "Dec"};

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        private TextView moreOptions,date_requete,distance_requete,statut_reservation,cout_reservation;
        private LinearLayout layout_distance_requete;


        public MyViewHolder(View view) {
            super(view);
            date_requete = (TextView) view.findViewById(R.id.date_requete);
            distance_requete = (TextView) view.findViewById(R.id.distance_requete);
            moreOptions = (TextView) view.findViewById(R.id.moreOptions);
            statut_reservation = (TextView) view.findViewById(R.id.statut_reservation);
            cout_reservation = (TextView) view.findViewById(R.id.cout_reservation);
            layout_distance_requete = (LinearLayout) view.findViewById(R.id.layout_distance_requete);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ReservationPojo reservationPojo = albumList.get(getAdapterPosition());
            if(reservationPojo.getStatut().equals("en cours"))
                showMessageAnnuler(String.valueOf(reservationPojo.getId()),String.valueOf(getAdapterPosition()));
        }
    }

    public ReservationAdapter(Context mContext, List<ReservationPojo> albumList, Activity activity) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card_mes_reservations, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ReservationPojo reservationPojo = albumList.get(position);
        String tabDateDebut[] = reservationPojo.getDate().split("-");
        holder.date_requete.setText(tabDateDebut[2] + " " + MONTHS[Integer.parseInt(tabDateDebut[1])-1] + ". à "+reservationPojo.getHeure());
        distance = reservationPojo.getDistance();
        if(distance.length() > 3) {
            String virgule = distance.substring(distance.length() - 3,distance.length() - 2);
            distance = distance.substring(0, distance.length() - 3);
            distance = distance+"."+virgule+" km";
        }else
            distance = distance+" m";
        holder.distance_requete.setText(distance);
        holder.cout_reservation.setText(reservationPojo.getCout()+" $");

        holder.statut_reservation.setText(reservationPojo.getStatut());
        if(reservationPojo.getStatut().equals("en cours")){
            holder.distance_requete.setVisibility(View.VISIBLE);
            holder.statut_reservation.setBackground(mContext.getResources().getDrawable(R.drawable.custom_bg_statut_en_cours));
            holder.statut_reservation.setTextColor(mContext.getResources().getColor(R.color.colorLogoBlack));
            holder.moreOptions.setVisibility(View.VISIBLE);
        }else if(reservationPojo.getStatut().equals("accepter")){
            holder.distance_requete.setVisibility(View.VISIBLE);
            holder.statut_reservation.setBackground(mContext.getResources().getDrawable(R.drawable.custom_bg_statut_valide));
            holder.statut_reservation.setTextColor(Color.WHITE);
            holder.moreOptions.setVisibility(View.INVISIBLE);
        }else if(reservationPojo.getStatut().equals("annuler")){
            holder.distance_requete.setVisibility(View.VISIBLE);
            holder.statut_reservation.setBackground(mContext.getResources().getDrawable(R.drawable.custom_bg_statut_annuler));
            holder.statut_reservation.setTextColor(Color.WHITE);
            holder.moreOptions.setVisibility(View.VISIBLE);
        }else{
            holder.distance_requete.setVisibility(View.INVISIBLE);
            holder.statut_reservation.setBackground(mContext.getResources().getDrawable(R.drawable.custom_bg_statut_execute));
            holder.statut_reservation.setTextColor(Color.WHITE);
            holder.moreOptions.setVisibility(View.VISIBLE);
        }

        holder.moreOptions.setVisibility(View.GONE);
        /*
        holder.moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(mContext, holder.moreOptions);
                //inflating menu from xml resource
                popup.inflate(R.menu.menu_action_reservation);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_annuler:
                                showMessageAnnuler(String.valueOf(reservationPojo.getId()),String.valueOf(position));
                                return true;
//                            case R.id.action_supprimer:
//
//                                return true;
//                            case R.id.action_voir_trajet:
//                                BottomSheetFragmentRequete bottomSheetFragmentRequete = new BottomSheetFragmentRequete(activity,"","","");
//                                bottomSheetFragmentRequete.show(((FragmentActivity)mContext).getSupportFragmentManager(), bottomSheetFragmentRequete.getTag());
//                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();
            }
        });*/
    }

    public void showMessageAnnuler(final String idReservation, final String position){
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(mContext.getResources().getString(R.string.voulez_vous_annuler_votre_reservation))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FragmentReservation.showDialog();
                        new deleteReservation().execute(idReservation, position);
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

    /** Supprimer une réservation **/
    private class deleteReservation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"delete_reservation.php";
            final String reservation = params[0];
            final String position = params[1];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                FragmentReservation.dismissDialog();
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    delete(Integer.parseInt(position));
                                    notifyDataSetChanged();
                                    Toast.makeText(mContext, mContext.getResources().getString(R.string.votre_reservation_a_ete_annule_avec_succes), Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(mContext, mContext.getResources().getString(R.string.erreur_de_suppression), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    FragmentReservation.dismissDialog();
//                    Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_user_app", M.getID(mContext));
                    params.put("id_reservation", reservation);
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

    public ReservationPojo getReservation(int id){
        ReservationPojo reservationPojo = null;
        for (int i=0; i< albumList.size(); i++){
            if(albumList.get(i).getId() == id){
                reservationPojo = albumList.get(i);
                break;
            }
        }
        return reservationPojo;
    }
}