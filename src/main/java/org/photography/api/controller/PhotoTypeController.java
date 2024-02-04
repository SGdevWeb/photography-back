package org.photography.api.controller;

import org.photography.api.dto.PhotoDTO;
import org.photography.api.dto.PhototypeDTO.PhotoTypeCreationDTO;
import org.photography.api.dto.PhototypeDTO.PhotoTypeDTO;
import org.photography.api.dto.PhototypeDTO.PhotoTypeDetailDTO;
import org.photography.api.dto.PhototypeDTO.PhotoTypeUpdateDTO;
import org.photography.api.dto.ThemeDTO.ThemeDTO;
import org.photography.api.exception.AlreadyExists;
import org.photography.api.exception.EntityNotFoundException;
import org.photography.api.service.PhotoTypeService;
import org.photography.api.service.ThemeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/photo-types")
public class PhotoTypeController {

    private final PhotoTypeService photoTypeService;

    private static final Logger logger = LoggerFactory.getLogger(ThemeService.class);

    @Autowired
    public PhotoTypeController(PhotoTypeService photoTypeService) {
        this.photoTypeService = photoTypeService;
    }

    @PostMapping("/{themeId}")
    public ResponseEntity<?> createPhotoType(@RequestBody PhotoTypeCreationDTO photoTypeCreationDTO, @PathVariable Long themeId){
        try {
            PhotoTypeDetailDTO photoType = photoTypeService.createPhotoType(photoTypeCreationDTO, themeId);
            return new ResponseEntity<>(photoType, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.info("Erreur lors de la récupération d'un type de photo : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{photoTypeId}")
    public ResponseEntity<?> getPhotoTypeById(@PathVariable Long photoTypeId) {
        try {
            PhotoTypeDetailDTO photoType = photoTypeService.getPhotoTypeById(photoTypeId);
            return new ResponseEntity<>(photoType, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.info("Erreur lors de la récupération d'un type de photo : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllPhotoTypes() {
        try {
            Set<PhotoTypeDetailDTO> photoTypes = new HashSet<>(photoTypeService.getAllPhotoTypes());
            return new ResponseEntity<>(photoTypes, HttpStatus.OK);
        } catch (Exception e) {
            logger.info("Erreur lors de la récupération des types de photo : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{photoTypeId}")
    public ResponseEntity<?> updatePhotoTypeName(@PathVariable Long photoTypeId, @RequestBody PhotoTypeUpdateDTO updatedPhotoTypeDTO) {
        try {
            PhotoTypeDTO updatedPhotoType = photoTypeService.updatePhotoTypeName(photoTypeId, updatedPhotoTypeDTO);
            return new ResponseEntity<>(updatedPhotoType, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.info("Erreur lors de la mise à jour d'un type de photo : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{photoTypeId}")
    public ResponseEntity<?> deletePhotoType(@PathVariable Long photoTypeId, @RequestParam Long themeId) {
        try {
            photoTypeService.deletePhotoType(photoTypeId, themeId);
            return new ResponseEntity<>("PhotoType successfully deleted", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.info("Erreur lors de la suppression d'un type de photo : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
