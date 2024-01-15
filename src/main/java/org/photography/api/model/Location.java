package org.photography.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String locationName;

    @OneToMany(mappedBy = "location", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<PhotoLibrary> photoLibraries;

}
