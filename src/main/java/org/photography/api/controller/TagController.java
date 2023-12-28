package org.photography.api.controller;

import org.photography.api.dto.TagDTO;
import org.photography.api.exception.EntityNotFoundException;
import org.photography.api.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

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
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllTags() {
        try {
            List<TagDTO> tags = tagService.getAllTagDTOs();
            return new ResponseEntity<>(tags, HttpStatus.OK);
        } catch (Exception e) {
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
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<?> deleteTag(@PathVariable Long tagId) {
        try {
            tagService.deleteTag(tagId);
            return new ResponseEntity<>("Tag successfully deleted", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
