package org.photography.api.dto.ThemeDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ThemeUpdateDTO {
    private int id;

    private Integer yearFrom;

    private Integer yearTo;

    private String themeName;

    private String descriptionText;

    private String presentationPhotoUrl;
}
