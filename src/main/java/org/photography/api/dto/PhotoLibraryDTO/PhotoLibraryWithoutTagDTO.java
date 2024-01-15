package org.photography.api.dto.PhotoLibraryDTO;

import lombok.Getter;
import lombok.Setter;
import org.photography.api.dto.LocationDTO;

@Getter
@Setter
public class PhotoLibraryWithoutTagDTO {

    private long id;

    private String photoUrl;

    private LocationDTO location;

}
