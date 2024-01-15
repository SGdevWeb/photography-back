package org.photography.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "theme_photoType")
public class ThemePhotoPhotoType {

    @Id
    @ManyToOne
    @JoinColumn(name = "themeId")
    private Theme theme;

    @Id
    @ManyToOne
    @JoinColumn(name = "typeId")
    private PhotoType photoType;

}
