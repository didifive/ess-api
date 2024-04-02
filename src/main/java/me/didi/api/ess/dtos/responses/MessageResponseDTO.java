package me.didi.api.ess.dtos.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import me.didi.api.ess.entities.Message;

import java.time.LocalDateTime;

import static me.didi.api.ess.utils.constants.GeneralConstantsUtils.DATE_TIME_PATTERN;
import static me.didi.api.ess.utils.constants.ValidationMessagesAndOpenApiConstantsUtils.*;

public record MessageResponseDTO(
        @Schema(example = UUID_ID_EXAMPLE)
        String id,
        String icon,
        String title,
        String description,
        @Schema(example = DATE_TIME_EXAMPLE)
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
