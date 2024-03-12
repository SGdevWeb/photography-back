package org.photography.api.dto.PhotoLibraryDTO;

import lombok.Getter;
import lombok.Setter;
import org.photography.api.dto.TagDTO.TagCreationDTO;

import java.util.Set;

@Getter
@Setter
public class PhotoLibraryCreationDTO {

    private long id;

    private String photoUrl;

    private String location;

    private Set<TagCreationDTO> tags;

}
