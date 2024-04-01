package me.didi.api.ess.entities.pks;

import jakarta.persistence.*;
import me.didi.api.ess.entities.Clazz;
import me.didi.api.ess.entities.Student;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RegistrationId implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY
            , cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE})
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY
            , cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE})
    @JoinColumn(name = "class_id")
    private Clazz clazz;

    public RegistrationId() {
    }

    public RegistrationId(Student student, Clazz clazz) {
        this.student = student;
        this.clazz = clazz;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Clazz getClazz() {
        return clazz;
    }

    public void setClazz(Clazz clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationId that = (RegistrationId) o;
        return Objects.equals(getStudent(), that.getStudent()) && Objects.equals(getClazz(), that.getClazz());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStudent(), getClazz());
    }
}
