package me.didi.api.ess.resources.repositories.entities;

import jakarta.persistence.Entity;

import java.io.Serializable;

@Entity(name = "message")
public class Message extends BasicItem implements Serializable {
    public Message() {
    }

    protected Message(String id,
                      String icon,
                      String title,
                      String description) {
        super(id, icon, title, description);
    }
}
