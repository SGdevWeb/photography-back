package org.photography.api.service;

import org.photography.api.dto.TagDTO;
import org.photography.api.exception.TagNotFoundException;
import org.photography.api.model.Tag;
import org.photography.api.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag createTag(Tag tag) {
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
        if (tagName == null || tagName.isEmpty()) {
            throw new IllegalArgumentException("Tag name cannot be null or empty");
        }

        if (!tagName.matches("^[a-zA-Z0-9 ]+$")) {
            throw new IllegalArgumentException("Tag name can only contain letters and numbers");
        }

        if (tagName.length() > 255) {
            throw new IllegalArgumentException("Tag name length cannot exceed 255 characters");
        }

        if (tagRepository.existsByTagName(tagName)) {
            throw new IllegalArgumentException("Tag with the same name already exists");
        }
    }

    public Tag getTagById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException(id));
    }

    public TagDTO getTagDTOById(Long id) {
        Tag tag = getTagById(id);
        return convertToDTO(tag);
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public List<TagDTO> getAllTagDTOs() {
        List<Tag> tags = getAllTags();
        return tags.stream()
                .map(tag -> convertToDTO(tag))
                .collect(Collectors.toList());
    }

    public Tag updateTag(Long id, Tag updatedTag) {
        if (tagRepository.existsById(id)) {
            Tag existingTag = getTagById(id);

            existingTag.setTagName(updatedTag.getTagName());

            return tagRepository.save(existingTag);
        } else {
            throw new TagNotFoundException(id);
        }
    }

    public TagDTO updateTagDTO(Long id, TagDTO updatedTagDTO) {
        Tag updatedTag = convertToEntity(updatedTagDTO);

        return convertToDTO(updateTag(id, updatedTag));
    }

    public void deleteTag(Long id) {
        if (tagRepository.existsById(id)) {
            tagRepository.deleteById(id);
        } else {
            throw new TagNotFoundException(id);
        }

    }

}
