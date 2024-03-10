package me.didi.api.ess.domain;

import me.didi.api.ess.domain.enums.Frequency;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record Course(UUID id,
                     String name,
                     Frequency frequency,
                     String period,
                     LocalDate initDate,
                     LocalDate recoveryDate,
                     LocalDate endDate,
                     Set<Student> students,
                     Set<Subject> subjects,
                     Set<Message> messages,
                     Set<News> news,
                     Set<Shortcut> shortcuts
) {
    public static Course createNew(String name,
                                Frequency frequency,
                                String period,
                                LocalDate initDate,
                                LocalDate recoveryDate,
                                LocalDate endDate,
                                Set<Student> students,
                                Set<Subject> subjects,
                                Set<Message> messages,
                                Set<News> news,
                                Set<Shortcut> shortcuts) {
        return new Course(
                UUID.randomUUID(),
                name,
                frequency,
                period,
                initDate,
                recoveryDate,
                endDate,
                students,
                subjects,
                messages,
                news,
                shortcuts
        );
    }
}
