package me.didi.api.ess.entities;

import jakarta.persistence.Entity;
import me.didi.api.ess.entities.abstracts.BasicItem;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "message")
public class Message extends BasicItem implements Serializable {
    LocalDateTime dateTime;
    public Message() {
    }

    public Message(String id,
                   String icon,
                   String title,
                   String description,
                   LocalDateTime dateTime) {
        super(id, icon, title, description);
        this.dateTime = dateTime;
    }

    public Message(String icon,
                String title,
                String description) {
        super(icon, title, description);
        this.dateTime = LocalDateTime.now();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Message message = (Message) o;
        return Objects.equals(getId(), message.getId())
                && Objects.equals(getIcon(), message.getIcon())
                && Objects.equals(getTitle(), message.getTitle())
                && Objects.equals(getDescription(), message.getDescription())
                && Objects.equals(getDateTime(), message.getDateTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDateTime());
    }
}
