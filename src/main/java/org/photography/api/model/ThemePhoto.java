package org.photography.api.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "themePhoto")
public class ThemePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "themeId")
    private Theme theme;

    @ManyToOne
    @JoinColumn(name = "typeId")
    private PhotoType photoType;

    @Column
    private String photoUrl;

}
