package me.didi.api.ess.api.dtos.out;

import java.time.LocalDateTime;

public record Message(
        String description,
        LocalDateTime dateTime,
        Boolean read
) {
}
