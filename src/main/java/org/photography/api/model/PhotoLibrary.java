package org.photography.api.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "photoLibrary")
public class PhotoLibrary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "locationId")
    private Location location;

}
