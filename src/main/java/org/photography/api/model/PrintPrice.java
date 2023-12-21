package org.photography.api.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "printPrice")
public class PrintPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String format;

    @Column
    private double paperPrice;

    @Column
    private double dibondPrice;

}
