package com.gtemate.petiteannoncekmer.domain;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PersistentAuditEvent.class)
public abstract class PersistentAuditEvent_ {

	public static volatile SingularAttribute<PersistentAuditEvent, String> principal;
	public static volatile SingularAttribute<PersistentAuditEvent, LocalDateTime> auditEventDate;
	public static volatile MapAttribute<PersistentAuditEvent, String, String> data;
	public static volatile SingularAttribute<PersistentAuditEvent, Long> id;
	public static volatile SingularAttribute<PersistentAuditEvent, String> auditEventType;

}

