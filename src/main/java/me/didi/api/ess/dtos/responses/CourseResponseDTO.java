package me.didi.api.ess.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import me.didi.api.ess.entities.Course;
import me.didi.api.ess.enums.Frequency;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.didi.api.ess.utils.constants.ValidationMessagesAndOpenApiConstantsUtils.UUID_ID_EXAMPLE;

public record CourseResponseDTO(
        @Schema(example = UUID_ID_EXAMPLE)
        String id,
        String name,
        @Enumerated(EnumType.STRING)
        Frequency frequency,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        List<MessageResponseDTO> messages,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        List<NewsResponseDTO> news,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        List<ShortcutResponseDTO> shortcuts
) {

    public static synchronized CourseResponseDTO toDto(Course entity) {
        List<MessageResponseDTO> messages = new ArrayList<>();
        List<NewsResponseDTO> news = new ArrayList<>();
        List<ShortcutResponseDTO> shortcuts = new ArrayList<>();

        if (Objects.nonNull(entity.getMessages()))
            messages = entity.getMessages().stream()
                    .map(MessageResponseDTO::toDto).toList();

        if (Objects.nonNull(entity.getNews()))
            news = entity.getNews().stream()
                    .map(NewsResponseDTO::toDto).toList();

        if (Objects.nonNull(entity.getShortcuts()))
            shortcuts = entity.getShortcuts().stream()
                    .map(ShortcutResponseDTO::toDto).toList();


        return new CourseResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getFrequency(),
                messages,
                news,
                shortcuts
        );
    }
}
