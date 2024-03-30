package me.didi.api.ess.api.dtos.out;

import me.didi.api.ess.domain.enums.RegistrationStatus;
import me.didi.api.ess.domain.enums.Frequency;

public record Course(
        String name,
        Frequency frequency,
        String period,
        RegistrationStatus status
) {
}
