package org.photography.api.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PhotoDTO {

    private MultipartFile photo;

    private String contentType;

}
