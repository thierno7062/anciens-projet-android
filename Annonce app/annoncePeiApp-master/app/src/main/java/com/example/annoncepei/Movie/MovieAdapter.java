package com.example.annoncepei.Movie;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.annoncepei.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Movie> movie;
    //private AdapterView.OnItemClickListener mListener;
    private  OnItemClickListener listener;


    /*
        Récupére la position du clic
     */
    public interface OnItemClickListener{

        void onItemClick(int position);
    }


    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        this.context = context;
        movie = movies;
    }



    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate( R.layout.list_movies, parent, false );

        return new ViewHolder( v );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Movie lisMovie = movie.get( position );

        //String Path = "http://image.tmdb.org/t/p/original//qO1cfr4UxcwQ858Nxp470QNS3v8.jpg";

        String UrlImage = lisMovie.getImageUrl();

        Picasso.get().load( UrlImage ).into(holder.imageView);

        holder.Titre.setText(lisMovie.getTitre());
        holder.Note.setText( lisMovie.getVote());
        holder.content.setText( lisMovie.getDescription() );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView Titre;
        TextView Note ;
        TextView content;
        ImageView imageView;



        public ViewHolder(View itemView){
            super (itemView);

            TextView Titre = (TextView) itemView.findViewById(R.id.textViewTitre);
            TextView Note = (TextView) itemView.findViewById( (R.id.textViewRating) );
            TextView content = (TextView) itemView.findViewById( R.id.textViewContent );
            ImageView imageView = itemView.findViewById( R.id.imageCardView ) ;

        }


    }

  /*  @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = (View) View.inflate( context, R.layout.list_movies, null);
        }

        TextView Titre = (TextView) convertView.findViewById(R.id.textViewTitre);
        TextView Note = (TextView) convertView.findViewById( (R.id.textViewRating) );
        TextView content = (TextView) convertView.findViewById( R.id.textViewContent );
        ImageView imageView = convertView.findViewById( R.id.imageCardView ) ;

        Movie movie = Movies.get( position );


       //String Path = "http://image.tmdb.org/t/p/original//qO1cfr4UxcwQ858Nxp470QNS3v8.jpg";

        String UrlImage = movie.getImageUrl();

        Picasso.get().load( UrlImage ).into(imageView);

        Titre.setText(movie.getTitre());
        Note.setText( movie.getVote());
       content.setText( movie.getDescription() );



        return convertView;
    }*/
}
