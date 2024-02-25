package org.photography.api.dto;

import jakarta.persistence.Column;
import lombok.Data;
import org.photography.api.model.Testimonial;

@Data
public class TestimonialDTO {

    private long id;

    private String lastName;

    private String firstName;

    private String email;

    private String testimonialText;

    private Boolean isValid;

    private Boolean isProcessed;

}
