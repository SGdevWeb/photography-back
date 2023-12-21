package org.photography.api.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "testimonial")
public class Testimonial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String lastName;

    @Column
    private String firstName;

    @Column
    private String testimonialText;

    @Column
    private Boolean isValid;

}
