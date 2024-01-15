package org.photography.api.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

//@Getter
//@Setter
@Entity
@Table(name = "themeDescription")
//@EqualsAndHashCode(exclude = {"themes"})
public class ThemeDescription implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String descriptionText;

    @OneToMany(mappedBy = "description", cascade = CascadeType.PERSIST)
//    @JsonManagedReference
//    @JsonIgnore
    private Set<Theme> themes;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    public Set<Theme> getThemes() {
        return themes;
    }

    public void setThemes(Set<Theme> themes) {
        this.themes = themes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThemeDescription that = (ThemeDescription) o;
        return id == that.id &&
                Objects.equals(descriptionText, that.descriptionText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, descriptionText);
    }
}
