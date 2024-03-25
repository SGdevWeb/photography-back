package org.photography.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

//@Getter
//@Setter
@Entity
@Table(name = "tag")
public class Tag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "tagName")
    private String tagName;

    @JsonBackReference
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<PhotoLibrary> photoLibraries;

    public Tag() {}

    public Tag(String tagName) {
        this.tagName = tagName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Set<PhotoLibrary> getPhotoLibraries() {
        return photoLibraries;
    }

    public void setPhotoLibraries(Set<PhotoLibrary> photoLibraries) {
        this.photoLibraries = photoLibraries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return id == tag.id && Objects.equals(tagName, tag.tagName) && Objects.equals(photoLibraries, tag.photoLibraries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tagName);
    }
}
