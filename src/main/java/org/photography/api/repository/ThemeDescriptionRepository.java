package org.photography.api.repository;

import org.photography.api.model.ThemeDescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThemeDescriptionRepository extends JpaRepository<ThemeDescription, Long> {
}
