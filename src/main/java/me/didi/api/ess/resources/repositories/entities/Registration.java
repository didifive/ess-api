package me.didi.api.ess.resources.repositories.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity(name = "registration")
public class Registration implements Serializable {
    @EmbeddedId
    private RegistrationId id;
    @ManyToMany
    @JoinTable(
            name = "registrations_subjects",
            joinColumns = {@JoinColumn(name = "student_id", referencedColumnName = "student_id"),
                    @JoinColumn(name = "class_id", referencedColumnName = "class_id")},
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private Set<Subject> subjects;
    private LocalDate registrationDate;
    private LocalDate recoveryDate;
    private LocalDate endDate;

    public RegistrationId getId() {
        return id;
    }

    public void setId(RegistrationId id) {
        this.id = id;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
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
        Registration that = (Registration) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
