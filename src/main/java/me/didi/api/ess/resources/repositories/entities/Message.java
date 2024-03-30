package me.didi.api.ess.resources.repositories.entities;

import jakarta.persistence.Entity;

import java.io.Serializable;
import java.util.UUID;

@Entity(name = "message")
public class Message extends BasicItem implements Serializable {
    public Message() {
    }

    protected Message(UUID id,
                      String icon,
                      String title,
                      String description) {
        super(id, icon, title, description);
    }
}
