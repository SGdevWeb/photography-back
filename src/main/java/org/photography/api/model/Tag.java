package org.photography.api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String tagName;

    @ManyToMany
    @JoinTable(
            name = "photoLibrary_tag",
            joinColumns = @JoinColumn(name = "tagId"),
            inverseJoinColumns = @JoinColumn(name = "photoId")
    )
    private Set<PhotoLibrary> photoLibrarySet;

    public Tag() {}

    public Tag(String tagName) {
        this.tagName = tagName;
    }
}
