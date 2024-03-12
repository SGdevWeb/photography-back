package org.photography.api.service;

import org.modelmapper.ModelMapper;
import org.photography.api.dto.PhotoLibraryDTO.PhotoLibraryCreationDTO;
import org.photography.api.dto.PhotoLibraryDTO.PhotoLibraryDTO;
import org.photography.api.dto.TagDTO.TagDTO;
import org.photography.api.exception.EntityNotFoundException;
import org.photography.api.model.Location;
import org.photography.api.model.PhotoLibrary;
import org.photography.api.model.Tag;
import org.photography.api.repository.PhotoLibraryRepository;
import org.photography.api.utils.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PhotoLibraryService {

    private final PhotoLibraryRepository photoLibraryRepository;

    private final LocationService locationService;

    private final TagService tagService;

    private static final Logger logger = LoggerFactory.getLogger(TestimonialService.class);

    @Autowired
    public PhotoLibraryService(PhotoLibraryRepository photoLibraryRepository,
                               LocationService locationService,
                               TagService tagService) {
        this.photoLibraryRepository = photoLibraryRepository;
        this.locationService = locationService;
        this.tagService = tagService;
    }

    @Autowired
    private ModelMapper modelMapper;

    public PhotoLibraryDTO createPhotoLibrary(PhotoLibraryCreationDTO photoLibraryDTO) {
        String locationName = photoLibraryDTO.getLocation();
        Location location = locationService.createLocationIfNotExistsOrGet(locationName);

        if (photoLibraryDTO.getTags() == null || photoLibraryDTO.getTags().isEmpty()) {
            throw new IllegalArgumentException("Une Photo doit avoir au moins un tag.");
        }

        Set<Tag> tags = photoLibraryDTO.getTags().stream()
                .map(tagCreationDTO -> tagService.createTagIfNotExistsOrGet(tagCreationDTO.getTagName()))
                .collect(Collectors.toSet());

        PhotoLibrary photoLibraryToCreate = modelMapper.map(photoLibraryDTO, PhotoLibrary.class);

        photoLibraryToCreate.setLocation(location);

        photoLibraryToCreate.setTags(tags);

        PhotoLibrary createdPhotoLibrary = photoLibraryRepository.save(photoLibraryToCreate);

        return modelMapper.map(createdPhotoLibrary, PhotoLibraryDTO.class);
    }


    public PhotoLibraryDTO getPhotoLibraryById(Long id) {
        ValidationUtils.validateId(id);

        PhotoLibrary photoLibrary = photoLibraryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PhotoLibrary", id));

        return modelMapper.map(photoLibrary, PhotoLibraryDTO.class);
    }

    @Transactional
    public Set<PhotoLibraryDTO> getAllPhotoLibraries() {
        Set<PhotoLibrary> photoLibraries = new HashSet<>(photoLibraryRepository.findAll());
        return photoLibraries.stream()
                .map(photoLibrary -> modelMapper.map(photoLibrary, PhotoLibraryDTO.class))
                .collect(Collectors.toSet());
    }

    public PhotoLibraryDTO updatePhotoLibrary(Long id, PhotoLibraryDTO updatedPhotoLibraryDTO) {
        ValidationUtils.validateId(id);

        PhotoLibrary existingPhotoLibrary = photoLibraryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PhotoLibrary", id));

        Location location = locationService.createLocationIfNotExistsOrGet(updatedPhotoLibraryDTO.getLocation());

        Set<TagDTO> tagDTOs = tagService.getTagsByNameList(updatedPhotoLibraryDTO.getTags().stream()
                .map(TagDTO::getTagName)
                .collect(Collectors.toSet()));

        existingPhotoLibrary.setPhotoUrl(updatedPhotoLibraryDTO.getPhotoUrl());
        existingPhotoLibrary.setLocation(location);
        existingPhotoLibrary.setTags(tagDTOs.stream()
                .map(tag -> modelMapper.map(tag, Tag.class))
                .collect(Collectors.toSet()));

        PhotoLibrary updatedPhotoLibrary = photoLibraryRepository.save(existingPhotoLibrary);

        return modelMapper.map(updatedPhotoLibrary, PhotoLibraryDTO.class);
    }

    public void deletePhotoLibrary(Long id) {
        ValidationUtils.validateId(id);

        if (photoLibraryRepository.existsById(id)) {
            photoLibraryRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("PhotoLibrary", id);
        }
    }

}
