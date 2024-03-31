package me.didi.api.ess.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import me.didi.api.ess.entities.News;

public record NewsRequestDTO(
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String icon,
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String title,
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String description
) {

    public static final String NOT_BE_NULL_EMPTY_OR_BLANK = "Not be null, empty or blank";

    public static synchronized News toEntity(NewsRequestDTO dto) {
        return new News(
                dto.icon(),
                dto.title(),
                dto.description()
        );
    }
}
