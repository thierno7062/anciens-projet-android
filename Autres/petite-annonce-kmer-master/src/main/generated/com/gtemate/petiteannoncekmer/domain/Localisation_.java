package com.gtemate.petiteannoncekmer.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Localisation.class)
public abstract class Localisation_ extends com.gtemate.petiteannoncekmer.domain.BaseEntity_ {

	public static volatile SingularAttribute<Localisation, String> area;
	public static volatile SingularAttribute<Localisation, Country> country;
	public static volatile SingularAttribute<Localisation, String> streetName;
	public static volatile SingularAttribute<Localisation, String> specialAdress;
	public static volatile SingularAttribute<Localisation, String> city;
	public static volatile SingularAttribute<Localisation, String> streetNumber;
	public static volatile SingularAttribute<Localisation, String> postalCode;
	public static volatile SingularAttribute<Localisation, String> village;
	public static volatile SingularAttribute<Localisation, Region> region;
	public static volatile SingularAttribute<Localisation, User> user;
	public static volatile SetAttribute<Localisation, Declaration> declaration;

}

