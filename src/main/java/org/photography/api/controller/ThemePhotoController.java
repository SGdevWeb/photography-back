package org.photography.api.controller;

import org.photography.api.dto.PhotoDTO;
import org.photography.api.dto.ThemePhotoDTO.ThemePhotoDTO;
import org.photography.api.dto.ThemePhotoDTO.ThemePhotoUpdateDTO;
import org.photography.api.exception.EntityNotFoundException;
import org.photography.api.service.ThemePhotoService;
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
@RequestMapping("/api/theme-photos")
public class ThemePhotoController {

    private final ThemePhotoService themePhotoService;

    private static final Logger logger = LoggerFactory.getLogger(ThemeService.class);

    @Autowired
    public ThemePhotoController(ThemePhotoService themePhotoService) {
        this.themePhotoService = themePhotoService;
    }

    @GetMapping("/{themePhotoId}")
    public ResponseEntity<?> getThemePhotoById(@PathVariable Long themePhotoId) {
        try {
            ThemePhotoDTO themePhoto = themePhotoService.getThemePhotoById(themePhotoId);
            return new ResponseEntity<>(themePhoto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Erreur lors de la création de la photo d'un thème : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllThemePhotos() {
        try {
            Set<ThemePhotoDTO> themePhotos = new HashSet<>(themePhotoService.getAllThemePhotos());
            return new ResponseEntity<>(themePhotos, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la photo d'un thème : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{themePhotoId}")
    public ResponseEntity<?> updateThemePhoto(@PathVariable Long themePhotoId, @RequestParam("photo") MultipartFile photo,
                                              @RequestParam("contentType") String contentType) {
        try {
            PhotoDTO photoDTO = new PhotoDTO();
            photoDTO.setPhoto(photo);
            photoDTO.setContentType(contentType);
            ThemePhotoDTO updatedThemePhoto = themePhotoService.updateThemePhotoUrl(themePhotoId, photoDTO);
            return new ResponseEntity<>(updatedThemePhoto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des photos d'un thème : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{themePhotoId}")
    public ResponseEntity<?> deleteThemePhoto(@PathVariable Long themePhotoId) {
        try {
            themePhotoService.deleteThemePhoto(themePhotoId);
            return new ResponseEntity<>("ThemePhoto successfully deleted", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de la photo d'un thème : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

