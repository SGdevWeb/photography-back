package org.photography.api.dto.ThemePhotoDTO;

import lombok.Getter;
import lombok.Setter;
import org.photography.api.model.Theme;

import java.util.Set;

@Getter
@Setter
public class ThemePhotoDTO {

    private long id;

    private long typeId;

    private long themeId;

    private String typeName;

    private String photoUrl;

    private int photoPosition;
}
