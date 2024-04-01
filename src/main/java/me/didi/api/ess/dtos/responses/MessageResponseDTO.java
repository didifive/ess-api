package me.didi.api.ess.dtos.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import me.didi.api.ess.entities.Message;

import java.time.LocalDateTime;

import static me.didi.api.ess.utils.constants.ConstantsUtils.DATE_TIME_PATTERN;

public record MessageResponseDTO(
        String id,
        String icon,
        String title,
        String description,
        @JsonFormat(pattern = DATE_TIME_PATTERN)
        LocalDateTime dateTime
) {

    public static synchronized MessageResponseDTO toDto(Message entity) {
        return new MessageResponseDTO(
                entity.getId(),
                entity.getIcon(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getDateTime()
        );
    }
}
