package me.didi.api.ess.domain;

import me.didi.api.ess.domain.enums.Frequency;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public record Course(UUID id,
                     String name,
                     Frequency frequency,
                     Set<Message> messages,
                     Set<News> news,
                     Set<Shortcut> shortcuts
) {
    public static Course createNew(String name,
                                   Frequency frequency,
                                   Set<Message> messages,
                                   Set<News> news,
                                   Set<Shortcut> shortcuts) {
        return new Course(
                UUID.randomUUID(),
                name,
                frequency,
                messages,
                news,
                shortcuts
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id) && Objects.equals(name, course.name) && frequency == course.frequency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, frequency);
    }
}
