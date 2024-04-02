package me.didi.api.ess.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(implementation = GradeType.class)
public enum GradeType {
    FINAL,
    PARTIAL,
    ONGOING
}
