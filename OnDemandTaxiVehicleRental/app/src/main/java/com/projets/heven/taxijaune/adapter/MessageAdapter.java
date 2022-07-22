package com.projets.heven.taxijaune.adapter;

/**
 * Created by Woumtana on 01/12/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.projets.heven.taxijaune.R;
import com.projets.heven.taxijaune.fragment.BottomSheetFragmentRequete;
import com.projets.heven.taxijaune.model.M;
import com.projets.heven.taxijaune.model.MessagePojo;
import com.projets.heven.taxijaune.model.RequetePojo;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    private Context mContext;
    private List<MessagePojo> albumList;
    public static final String[] MONTHS = {"Jan", "Fev", "Mar", "Avr", "Mai", "Jui", "Jul", "Aou", "Sep", "Oct", "Nov", "Dec"};

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        private TextView user_name_message,user_name_message_current,desc_message,desc_message_current,date_message,date_message_current;
        private RelativeLayout relativeLayout,relativeLayout_current;


        public MyViewHolder(View view) {
            super(view);
            user_name_message = (TextView) view.findViewById(R.id.user_name_message);
            user_name_message_current = (TextView) view.findViewById(R.id.user_name_message_current);
            desc_message = (TextView) view.findViewById(R.id.desc_message);
            desc_message_current = (TextView) view.findViewById(R.id.desc_message_current);
            date_message = (TextView) view.findViewById(R.id.date_message);
            date_message_current = (TextView) view.findViewById(R.id.date_message_current);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
            relativeLayout_current = (RelativeLayout) view.findViewById(R.id.relativeLayout_current);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            RequeteListe localItem = albumList.get(getAdapterPosition());
        }
    }

    public MessageAdapter(Context mContext, List<MessagePojo> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_card_message, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        MessagePojo messagePojo = albumList.get(position);
        String tabDateHeure[] = messagePojo.getDate().split(" ");
        String tabDate[] = tabDateHeure[0].split("-");
        String tabHeure = tabDateHeure[1];

        if(messagePojo.getUser_cat().equals(M.getUserCategorie(mContext))){
            holder.relativeLayout_current.setVisibility(View.VISIBLE);
            holder.user_name_message_current.setText(messagePojo.getUser_name());
            holder.desc_message_current.setText(messagePojo.getDescription());
            holder.date_message_current.setText(tabDate[2] + " " + MONTHS[Integer.parseInt(tabDate[1])-1]+" à "+tabHeure);
            holder.relativeLayout.setVisibility(View.GONE);
            holder.user_name_message.setText("");
            holder.desc_message.setText("");
            holder.date_message.setText("");
        }else {
            holder.relativeLayout.setVisibility(View.VISIBLE);
            holder.user_name_message.setText(messagePojo.getUser_name());
            holder.desc_message.setText(messagePojo.getDescription());
            holder.date_message.setText(tabDate[2] + " " + MONTHS[Integer.parseInt(tabDate[1])-1]+" à "+tabHeure);
            holder.relativeLayout_current.setVisibility(View.GONE);
            holder.user_name_message_current.setText("");
            holder.desc_message_current.setText("");
            holder.date_message_current.setText("");
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

    public MessagePojo getMessage(int id){
        MessagePojo messagePojo = null;
        for (int i=0; i< albumList.size(); i++){
            if(albumList.get(i).getId() == id){
                messagePojo = albumList.get(i);
                break;
            }
        }
        return messagePojo;
    }
}