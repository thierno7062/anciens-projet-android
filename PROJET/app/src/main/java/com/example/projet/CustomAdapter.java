package com.example.projet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList id, nom, type_piece, CNI_piece;

    CustomAdapter(Activity activity, Context context, ArrayList id, ArrayList nom, ArrayList type_piece,
                  ArrayList CNI_piece){
        this.activity = activity;
        this.context = context;
        this.id = id;
        this.nom = nom;
        this.type_piece = type_piece;
        this.CNI_piece = CNI_piece;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.id_txt.setText(String.valueOf(id.get(position)));
        holder.nom_txt.setText(String.valueOf(nom.get(position)));
        holder.type_txt.setText(String.valueOf(type_piece.get(position)));
        holder.cni_txt.setText(String.valueOf(CNI_piece.get(position)));
        //Recyclerview onClickListener
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(id.get(position)));
                intent.putExtra("title", String.valueOf(nom.get(position)));
                intent.putExtra("author", String.valueOf(type_piece.get(position)));
                intent.putExtra("pages", String.valueOf(CNI_piece.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });


    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id_txt, nom_txt, type_txt, cni_txt;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id_txt = itemView.findViewById(R.id.id_txt);
            nom_txt = itemView.findViewById(R.id.nom_txt);
            type_txt = itemView.findViewById(R.id.type_txt);
            cni_txt = itemView.findViewById(R.id.cni_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            //Animate Recyclerview
            Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);
        }

    }

}
