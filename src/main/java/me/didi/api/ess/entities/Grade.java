package me.didi.api.ess.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import me.didi.api.ess.entities.pks.GradeId;
import me.didi.api.ess.enums.GradeType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity(name = "grade")
public class Grade implements Serializable {
    @EmbeddedId
    private GradeId id;
    @Enumerated
    private GradeType gradeType;
    @Column(name = "grade_value", precision = 4, scale = 2)
    @Max(10)
    @Min(0)
    private BigDecimal value;

    public Grade() {
    }

    public Grade(Registration registration, Subject subject, GradeType gradeType, BigDecimal value) {
        GradeId gradeId = new GradeId();
        gradeId.setRegistration(registration);
        gradeId.setSubject(subject);

        this.id = gradeId;
        this.gradeType = gradeType;
        this.value = value;
    }

    public Grade(Student student, Clazz clazz, Subject subject, GradeType gradeType, BigDecimal value) {
        Registration registration = new Registration(student, clazz, null);

        GradeId gradeId = new GradeId();
        gradeId.setRegistration(registration);
        gradeId.setSubject(subject);

        this.id = gradeId;
        this.gradeType = gradeType;
        this.value = value;
    }

    public GradeId getId() {
        return id;
    }

    public void setId(GradeId id) {
        this.id = id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal gradeValue) {
        this.value = gradeValue;
    }

    public GradeType getGradeType() {
        return gradeType;
    }

    public void setGradeType(GradeType gradeType) {
        this.gradeType = gradeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grade grade = (Grade) o;
        return Objects.equals(getId(), grade.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", gradeType=" + gradeType +
                ", value=" + value +
                '}';
    }
}
