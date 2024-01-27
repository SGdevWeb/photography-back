package org.photography.api.service;

import org.modelmapper.ModelMapper;
import org.photography.api.dto.PhototypeDTO.PhotoTypeCreationDTO;
import org.photography.api.dto.PhototypeDTO.PhotoTypeDTO;
import org.photography.api.dto.PhototypeDTO.PhotoTypeDetailDTO;
import org.photography.api.dto.ThemeDTO.ThemeCreationDTO;
import org.photography.api.dto.ThemeDTO.ThemeDTO;
import org.photography.api.dto.ThemeDTO.ThemeDetailDTO;
import org.photography.api.dto.ThemeDescriptionDTO.ThemeDescriptionDTO;
import org.photography.api.dto.ThemePhotoDTO.ThemePhotoCreationDTO;
import org.photography.api.dto.ThemePhotoDTO.ThemePhotoDTO;
import org.photography.api.exception.AlreadyExists;
import org.photography.api.exception.EntityNotFoundException;
import org.photography.api.exception.NonUniquePhotoUrlException;
import org.photography.api.model.*;
import org.photography.api.repository.PhotoTypeRepository;
import org.photography.api.repository.ThemeDescriptionRepository;
import org.photography.api.repository.ThemePhotoRepository;
import org.photography.api.repository.ThemeRepository;
import org.photography.api.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    private final ThemeDescriptionService themeDescriptionService;

    private final ThemeDescriptionRepository themeDescriptionRepository;

    private final PhotoTypeService photoTypeService;

    private final PhotoTypeRepository photoTypeRepository;

    private final ThemePhotoService themePhotoService;

    private final ThemePhotoRepository themePhotoRepository;

    @Autowired
    public ThemeService(ThemeRepository themeRepository,
                        ThemeDescriptionService themeDescriptionService,
                        ThemeDescriptionRepository themeDescriptionRepository,
                        PhotoTypeService photoTypeService,
                        PhotoTypeRepository photoTypeRepository,
                        ThemePhotoService themePhotoService,
                        ThemePhotoRepository themePhotoRepository) {
        this.themeRepository = themeRepository;
        this.themeDescriptionService = themeDescriptionService;
        this.themeDescriptionRepository = themeDescriptionRepository;
        this.photoTypeService = photoTypeService;
        this.photoTypeRepository = photoTypeRepository;
        this.themePhotoService = themePhotoService;
        this.themePhotoRepository = themePhotoRepository;
    }

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public ThemeDetailDTO createTheme(ThemeCreationDTO themeCreationDTO) {
        if (themeRepository.existsByThemeName(themeCreationDTO.getThemeName())){
            throw new AlreadyExists(themeCreationDTO.getThemeName());
        }

        ValidationUtils.validateYearRange(themeCreationDTO.getYearFrom(), themeCreationDTO.getYearTo());
        ValidationUtils.validateName(themeCreationDTO.getThemeName());

        Theme themeToCreate = new Theme();

        themeToCreate.setThemePhotos(new HashSet<>());
        themeToCreate.setYearFrom(themeCreationDTO.getYearFrom());
        themeToCreate.setYearTo(themeCreationDTO.getYearTo());
        themeToCreate.setThemeName(themeCreationDTO.getThemeName());

        // Description
        if (themeCreationDTO.getDescription() != null) {
            ThemeDescriptionDTO themeDescriptionDTO = themeCreationDTO.getDescription();
            ThemeDescriptionDTO themeDescriptionDTOToCreate = themeDescriptionService.createThemeDescriptionIfNotExistsOrGet(themeDescriptionDTO.getDescriptionText());
            ThemeDescription themeDescriptionToCreate = modelMapper.map(themeDescriptionDTOToCreate, ThemeDescription.class);

            themeToCreate.setDescription(themeDescriptionToCreate);
        }

        Theme createdTheme = themeRepository.save(themeToCreate);

        // PhotoTypes with themePhoto
        Set<PhotoType> photoTypes = createPhotoTypes(themeCreationDTO.getPhotoTypes(), createdTheme);

        List<PhotoType> photoTypesCreated = photoTypeRepository.saveAll(photoTypes);

        createdTheme.setPhotoTypes(new HashSet<>(photoTypesCreated));

        return modelMapper.map(createdTheme, ThemeDetailDTO.class);
    }

    private Set<PhotoType> createPhotoTypes(Set<PhotoTypeCreationDTO> photoTypeDTOs, Theme theme) {
        Set<PhotoType> photoTypes = new HashSet<>();

        if (photoTypeDTOs != null) {
            for (PhotoTypeCreationDTO photoTypeDTO : photoTypeDTOs) {
                PhotoTypeDTO photoTypeDTOToCreate = photoTypeService.createPhotoTypeIfNotExistsOrGet(photoTypeDTO.getTypeName());
                PhotoType photoType = modelMapper.map(photoTypeDTOToCreate, PhotoType.class);
                photoType.setThemePhotos(new HashSet<>());

                Set<ThemePhotoDTO> themePhotoDTOS = themePhotoService.getAllThemePhotos();
                Set<String> allPhotoUrls = themePhotoDTOS.stream()
                                .map(ThemePhotoDTO::getPhotoUrl)
                                .collect(Collectors.toSet());
                validateUniquePhotoUrl(allPhotoUrls, photoTypeDTO.getPhotoUrl());

                ThemePhoto themePhoto = new ThemePhoto();

                themePhoto.setPhotoUrl(photoTypeDTO.getPhotoUrl());
                themePhoto.setPhotoType(photoType);
                themePhoto.setTheme(theme);

                ThemePhoto themePhotoCreated = themePhotoRepository.save(themePhoto);

                Set<ThemePhoto> themePhotos = theme.getThemePhotos();
                themePhotos.add(themePhotoCreated);
                theme.setThemePhotos(themePhotos);

                themeRepository.save(theme);

                photoType.getThemePhotos().add(themePhotoCreated);

                photoTypes.add(photoType);
            }
        }

        return photoTypes;
    }

    private void validateUniquePhotoUrl(Set<String> existingPhotoUrls, String newPhotoUrl) {
        if (existingPhotoUrls.contains(newPhotoUrl)) {
            throw new NonUniquePhotoUrlException("Cette Url '" +  newPhotoUrl + "' appartient déjà à un thème, veuillez choisir une autre URL.");
        }
    }

    public ThemeDetailDTO getThemeById(Long id) {
        ValidationUtils.validateId(id);

        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Theme", id));

        return modelMapper.map(theme, ThemeDetailDTO.class);
    }

    public Set<ThemeDetailDTO> getAllThemes() {
        Set<Theme> themes = new HashSet<>(themeRepository.findAll());
        return themes.stream()
                .map(theme -> modelMapper.map(theme, ThemeDetailDTO.class))
                .collect(Collectors.toSet());
    }

    public void deleteTheme(Long id) {
        ValidationUtils.validateId(id);

        Theme themeToDelete = themeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Theme", id));

        ThemeDescription themeDescription = themeToDelete.getDescription();

        if (themeDescription != null && themeDescription.getThemes().size() == 1) {
            themeToDelete.setDescription(null);
            themeDescriptionRepository.deleteById(themeDescription.getId());
        }

        themeToDelete.setDescription(null);

        Set<PhotoType> photoTypesToRemove = themeToDelete.getPhotoTypes().stream()
                .filter(photoType -> photoType.getThemes().size() == 1)
                .collect(Collectors.toSet());

        if (!photoTypesToRemove.isEmpty()) {
            themeToDelete.getPhotoTypes().removeAll(photoTypesToRemove);
            photoTypesToRemove.forEach(photoTypeRepository::delete);
        }

        themeToDelete.getThemePhotos().forEach(themePhotoRepository::delete);

        themeRepository.delete(themeToDelete);
    }

}
