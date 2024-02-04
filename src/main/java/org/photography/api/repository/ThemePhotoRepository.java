package org.photography.api.repository;

import org.photography.api.model.PhotoType;
import org.photography.api.model.ThemePhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ThemePhotoRepository extends JpaRepository<ThemePhoto, Long> {

    Optional<ThemePhoto> findByPhotoUrl(String photoUrl);

    List<ThemePhoto> findByPhotoTypeOrderByCreatedAtDesc(PhotoType photoType);

    List<ThemePhoto> findByPhotoType(PhotoType oldPhotoType);
}
