package org.photography.api.dto.ThemeDTO;

import lombok.Getter;
import lombok.Setter;
import org.photography.api.dto.PhototypeDTO.PhotoTypeCreationDTO;
import org.photography.api.dto.ThemeDescriptionDTO.ThemeDescriptionDTO;
import org.photography.api.dto.ThemePhotoDTO.ThemePhotoDTO;

import java.util.Set;

@Getter
@Setter
public class ThemeCreationDTO {

    private int yearFrom;

    private int yearTo;

    private String themeName;

    private ThemeDescriptionDTO description;

    private Set<PhotoTypeCreationDTO> photoTypes;

}
