package org.photography.api.controller;

import org.photography.api.dto.ThemeDescriptionDTO.ThemeDescriptionDTO;
import org.photography.api.exception.AlreadyExists;
import org.photography.api.exception.EntityNotFoundException;
import org.photography.api.model.Theme;
import org.photography.api.service.ThemeDescriptionService;
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
@RequestMapping("/api/theme-descriptions")
public class ThemeDescriptionController {

    private final ThemeDescriptionService themeDescriptionService;

    private static final Logger logger = LoggerFactory.getLogger(ThemeService.class);

    @Autowired
    public ThemeDescriptionController(ThemeDescriptionService themeDescriptionService) {
        this.themeDescriptionService = themeDescriptionService;
    }

    @PostMapping
    public ResponseEntity<?> createThemeDescription(@RequestBody ThemeDescriptionDTO themeDescriptionDTO) {
        try {
            ThemeDescriptionDTO createdThemeDescription = themeDescriptionService.createThemeDescription(themeDescriptionDTO);
            return new ResponseEntity<>(createdThemeDescription, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }catch (AlreadyExists e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            logger.error("Erreur lors de la création de la description : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{themeDescriptionId}")
    public ResponseEntity<?> getThemeDescriptionById(@PathVariable Long themeDescriptionId) {
        try {
            ThemeDescriptionDTO themeDescription = themeDescriptionService.getThemeDescriptionById(themeDescriptionId);
            return new ResponseEntity<>(themeDescription, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la description : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllDescriptionThemes() {
        try {
            Set<ThemeDescriptionDTO> themes = new HashSet<>(themeDescriptionService.getAllThemeDescriptions());
            return new ResponseEntity<>(themes, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des descriptions : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{themeDescriptionId}")
    public ResponseEntity<?> updateThemeDescription(@PathVariable Long themeDescriptionId,
                                                    @RequestBody ThemeDescriptionDTO updatedThemeDescriptionDTO) {
        try {
            ThemeDescriptionDTO updatedThemeDescription = themeDescriptionService
                    .updateThemeDescription(themeDescriptionId, updatedThemeDescriptionDTO);
            return new ResponseEntity<>(updatedThemeDescription, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de la description : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{themeDescriptionId}")
    public ResponseEntity<?> deleteThemeDescription(@PathVariable Long id) {
        try {
            Set<Theme> themesWithoutDescription = themeDescriptionService.deleteThemeDescription(id);
            return new ResponseEntity<>(themesWithoutDescription, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de la description : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

