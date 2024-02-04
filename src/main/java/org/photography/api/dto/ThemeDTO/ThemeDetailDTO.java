package org.photography.api.dto.ThemeDTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.photography.api.dto.PhototypeDTO.PhotoTypeDetailDTO;
import org.photography.api.dto.ThemePhotoDTO.ThemePhotoDTO;

import java.util.Set;

@Getter
@Setter
public class ThemeDetailDTO {

    private long id;

    private int yearFrom;

    private int yearTo;

    private String themeName;

    private String descriptionText;

    //    @JsonIgnore
//    @JsonManagedReference
//    @JsonBackReference
    private Set<PhotoTypeDetailDTO> photoTypes;

    //    @JsonIgnore
//    @JsonManagedReference
    private Set<ThemePhotoDTO> themePhotos;

}
