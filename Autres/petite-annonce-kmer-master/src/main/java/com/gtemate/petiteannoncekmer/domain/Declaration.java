package com.gtemate.petiteannoncekmer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Declaration.
 */
@Entity
@Table(name = "declaration")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Declaration extends BaseEntity{

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(min = 5)
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private ZonedDateTime creationDate;

    @NotNull
    @Column(name = "last_modified_date", nullable = false)
    private ZonedDateTime lastModifiedDate;

    @NotNull
    @Column(name = "is_published", nullable = false)
    private Boolean isPublished;

    @NotNull
    @Column(name = "price", precision=10, scale=2, nullable = false)
    private BigDecimal price;

    @Column(name = "published_date")
    private ZonedDateTime publishedDate;

    @ManyToOne
    private User owner;

    @ManyToOne
    @JoinColumn(name="localisation_id")
    private Localisation localisation;

    @OneToMany(mappedBy = "declaration", cascade = CascadeType.ALL)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Image> images = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER)
    private Image miniature;

    @PreUpdate
    public void updatePublishDate(){
        if(isPublished){
            publishedDate = ZonedDateTime.now();
        }
        lastModifiedDate = ZonedDateTime.now();
    }


    public String getTitle() {
        return title;
    }

    public Declaration title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Declaration description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public Declaration creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ZonedDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Declaration lastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Boolean isIsPublished() {
        return isPublished;
    }

    public Declaration isPublished(Boolean isPublished) {
        this.isPublished = isPublished;
        return this;
    }

    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Declaration price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ZonedDateTime getPublishedDate() {
        return publishedDate;
    }

    public Declaration publishedDate(ZonedDateTime publishedDate) {
        this.publishedDate = publishedDate;
        return this;
    }

    public void setPublishedDate(ZonedDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }

    public User getOwner() {
        return owner;
    }

    public Declaration owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public Localisation getLocalisation() {
        return localisation;
    }

    public Declaration localisation(Localisation localisation) {
        this.localisation = localisation;
        return this;
    }

    public void setLocalisation(Localisation localisation) {
        this.localisation = localisation;
    }

    public Set<Image> getImages() {
        return images;
    }

    public Declaration images(Set<Image> images) {
        this.images = images;
        return this;
    }

    public Declaration addImages(Image image) {
        images.add(image);
        image.setDeclaration(this);
        return this;
    }

    public Declaration removeImages(Image image) {
        images.remove(image);
        image.setDeclaration(null);
        return this;
    }

    public Boolean getPublished() {
        return isPublished;
    }

    public Image getMiniature() {
        return miniature;
    }

    public void setMiniature(Image miniature) {
        this.miniature = miniature;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Declaration{" +
            "title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", creationDate=" + creationDate +
            ", lastModifiedDate=" + lastModifiedDate +
            ", isPublished=" + isPublished +
            ", price=" + price +
            ", publishedDate=" + publishedDate +
            ", owner=" + owner +
            ", localisation=" + localisation +
            '}';
    }
}
