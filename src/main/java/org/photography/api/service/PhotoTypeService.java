package org.photography.api.service;

import org.modelmapper.ModelMapper;
import org.photography.api.dto.PhototypeDTO.PhotoTypeCreationDTO;
import org.photography.api.dto.PhototypeDTO.PhotoTypeDTO;
import org.photography.api.dto.PhototypeDTO.PhotoTypeDetailDTO;
import org.photography.api.dto.PhototypeDTO.PhotoTypeUpdateDTO;
import org.photography.api.exception.AlreadyExists;
import org.photography.api.exception.EntityNotFoundException;
import org.photography.api.model.PhotoType;
import org.photography.api.model.Theme;
import org.photography.api.repository.PhotoTypeRepository;
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

    @Autowired
    public PhotoTypeService(PhotoTypeRepository photoTypeRepository,
                            ThemeRepository themeRepository) {
        this.photoTypeRepository = photoTypeRepository;
        this.themeRepository = themeRepository;
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

    public void deletePhotoType(Long id) {
        ValidationUtils.validateId(id);

        if (photoTypeRepository.existsById(id)) {
            photoTypeRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("PhotoType", id);
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
}
