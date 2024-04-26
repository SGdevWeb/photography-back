package org.photography.api.controller;

import org.photography.api.dto.PhotoDTO;
import org.photography.api.dto.ThemeDTO.ThemeCreationDTO;
import org.photography.api.dto.ThemeDTO.ThemeDTO;
import org.photography.api.dto.ThemeDTO.ThemeDetailDTO;
import org.photography.api.dto.ThemeDTO.ThemeUpdateDTO;
import org.photography.api.dto.ThemePhotoDTO.ThemePhotoDTO;
import org.photography.api.exception.AlreadyExists;
import org.photography.api.exception.EntityNotFoundException;
import org.photography.api.exception.NonUniquePhotoUrlException;
import org.photography.api.model.Theme;
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
@RequestMapping("/api/themes")
public class ThemeController {

    private final ThemeService themeService;

    private static final Logger logger = LoggerFactory.getLogger(ThemeService.class);

    @Autowired
    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<?> createTheme(@RequestBody ThemeCreationDTO themeCreationDTO) {
        try {
            ThemeDetailDTO createdTheme = themeService.createTheme(themeCreationDTO);
            return new ResponseEntity<>(createdTheme, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (AlreadyExists | NonUniquePhotoUrlException | IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            logger.error("Erreur lors de la création du thème : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{themeId}")
    public ResponseEntity<?> getThemeById(@PathVariable Long themeId) {
        try {
            ThemeDetailDTO theme = themeService.getThemeById(themeId);
            System.out.println(theme.toString());
            return new ResponseEntity<>(theme, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du thème : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllThemes() {
        try {
            Set<ThemeDetailDTO> themes = themeService.getAllThemes();
            return new ResponseEntity<>(themes, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la création des thèmes : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{themeId}")
    public ResponseEntity<?> updateTheme(@PathVariable Long themeId, @RequestBody ThemeUpdateDTO themeUpdateDTO){
        try {
            ThemeDetailDTO updatedTheme = themeService.updateTheme(themeId, themeUpdateDTO);
            return new ResponseEntity<>(updatedTheme, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{themeId}/presentation-photo")
    public ResponseEntity<?> updatePresentationPhoto(@PathVariable Long themeId, @RequestParam("photo") MultipartFile photo,
                                              @RequestParam("contentType") String contentType) {
        try {
            PhotoDTO photoDTO = new PhotoDTO();
            photoDTO.setPhoto(photo);
            photoDTO.setContentType(contentType);
            ThemeDetailDTO updatedTheme = themeService.updatePresentationPhoto(themeId, photoDTO);
            return new ResponseEntity<>(updatedTheme, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la photo du thème : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{themeId}")
    public ResponseEntity<?> deleteTheme(@PathVariable Long themeId) {
        try {
            themeService.deleteTheme(themeId);
            return new ResponseEntity<>("Theme successfully deleted", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.info("Erreur lors de la suppression du thème : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

