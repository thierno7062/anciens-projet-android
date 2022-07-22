package com.projets.heven.taxijaune.model;

/**
 * Created by Woumtana on 01/12/2016.
 */

public class MessagePojo {
    private int id;
    private int id_user_app;
    private int id_conducteur;
    private String user_name;
    private String description;
    private String date;
    private String statut;
    private String conducteur_name;
    private String user_cat;


    public MessagePojo() {
    }

    public MessagePojo(int id, int id_user_app, int id_conducteur, String user_name, String conducteur_name, String description, String date, String user_cat) {
        this.id = id;
        this.id_user_app = id_user_app;
        this.user_name = user_name;
        this.conducteur_name = conducteur_name;
        this.description = description;
        this.date = date;
        this.statut = statut;
        this.id_conducteur = id_conducteur;
        this.user_cat = user_cat;
    }

    public int getId() {
        return id;
    }


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId_user_app() {
        return id_user_app;
    }

    public void setId_user_app(int id_user_app) {
        this.id_user_app = id_user_app;
    }

    public int getId_conducteur() {
        return id_conducteur;
    }

    public void setId_conducteur(int id_conducteur) {
        this.id_conducteur = id_conducteur;
    }

    public String getConducteur_name() {
        return conducteur_name;
    }

    public void setConducteur_name(String conducteur_name) {
        this.conducteur_name = conducteur_name;
    }

    public String getUser_cat() {
        return user_cat;
    }

    public void setUser_cat(String user_cat) {
        this.user_cat = user_cat;
    }
}