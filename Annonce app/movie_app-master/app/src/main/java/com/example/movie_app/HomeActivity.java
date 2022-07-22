package com.example.movie_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.movie_app.adapter.MoviesAdapter;
import com.example.movie_app.api.Client;
import com.example.movie_app.api.Service;
import com.example.movie_app.model.Movie;
import com.example.movie_app.model.MovieResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Call;
import retrofit2.Response;


public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private List<Movie> movieList;
    ProgressDialog pd;
    private SwipeRefreshLayout swipeContainer;
    public static final String LOG_TAG = MoviesAdapter.class.getName();
    public static final String API_KEY = "f0af4e8d2c5f9c06f156cdf5ccd84bf7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();

        swipeContainer = (SwipeRefreshLayout)findViewById(R.id.main_content);
        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initViews();
                Toast.makeText(HomeActivity.this, "Movies Refreshed", Toast.LENGTH_SHORT).show();
            }
        });

        }

    public Activity getActivity(){
        Context context = this;
        while (context instanceof ContextWrapper){
            if (context instanceof Activity){
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    private void initViews(){
        pd = new ProgressDialog(this);
        pd.setMessage("Fetching movies...");
        pd.setCancelable(false);
        pd.show();

        recyclerView = findViewById(R.id.recycler_view);

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

        loadJSON();

    }

    private  void loadJSON(){

        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please obtain API Key firstly from the moviedb.org", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                return;
            }

            Client client = new Client();
            Service apiService =
                    client.getClient().create(Service.class);
            Call<MovieResponse> call = apiService.getPopularMovies(API_KEY);

            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    List<Movie> movies = response.body().getResults();
                    recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), movies));
                    recyclerView.smoothScrollToPosition(0);
                    if (swipeContainer.isRefreshing()){
                        swipeContainer.setRefreshing(false);
                    }
                    pd.dismiss();
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    Log.d("ERROR",t.getMessage());
                    Toast.makeText(HomeActivity.this, "ERROR Fetching Data!", Toast.LENGTH_SHORT).show();
                }

            });
        }catch (Exception e){
            Log.d("ERROR",e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }



}