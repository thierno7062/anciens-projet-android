package com.gtemate.petiteannoncekmer.domain;

import java.time.ZonedDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AbstractAuditingEntity.class)
public abstract class AbstractAuditingEntity_ extends com.gtemate.petiteannoncekmer.domain.BaseEntity_ {

	public static volatile SingularAttribute<AbstractAuditingEntity, ZonedDateTime> createdDate;
	public static volatile SingularAttribute<AbstractAuditingEntity, String> createdBy;
	public static volatile SingularAttribute<AbstractAuditingEntity, ZonedDateTime> lastModifiedDate;
	public static volatile SingularAttribute<AbstractAuditingEntity, String> lastModifiedBy;

}

