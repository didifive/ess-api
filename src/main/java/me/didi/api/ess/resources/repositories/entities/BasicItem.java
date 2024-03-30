package me.didi.api.ess.resources.repositories.entities;

import jakarta.persistence.MappedSuperclass;

import java.util.Objects;

@MappedSuperclass
public abstract class BasicItem extends BasicEntity {

    private String icon;
    private String title;
    private String description;

    protected BasicItem() {
    }

    protected BasicItem(String id,
                        String icon,
                        String title,
                        String description) {
        super(id);
        this.icon = icon;
        this.title = title;
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicItem basicItem = (BasicItem) o;
        return Objects.equals(getIcon(), basicItem.getIcon()) && Objects.equals(getTitle(), basicItem.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIcon(), getTitle());
    }
}
