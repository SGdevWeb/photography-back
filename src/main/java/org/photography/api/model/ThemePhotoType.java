package org.photography.api.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "theme_photoType")
public class ThemePhotoType {

    @Id
    @ManyToOne
    @JoinColumn(name = "themeId")
    private Theme theme;

    @Id
    @ManyToOne
    @JoinColumn(name = "typeId")
    private PhotoType photoType;

}
