package org.photography.api.dto.TagDTO;

import lombok.Getter;
import lombok.Setter;
import org.photography.api.dto.PhotoLibraryDTO.PhotoLibraryDTO;

import java.util.Set;

@Getter
@Setter
public class TagDetailDTO {

    private long id;

    private String tagName;

    private Set<PhotoLibraryDTO> photoLibraries;

}
