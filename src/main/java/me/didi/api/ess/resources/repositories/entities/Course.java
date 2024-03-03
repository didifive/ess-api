package me.didi.api.ess.resources.repositories.entities;

import jakarta.persistence.*;
import me.didi.api.ess.domain.enums.Frequency;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity(name="course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    @Enumerated
    private Frequency frequency;
    private String period;

    @ManyToMany(mappedBy = "courses")
    Set<Student> students;

    @ManyToMany
    @JoinTable(
            name = "courses_subjects",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    Set<Subject> subjects;

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
    Set<Subject> news;

    @ManyToMany
    @JoinTable(
            name = "courses_shortcuts",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "shortcut_id"))
    Set<Shortcut> shortcuts;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public Set<Subject> getNews() {
        return news;
    }

    public void setNews(Set<Subject> news) {
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
        return Objects.equals(getName(), course.getName()) && getFrequency() == course.getFrequency() && Objects.equals(getPeriod(), course.getPeriod());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getFrequency(), getPeriod());
    }
}
