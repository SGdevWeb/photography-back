package org.photography.api.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

//@Getter
//@Setter
@Entity
@Table(name = "themePhoto")
public class ThemePhoto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "typeId")
//    @JsonBackReference
//    @JsonIgnore
    private PhotoType photoType;

    @ManyToOne
    @JoinColumn(name = "themeId")
    private Theme theme;

    @Column(nullable = false)
    private String photoUrl;

    @Column(nullable = false)
    private int photoPosition;

    @Column
    private LocalDate createdAt;

    public ThemePhoto() {}

    public ThemePhoto(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PhotoType getPhotoType() {
        return photoType;
    }

    public void setPhotoType(PhotoType photoType) {
        this.photoType = photoType;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThemePhoto that = (ThemePhoto) o;
        return id == that.id &&
                Objects.equals(photoType, that.photoType) &&
                Objects.equals(photoUrl, that.photoUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, photoType, photoUrl);
    }
}
