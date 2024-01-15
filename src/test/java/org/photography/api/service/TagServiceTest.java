package org.photography.api.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.photography.api.exception.EntityNotFoundException;
import org.photography.api.model.Tag;
import org.photography.api.repository.TagRepository;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@SpringBootTest
public class TagServiceTest {

    @InjectMocks
    private TagService tagService;

    @Mock
    private TagRepository tagRepository;

    @Test
    void createTagTest() {
        Tag tagToCreate = new Tag();
        tagToCreate.setTagName("Test Tag");

        Mockito.when(tagRepository.save(Mockito.any(Tag.class))).thenReturn(tagToCreate);

        Tag createdTag = tagService.createTag(tagToCreate);

        Assertions.assertNotNull(createdTag);
        Assertions.assertEquals("Test Tag", createdTag.getTagName());
        Mockito.verify(tagRepository, Mockito.times(1)).save(tagToCreate);
    }

    @Test
    void createTag_shouldThrowIllegalArgumentException_whenTagNameIsNullOrEmpty() {
        // Arrange
        Tag tagToCreate = new Tag();
        tagToCreate.setTagName("");

        // Act & Assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> tagService.createTag(tagToCreate));

        Assertions.assertEquals("Name cannot be null or empty", exception.getMessage());

        // Verify
        Mockito.verify(tagRepository, Mockito.never()).save(Mockito.any(Tag.class));
    }

    @Test
    void getTagByIdTest() {
        Tag expectedTag = new Tag();
        expectedTag.setId(1);
        expectedTag.setTagName("Test Tag");

        Mockito.when(tagRepository.findById(1L)).thenReturn(Optional.of(expectedTag));

        Tag actualTag = tagService.getTagById(1L);

        Assertions.assertNotNull(actualTag);
        Assertions.assertEquals(expectedTag.getId(), actualTag.getId());
        Assertions.assertEquals(expectedTag.getTagName(), actualTag.getTagName());

        Mockito.verify(tagRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void getTagById_shouldThrowTagNotFoundException_whenNonExistentTag() {
        // Arrange
        Long nonExistentTagId = 999L;

        // Act & Assert
        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> tagService.getTagById(nonExistentTagId));

        Assertions.assertEquals("Tag not found with ID : " + nonExistentTagId, exception.getMessage());

        // Verify
        Mockito.verify(tagRepository, Mockito.times(1)).findById(nonExistentTagId);
    }

    @Test
    void getAllTagsTest() {
        List<Tag> tagList = new ArrayList<>();
        tagList.add(new Tag("Tag 1"));
        tagList.add(new Tag("Tag 2"));

        Mockito.when(tagRepository.findAll()).thenReturn(tagList);

        Set<Tag> retrievedTags = tagService.getAllTags();

        Assertions.assertNotNull(retrievedTags);
        Assertions.assertEquals(2, retrievedTags.size());
    }

    @Test
    void updateTag_shouldUpdateTag_whenTagExists() {
        // Arrange
        Long existingTagId = 1L;
        Tag updatedTag = new Tag();
        updatedTag.setTagName("Updated Tag");

        // Mock le comportement du repository pour simuler l'existence du tag
        Mockito.when(tagRepository.findById(existingTagId)).thenReturn(Optional.of(new Tag()));
        Mockito.when(tagRepository.save(Mockito.any(Tag.class))).thenAnswer(invocation -> {
            Tag savedTag = invocation.getArgument(0);
            savedTag.setId(existingTagId.intValue());
            return savedTag;
        });

        // Act
        Tag resultTag = tagService.updateTag(existingTagId, updatedTag);

        // Assert
        Assertions.assertNotNull(resultTag);
        Assertions.assertEquals("Updated Tag", resultTag.getTagName());

        // Verify
        Mockito.verify(tagRepository, Mockito.times(1)).findById(existingTagId);
        Mockito.verify(tagRepository, Mockito.times(1)).save(Mockito.any(Tag.class));
    }

    @Test
    void updateTag_shouldThrowEntityNotFoundException_whenTagDoesNotExist() {
        // Arrange
        Long nonExistentTagId = 999L;
        Tag updatedTag = new Tag();
        updatedTag.setTagName("Updated Tag");

        // Act & Assert
        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> tagService.updateTag(nonExistentTagId, updatedTag));

        Assertions.assertEquals("Tag not found with ID : " + nonExistentTagId, exception.getMessage());

        // Verify
        Mockito.verify(tagRepository, Mockito.times(1)).findById(nonExistentTagId);
        Mockito.verify(tagRepository, Mockito.never()).save(Mockito.any(Tag.class));
    }

    @Test
    void deleteTag_shouldDeleteTag_whenTagExists() {
        // Arrange
        Long existingTagId = 1L;

        // Mock le comportement du repository pour simuler l'existence du tag
        Mockito.when(tagRepository.existsById(existingTagId)).thenReturn(true);

        // Act
        tagService.deleteTag(existingTagId);

        // Verify
        Mockito.verify(tagRepository, Mockito.times(1)).existsById(existingTagId);
        Mockito.verify(tagRepository, Mockito.times(1)).deleteById(existingTagId);
    }

    @Test
    void deleteTag_shouldThrowEntityNotFoundException_whenTagDoesNotExist() {
        // Arrange
        Long nonExistentTagId = 999L;

        // Mock le comportement du repository pour simuler l'absence du tag
        Mockito.when(tagRepository.existsById(nonExistentTagId)).thenReturn(false);

        // Act & Assert
        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> tagService.deleteTag(nonExistentTagId));

        Assertions.assertEquals("Tag not found with ID : " + nonExistentTagId, exception.getMessage());

        // Verify
        Mockito.verify(tagRepository, Mockito.times(1)).existsById(nonExistentTagId);
        Mockito.verify(tagRepository, Mockito.never()).deleteById(Mockito.anyLong());
    }


}
