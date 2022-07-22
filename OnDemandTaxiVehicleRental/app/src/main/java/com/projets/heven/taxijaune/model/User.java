package com.projets.heven.taxijaune.model;

/**
 * Created by Woumtana on 01/01/2019.
 */

public class User {

    private String id;
    private String nom;
    private String prenom;
    private String phone;
    private String photo;
    private String email;
    private String etat;
    private String login_type;
//    private String latitude;
//    private String longitude;
    private String tonotify;
    private String device_id;
    private String fcm_id;
    private String creer;
    private String modifier;
    private String user_cat;
    private String statut_online;

    public User(String id, String nom, String prenom, String phone, String email, String etat, String login_type, String tonotify
            , String device_id, String fcm_id, String creer, String modifier, String photo, String user_cat, String statut_online) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.phone = phone;
        this.email = email;
        this.etat = etat;
        this.login_type = login_type;
        this.tonotify = tonotify;
        this.device_id = device_id;
        this.fcm_id = fcm_id;
        this.creer = creer;
        this.modifier = modifier;
        this.photo = photo;
        this.user_cat = user_cat;
        this.statut_online = statut_online;
    }

    public String getId() {
    return id;
    }

    public void setId(String id) {
    this.id = id;
    }

    public String getNom() {
    return nom;
    }

    public void setNom(String name) {
    this.nom = name;
    }

    public String getEmail() {
    return email;
    }

    public void setEmail(String email) {
    this.email = email;
    }

    public String getPhone() {
    return phone;
    }

    public void setPhone(String mobile_no) {
    this.phone = mobile_no;
    }

    public String getLogin_type() {
    return login_type;
    }

    public void setLogin_type(String login_type) {
    this.login_type = login_type;
    }

//    public String getLatitude() {
//    return latitude;
//    }
//
//    public void setLatitude(String latitude) {
//    this.latitude = latitude;
//    }
//
//    public String getLongitude() {
//    return longitude;
//    }
//
//    public void setLongitude(String longitude) {
//    this.longitude = longitude;
//    }

    public String getDevice_id() {
    return device_id;
    }

    public void setDevice_id(String device_id) {
    this.device_id = device_id;
    }

    public String getFcm_id() {
    return fcm_id;
    }

    public void setFcm_id(String fcm_id) {
    this.fcm_id = fcm_id;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTonotify() {
        return tonotify;
    }

    public void setTonotify(String tonotify) {
        this.tonotify = tonotify;
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

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUser_cat() {
        return user_cat;
    }

    public void setUser_cat(String user_cat) {
        this.user_cat = user_cat;
    }

    public String getStatut_online() {
        return statut_online;
    }

    public void setStatut_online(String statut_online) {
        this.statut_online = statut_online;
    }
}