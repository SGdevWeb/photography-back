package org.photography.api.dto.PhototypeDTO;

import lombok.Getter;
import lombok.Setter;
import org.photography.api.dto.PhotoDTO;

@Getter
@Setter
public class PhotoTypeCreationDTO {

    private String typeName;

    private String photoUrl;

    private int photoPosition;

}
