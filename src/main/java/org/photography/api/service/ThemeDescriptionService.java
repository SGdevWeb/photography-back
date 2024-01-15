package org.photography.api.service;

import org.modelmapper.ModelMapper;
import org.photography.api.dto.ThemeDescriptionDTO.ThemeDescriptionDTO;
import org.photography.api.exception.AlreadyExists;
import org.photography.api.exception.EntityNotFoundException;
import org.photography.api.model.Theme;
import org.photography.api.model.ThemeDescription;
import org.photography.api.repository.ThemeDescriptionRepository;
import org.photography.api.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ThemeDescriptionService {

    private final ThemeDescriptionRepository themeDescriptionRepository;

    @Autowired
    public ThemeDescriptionService(ThemeDescriptionRepository themeDescriptionRepository) {
        this.themeDescriptionRepository = themeDescriptionRepository;
    }

    @Autowired
    private ModelMapper modelMapper;

    public ThemeDescriptionDTO createThemeDescription(ThemeDescriptionDTO themeDescriptionDTO) {
        if (themeDescriptionDTO == null) {
            throw new IllegalArgumentException("themeDescriptionDTO ne peut pas être null");
        }

        ValidationUtils.validateText(themeDescriptionDTO.getDescriptionText());

        Optional<ThemeDescription> existingThemeDescription = themeDescriptionRepository.findByDescriptionText(themeDescriptionDTO.getDescriptionText());

        if (existingThemeDescription.isPresent()) {
            throw new AlreadyExists(themeDescriptionDTO.getDescriptionText());
        }

        ThemeDescription themeDescriptionToCreate = modelMapper.map(themeDescriptionDTO, ThemeDescription.class);

        ThemeDescription createdThemeDescription = themeDescriptionRepository.save(themeDescriptionToCreate);

        return modelMapper.map(createdThemeDescription, ThemeDescriptionDTO.class);
    }


    public ThemeDescriptionDTO getThemeDescriptionById(Long id) {
        ValidationUtils.validateId(id);

        ThemeDescription themeDescription = themeDescriptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ThemeDescription", id));

        return modelMapper.map(themeDescription, ThemeDescriptionDTO.class);
    }

    public Set<ThemeDescriptionDTO> getAllThemeDescriptions() {
        Set<ThemeDescription> themeDescriptions = new HashSet<>(themeDescriptionRepository.findAll());

        return themeDescriptions.stream()
                .map(themeDescription -> modelMapper.map(themeDescription, ThemeDescriptionDTO.class))
                .collect(Collectors.toSet());
    }

    public ThemeDescriptionDTO updateThemeDescription(Long id, ThemeDescriptionDTO updatedThemeDescriptionDTO) {
        ValidationUtils.validateId(id);
        ValidationUtils.validateText(updatedThemeDescriptionDTO.getDescriptionText());

        ThemeDescription existingThemeDescription = themeDescriptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ThemeDescription", id));

        existingThemeDescription.setDescriptionText(updatedThemeDescriptionDTO.getDescriptionText());

        ThemeDescription updatedThemeDescription = themeDescriptionRepository.save(existingThemeDescription);

        return modelMapper.map(updatedThemeDescription, ThemeDescriptionDTO.class);
    }

    public Set<Theme> deleteThemeDescription(Long id) {
        ValidationUtils.validateId(id);

        ThemeDescription themeDescriptionToDelete = themeDescriptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ThemeDescription", id));

        Set<Theme> themesToUpdate = themeDescriptionToDelete.getThemes();

        themesToUpdate.forEach(theme -> {
            theme.setDescription(null);
            themeDescriptionToDelete.getThemes().remove(theme);
        });

        themeDescriptionRepository.deleteById(id);

        return themesToUpdate;
    }

    public ThemeDescriptionDTO createThemeDescriptionIfNotExistsOrGet(String descriptionText) {
        Optional<ThemeDescription> existingDescription = themeDescriptionRepository.findByDescriptionText(descriptionText);

        if (existingDescription.isPresent()) {
            return modelMapper.map(existingDescription.get(), ThemeDescriptionDTO.class);
        } else {
            ThemeDescription newThemeDescription = new ThemeDescription();
            newThemeDescription.setDescriptionText(descriptionText);
            ThemeDescription themeDescriptionCreated = themeDescriptionRepository.save(newThemeDescription);
            return modelMapper.map(themeDescriptionCreated, ThemeDescriptionDTO.class);
        }
    }

}
