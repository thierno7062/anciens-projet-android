package com.example.movie_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_app.adapter.MoviesAdapter;
import com.example.movie_app.data.FavoriteDbHelper;
import com.example.movie_app.model.Movie;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private List<Movie> movieList;
    ProgressDialog pd;
    private FavoriteDbHelper favoriteDBHelper;
    private final AppCompatActivity activity = FavoritesActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        initViewsFavorite();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_home:
                        Intent intent1 = new Intent(FavoritesActivity.this, HomeActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_fav:
                        Intent intent2 = new Intent(FavoritesActivity.this, FavoritesActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_profile:
                        Intent intent3 = new Intent(FavoritesActivity.this, ProfileActivity.class);
                        startActivity(intent3);
                        break;
                }

                return false;
            }
        });
    }

    private void initViewsFavorite(){
        recyclerView = findViewById(R.id.recycler_favorites);

        movieList = new ArrayList<>();
        adapter = new MoviesAdapter(this, movieList);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        }
        else{
            recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        favoriteDBHelper = new FavoriteDbHelper(activity);

        getAllFavorite();

    }

    private void getAllFavorite(){
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                movieList.clear();
                movieList.addAll(favoriteDBHelper.getAllFavorite());
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid){
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }

    public Activity getActivity(){
        Context context = this;
        while(context instanceof ContextWrapper){
            if(context instanceof Activity){
                return (Activity) context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }
}