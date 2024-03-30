package me.didi.api.ess.resources.repositories.entities;

import jakarta.persistence.Entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "shorcut")
public class Shortcut extends BasicItem implements Serializable {

    private String link;

    public Shortcut() {
    }

    public Shortcut(UUID id,
                    String icon,
                    String title,
                    String description,
                    String link) {
        super(id, icon, title, description);
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Shortcut shortcut = (Shortcut) o;
        return Objects.equals(getIcon(), shortcut.getIcon())
                && Objects.equals(getTitle(), shortcut.getTitle())
                && Objects.equals(getLink(), shortcut.getLink());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getIcon(), getLink());
    }
}
