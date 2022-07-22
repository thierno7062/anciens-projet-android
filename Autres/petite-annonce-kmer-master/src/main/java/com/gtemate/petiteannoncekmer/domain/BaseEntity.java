package com.gtemate.petiteannoncekmer.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by admin on 04/12/2016.
 */

@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    @Column(columnDefinition = "int default 0")
    private int version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseEntity entity = (BaseEntity) o;
        if (entity.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        if (id == null) {
            return super.toString();
        } else {
            return getClass().getName() + " [id=" + id + "]";
        }
    }

}
