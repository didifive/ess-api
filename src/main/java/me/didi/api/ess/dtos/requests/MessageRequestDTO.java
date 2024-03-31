package me.didi.api.ess.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import me.didi.api.ess.entities.Message;

public record MessageRequestDTO(
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String icon,
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String title,
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String description
) {

    public static final String NOT_BE_NULL_EMPTY_OR_BLANK = "Not be null, empty or blank";

    public static synchronized Message toEntity(MessageRequestDTO dto) {
        return new Message(
                dto.icon(),
                dto.title(),
                dto.description()
        );
    }
}
