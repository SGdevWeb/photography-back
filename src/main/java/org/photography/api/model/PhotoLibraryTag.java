package org.photography.api.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "photoLibrary_tag")
public class PhotoLibraryTag {

    @Id
    @ManyToOne
    @JoinColumn(name = "photoId")
    private PhotoLibrary photoLibrary;

    @Id
    @ManyToOne
    @JoinColumn(name = "tagId")
    private Tag tag;

}
