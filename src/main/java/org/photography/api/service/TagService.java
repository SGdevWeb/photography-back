package org.photography.api.service;

import org.modelmapper.ModelMapper;
import org.photography.api.dto.PhotoLibraryDTO.PhotoLibraryWithoutTagDTO;
import org.photography.api.dto.TagDTO;
import org.photography.api.exception.EntityNotFoundException;
import org.photography.api.model.PhotoLibrary;
import org.photography.api.model.Tag;
import org.photography.api.repository.TagRepository;
import org.photography.api.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository, ModelMapper modelMapper) {
        this.tagRepository = tagRepository;
        this.modelMapper = modelMapper;
    }

    @Autowired
    private final ModelMapper modelMapper;

    public Tag createTag(Tag tag) {
        validateTagName(tag.getTagName());

        return tagRepository.save(tag);
    }

    public TagDTO createTag(TagDTO tagDTO) {
        validateTagName(tagDTO.getTagName());

        Tag tagToCreate = convertToEntity(tagDTO);

        Tag createdTag = createTag(tagToCreate);

        return convertToDTO(createdTag);
    }

    // Conversion methods
    private Tag convertToEntity(TagDTO tagDTO) {
        Tag tag = new Tag();

        tag.setTagName(tagDTO.getTagName());

        return tag;
    }

    private TagDTO convertToDTO(Tag tag) {
        TagDTO tagDTO = new TagDTO();

        tagDTO.setTagName(tag.getTagName());

        return tagDTO;
    }

    private void validateTagName(String tagName) {
        ValidationUtils.validateName(tagName);

        if (tagRepository.existsByTagName(tagName)) {
            throw new IllegalArgumentException("Tag with the same name already exists");
        }
    }

    public Tag getTagById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag", id));
    }

    public TagDTO getTagDTOById(Long id) {
        Tag tag = getTagById(id);

        return convertToDTO(tag);
    }

    public Set<Tag> getAllTags() {
        return new HashSet<>(tagRepository.findAll());
    }

    public Set<TagDTO> getAllTagDTOs() {
        Set<Tag> tags = getAllTags();

        return tags.stream()
                .map(tag -> convertToDTO(tag))
                .collect(Collectors.toSet());
    }

    public Set<TagDTO> getTagsByNameList(Set<String> tagNames) {
        return tagNames.stream()
                .map(this::getTagDTOByName)
                .collect(Collectors.toSet());
    }

    public TagDTO getTagDTOByName(String tagName) {
        if (tagRepository.findByTagName(tagName) == null) {
            throw new jakarta.persistence.EntityNotFoundException("Tag not found with name : " + tagName);
        } else {
            Tag tag = tagRepository.findByTagName(tagName);
            return convertToDTO(tag);
        }
    }

    public Tag updateTag(Long id, Tag updatedTag) {
        ValidationUtils.validateId(id);
        ValidationUtils.validateName(updatedTag.getTagName());

        Tag existingTag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag", id));

        existingTag.setTagName(updatedTag.getTagName());

        return tagRepository.save(existingTag);
    }

    public TagDTO updateTagDTO(Long id, TagDTO updatedTagDTO) {
        Tag updatedTag = convertToEntity(updatedTagDTO);

        return convertToDTO(updateTag(id, updatedTag));
    }

    @Transactional
    public Set<PhotoLibraryWithoutTagDTO> deleteTag(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Tag", id)
        );

        Set<PhotoLibrary> photoLibraries = tag.getPhotoLibraries();

        photoLibraries.forEach(photoLibrary -> photoLibrary.getTags().remove(tag));

        tagRepository.deleteById(id);

        Set<PhotoLibrary> photoLibrariesWithoutTags = photoLibraries.stream()
                .filter(photoLibrary -> photoLibrary.getTags().isEmpty())
                .collect(Collectors.toSet());

        Set<PhotoLibraryWithoutTagDTO> photoLibraryWithoutTagDTOs = photoLibrariesWithoutTags.stream()
                .map(photoLibrary -> modelMapper.map(photoLibrary, PhotoLibraryWithoutTagDTO.class))
                .collect(Collectors.toSet());

        return photoLibraryWithoutTagDTOs;
    }

    public Tag createTagIfNotExistsOrGet(String tagName) {
        Tag existingTag = tagRepository.findByTagName(tagName);

        if (existingTag != null) {
            return existingTag;
        } else {
            Tag newTag = new Tag(tagName);
            return tagRepository.save(newTag);
        }
    }

}
