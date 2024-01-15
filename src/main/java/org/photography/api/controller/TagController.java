package org.photography.api.controller;

import org.photography.api.dto.PhotoLibraryDTO.PhotoLibraryWithoutTagDTO;
import org.photography.api.dto.TagDTO;
import org.photography.api.exception.EntityNotFoundException;
import org.photography.api.service.TagService;
import org.photography.api.service.ThemeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    private static final Logger logger = LoggerFactory.getLogger(ThemeService.class);

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody TagDTO tagDTO) {
        try {
            TagDTO createdTag = tagService.createTag(tagDTO);
            return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.info("Erreur lors de la création d'un tag : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/{tagId}")
    public ResponseEntity<?> getTagById(@PathVariable Long tagId) {
        try {
            TagDTO tag = tagService.getTagDTOById(tagId);
            return new ResponseEntity<>(tag, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.info("Erreur lors de la récupération d'un tag : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllTags() {
        try {
            Set<TagDTO> tags = tagService.getAllTagDTOs();
            return new ResponseEntity<>(tags, HttpStatus.OK);
        } catch (Exception e) {
            logger.info("Erreur lors de la récupération des tags : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{tagId}")
    public ResponseEntity<?> updateTag(@PathVariable Long tagId, @RequestBody TagDTO updatedTagDTO) {
        try {
            TagDTO updatedTag = tagService.updateTagDTO(tagId, updatedTagDTO);
            return new ResponseEntity<>(updatedTag, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.info("Erreur lors de la mise à jour d'un tag : ", e);
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<?> deleteTag(@PathVariable Long tagId) {
        try {
            Set<PhotoLibraryWithoutTagDTO> photoLibrariesWithoutTag = tagService.deleteTag(tagId);
            return new ResponseEntity<>(photoLibrariesWithoutTag, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.info("Erreur lors de la suppression d'un tag : ", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
