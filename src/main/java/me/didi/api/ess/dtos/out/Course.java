package me.didi.api.ess.dtos.out;

import me.didi.api.ess.enums.Frequency;

import java.util.List;

public record Course(String id,
                     String name,
                     Frequency frequency,
                     List<Message> messages,
                     List<News> news,
                     List<Shortcut> shortcuts
) {
}
