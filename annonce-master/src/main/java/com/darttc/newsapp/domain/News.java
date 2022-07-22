package com.darttc.newsapp.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.darttc.newsapp.domain.util.CustomDateTimeDeserializer;
import com.darttc.newsapp.domain.util.CustomDateTimeSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A News.
 */
@Entity
@Table(name = "T_NEWS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class News implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "subject")
    private String subject;

    @Column(name = "description")
    private String description;

    @Column(name = "author")
    private String author;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "creation_date", nullable = false)
    private DateTime creationDate;

    @Column(name = "image")
    private String image;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        News news = (News) o;

        if (id != null ? !id.equals(news.id) : news.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", subject='" + subject + "'" +
                ", description='" + description + "'" +
                ", author='" + author + "'" +
                ", creationDate='" + creationDate + "'" +
                ", image='" + image + "'" +
                '}';
    }
}
