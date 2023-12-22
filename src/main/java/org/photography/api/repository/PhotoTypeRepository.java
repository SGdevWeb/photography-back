package org.photography.api.repository;

import org.photography.api.model.PhotoType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoTypeRepository extends JpaRepository<PhotoType, Long> {
}
