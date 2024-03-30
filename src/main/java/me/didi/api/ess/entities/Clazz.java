package me.didi.api.ess.entities;

import jakarta.persistence.*;
import me.didi.api.ess.entities.abstracts.BasicEntity;

import java.util.Objects;

@Entity(name = "class")
public class Clazz extends BasicEntity {
    private String name;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "course_id")
    private Course course;

    public Clazz() {
    }

    public Clazz(String id,
                 String name,
                 Course course) {
        super(id);
        this.name = name;
        this.course = course;
    }

    public Clazz(String name,
                 Course course) {
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
