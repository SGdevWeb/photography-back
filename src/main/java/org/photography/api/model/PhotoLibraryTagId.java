package org.photography.api.model;

import java.io.Serializable;

public class PhotoLibraryTagId implements Serializable {

    private Long photoLibrary;

    private Long tag;

    public Long getPhotoLibrary() {
        return photoLibrary;
    }

    public void setPhotoLibrary(Long photoLibrary) {
        this.photoLibrary = photoLibrary;
    }

    public Long getTag() {
        return tag;
    }

    public void setTag(Long tag) {
        this.tag = tag;
    }
}
