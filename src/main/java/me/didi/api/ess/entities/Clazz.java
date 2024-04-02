package me.didi.api.ess.entities;

import jakarta.persistence.*;
import me.didi.api.ess.entities.abstracts.BasicEntity;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity(name = "class")
public class Clazz extends BasicEntity {
    private String name;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "course_id")
    private Course course;
    private LocalDate initDate;
    private LocalDate recoveryDate;
    private LocalDate endDate;

    @OneToMany(mappedBy = "id.clazz"
            , fetch = FetchType.LAZY
            , cascade = {CascadeType.ALL}
            , orphanRemoval = true)
    private Set<Registration> registrations;

    public Set<Registration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(Set<Registration> registrations) {
        this.registrations = registrations;
    }

    public Clazz() {
    }

    public Clazz(String id
    ) {
        super(id);
    }

    public Clazz(String id,
                 String name,
                 Course course,
                 LocalDate initDate,
                 LocalDate recoveryDate,
                 LocalDate endDate
                 ) {
        super(id);
        this.name = name;
        this.course = course;
        this.initDate = initDate;
        this.recoveryDate = recoveryDate;
        this.endDate = endDate;
    }

    public Clazz(String name,
                 Course course,
                 LocalDate initDate,
                 LocalDate recoveryDate,
                 LocalDate endDate) {
        this.name = name;
        this.course = course;
        this.initDate = initDate;
        this.recoveryDate = recoveryDate;
        this.endDate = endDate;
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

    public LocalDate getInitDate() {
        return initDate;
    }

    public void setInitDate(LocalDate initDate) {
        this.initDate = initDate;
    }

    public LocalDate getRecoveryDate() {
        return recoveryDate;
    }

    public void setRecoveryDate(LocalDate recoveryDate) {
        this.recoveryDate = recoveryDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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
