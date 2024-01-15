package org.photography.api.dto;

import lombok.Data;

@Data
public class PrintPriceDTO {

    private long id;

    private String format;

    private double paperPrice;

    private double dibondPrice;

}
