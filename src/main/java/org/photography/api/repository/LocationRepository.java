package org.photography.api.repository;

import org.photography.api.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
    boolean existsByLocationName(String locationName);
}
