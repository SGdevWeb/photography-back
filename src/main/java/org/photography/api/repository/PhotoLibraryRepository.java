package org.photography.api.repository;

import org.photography.api.model.PhotoLibrary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface PhotoLibraryRepository extends JpaRepository<PhotoLibrary, Long> {

}
