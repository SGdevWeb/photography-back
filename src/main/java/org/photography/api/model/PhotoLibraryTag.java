package org.photography.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;


//@Getter
//@Setter
@Entity
@Table(name = "photoLibrary_tag")
@IdClass(PhotoLibraryTagId.class)
public class PhotoLibraryTag implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumn(name = "photoId")
    private PhotoLibrary photoLibrary;

    @Id
    @ManyToOne
    @JoinColumn(name = "tagId")
    private Tag tag;

    public PhotoLibrary getPhotoLibrary() {
        return photoLibrary;
    }

    public void setPhotoLibrary(PhotoLibrary photoLibrary) {
        this.photoLibrary = photoLibrary;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
