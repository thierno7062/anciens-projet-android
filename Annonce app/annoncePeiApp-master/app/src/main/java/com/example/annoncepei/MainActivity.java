package com.example.annoncepei;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.annoncepei.Movie.ListDetails;
import com.example.annoncepei.Movie.Movie;
import com.example.annoncepei.Movie.MovieAdapter;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RequestQueue rq;
    String API_KEY = "1639ef21d9cef54e2c9656ade400d223";

    TextView Title, shortDescription, Price, Rating, Titre ;
    String titre, description, prix, note;

    String url =  "http://api.themoviedb.org/3/search/movie?api_key=1639ef21d9cef54e2c9656ade400d223&query=heroes";

    //adapter
   /* private RecyclerView recyclerView;
    private AlbumsAdapter adapter;
    private List<Album> albumList;*/

   //List de film static
    private ListView listView;
    private ArrayList<Movie> arrayMovie;
    private MovieAdapter movieAdapter;

   // ArrayList<Movie> ListMovie = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

       DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener( toggle );
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener( this );

      listView = (ListView) findViewById( R.id.list_view );

      // ArrayMovie = ListDetails.getList();

        //initialisation de l'objet Volley pr fetch
        rq = Volley.newRequestQueue(this);

        //Appel la fonction qui return JSONObject response
        sendJsonRequest();




}


    //Fonction qui gére l'appel API
    public void sendJsonRequest() {

       /* JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.GET, url, null, new Response.Listener<JSONObject>() */
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.GET, url, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {

                arrayMovie = new ArrayList<>(  );

                try {

                    JSONArray jsonArray = response.getJSONArray( "results" );
                    //Log.d( "json", jsonArray.getJSONObject( 0 ).getString( "title" ));

                     //arrayMovie.add( new Movie( jsonArray.getJSONObject( 0 ).getString( "title" ), "17", "./", "Fr" ) );

                    String posterPath = "http://image.tmdb.org/t/p/original/";


                    for (int i = 0; i <= jsonArray.length(); i++) {

                       JSONObject obj =  jsonArray.getJSONObject(i);


                       String Titre = obj.getString( "title" );
                       String Vote = obj.getString( "vote_average" );
                       String ImageUrl= posterPath + obj.getString( "poster_path" );
                       String Langue= obj.getString( "original_language" );
                       String Overview = obj.getString( "overview" );


                        arrayMovie.add(new Movie(Titre,Vote,ImageUrl, Langue, Overview));

                        movieAdapter = new MovieAdapter( MainActivity.this, arrayMovie );

                        listView.setAdapter( (ListAdapter) arrayMovie );
                        //listView.setAdapter( movieAdapter );

                        Log.d( "result",   String.valueOf( arrayMovie ));

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d( "ErrorRequest", String.valueOf( error ) );
            }
        } );


      rq.add(jsonObjectRequest);

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        if (drawer.isDrawerOpen( GravityCompat.START )) {
            drawer.closeDrawer( GravityCompat.START );
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent( this, AnnonceContentActivity.class );
            startActivity( intent );
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
        return true;
    }
}
