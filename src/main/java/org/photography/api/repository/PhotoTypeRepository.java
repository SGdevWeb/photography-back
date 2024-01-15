package org.photography.api.repository;

import org.photography.api.model.PhotoType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotoTypeRepository extends JpaRepository<PhotoType, Long> {

    Optional<PhotoType> findByTypeName(String typeName);

    boolean existsByTypeName(String typeName);

}
