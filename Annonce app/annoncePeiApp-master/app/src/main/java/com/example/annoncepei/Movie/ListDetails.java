package com.example.annoncepei.Movie;

import java.util.ArrayList;

public class ListDetails {

    public static ArrayList<Movie> getList(){

        ArrayList<Movie> moviesList = new ArrayList<>(  );

            moviesList.add( new Movie( "Un super film", "17", "./", "Fr", "lkfdlmfklmdk" ) );
            moviesList.add( new Movie( "Un super film", "17", "./", "Fr", "lkfdlmfklmdk" ) );
            moviesList.add( new Movie( "Un super film", "17", "./", "Fr", "lkfdlmfklmdk" ) );
            moviesList.add( new Movie( "Un super film", "17", "./", "Fr", "lkfdlmfklmdk" ) );
            moviesList.add( new Movie( "Un super film", "17", "./", "Fr", "lkfdlmfklmdk" ) );
            moviesList.add( new Movie( "Un super film", "17", "./", "Fr", "lkfdlmfklmdk" ) );



            return  moviesList;
    }
}
