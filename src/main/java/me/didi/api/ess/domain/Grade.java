package me.didi.api.ess.domain;

import me.didi.api.ess.domain.enums.GradeType;

import java.math.BigDecimal;
import java.util.Objects;

public record Grade(Registration registration,
                    Subject subject,
                    GradeType type,
                    BigDecimal value) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grade grade = (Grade) o;
        return Objects.equals(registration, grade.registration) && Objects.equals(subject, grade.subject) && type == grade.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(registration, subject, type);
    }
}
