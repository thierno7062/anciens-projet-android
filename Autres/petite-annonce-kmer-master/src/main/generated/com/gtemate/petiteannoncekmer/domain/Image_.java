package com.gtemate.petiteannoncekmer.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Image.class)
public abstract class Image_ extends com.gtemate.petiteannoncekmer.domain.BaseEntity_ {

	public static volatile SingularAttribute<Image, String> fileName;
	public static volatile SingularAttribute<Image, String> extention;
	public static volatile SingularAttribute<Image, String> contentContentType;
	public static volatile SingularAttribute<Image, String> title;
	public static volatile SingularAttribute<Image, String> contentType;
	public static volatile SingularAttribute<Image, Declaration> declaration;
	public static volatile SingularAttribute<Image, byte[]> content;

}

