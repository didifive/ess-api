package me.didi.api.ess.entities;

import jakarta.persistence.Entity;
import me.didi.api.ess.entities.abstracts.BasicItem;

import java.io.Serializable;

@Entity(name = "subject")
public class Subject extends BasicItem implements Serializable {
    public Subject() {
    }

    public Subject(String id,
                      String icon,
                      String title,
                      String description) {
        super(id, icon, title, description);
    }

    public Subject(String icon,
                      String title,
                      String description) {
        super(icon, title, description);
    }

}
