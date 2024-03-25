package org.photography.api.repository;

import org.photography.api.model.PhotoLibrary;
import org.photography.api.model.PhotoLibraryTag;
import org.photography.api.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface PhotoLibraryTagRepository extends JpaRepository<PhotoLibraryTag, Long> {

    boolean existsByTagId(Long tagId);

    void deleteByPhotoLibraryAndTag(PhotoLibrary photoLibrary, Tag tag);

    void deleteByTag(Tag tag);

}
