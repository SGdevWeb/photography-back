package org.photography.api.dto.TagDTO;

import org.photography.api.dto.PhotoLibraryDTO.PhotoLibraryDTO;

import java.util.Set;

public class TagDetailDTO {

    private long id;

    private String tagName;

    private Set<PhotoLibraryDTO> photoLibraries;

}
