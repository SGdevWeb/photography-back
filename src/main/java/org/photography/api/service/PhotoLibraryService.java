package org.photography.api.service;

import org.modelmapper.ModelMapper;
import org.photography.api.dto.PhotoDTO;
import org.photography.api.dto.PhotoLibraryDTO.PhotoLibraryCreationDTO;
import org.photography.api.dto.PhotoLibraryDTO.PhotoLibraryDTO;
import org.photography.api.dto.TagDTO.TagCreationDTO;
import org.photography.api.dto.TagDTO.TagDTO;
import org.photography.api.exception.AlreadyExists;
import org.photography.api.exception.EntityNotFoundException;
import org.photography.api.model.Location;
import org.photography.api.model.PhotoLibrary;
import org.photography.api.model.PhotoLibraryTag;
import org.photography.api.model.Tag;
import org.photography.api.repository.LocationRepository;
import org.photography.api.repository.PhotoLibraryRepository;
import org.photography.api.repository.PhotoLibraryTagRepository;
import org.photography.api.repository.TagRepository;
import org.photography.api.utils.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Objects.*;

@Service
public class PhotoLibraryService {

    private final PhotoLibraryRepository photoLibraryRepository;

    private final LocationService locationService;

    private final LocationRepository locationRepository;

    private final TagService tagService;

    private final TagRepository tagRepository;

    private final PhotoService photoService;

    private final PhotoLibraryTagRepository photoLibraryTagRepository;

    private static final Logger logger = LoggerFactory.getLogger(PhotoLibraryService.class);

    @Autowired
    public PhotoLibraryService(PhotoLibraryRepository photoLibraryRepository,
                               LocationService locationService,
                               LocationRepository locationRepository,
                               TagService tagService,
                               TagRepository tagRepository,
                               PhotoService photoService,
                               PhotoLibraryTagRepository photoLibraryTagRepository) {
        this.photoLibraryRepository = photoLibraryRepository;
        this.locationService = locationService;
        this.locationRepository = locationRepository;
        this.tagService = tagService;
        this.tagRepository = tagRepository;
        this.photoService = photoService;
        this.photoLibraryTagRepository = photoLibraryTagRepository;
    }

    @Autowired
    private ModelMapper modelMapper;

    public PhotoLibraryDTO createPhotoLibrary(PhotoLibraryCreationDTO photoLibraryDTO) {
        String locationName = photoLibraryDTO.getLocation().getLocationName();
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

        String photoName = extractPhotoName(photoLibraryDTO.getPhotoUrl());
        photoLibraryToCreate.setPhotoName(photoName);

        PhotoLibrary createdPhotoLibrary = photoLibraryRepository.save(photoLibraryToCreate);

        return modelMapper.map(createdPhotoLibrary, PhotoLibraryDTO.class);
    }

    public String extractPhotoName(String photoUrl) {
        String[] parts = photoUrl.split("/");
        String fileName = parts[parts.length - 1];

        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1) {
            fileName = fileName.substring(0, dotIndex);
        }

        int dashIndex = fileName.indexOf("-");
        if (dashIndex != -1) {
            fileName = fileName.substring(dashIndex + 1);
        }
        return fileName;
    }

    public PhotoLibraryDTO getPhotoLibraryById(Long id) {
        ValidationUtils.validateId(id);

        PhotoLibrary photoLibrary = photoLibraryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PhotoLibrary", id));

        System.out.println(photoLibrary != null);

        if (photoLibrary != null) {
            // Accédez aux tags et vérifiez s'ils sont chargés
            Set<Tag> tags = photoLibrary.getTags();

            System.out.println(tags != null);
            if (tags != null) {
                // Loguez les tags pour vérifier s'ils sont chargés correctement
                for (Tag tag : tags) {
                    System.out.println("Tag: " + tag.getTagName());
                }
            } else {
                System.out.println("Aucun tag trouvé pour cette PhotoLibrary.");
            }
        } else {
            System.out.println("PhotoLibrary non trouvée pour l'ID spécifié.");
        }

        return modelMapper.map(photoLibrary, PhotoLibraryDTO.class);
    }

//    @Transactional
//    public Set<PhotoLibraryDTO> getAllPhotoLibraries() {
//        Set<PhotoLibrary> photoLibraries = new HashSet<>(photoLibraryRepository.findAll());
//        return photoLibraries.stream()
//                .map(photoLibrary -> modelMapper.map(photoLibrary, PhotoLibraryDTO.class))
//                .collect(Collectors.toSet());
//    }

    @Transactional
    public Page<PhotoLibraryDTO> getAllPhotoLibraries(int page, int size, Set<String> tags, Set<String> locations) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("photoName").ascending());

        Page<PhotoLibrary> photoLibrariesPage;

        if ((tags != null && !tags.isEmpty()) || (locations != null && !locations.isEmpty())) {
            photoLibrariesPage = photoLibraryRepository.findFiltered(tags, locations, pageable);
        } else {
            photoLibrariesPage = photoLibraryRepository.findAll(pageable);
        }

        return photoLibrariesPage.map(photoLibrary -> modelMapper.map(photoLibrary, PhotoLibraryDTO.class));
    }

    public PhotoLibraryDTO updatePhotoLibrary(Long id, PhotoLibraryDTO updatedPhotoLibraryDTO) {
        ValidationUtils.validateId(id);

        PhotoLibrary existingPhotoLibrary = photoLibraryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PhotoLibrary", id));

        Location location = locationService.createLocationIfNotExistsOrGet(updatedPhotoLibraryDTO.getLocation().getLocationName());
        existingPhotoLibrary.setLocation(location);

        Set<TagDTO> tagDTOs = tagService.getTagsByNameList(updatedPhotoLibraryDTO.getTags().stream()
                .map(TagDTO::getTagName)
                .collect(Collectors.toSet()));
        existingPhotoLibrary.setTags(tagDTOs.stream()
                .map(tag -> modelMapper.map(tag, Tag.class))
                .collect(Collectors.toSet()));

        String newPhotoUrl = updatedPhotoLibraryDTO.getPhotoUrl();
        String oldFileName = existingPhotoLibrary.getPhotoUrl().substring(existingPhotoLibrary.getPhotoUrl().lastIndexOf("/") + 1);
        String extension = existingPhotoLibrary.getPhotoUrl().substring(existingPhotoLibrary.getPhotoUrl().lastIndexOf(".") + 1);
        String newFileName = extractNumbers(oldFileName) + "-" + updatedPhotoLibraryDTO.getPhotoName() + "." + extension;
        System.out.println(newPhotoUrl);
        System.out.println(oldFileName);
        System.out.println(newFileName);

        if (!existingPhotoLibrary.getPhotoUrl().equals(newPhotoUrl)) {
            photoService.updatePhoto("library", oldFileName, newFileName);
        }

        System.out.println(newFileName);

        existingPhotoLibrary.setPhotoUrl(newPhotoUrl);

        String newPhotoName = updatedPhotoLibraryDTO.getPhotoName();
        existingPhotoLibrary.setPhotoName(newPhotoName);

        PhotoLibrary updatedPhotoLibrary = photoLibraryRepository.save(existingPhotoLibrary);

        return modelMapper.map(updatedPhotoLibrary, PhotoLibraryDTO.class);
    }

    private String extractNumbers(String input) {
        int index = input.indexOf("-");

        if (index != -1) {
            return input.substring(0, index);
        } else {
            return input;
        }
    }

    @Transactional
    public void deletePhotoLibrary(Long photoLibraryId) {
        ValidationUtils.validateId(photoLibraryId);
        PhotoLibrary photoLibrary = photoLibraryRepository.findById(photoLibraryId)
                .orElseThrow(() -> new EntityNotFoundException("PhotoLibrary", photoLibraryId));

        Location location = photoLibrary.getLocation();
        Set<PhotoLibrary> photoLibrariesInLocation = location.getPhotoLibraries();
        boolean locationInUse = photoLibrariesInLocation.size() > 1;
        if (!locationInUse) {
            locationRepository.deleteById(location.getId());
        }

        Set<Tag> tags = photoLibrary.getTags();
        for (Tag tag: tags) {
            Set<PhotoLibrary> photoLibrariesInTag = tag.getPhotoLibraries();
            if (photoLibrariesInTag.size() == 1) {
                tagRepository.deleteById(tag.getId());
            }
        }

        int lastSlashIndex = photoLibrary.getPhotoUrl().lastIndexOf('/');
        String fileName = photoLibrary.getPhotoUrl().substring(lastSlashIndex + 1);
        photoService.deletePhoto("library", fileName);

        photoLibraryRepository.deleteById(photoLibraryId);
    }

    public void deleteLocation(Long photoLibraryId) {
        PhotoLibrary photoLibrary = photoLibraryRepository.findById(photoLibraryId)
                .orElseThrow(() -> new EntityNotFoundException("PhotoLibrary", photoLibraryId));

        Location locationToDelete = photoLibrary.getLocation();

        if (locationToDelete != null) {
            Set<PhotoLibrary> photoLibraries = locationToDelete.getPhotoLibraries();

            if (photoLibraries.size() == 1 && photoLibraries.contains(photoLibrary)) {
                System.out.println("Seul");
                System.out.println(locationToDelete.getId());
                photoLibrary.setLocation(null);
                locationRepository.delete(locationToDelete);
            } else {
                System.out.println("Pas seul");
                photoLibrary.setLocation(null);
                photoLibraryRepository.save(photoLibrary);
            }
        }
    }

    @Transactional
    public void removeTag(Long photoLibraryId, Long tagId) {
        PhotoLibrary photoLibrary = photoLibraryRepository.findById(photoLibraryId)
                .orElseThrow(() -> new EntityNotFoundException("PhotoLibrary", photoLibraryId));

        Tag tagToRemove = photoLibrary.getTags().stream()
                .filter(tag -> tag.getId() == tagId)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Tag", tagId));

        photoLibrary.getTags().remove(tagToRemove);

        boolean tagAssociatedWithOtherPhotos = photoLibraryTagRepository.existsByTagId(tagId);

        if (!tagAssociatedWithOtherPhotos) {
            tagService.deleteTag(tagId);
        }

        photoLibraryRepository.save(photoLibrary);
    }

    public PhotoLibrary addTagToPhotoLibrary(Long photoLibraryId, TagCreationDTO tagCreationDTO) {
        System.out.println(photoLibraryId);
        System.out.println(tagCreationDTO);
        System.out.println(tagCreationDTO.getTagName());

        PhotoLibrary existingPhotoLibrary = photoLibraryRepository.findById(photoLibraryId)
                .orElseThrow(() -> new EntityNotFoundException("PhotoLibrary", photoLibraryId));

        Tag tag = tagService.createTagIfNotExistsOrGet(tagCreationDTO.getTagName());

        if (!existingPhotoLibrary.getTags().contains(tag)) {
            existingPhotoLibrary.getTags().add(tag);
            return photoLibraryRepository.save(existingPhotoLibrary);
        } else {
            throw new AlreadyExists(tag.getTagName());
        }

    }

    public PhotoLibrary updatePhotoInPhotoLibrary(Long photoLibraryId, PhotoDTO photoDTO) {

        System.out.println("entrée UpdatePhotoInPhotoLibrary");

        String newPhotoUrl = photoService.uploadPhoto(photoDTO);

        PhotoLibrary existingPhotoLibrary = photoLibraryRepository.findById(photoLibraryId)
                .orElseThrow(() -> new EntityNotFoundException("PhotoLibrary", photoLibraryId));

        String oldFileName = existingPhotoLibrary.getPhotoUrl().substring(existingPhotoLibrary.getPhotoUrl().lastIndexOf("/") + 1);
        photoService.deletePhoto("library", oldFileName);

        existingPhotoLibrary.setPhotoUrl(newPhotoUrl);

        String newPhotoName = extractPhotoName(newPhotoUrl);
        existingPhotoLibrary.setPhotoName(newPhotoName);

        PhotoLibrary updatedPhotoLibrary = photoLibraryRepository.save(existingPhotoLibrary);

        return modelMapper.map(updatedPhotoLibrary, PhotoLibrary.class);
    }

}
