package org.photography.api.controller;

import org.photography.api.dto.LocationDTO;
import org.photography.api.exception.EntityNotFoundException;
import org.photography.api.service.LocationService;
import org.photography.api.service.TestimonialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;

    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    public ResponseEntity<?> createLocation(@RequestBody LocationDTO locationDTO) {
        try {
            LocationDTO createdLocation = locationService.createLocation(locationDTO);
            return new ResponseEntity<>(createdLocation, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.info("Erreur lors de la création d'un lieu : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<?> getLocationById(@PathVariable Long locationId) {
        try {
            LocationDTO location = locationService.getLocationById(locationId);
            return new ResponseEntity<>(location, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.info("Erreur lors de la récupération d'un lieu : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllLocations() {
        try {
            Set<LocationDTO> locations = new HashSet<>(locationService.getAllLocations());
            return new ResponseEntity<>(locations, HttpStatus.OK);
        } catch (Exception e) {
            logger.info("Erreur lors de la récupération des lieux : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<?> updateLocation(@PathVariable Long locationId, @RequestBody LocationDTO updatedLocationDTO) {
        try {
            LocationDTO updatedLocation = locationService.updateLocation(locationId, updatedLocationDTO);
            return new ResponseEntity<>(updatedLocation, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.info("Erreur lors de la mise à jour du lieu : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<?> deleteLocation(@PathVariable Long locationId) {
        try {
            locationService.deleteLocation(locationId);
            return new ResponseEntity<>("Location successfully deleted", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.info("Erreur lors de la suppression du lieux : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
