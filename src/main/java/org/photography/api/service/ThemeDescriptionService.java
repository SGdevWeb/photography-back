package org.photography.api.service;

import org.photography.api.repository.ThemeDescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThemeDescriptionService {

    private final ThemeDescriptionRepository themeDescriptionRepository;

    @Autowired
    public ThemeDescriptionService(ThemeDescriptionRepository themeDescriptionRepository) {
        this.themeDescriptionRepository = themeDescriptionRepository;
    }

}
