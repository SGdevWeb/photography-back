package org.photography.api.service;

import org.photography.api.repository.PhotoLibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoLibraryService {

    private final PhotoLibraryRepository photoLibraryRepository;

    @Autowired
    public PhotoLibraryService(PhotoLibraryRepository photoLibraryRepository) {
        this.photoLibraryRepository = photoLibraryRepository;
    }

}
