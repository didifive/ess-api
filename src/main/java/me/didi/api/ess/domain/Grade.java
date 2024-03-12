package me.didi.api.ess.domain;

import me.didi.api.ess.domain.enums.GradeType;

import java.math.BigDecimal;
import java.util.Objects;

public record Grade(Student student,
                    Course course,
                    Subject subject,
                    GradeType type,
                    BigDecimal value) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grade grade = (Grade) o;
        return Objects.equals(student, grade.student) && Objects.equals(course, grade.course) && Objects.equals(subject, grade.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, course, subject);
    }
}
