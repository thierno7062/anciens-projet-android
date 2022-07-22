package com.gtemate.petiteannoncekmer.domain;

import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ extends com.gtemate.petiteannoncekmer.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<User, String> lastName;
	public static volatile SingularAttribute<User, ZonedDateTime> resetDate;
	public static volatile SetAttribute<User, Localisation> localisation;
	public static volatile SetAttribute<User, PersistentToken> persistentTokens;
	public static volatile SingularAttribute<User, String> login;
	public static volatile SingularAttribute<User, String> activationKey;
	public static volatile SingularAttribute<User, String> resetKey;
	public static volatile SetAttribute<User, Declaration> declaration;
	public static volatile SetAttribute<User, Authority> authorities;
	public static volatile SingularAttribute<User, String> firstName;
	public static volatile SingularAttribute<User, String> password;
	public static volatile SingularAttribute<User, String> phoneNumber;
	public static volatile SingularAttribute<User, String> langKey;
	public static volatile SingularAttribute<User, String> imageUrl;
	public static volatile SingularAttribute<User, Long> id;
	public static volatile SingularAttribute<User, String> email;
	public static volatile SingularAttribute<User, Boolean> activated;

}

