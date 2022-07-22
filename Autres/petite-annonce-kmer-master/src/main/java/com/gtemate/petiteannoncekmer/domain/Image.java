package com.gtemate.petiteannoncekmer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Image.
 */
@Entity
@Table(name = "image")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Image  extends BaseEntity{

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "title")
    private String title;

    @NotNull
    @Size(min = 2, max = 10)
    @Column(name = "extention", length = 10, nullable = false)
    private String extention;

    @Column(name = "content_type")
    private String contentType;

    @NotNull
    @Lob
    @Column(name = "content", nullable = false)
    private byte[] content;

    @Column(name = "content_content_type", nullable = false)
    private String contentContentType;

    @ManyToOne
    private Declaration declaration;

    public String getFileName() {
        return fileName;
    }

    public Image fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTitle() {
        return title;
    }

    public Image title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExtention() {
        return extention;
    }

    public Image extention(String extention) {
        this.extention = extention;
        return this;
    }

    public void setExtention(String extention) {
        this.extention = extention;
    }

    public String getContentType() {
        return contentType;
    }

    public Image contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getContent() {
        return content;
    }

    public Image content(byte[] content) {
        this.content = content;
        return this;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentContentType() {
        return contentContentType;
    }

    public Image contentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
        return this;
    }

    public void setContentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
    }

    public Declaration getDeclaration() {
        return declaration;
    }

    public Image declaration(Declaration declaration) {
        this.declaration = declaration;
        return this;
    }

    public void setDeclaration(Declaration declaration) {
        this.declaration = declaration;
    }

    @Override
    public String toString() {
        return "Image{" +
            ", fileName='" + fileName + "'" +
            ", title='" + title + "'" +
            ", extention='" + extention + "'" +
            ", contentType='" + contentType + "'" +
            ", content='" + content + "'" +
            ", contentContentType='" + contentContentType + "'" +
            '}';
    }
}
