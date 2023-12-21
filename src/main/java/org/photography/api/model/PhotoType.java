package org.photography.api.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "photoType")
public class PhotoType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String typeName;

}
