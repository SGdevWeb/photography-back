package org.photography.api.repository;

import org.photography.api.model.PhotoType;
import org.photography.api.model.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ThemeRepository extends JpaRepository<Theme, Long> {

    boolean existsByThemeName(String themeName);

    List<Theme> findByPhotoTypesContains(PhotoType oldPhotoType);
}
