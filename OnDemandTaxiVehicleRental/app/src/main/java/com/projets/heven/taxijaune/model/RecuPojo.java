package com.projets.heven.taxijaune.model;

/**
 * Created by Woumtana on 01/12/2016.
 */

public class RecuPojo {
    private int id;
    private String image;
    private String image_name;
    private String montant;
    private String duree;
    private String distance;
    private int id_course;
    private int id_conducteur;
    private int id_user_app;
    private int creer;

    public RecuPojo(int id, String image, String image_name, String montant, String duree, String distance, int id_course, int id_conducteur, int id_user_app, int creer) {
        this.id = id;
        this.image = image;
        this.image_name = image_name;
        this.montant = montant;
        this.duree = duree;
        this.distance = distance;
        this.id_course = id_course;
        this.id_conducteur = id_conducteur;
        this.id_user_app = id_user_app;
        this.creer = creer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getId_course() {
        return id_course;
    }

    public void setId_course(int id_course) {
        this.id_course = id_course;
    }

    public int getId_conducteur() {
        return id_conducteur;
    }

    public void setId_conducteur(int id_conducteur) {
        this.id_conducteur = id_conducteur;
    }

    public int getId_user_app() {
        return id_user_app;
    }

    public void setId_user_app(int id_user_app) {
        this.id_user_app = id_user_app;
    }

    public int getCreer() {
        return creer;
    }

    public void setCreer(int creer) {
        this.creer = creer;
    }

    public RecuPojo() {

    }
}