package org.photography.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "printPrice")
public class PrintPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String format;

    @Column
    private double paperPrice;

    @Column
    private double dibondPrice;

}
