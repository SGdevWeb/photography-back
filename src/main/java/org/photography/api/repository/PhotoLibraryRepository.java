package org.photography.api.repository;

import org.photography.api.model.Location;
import org.photography.api.model.PhotoLibrary;
import org.photography.api.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface PhotoLibraryRepository extends JpaRepository<PhotoLibrary, Long> {

    boolean existsByTags(Tag tag);

    boolean existsByLocation(Location location);

    @Query("SELECT p FROM PhotoLibrary p " +
            "LEFT JOIN p.tags t " +
            "LEFT JOIN p.location l " +
            "WHERE (:tags IS NULL OR t.tagName IN :tags) " +
            "AND (:locations IS NULL OR l.locationName IN :locations)")
    Page<PhotoLibrary> findFiltered(@Param("tags") Set<String> tags,
                                    @Param("locations") Set<String> locations,
                                    Pageable pageable);

}


