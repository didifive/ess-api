package me.didi.api.ess.domain;

import java.util.UUID;

public record News(UUID id,
                   String icon,
                   String title,
                   String description) {

    public News newNews(String icon,
                              String title,
                              String description){
        return new News(UUID.randomUUID(),
                icon,
                title,
                description);
    }
}
