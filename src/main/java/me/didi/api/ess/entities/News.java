package me.didi.api.ess.entities;

import jakarta.persistence.Entity;
import me.didi.api.ess.entities.abstracts.BasicItem;

import java.io.Serializable;

@Entity(name = "news")
public class News extends BasicItem implements Serializable {
    public News() {
    }

    public News(String id,
                   String icon,
                   String title,
                   String description) {
        super(id, icon, title, description);
    }

    public News(String icon,
                   String title,
                   String description) {
        super(icon, title, description);
    }
}
