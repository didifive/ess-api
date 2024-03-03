package me.didi.api.ess.api.dtos.out;

import java.util.List;

public record Student(
        String name,
        String guardian,

        String photo,
        Course course,
        List<Subject> subjects,
        List<Message> messages,
        List<Shortcut> shortcuts,
        List<News> news
) {
}
