package org.photography.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

//@Getter
//@Setter
@Entity
@Table(name = "location")
public class Location  implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String locationName;

    @OneToMany(mappedBy = "location")
    private Set<PhotoLibrary> photoLibraries;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
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
        Location location = (Location) o;
        return id == location.id && Objects.equals(locationName, location.locationName) && Objects.equals(photoLibraries, location.photoLibraries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, locationName, photoLibraries);
    }
}
