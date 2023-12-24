package org.photography.api.service;

import org.photography.api.exception.TagNotFoundException;
import org.photography.api.model.Tag;
import org.photography.api.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag createTag(Tag tag) {
        if (tag.getTagName() == null || tag.getTagName().isEmpty()) {
            throw new IllegalArgumentException("Tag name cannot be null or empty");
        } else {
            return tagRepository.save(tag);
        }
    }

    public Tag getTagById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException(id));
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Tag updateTag(Long id, Tag updatedTag) {
        if (tagRepository.existsById(id)) {
            updatedTag.setId(id.intValue());
            return tagRepository.save(updatedTag);
        } else {
            throw new TagNotFoundException(id);
        }
    }

    public void deleteTag(Long id) {
        if (tagRepository.existsById(id)) {
            tagRepository.deleteById(id);
        } else {
            throw new TagNotFoundException(id);
        }

    }

}
