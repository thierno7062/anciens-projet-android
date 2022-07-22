package com.projets.heven.taxijaune.model;

/**
 * Created by Woumtana on 01/12/2016.
 */

public class ConducteurDispoPojo {
    private int id;
    private int conducteur_id;
    private String conducteur_name;
    private String immatriculation;
    private String numero;

    public ConducteurDispoPojo() {

    }

    public ConducteurDispoPojo(int id, int conducteur_id, String conducteur_name, String immatriculation, String numero) {
        this.id = id;
        this.conducteur_id = conducteur_id;
        this.conducteur_name = conducteur_name;
        this.immatriculation = immatriculation;
        this.numero = numero;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}