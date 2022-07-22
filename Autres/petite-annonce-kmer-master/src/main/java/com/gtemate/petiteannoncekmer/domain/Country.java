package com.gtemate.petiteannoncekmer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by admin on 04/12/2016.
 */
@Entity
@Table(name = "country")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Country extends BaseEntity{

    @Size(max = 255)
    @Column(name = "name", length = 255)
    private String name;

    @OneToMany(mappedBy = "country")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Localisation> localisation = new HashSet<>();

    @OneToMany(mappedBy = "country")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Region> region = new HashSet<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Localisation> getLocalisation() {
        return localisation;
    }

    public Set<Region> getRegion() {
        return region;
    }

    public void setLocalisation(Set<Localisation> localisation) {
        this.localisation = localisation;
    }

    public void setRegion(Set<Region> region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "Country{" +
            "name='" + name + '\'' +
            '}';
    }
}
