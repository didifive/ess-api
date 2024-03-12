package me.didi.api.ess.domain;

import java.time.LocalDateTime;

public record ReadMessage(Student student,
                          Message message,
                          Boolean read,
                          LocalDateTime statusDateTime) {
}
