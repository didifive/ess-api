package me.didi.api.ess.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record Message(UUID id,
                      String icon,
                      String title,
                      String description,
                      LocalDateTime dateTime) {

    public Message newMessage(String icon,
                              String title,
                              String description){
        return new Message(UUID.randomUUID(),
                icon,
                title,
                description,
                LocalDateTime.now());
    }
}
