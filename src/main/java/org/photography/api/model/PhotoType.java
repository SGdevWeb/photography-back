package org.photography.api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "photoType")
public class PhotoType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String typeName;

    @ManyToMany
    @JoinTable(
            name = "theme_photoType",
            joinColumns = @JoinColumn(name = "typeId"),
            inverseJoinColumns = @JoinColumn(name = "themeId")
    )
    private Set<Theme> themes;

}
