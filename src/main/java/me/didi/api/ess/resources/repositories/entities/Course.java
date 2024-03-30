package me.didi.api.ess.resources.repositories.entities;

import jakarta.persistence.*;
import me.didi.api.ess.domain.enums.Frequency;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity(name = "course")
public class Course extends BasicEntity implements Serializable {
    @ManyToMany
    @JoinTable(
            name = "courses_messages",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "messages_id"))
    Set<Message> messages;
    @ManyToMany
    @JoinTable(
            name = "courses_news",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "news_id"))
    Set<News> news;
    @ManyToMany
    @JoinTable(
            name = "courses_shortcuts",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "shortcut_id"))
    Set<Shortcut> shortcuts;
    private String name;
    @Enumerated
    private Frequency frequency;

    public Course() {
    }

    public Course(String id,
                  String name,
                  Frequency frequency,
                  Set<Message> messages,
                  Set<News> news,
                  Set<Shortcut> shortcuts) {
        super(id);
        this.name = name;
        this.frequency = frequency;
        this.messages = messages;
        this.news = news;
        this.shortcuts = shortcuts;
    }

    public Course(String name,
                  Frequency frequency) {
        this.name = name;
        this.frequency = frequency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public Set<News> getNews() {
        return news;
    }

    public void setNews(Set<News> news) {
        this.news = news;
    }

    public Set<Shortcut> getShortcuts() {
        return shortcuts;
    }

    public void setShortcuts(Set<Shortcut> shortcuts) {
        this.shortcuts = shortcuts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(getName(), course.getName()) && getFrequency() == course.getFrequency();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getFrequency());
    }
}
