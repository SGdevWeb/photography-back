package org.photography.api.service;

import org.photography.api.repository.ThemePhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThemePhotoService {

    private final ThemePhotoRepository themePhotoRepository;

    @Autowired
    public ThemePhotoService(ThemePhotoRepository themePhotoRepository) {
        this.themePhotoRepository = themePhotoRepository;
    }

}
