package org.photography.api.repository;

import org.photography.api.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsByTagName(String tagName);

    Tag findByTagName(String tagName);
}
