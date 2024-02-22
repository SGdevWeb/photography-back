package org.photography.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactFormDTO {

    private String lastName;

    private String firstName;

    private String email;

    private String subject;

    private String message;

}
