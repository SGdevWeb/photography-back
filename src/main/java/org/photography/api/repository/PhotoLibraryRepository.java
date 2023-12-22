package org.photography.api.repository;

import org.photography.api.model.PhotoLibrary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoLibraryRepository extends JpaRepository<PhotoLibrary, Long> {

}
