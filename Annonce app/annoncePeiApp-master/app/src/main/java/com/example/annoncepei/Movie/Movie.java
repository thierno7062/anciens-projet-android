package com.example.annoncepei.Movie;

public class Movie {

    String Titre;
    String  Vote;
    String ImageUrl;
    String Langue;
    String Description;

    public Movie(String titre, String vote, String imageUrl, String langue, String description) {
        Titre = titre;
        Vote = vote;
        ImageUrl = imageUrl;
        Langue = langue;
        Description= description;
    }

    public String getTitre() {
        return Titre;
    }

    public void setTitre(String titre) {
        Titre = titre;
    }

    public String getVote() {
        return Vote;
    }

    public void setVote(String vote) {
        Vote = vote;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getLangue() {
        return Langue;
    }

    public void setLangue(String langue) {
        Langue = langue;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String toString()
    {
        return Titre + " " + Vote + " " + ImageUrl + " " + Langue+" "+ Description ;
    }
}
