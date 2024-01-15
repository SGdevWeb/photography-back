package org.photography.api.service;

import org.modelmapper.ModelMapper;
import org.photography.api.dto.PhototypeDTO.PhotoTypeCreationDTO;
import org.photography.api.dto.PhototypeDTO.PhotoTypeDetailDTO;
import org.photography.api.dto.ThemePhotoDTO.ThemePhotoDTO;
import org.photography.api.dto.ThemePhotoDTO.ThemePhotoUpdateDTO;
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
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ThemePhotoService {

    private final ThemePhotoRepository themePhotoRepository;

    @Autowired
    public ThemePhotoService(ThemePhotoRepository themePhotoRepository) {
        this.themePhotoRepository = themePhotoRepository;
    }

    @Autowired
    private ModelMapper modelMapper;

    public ThemePhotoDTO getThemePhotoById(Long id) {
        ValidationUtils.validateId(id);

        ThemePhoto themePhoto = themePhotoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ThemePhoto", id));

        return modelMapper.map(themePhoto, ThemePhotoDTO.class);
    }

    public Set<ThemePhotoDTO> getAllThemePhotos() {
        Set<ThemePhoto> themePhotos = new HashSet<>(themePhotoRepository.findAll());

        return themePhotos.stream()
                .map(themePhoto -> modelMapper.map(themePhoto, ThemePhotoDTO.class))
                .collect(Collectors.toSet());
    }

    public ThemePhotoDTO updateThemePhotoUrl(Long themePhotoId, ThemePhotoUpdateDTO themePhotoUpdateDTO) {
        ThemePhoto themePhoto = themePhotoRepository.findById(themePhotoId)
                .orElseThrow(() -> new EntityNotFoundException("ThemePhoto", themePhotoId));

        String newUrl = themePhotoUpdateDTO.getPhotoUrl();

        // Mise à jour de l'URL
        themePhoto.setPhotoUrl(newUrl);
        ThemePhoto themePhotoUpdated = themePhotoRepository.save(themePhoto);
        return modelMapper.map(themePhotoUpdated, ThemePhotoDTO.class);
    }

    public void deleteThemePhoto(Long id) {
        ValidationUtils.validateId(id);

        if (themePhotoRepository.existsById(id)) {
            themePhotoRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("ThemePhoto", id);
        }
    }

}
