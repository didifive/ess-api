package me.didi.api.ess.resources.repositories.entities;

import jakarta.persistence.Entity;

import java.io.Serializable;
import java.util.UUID;

@Entity(name = "news")
public class News extends BasicItem implements Serializable {
    public News() {
    }

    protected News(UUID id,
                   String icon,
                   String title,
                   String description) {
        super(id, icon, title, description);
    }
}
