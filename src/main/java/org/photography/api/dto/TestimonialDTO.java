package org.photography.api.dto;

import lombok.Data;

@Data
public class TestimonialDTO {

    private Long id;

    private String lastName;

    private String firstName;

    private String testimonialText;

    private Boolean isValid;

}
