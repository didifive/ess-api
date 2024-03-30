package me.didi.api.ess.api.dtos.out;

import me.didi.api.ess.enums.GradeType;

import java.math.BigDecimal;

public record Grade(
        GradeType type,
        BigDecimal value
) {
}
