package org.photography.api.controller;

import org.photography.api.dto.PhotoLibraryDTO.PhotoLibraryCreationDTO;
import org.photography.api.dto.PhotoLibraryDTO.PhotoLibraryDTO;
import org.photography.api.exception.EntityNotFoundException;
import org.photography.api.service.PhotoLibraryService;
import org.photography.api.service.TestimonialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/photo-libraries")
public class PhotoLibraryController {

    private final PhotoLibraryService photoLibraryService;

    private static final Logger logger = LoggerFactory.getLogger(TestimonialService.class);

    @Autowired
    public PhotoLibraryController(PhotoLibraryService photoLibraryService) {
        this.photoLibraryService = photoLibraryService;
    }

    @PostMapping
    public ResponseEntity<?> createPhotoLibrary(@RequestBody PhotoLibraryCreationDTO photoLibraryDTO) {
        try {
            PhotoLibraryDTO createdPhotoLibrary = photoLibraryService.createPhotoLibrary(photoLibraryDTO);
            return new ResponseEntity<>(createdPhotoLibrary, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.info("Erreur lors de la création d'une photo : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{photoLibraryId}")
    public ResponseEntity<?> getPhotoLibraryById(@PathVariable Long photoLibraryId) {
        try {
            PhotoLibraryDTO photoLibrary = photoLibraryService.getPhotoLibraryById(photoLibraryId);
            return new ResponseEntity<>(photoLibrary, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.info("Erreur lors de la récupération d'une photo : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllPhotoLibraries() {
        try {
            Set<PhotoLibraryDTO> photoLibraries = photoLibraryService.getAllPhotoLibraries();
            return new ResponseEntity<>(photoLibraries, HttpStatus.OK);
        } catch (Exception e) {
            logger.info("Erreur lors de la récupération des photos : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{photoLibraryId}")
    public ResponseEntity<?> updatePhotoLibrary(@PathVariable Long photoLibraryId,
                                                @RequestBody PhotoLibraryDTO updatedPhotoLibraryDTO) {
        try {
            PhotoLibraryDTO updatedPhotoLibrary = photoLibraryService.updatePhotoLibrary(photoLibraryId, updatedPhotoLibraryDTO);
            return new ResponseEntity<>(updatedPhotoLibrary, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.info("Erreur lors de la mise à jour d'une photo : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{photoLibraryId}")
    public ResponseEntity<?> deletePhotoLibrary(@PathVariable Long photoLibraryId) {
        try {
            photoLibraryService.deletePhotoLibrary(photoLibraryId);
            return new ResponseEntity<>("PhotoLibrary successfully deleted", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.info("Erreur lors de la suppression d'une photo : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

