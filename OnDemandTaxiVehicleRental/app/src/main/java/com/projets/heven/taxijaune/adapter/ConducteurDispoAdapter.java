package com.projets.heven.taxijaune.adapter;

/**
 * Created by Woumtana on 01/12/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.projets.heven.taxijaune.R;
import com.projets.heven.taxijaune.activity.TchatActivity;
import com.projets.heven.taxijaune.controller.AppController;
import com.projets.heven.taxijaune.fragment.BottomSheetFragmentCourse;
import com.projets.heven.taxijaune.fragment.FragmentMesCourses;
import com.projets.heven.taxijaune.model.ConducteurDispoPojo;
import com.projets.heven.taxijaune.model.CoursePojo;
import com.projets.heven.taxijaune.model.M;
import com.projets.heven.taxijaune.settings.AppConst;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConducteurDispoAdapter extends RecyclerView.Adapter<ConducteurDispoAdapter.MyViewHolder> {

    private Context mContext;
    private List<ConducteurDispoPojo> albumList;
    Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        private TextView immatriculation;
//        private ImageView img_vehicule;
//        private ProgressBar progressBar;


        public MyViewHolder(View view) {
            super(view);
            immatriculation = (TextView) view.findViewById(R.id.immatriculation);
//            img_vehicule = (ImageView) view.findViewById(R.id.img_vehicule);
//            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            CoursePojo coursePojo = albumList.get(getAdapterPosition());
        }
    }

    public ConducteurDispoAdapter(Context mContext, List<ConducteurDispoPojo> albumList, Activity activity) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card_conducteur_dispo, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ConducteurDispoPojo conducteurDispoPojo = albumList.get(position);
        holder.immatriculation.setText(conducteurDispoPojo.getImmatriculation());
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

    public ConducteurDispoPojo getConducteurDispo(int id){
        ConducteurDispoPojo conducteurDispoPojo = null;
        for (int i=0; i< albumList.size(); i++){
            if(albumList.get(i).getId() == id){
                conducteurDispoPojo = albumList.get(i);
                break;
            }
        }
        return conducteurDispoPojo;
    }
}