package org.photography.api.repository;

import org.photography.api.model.ThemePhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThemePhotoRepository extends JpaRepository<ThemePhoto, Long> {
}
