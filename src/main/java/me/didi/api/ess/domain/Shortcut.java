package me.didi.api.ess.domain;

import java.util.Objects;
import java.util.UUID;

public record Shortcut(UUID id,
                       String icon,
                       String title,
                       String description,
                       String link) {
    public Shortcut newShorcut(String icon,
                               String title,
                               String description,
                               String link) {
        return new Shortcut(UUID.randomUUID(),
                icon,
                title,
                description,
                link);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shortcut shortcut = (Shortcut) o;
        return Objects.equals(icon, shortcut.icon) && Objects.equals(title, shortcut.title) && Objects.equals(description, shortcut.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(icon, title, description);
    }
}