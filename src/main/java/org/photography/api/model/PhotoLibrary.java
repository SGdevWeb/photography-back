package org.photography.api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "photoLibrary")
public class PhotoLibrary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "locationId")
    private Location location;

    @ManyToMany
    @JoinTable(
            name = "photoLibrary_tag",
            joinColumns = @JoinColumn(name = "photoId"),
            inverseJoinColumns = @JoinColumn(name = "tagId")
    )
    private Set<Tag> tags;

}
