package me.didi.api.ess.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import me.didi.api.ess.entities.Shortcut;

public record ShortcutRequestDTO(
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String icon,
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String title,
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String description,
        @NotBlank(message = NOT_BE_NULL_EMPTY_OR_BLANK)
        String link
) {

    public static final String NOT_BE_NULL_EMPTY_OR_BLANK = "Not be null, empty or blank";

    public static synchronized Shortcut toEntity(ShortcutRequestDTO dto) {
        return new Shortcut(
                dto.icon(),
                dto.title(),
                dto.description(),
                dto.link()
        );
    }
}
