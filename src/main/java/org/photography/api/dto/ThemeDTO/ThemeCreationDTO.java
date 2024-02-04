package org.photography.api.dto.ThemeDTO;

import lombok.Getter;
import lombok.Setter;
import org.photography.api.dto.PhototypeDTO.PhotoTypeCreationDTO;

import java.util.Set;

@Getter
@Setter
public class ThemeCreationDTO {

    private int yearFrom;

    private int yearTo;

    private String themeName;

    private String descriptionText;

    private Set<PhotoTypeCreationDTO> photoTypes;

}
