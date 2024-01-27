package org.photography.api.dto.PhotoLibraryDTO;

import lombok.Getter;
import lombok.Setter;
import org.photography.api.dto.LocationDTO;
import org.photography.api.dto.TagDTO.TagDTO;

import java.util.Set;

@Getter
@Setter
public class PhotoLibraryDTO {

    private long id;

    private String photoUrl;

    private LocationDTO location;

    private Set<TagDTO> tags;
}
