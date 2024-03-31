package me.didi.api.ess.dtos.out;

import java.time.LocalDateTime;

public record Message(
        String icon,
        String title,
        String description,
        LocalDateTime dateTime
) {
}
