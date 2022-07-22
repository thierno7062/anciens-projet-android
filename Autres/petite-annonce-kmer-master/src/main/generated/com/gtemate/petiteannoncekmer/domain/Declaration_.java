package com.gtemate.petiteannoncekmer.domain;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Declaration.class)
public abstract class Declaration_ extends com.gtemate.petiteannoncekmer.domain.BaseEntity_ {

	public static volatile SingularAttribute<Declaration, User> owner;
	public static volatile SetAttribute<Declaration, Image> images;
	public static volatile SingularAttribute<Declaration, ZonedDateTime> lastModifiedDate;
	public static volatile SingularAttribute<Declaration, Boolean> isPublished;
	public static volatile SingularAttribute<Declaration, BigDecimal> price;
	public static volatile SingularAttribute<Declaration, Localisation> localisation;
	public static volatile SingularAttribute<Declaration, Image> miniature;
	public static volatile SingularAttribute<Declaration, String> description;
	public static volatile SingularAttribute<Declaration, ZonedDateTime> publishedDate;
	public static volatile SingularAttribute<Declaration, String> title;
	public static volatile SingularAttribute<Declaration, ZonedDateTime> creationDate;

}

