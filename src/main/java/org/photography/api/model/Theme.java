package org.photography.api.model;

import jakarta.persistence.*;
import lombok.Data;

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

}
