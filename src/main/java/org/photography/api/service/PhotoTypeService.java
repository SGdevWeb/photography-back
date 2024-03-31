package org.photography.api.service;

import org.modelmapper.ModelMapper;
import org.photography.api.dto.PhotoDTO;
import org.photography.api.dto.PhototypeDTO.PhotoTypeCreationDTO;
import org.photography.api.dto.PhototypeDTO.PhotoTypeDTO;
import org.photography.api.dto.PhototypeDTO.PhotoTypeDetailDTO;
import org.photography.api.dto.PhototypeDTO.PhotoTypeUpdateDTO;
import org.photography.api.dto.ThemeDTO.ThemeDTO;
import org.photography.api.exception.AlreadyExists;
import org.photography.api.exception.EntityNotFoundException;
import org.photography.api.model.PhotoType;
import org.photography.api.model.Theme;
import org.photography.api.model.ThemePhoto;
import org.photography.api.repository.PhotoTypeRepository;
import org.photography.api.repository.ThemePhotoRepository;
import org.photography.api.repository.ThemeRepository;
import org.photography.api.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PhotoTypeService {

    private final PhotoTypeRepository photoTypeRepository;

    private final ThemeRepository themeRepository;

    private final PhotoService photoService;

    private final ThemePhotoService themePhotoService;

    private final ThemePhotoRepository themePhotoRepository;

    @Autowired
    public PhotoTypeService(PhotoTypeRepository photoTypeRepository,
                            ThemeRepository themeRepository,
                            PhotoService photoService,
                            ThemePhotoService themePhotoService,
                            ThemePhotoRepository themePhotoRepository) {
        this.photoTypeRepository = photoTypeRepository;
        this.themeRepository = themeRepository;
        this.photoService = photoService;
        this.themePhotoService = themePhotoService;
        this.themePhotoRepository = themePhotoRepository;
    }

    @Autowired
    ModelMapper modelMapper;

    public PhotoTypeDetailDTO getPhotoTypeById(Long id) {
        ValidationUtils.validateId(id);

        PhotoType photoType = photoTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PhotoType", id));

        return modelMapper.map(photoType, PhotoTypeDetailDTO.class);
    }

    public Set<PhotoTypeDetailDTO> getAllPhotoTypes() {
        Set<PhotoType> photoTypes = new HashSet<>(photoTypeRepository.findAll());

        return photoTypes.stream()
                .map(photoType -> modelMapper.map(photoType, PhotoTypeDetailDTO.class))
                .collect(Collectors.toSet());
    }

    public PhotoTypeDTO updatePhotoTypeName(Long photoTypeId, PhotoTypeUpdateDTO photoTypeUpdateDTO) {
        PhotoType photoType = photoTypeRepository.findById(photoTypeId)
                .orElseThrow(() -> new EntityNotFoundException("PhotoType", photoTypeId));

        String newTypeName = photoTypeUpdateDTO.getTypeName();

        // Mise à jour du typeName
        photoType.setTypeName(newTypeName);
        photoTypeRepository.save(photoType);

        // Mise à jour de tous les liens dans les thèmes associés
        updatePhototypeInThemes(photoType);

        return modelMapper.map(photoType, PhotoTypeDTO.class);
    }

    public void updatePhototypeInThemes(PhotoType updatedPhototype) {
        Set<Theme> themes = updatedPhototype.getThemes();

        for (Theme theme : themes) {
            // Retire l'ancien phototype
            theme.getPhotoTypes().removeIf(photoType  -> photoType.getId() == updatedPhototype.getId());

            // Ajoute le phototype mis à jour
            theme.getPhotoTypes().add(updatedPhototype);

            themeRepository.save(theme);
        }
    }

    public void deletePhotoType(Long photoTypeId, Long themeId) {
        ValidationUtils.validateId(photoTypeId);
        ValidationUtils.validateId(themeId);

        PhotoType photoType = photoTypeRepository.findById(photoTypeId)
                .orElseThrow(() -> new EntityNotFoundException("PhotoType", photoTypeId));

        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new EntityNotFoundException("Theme", themeId));

        if (photoType.getThemePhotos().size() > 1) {
            Set<ThemePhoto> themePhotos = photoType.getThemePhotos();
            for (ThemePhoto themePhoto : themePhotos) {
                if (themePhoto.getTheme().equals(theme)){
                    theme.getPhotoTypes().remove(photoType);
                    photoType.getThemes().remove(theme);
                    themePhoto.setTheme(null);
                    themePhoto.setPhotoType(null);
                    String[] parts = themePhoto.getPhotoUrl().split("/");
                    photoService.deletePhoto("themes", parts[parts.length - 1]);
                    themePhotoRepository.delete(themePhoto);
                }
            }
        } else if (photoType.getThemePhotos().size() == 1) {
            ThemePhoto themePhoto = photoType.getThemePhotos().iterator().next();
            themePhoto.setPhotoType(null);
            themePhoto.setTheme(null);
            String[] parts = themePhoto.getPhotoUrl().split("/");
            photoService.deletePhoto("themes", parts[parts.length - 1]);
            theme.getThemePhotos().remove(themePhoto);
            theme.getPhotoTypes().remove(photoType);
            themePhotoRepository.delete(themePhoto);
            photoTypeRepository.delete(photoType);
        }
    }

    public PhotoTypeDTO createPhotoTypeIfNotExistsOrGet(String typeName) {
        Optional<PhotoType> existingPhotoType = photoTypeRepository.findByTypeName(typeName);

        if (existingPhotoType.isPresent()) {
            return modelMapper.map(existingPhotoType.get(), PhotoTypeDTO.class);
        } else {
            PhotoType newPhotoType = new PhotoType();
            newPhotoType.setTypeName(typeName);
            PhotoType photoTypeCreated = photoTypeRepository.save(newPhotoType);
            return modelMapper.map(photoTypeCreated, PhotoTypeDTO.class);
        }
    }

    public PhotoTypeDetailDTO createPhotoType(PhotoTypeCreationDTO photoTypeCreationDTO, Long themeId) {
        String typeName = photoTypeCreationDTO.getTypeName();

        PhotoTypeDTO photoTypeDTO = createPhotoTypeIfNotExistsOrGet(typeName);
        PhotoType photoType = modelMapper.map(photoTypeDTO, PhotoType.class);

        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new EntityNotFoundException("Theme", themeId));

        String photoUrl = photoTypeCreationDTO.getPhotoUrl();

        ThemePhoto themePhoto = new ThemePhoto();
        themePhoto.setPhotoUrl(photoUrl);
        themePhoto.setPhotoType(photoType);
        themePhoto.setTheme(theme);
        themePhoto.setPhotoPosition(photoTypeCreationDTO.getPhotoPosition());

        ThemePhoto themePhotoCreated = themePhotoRepository.save(themePhoto);

        Set<ThemePhoto> themePhotos = theme.getThemePhotos();
        themePhotos.add(themePhotoCreated);
        theme.setThemePhotos(themePhotos);

        if (theme.getPhotoTypes() == null) {
            theme.setPhotoTypes(new HashSet<>());
            theme.getPhotoTypes().add(photoType);
        } else {
            theme.getPhotoTypes().add(photoType);
        }

        themeRepository.save(theme);

        photoType.setThemePhotos(new HashSet<>());
        photoType.getThemePhotos().add(themePhotoCreated);

        if (photoType.getThemes() == null) {
            photoType.setThemes(new HashSet<>());
            photoType.getThemes().add(theme);
        } else {
            photoType.getThemes().add(theme);
        }

        PhotoType updatedPhotoType = photoTypeRepository.save(photoType);

        return modelMapper.map(updatedPhotoType, PhotoTypeDetailDTO.class);
    }
}
