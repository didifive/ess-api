package me.didi.api.ess.resources.repositories.entities;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity(name="shorcut")
public class Shortcut {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String link;

    @ManyToMany(mappedBy = "shortcuts")
    Set<Course> courses;

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shortcut shortcut = (Shortcut) o;
        return Objects.equals(getName(), shortcut.getName()) && Objects.equals(getLink(), shortcut.getLink());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getLink());
    }
}
