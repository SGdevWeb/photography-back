package org.photography.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "testimonial")
public class Testimonial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String lastName;

    @Column
    private String firstName;

    @Column
    private String testimonialText;

    @Column
    private Boolean isValid;

}
