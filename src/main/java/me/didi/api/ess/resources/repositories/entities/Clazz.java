package me.didi.api.ess.resources.repositories.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.Objects;
import java.util.UUID;

@Entity(name = "class")
public class Clazz extends BasicEntity {
    private String name;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public Clazz() {
    }
    public Clazz(UUID id,
                 String name,
                 Course course) {
        super(id);
        this.name = name;
        this.course = course;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clazz clazz = (Clazz) o;
        return Objects.equals(getName(), clazz.getName()) && Objects.equals(getCourse(), clazz.getCourse());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getCourse());
    }
}
