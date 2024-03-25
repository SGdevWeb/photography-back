package org.photography.api.dto.PhotoLibraryDTO;

import lombok.Getter;
import lombok.Setter;
import org.photography.api.dto.LocationDTO;
import org.photography.api.dto.TagDTO.TagCreationDTO;
import org.photography.api.dto.TagDTO.TagDTO;
import org.photography.api.dto.TagDTO.TagDetailDTO;

import java.util.Set;

@Getter
@Setter
public class PhotoLibraryDTO {

    private long id;

    private String photoUrl;

    private String photoName;

    private LocationDTO location;

    private Set<TagDTO> tags;
}
