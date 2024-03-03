package me.didi.api.ess.resources.repositories.entities;

import jakarta.persistence.*;

@Entity(name="read_message")
public class ReadMessage {
    @EmbeddedId
    private ReadMessageId id;
    private Boolean read;

    public ReadMessageId getId() {
        return id;
    }

    public void setId(ReadMessageId id) {
        this.id = id;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }
}
