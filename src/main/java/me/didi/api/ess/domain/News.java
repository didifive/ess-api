package me.didi.api.ess.domain;

import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return Objects.equals(icon, news.icon) && Objects.equals(title, news.title) && Objects.equals(description, news.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(icon, title, description);
    }
}
