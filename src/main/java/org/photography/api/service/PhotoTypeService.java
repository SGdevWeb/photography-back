package org.photography.api.service;

import org.photography.api.repository.PhotoTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoTypeService {

    private final PhotoTypeRepository photoTypeRepository;

    @Autowired
    public PhotoTypeService(PhotoTypeRepository photoTypeRepository) {
        this.photoTypeRepository = photoTypeRepository;
    }

}
