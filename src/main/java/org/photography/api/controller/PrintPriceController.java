package org.photography.api.controller;

import org.photography.api.dto.PrintPriceDTO;
import org.photography.api.exception.EntityNotFoundException;
import org.photography.api.service.PrintPriceService;
import org.photography.api.service.ThemeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/print-prices")
public class PrintPriceController {

    private final PrintPriceService printPriceService;

    private static final Logger logger = LoggerFactory.getLogger(PrintPriceService.class);

    @Autowired
    public PrintPriceController(PrintPriceService printPriceService) {
        this.printPriceService = printPriceService;
    }

    @PostMapping
    public ResponseEntity<?> createPrintPrice(@RequestBody PrintPriceDTO printPriceDTO) {
        try {
            PrintPriceDTO createdPrintPrice = printPriceService.createPrintPrice(printPriceDTO);
            return new ResponseEntity<>(createdPrintPrice, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.info("Erreur lors de la création d'un tarif d'impression : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{printPriceId}")
    public ResponseEntity<?> getPrintPriceById(@PathVariable Long printPriceId) {
        try {
            PrintPriceDTO printPrice = printPriceService.getPrintPriceById(printPriceId);
            return new ResponseEntity<>(printPrice, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.info("Erreur lors de la récupération d'un tarif d'impression : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllPrintPrices() {
        try {
            Set<PrintPriceDTO> PrintPrices = new HashSet<>(printPriceService.getAllPrintPrices());
            return new ResponseEntity<>(PrintPrices, HttpStatus.OK);
        } catch (Exception e) {
            logger.info("Erreur lors de la récupération des tarifs d'impression : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{printPriceId}")
    public ResponseEntity<?> updatePrintPrice(@PathVariable Long printPriceId, @RequestBody PrintPriceDTO updatedPrintPriceDTO) {
        try {
            PrintPriceDTO updatedPrintPrice = printPriceService.updatePrintPrice(printPriceId, updatedPrintPriceDTO);
            return new ResponseEntity<>(updatedPrintPrice, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.info("Erreur lors de la mise à jour d'un tarif d'impression : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{printPriceId}")
    public ResponseEntity<?> deletePrintPrice(@PathVariable Long printPriceId) {
        try {
            printPriceService.deletePrintPrice(printPriceId);
            return new ResponseEntity<>("PrintPrice successfully deleted", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.info("Erreur lors de la suppression d'un tarif d'impression : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
