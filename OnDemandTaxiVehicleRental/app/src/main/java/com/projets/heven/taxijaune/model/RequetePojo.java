package com.projets.heven.taxijaune.model;

/**
 * Created by Woumtana on 01/12/2016.
 */

public class RequetePojo {
    private int id;
    private int user_id;
    private int conducteur_id;
    private String user_name;
    private String conducteur_name;
    private String distance;
    private String distance_client;
    private String latitude_client;
    private String longitude_client;
    private String latitude_destination;
    private String longitude_destination;
    private String date;
    private String statut;
    private String statut_course;
    private String note;
    private String moyenne;
    private String nb_avis;
    private String cout;
    private String duree;

    public RequetePojo() {
    }

    public RequetePojo(int id, int user_id, int conducteur_id, String user_name, String conducteur_name, String distance, String date, String statut, String distance_client,
                       String latitude_client, String longitude_client, String latitude_destination, String longitude_destination
            , String statut_course, String note, String moyenne, String nb_avis, String cout, String duree) {
        this.id = id;
        this.user_id = user_id;
        this.conducteur_id = conducteur_id;
        this.user_name = user_name;
        this.conducteur_name = conducteur_name;
        this.distance = distance;
        this.date = date;
        this.statut = statut;
        this.statut_course = statut_course;
        this.distance_client = distance_client;
        this.longitude_client = longitude_client;
        this.latitude_client = latitude_client;
        this.longitude_destination = longitude_destination;
        this.latitude_destination = latitude_destination;
        this.note = note;
        this.moyenne = moyenne;
        this.nb_avis = nb_avis;
        this.cout = cout;
        this.duree = duree;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getDistance_client() {
        return distance_client;
    }

    public void setDistance_client(String distance_client) {
        this.distance_client = distance_client;
    }

    public String getLatitude_client() {
        return latitude_client;
    }

    public void setLatitude_client(String latitude_client) {
        this.latitude_client = latitude_client;
    }

    public String getLongitude_client() {
        return longitude_client;
    }

    public void setLongitude_client(String longitude_client) {
        this.longitude_client = longitude_client;
    }

    public String getLatitude_destination() {
        return latitude_destination;
    }

    public void setLatitude_destination(String latitude_destination) {
        this.latitude_destination = latitude_destination;
    }

    public String getLongitude_destination() {
        return longitude_destination;
    }

    public void setLongitude_destination(String longitude_destination) {
        this.longitude_destination = longitude_destination;
    }

    public int getConducteur_id() {
        return conducteur_id;
    }

    public void setConducteur_id(int conducteur_id) {
        this.conducteur_id = conducteur_id;
    }

    public String getConducteur_name() {
        return conducteur_name;
    }

    public void setConducteur_name(String conducteur_name) {
        this.conducteur_name = conducteur_name;
    }

    public String getStatut_course() {
        return statut_course;
    }

    public void setStatut_course(String statut_course) {
        this.statut_course = statut_course;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(String moyenne) {
        this.moyenne = moyenne;
    }

    public String getNb_avis() {
        return nb_avis;
    }

    public void setNb_avis(String nb_avis) {
        this.nb_avis = nb_avis;
    }

    public String getCout() {
        return cout;
    }

    public void setCout(String cout) {
        this.cout = cout;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }
}