package me.didi.api.ess.domain;

import java.util.Objects;
import java.util.UUID;

public record Subject(UUID id,
                      String icon,
                      String title,
                      String description) {
    public Subject newSubject(String icon,
                               String title,
                               String description){
        return new Subject(UUID.randomUUID(),
                icon,
                title,
                description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Objects.equals(icon, subject.icon) && Objects.equals(title, subject.title) && Objects.equals(description, subject.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(icon, title, description);
    }
}