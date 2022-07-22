package com.projets.heven.taxijaune.model;

/**
 * Created by Woumtana on 01/12/2016.
 */

public class TaxiPojo {
    private int id;
    private String numero;
    private String immatriculation;
    private String statut;
    private String latitude;
    private String longitude;
    private String creer;
    private String modifier;
    private String type_vehicule;

    public TaxiPojo() {

    }

    public TaxiPojo(int id, String numero, String immatriculation, String statut, String latitude, String longitude, String creer, String modifier, String type_vehicule) {
        this.id = id;
        this.numero = numero;
        this.immatriculation = immatriculation;
        this.statut = statut;
        this.latitude = latitude;
        this.longitude = longitude;
        this.creer = creer;
        this.modifier = modifier;
        this.type_vehicule = type_vehicule;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCreer() {
        return creer;
    }

    public void setCreer(String creer) {
        this.creer = creer;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getType_vehicule() {
        return type_vehicule;
    }

    public void setType_vehicule(String type_vehicule) {
        this.type_vehicule = type_vehicule;
    }
}