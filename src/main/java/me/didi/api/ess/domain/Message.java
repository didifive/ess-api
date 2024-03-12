package me.didi.api.ess.domain;

import java.time.LocalDateTime;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(icon, message.icon) && Objects.equals(title, message.title) && Objects.equals(description, message.description) && Objects.equals(dateTime, message.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(icon, title, description, dateTime);
    }
}
