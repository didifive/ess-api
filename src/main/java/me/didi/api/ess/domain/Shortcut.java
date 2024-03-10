package me.didi.api.ess.domain;

import java.util.UUID;

public record Shortcut(UUID id,
                       String icon,
                       String title,
                       String description) {
    public Shortcut newShorcut(String icon,
                        String title,
                        String description){
        return new Shortcut(UUID.randomUUID(),
                icon,
                title,
                description);
    }
}