package org.photography.api.controller;

import org.photography.api.dto.PhotoDTO;
import org.photography.api.dto.PhotoLibraryDTO.PhotoLibraryCreationDTO;
import org.photography.api.dto.PhotoLibraryDTO.PhotoLibraryDTO;
import org.photography.api.dto.TagDTO.TagCreationDTO;
import org.photography.api.exception.EntityNotFoundException;
import org.photography.api.model.PhotoLibrary;
import org.photography.api.service.PhotoLibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/photo-libraries")
public class PhotoLibraryController {

    private final PhotoLibraryService photoLibraryService;

    private static final Logger logger = LoggerFactory.getLogger(PhotoLibraryController.class);

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
    public ResponseEntity<?> getAllPhotoLibraries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Set<String> tags,
            @RequestParam(required = false) Set<String> locations
    ) {
        try {
            List<PhotoLibraryDTO> photoLibraries = photoLibraryService.getAllPhotoLibraries(page, size, tags, locations).getContent();
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

    @DeleteMapping("/{photoLibraryId}/location")
    public ResponseEntity<?> deleteLocation(@PathVariable Long photoLibraryId) {
        try {
            photoLibraryService.deleteLocation(photoLibraryId);
            return new ResponseEntity<>("Lieu supprimée avec succés", HttpStatus.OK);
        } catch (Exception e) {
            logger.info("Erreur lors de la suppression du lieu de la photo : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{photoLibraryId}/tags/{tagId}")
    public ResponseEntity<?> removeTag(@PathVariable Long photoLibraryId, @PathVariable Long tagId) {
        try {
            photoLibraryService.removeTag(photoLibraryId, tagId);
            return new ResponseEntity<>("Catégorie supprimée avec succès", HttpStatus.OK);
        } catch (Exception e) {
            logger.info("Erreur lors de la suppression de la catégorie de la photo : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{photoLibraryId}/addTag")
    public ResponseEntity<?> addTagToPhotoLibrary(@PathVariable Long photoLibraryId, @RequestBody TagCreationDTO tagCreationDTO) {
        try {
            PhotoLibrary updatedPhotoLibrary = photoLibraryService.addTagToPhotoLibrary(photoLibraryId, tagCreationDTO);
            return new ResponseEntity<>(updatedPhotoLibrary, HttpStatus.OK);
        } catch (Exception e) {
            logger.info("Erreur lors de l'ajout du tag à la photo : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{photoLibraryId}/updatePhoto")
    public ResponseEntity<?> updatePhotoInPhotoLibrary(
            @PathVariable Long photoLibraryId,
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("contentType") String contentType
    ) {
        try {
            System.out.println("entrée photoLibraryController");
            PhotoDTO photoDTO = new PhotoDTO();
            photoDTO.setPhoto(photo);
            photoDTO.setContentType(contentType);
            PhotoLibrary updatedPhotoLibrary = photoLibraryService.updatePhotoInPhotoLibrary(photoLibraryId, photoDTO);
            return new ResponseEntity<>(updatedPhotoLibrary, HttpStatus.OK);
        } catch (Exception e) {
            logger.info("Erreur lors de l'ajout du tag à la photo : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

