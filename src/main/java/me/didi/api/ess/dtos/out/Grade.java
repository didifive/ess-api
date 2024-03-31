package me.didi.api.ess.dtos.out;

import me.didi.api.ess.enums.GradeType;

import java.math.BigDecimal;

public record Grade(Subject subject,
                    GradeType type,
                    BigDecimal value
) {
}
