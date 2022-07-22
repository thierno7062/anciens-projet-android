package com.projets.heven.taxijaune.model;

/**
 * Created by Woumtana on 01/12/2016.
 */

public class ReservationPojo {
    private int id;
    private int user_id;
    private String distance;
    private String date;
    private String heure;
    private String statut;
    private String cout;

    public ReservationPojo() {
    }

    public ReservationPojo(int id, int user_id, String distance, String date, String heure, String statut, String cout) {
        this.id = id;
        this.user_id = user_id;
        this.distance = distance;
        this.date = date;
        this.heure = heure;
        this.statut = statut;
        this.cout = cout;
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

    public String getCout() {
        return cout;
    }

    public void setCout(String cout) {
        this.cout = cout;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }
}