package com.gtemate.petiteannoncekmer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Localisation.
 */
@Entity
@Table(name = "localisation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Localisation extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "village")
    private String village;

    @Column(name = "area")
    private String area;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "street_number")
    private String streetNumber;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "special_adress")
    private String specialAdress;

    @ManyToOne
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    private Country country;

    @ManyToOne(fetch = FetchType.EAGER)
    private Region region;

    @OneToMany(mappedBy = "localisation")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Declaration> declaration = new HashSet<>();


    public Country getCountry() {
        return country;
    }

    public Localisation country(Country country) {
        this.country = country;
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public Localisation city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getVillage() {
        return village;
    }

    public Localisation village(String village) {
        this.village = village;
        return this;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getArea() {
        return area;
    }

    public Localisation area(String area) {
        this.area = area;
        return this;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStreetName() {
        return streetName;
    }

    public Localisation streetName(String streetName) {
        this.streetName = streetName;
        return this;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public Localisation streetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
        return this;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public Localisation postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getSpecialAdress() {
        return specialAdress;
    }

    public Localisation specialAdress(String specialAdress) {
        this.specialAdress = specialAdress;
        return this;
    }

    public void setSpecialAdress(String specialAdress) {
        this.specialAdress = specialAdress;
    }

    public User getUser() {
        return user;
    }

    public Localisation user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Set<Declaration> getDeclaration() {
        return declaration;
    }

    public void setDeclaration(Set<Declaration> declaration) {
        this.declaration = declaration;
    }

    @Override
    public String toString() {
        return "Localisation{" +
            "city='" + city + '\'' +
            ", village='" + village + '\'' +
            ", area='" + area + '\'' +
            ", streetName='" + streetName + '\'' +
            ", streetNumber='" + streetNumber + '\'' +
            ", postalCode='" + postalCode + '\'' +
            ", specialAdress='" + specialAdress + '\'' +
            ", user=" + user +
            ", country=" + country +
            ", region=" + region +
            '}';
    }
}
