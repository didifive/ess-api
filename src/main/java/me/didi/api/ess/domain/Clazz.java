package me.didi.api.ess.domain;

import java.util.Objects;
import java.util.UUID;

public record Clazz(UUID id,
                    String name,
                    Course course
) {

    public static Clazz createNew(String name,
                                  Course course) {
        return new Clazz(
                UUID.randomUUID(),
                name,
                course
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clazz clazz = (Clazz) o;
        return Objects.equals(name, clazz.name) && Objects.equals(course, clazz.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, course);
    }
}