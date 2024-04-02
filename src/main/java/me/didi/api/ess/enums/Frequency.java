package me.didi.api.ess.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(implementation = Frequency.class)
public enum Frequency {
    YEARLY,
    SEMIANNUALLY,
    QUARTERLY,
    BIMONTHLY,
    MONTHLY
}
