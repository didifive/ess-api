package me.didi.api.ess.resources.repositories.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import me.didi.api.ess.domain.enums.GradeType;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GradeId implements Serializable {

    private String student;
    private String course;
    private String subject;
    @Enumerated
    private GradeType type;

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public GradeType getType() {
        return type;
    }

    public void setType(GradeType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GradeId gradeId = (GradeId) o;
        return Objects.equals(getStudent(), gradeId.getStudent()) && Objects.equals(getCourse(), gradeId.getCourse()) && Objects.equals(getSubject(), gradeId.getSubject()) && getType() == gradeId.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStudent(), getCourse(), getSubject(), getType());
    }
}
