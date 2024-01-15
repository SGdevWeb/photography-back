package org.photography.api.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

//@Getter
//@Setter
@Entity
@Table(name = "photoType")
//@EqualsAndHashCode(exclude = {"themes", "themePhotos"})
public class PhotoType implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String typeName;

    @ManyToMany(mappedBy = "photoTypes", fetch = FetchType.LAZY)
//    @JsonManagedReference
//    @JsonIgnore
    private Set<Theme> themes;

    @OneToMany(mappedBy = "photoType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonManagedReference
//    @JsonIgnore
    private  Set<ThemePhoto> themePhotos;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Set<Theme> getThemes() {
        return themes;
    }

    public void setThemes(Set<Theme> themes) {
        this.themes = themes;
    }

    public Set<ThemePhoto> getThemePhotos() {
        return themePhotos;
    }

    public void setThemePhotos(Set<ThemePhoto> themePhotos) {
        this.themePhotos = themePhotos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhotoType photoType = (PhotoType) o;
        return id == photoType.id &&
                Objects.equals(typeName, photoType.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, typeName);
    }
}
