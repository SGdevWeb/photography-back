package org.photography.api.dto.ThemeDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ThemeDTO {
    private int id;

    private int yearFrom;

    private int yearTo;

    private String themeName;

    private String descriptionText;
}