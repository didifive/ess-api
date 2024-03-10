package me.didi.api.ess.domain;

import java.util.UUID;

public record Subject(UUID id,
                      String icon,
                      String title,
                      String description) {
    public Subject newSubject(String icon,
                               String title,
                               String description){
        return new Subject(UUID.randomUUID(),
                icon,
                title,
                description);
    }
}