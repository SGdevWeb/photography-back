package org.photography.api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "theme")
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int yearFrom;
    private int yearTo;
    private String themeName;

    @ManyToOne
    @JoinColumn(name = "descriptionId")
    private ThemeDescription description;

    @ManyToMany
    @JoinTable(
            name = "theme_photoType",
            joinColumns = @JoinColumn(name = "themeId"),
            inverseJoinColumns = @JoinColumn(name = "typeId")
    )
    private Set<PhotoType> photoTypes;

}
