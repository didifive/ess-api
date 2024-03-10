package me.didi.api.ess.domain;

import me.didi.api.ess.domain.enums.GradeType;

import java.math.BigDecimal;

public record Grade(Student student,
                    Course course,
                    Subject subject,
                    GradeType type,
                    BigDecimal value) {
}
