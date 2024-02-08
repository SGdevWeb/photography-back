package org.photography.api.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

//@Getter
//@Setter
@Entity
@Table(name = "theme")
//@EqualsAndHashCode(exclude = {"photoTypes", "themePhotos"})
public class Theme implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int yearFrom;

    private int yearTo;

    private String themeName;

    private String descriptionText;

    private String presentationPhotoUrl;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "theme_photoType",
            joinColumns = @JoinColumn(name = "themeId"),
            inverseJoinColumns = @JoinColumn(name = "typeId")
    )
//    @JsonBackReference
//    @JsonIgnore
    private Set<PhotoType> photoTypes;

    @OneToMany(mappedBy = "theme")
    private Set<ThemePhoto> themePhotos;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getYearFrom() {
        return yearFrom;
    }

    public void setYearFrom(int yearFrom) {
        this.yearFrom = yearFrom;
    }

    public int getYearTo() {
        return yearTo;
    }

    public void setYearTo(int yearTo) {
        this.yearTo = yearTo;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    public String getPresentationPhotoUrl() {
        return presentationPhotoUrl;
    }

    public void setPresentationPhotoUrl(String presentationPhotoUrl) {
        this.presentationPhotoUrl = presentationPhotoUrl;
    }

    public Set<PhotoType> getPhotoTypes() {
        return photoTypes;
    }

    public void setPhotoTypes(Set<PhotoType> photoTypes) {
        this.photoTypes = photoTypes;
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
        Theme theme = (Theme) o;
        return id == theme.id &&
                yearFrom == theme.yearFrom &&
                yearTo == theme.yearTo &&
                Objects.equals(themeName, theme.themeName) &&
                Objects.equals(descriptionText, theme.descriptionText) &&
                Objects.equals(photoTypes, theme.photoTypes) &&
                Objects.equals(themePhotos, theme.themePhotos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, yearFrom, yearTo, themeName, descriptionText, photoTypes, themePhotos);
    }
}
