package com.gtemate.petiteannoncekmer.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Country.class)
public abstract class Country_ extends com.gtemate.petiteannoncekmer.domain.BaseEntity_ {

	public static volatile SetAttribute<Country, Localisation> localisation;
	public static volatile SingularAttribute<Country, String> name;
	public static volatile SetAttribute<Country, Region> region;

}

