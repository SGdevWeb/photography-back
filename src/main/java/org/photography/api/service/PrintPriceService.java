package org.photography.api.service;

import org.photography.api.repository.PrintPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrintPriceService {

    private final PrintPriceRepository printPriceRepository;

    @Autowired
    public PrintPriceService(PrintPriceRepository printPriceRepository) {
        this.printPriceRepository = printPriceRepository;
    }

}
