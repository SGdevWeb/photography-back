package org.photography.api.service;

import org.modelmapper.ModelMapper;
import org.photography.api.dto.PhotoLibraryDTO.PhotoLibraryWithoutTagDTO;
import org.photography.api.dto.TagDTO.TagCreationDTO;
import org.photography.api.dto.TagDTO.TagDTO;
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

        Tag createdTag = tagRepository.save(tag);

        return createdTag;
    }

    public TagDTO createTag(TagCreationDTO tagDTO) {
        validateTagName(tagDTO.getTagName());

        Tag tagToCreate = convertToEntity(tagDTO);

        Tag createdTag = createTag(tagToCreate);

        return modelMapper.map(createdTag, TagDTO.class);
    }

    // Conversion methods
    private Tag convertToEntity(TagDTO tagDTO) {
        Tag tag = new Tag();

        tag.setTagName(tagDTO.getTagName());

        return tag;
    }

    private Tag convertToEntity(TagCreationDTO tagCreationDTO) {
        Tag tag = new Tag();

        tag.setTagName(tagCreationDTO.getTagName());

        return tag;
    }

    private TagDTO convertToDTO(Tag tag) {
        TagDTO tagDTO = new TagDTO();

        tagDTO.setTagName(tag.getTagName());

        return tagDTO;
    }

    private TagCreationDTO convertToCreationDTO(Tag tag) {
        TagCreationDTO tagCreationDTO = new TagCreationDTO();

        tagCreationDTO.setTagName(tag.getTagName());

        return tagCreationDTO;
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

        return modelMapper.map(tag, TagDTO.class);
    }

    public Set<Tag> getAllTags() {
        return new HashSet<>(tagRepository.findAll());
    }

    public Set<TagDTO> getAllTagDTOs() {
        Set<Tag> tags = getAllTags();

        return tags.stream()
                .map(tag -> modelMapper.map(tag, TagDTO.class))
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
            return modelMapper.map(tag, TagDTO.class);
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

        return modelMapper.map(updateTag(id, updatedTag), TagDTO.class);
    }

    @Transactional
    public void deleteTag(Long id) {
        ValidationUtils.validateId(id);

        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag", id));

        Set<PhotoLibrary> photoLibraries = tag.getPhotoLibraries();
        photoLibraries.forEach(photoLibrary -> photoLibrary.getTags().remove(tag));

        tagRepository.deleteById(id);
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
