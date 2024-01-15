package org.photography.api.dto.PhototypeDTO;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.photography.api.dto.ThemeDTO.ThemeDTO;
import org.photography.api.dto.ThemePhotoDTO.ThemePhotoDTO;

import java.util.Set;

@Getter
@Setter
public class PhotoTypeDetailDTO {

    private long id;

    private String typeName;

//    @JsonIgnore
//    @JsonBackReference
//    @JsonManagedReference
    private Set<ThemeDTO> themes;

//    @JsonIgnore
//    @JsonManagedReference
    private Set<ThemePhotoDTO> themePhotos;
}
