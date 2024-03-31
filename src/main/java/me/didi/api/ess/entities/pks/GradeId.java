package me.didi.api.ess.entities.pks;

import jakarta.persistence.*;
import me.didi.api.ess.entities.Registration;
import me.didi.api.ess.entities.Subject;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GradeId implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id", referencedColumnName = "student_id")
    @JoinColumn(name = "class_id", referencedColumnName = "class_id")
    private Registration registration;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    public GradeId() {}

    public GradeId(Registration registration, Subject subject) {
        this.registration = registration;
        this.subject = subject;
    }

    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GradeId gradeId = (GradeId) o;
        return Objects.equals(getRegistration(), gradeId.getRegistration()) && Objects.equals(getSubject(), gradeId.getSubject());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRegistration(), getSubject());
    }

    @Override
    public String toString() {
        return "GradeId{" +
                "registration=" + registration +
                ", subject=" + subject +
                '}';
    }
}
