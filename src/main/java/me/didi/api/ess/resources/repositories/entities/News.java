package me.didi.api.ess.resources.repositories.entities;

import jakarta.persistence.Entity;

import java.io.Serializable;

@Entity(name = "news")
public class News extends BasicItem implements Serializable {
    public News() {
    }

    protected News(String id,
                   String icon,
                   String title,
                   String description) {
        super(id, icon, title, description);
    }
}
