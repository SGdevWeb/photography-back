package org.photography.api.repository;

import org.photography.api.model.ThemeDescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThemeDescriptionRepository extends JpaRepository<ThemeDescription, Long> {

    Optional<ThemeDescription> findByDescriptionText(String descriptionText);
}
